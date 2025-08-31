package org.asodev.dynamicsecurity.payload.response;

public record TokenResponse(String accessToken, String refreshToken, long expiresIn) {
}