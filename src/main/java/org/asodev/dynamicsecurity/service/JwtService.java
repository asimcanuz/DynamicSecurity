package org.asodev.dynamicsecurity.service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.asodev.dynamicsecurity.enums.TokenType;
import org.asodev.dynamicsecurity.model.Permission;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.model.Token;
import org.asodev.dynamicsecurity.model.User;
import org.asodev.dynamicsecurity.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@SuppressWarnings("ALL")
@Service
public class JwtService {

    private final byte[] secret; // HS256 key bytes (>= 256 bit)
    private static final String issuer = "jwtser";
    private final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
    private final TokenRepository tokenRepository;
    private final UserService userService;

    public JwtService(@Value("${jwt.secret}") String secretFromProp, TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        byte[] _secret;
        try {
            _secret = Base64.getDecoder().decode(secretFromProp);
        } catch (IllegalArgumentException e) {
            _secret = secretFromProp.getBytes(StandardCharsets.UTF_8);
        }
        this.secret = _secret;
        if (this.secret.length < 32) {
            throw new IllegalArgumentException("HS256 için secret en az 256-bit (32 byte) olmalı.");
        }
    }

    public String generateToken(String userName, Role role) {
        Instant now = Instant.now();

        List<String> permissions = role.getPermissions().stream().map(Permission::getCode).toList();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer(issuer)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(2, ChronoUnit.MINUTES))) // 2 dk
                .claim("permissions", permissions)
                .build();


        try {
            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(new MACSigner(secret));
            String token = jwt.serialize();
            saveToken(token , userName, TokenType.ACCESS, now.plus(2, ChronoUnit.MINUTES));

            return token;
        } catch (JOSEException e) {
            throw new RuntimeException("JWT oluşturulamadı", e);
        }
    }

    public long getAccessTokenExpirationInSeconds() {
        return 120;
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(issuer)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(604800, ChronoUnit.SECONDS)))
                .claim("type", "refresh")
                .build();

        try {
            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(new MACSigner(secret));
            String token = jwt.serialize();
            saveToken(token , username, TokenType.ACCESS,now.plus(604800, ChronoUnit.SECONDS));

            return token;
        } catch (JOSEException e) {
            throw new RuntimeException("Refresh token oluşturulamadı", e);
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            JWTClaimsSet claims = getVerifiedClaims(token);
            return "refresh".equals(claims.getStringClaim("type"));
        } catch (Exception e) {
            return false;
        }
    }



    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            if (!jwt.verify(new MACVerifier(secret)))
                return false;

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            if (!issuer.equals(claims.getIssuer()))
                return false;

            String subject = claims.getSubject();
            if (!Objects.equals(userDetails.getUsername(), claims.getSubject()))
                return false;

            Date exp = claims.getExpirationTime();

            return userDetails.getUsername().equals(subject)
                    && exp != null
                    && exp.after(new Date());
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            if (!jwt.verify(new MACVerifier(secret)))
                return false;

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            if (!issuer.equals(claims.getIssuer()))
                return false;

            if (!"refresh".equals(claims.getStringClaim("type")))
                return false;

            Date exp = claims.getExpirationTime();

            return exp != null && exp.after(new Date());
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    public void revokeToken(String token) {
        tokenRepository.revokeToken(token);
    }

    public void revokeAllUserTokens(Long userId) {
        tokenRepository.revokeAllUserTokens(userId);
    }
    public String extractUser(String token) {
        try {
            return getVerifiedClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> extractPermissions(String token) {
        try {
            JWTClaimsSet claims = getVerifiedClaims(token);
            return claims.getStringListClaim("permissions");
        } catch (Exception e) {
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            JWTClaimsSet claims = getVerifiedClaims(token);
            return claims.getExpirationTime();
        } catch (Exception e) {
            return null;
        }
    }

    private JWTClaimsSet getVerifiedClaims(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        if (!jwt.verify(new MACVerifier(secret))) {
            throw new JOSEException("JWT imza doğrulaması başarısız");
        }
        return jwt.getJWTClaimsSet();
    }


    private void saveToken(String token, String userName, TokenType tokenType, Instant expiresAt) {
        User user = userService.getByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userName));

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenEntity.setIssuedAt(Instant.now());
        tokenEntity.setExpiresAt(expiresAt);
        tokenEntity.setRevoked(false);
        tokenEntity.setType(tokenType);

        tokenRepository.save(tokenEntity);

    }
}
