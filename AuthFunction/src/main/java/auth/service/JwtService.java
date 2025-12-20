package auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtService {

    private static final long EXPIRATION_SECONDS = 1800;
    private static final String SECRET = "my-super-secret-key-for-hs256-test-only";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String document) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(document)
                .setIssuer("auth-lambda")
                .setAudience("techchallenge-12SOAT")
                .claim("scope", "api:access")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(EXPIRATION_SECONDS)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
