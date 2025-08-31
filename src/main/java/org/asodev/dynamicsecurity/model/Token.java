package org.asodev.dynamicsecurity.model;

import jakarta.persistence.*;
import lombok.*;
import org.asodev.dynamicsecurity.enums.TokenType;

import java.time.Instant;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    public boolean isValid() {
        return !revoked && expiresAt.isAfter(Instant.now());
    }

}
