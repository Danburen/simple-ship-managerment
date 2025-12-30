package org.waterwood.shipmanagerment.infrastructure.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.waterwood.shipmanagerment.api.TokenResult;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class RsaJwtUtil {
    private static final String ISSUER = "waterwood";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public RsaJwtUtil(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Build a jwt token with expiration of seconds
     * @param claims  claims
     * @param exp expiration in seconds
     * @return jwt token
     */
    public TokenResult buildToken(Map<String, String> claims, Duration exp){
        Date expiration = new Date(System.currentTimeMillis() + exp.toMillis());
        return new TokenResult(Jwts.builder()
                .issuer(ISSUER)
                .claims(claims)
                .issuedAt(new Date())
                .signWith(privateKey, Jwts.SIG.RS256)
                .expiration(expiration)
                .compact(), exp.toSeconds());
    }

    public Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
