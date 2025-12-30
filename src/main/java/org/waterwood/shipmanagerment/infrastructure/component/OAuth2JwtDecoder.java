package org.waterwood.shipmanagerment.infrastructure.component;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.waterwood.shipmanagerment.service.TokenService;

@Component
@RequiredArgsConstructor
public class OAuth2JwtDecoder implements Converter<String, Jwt>, JwtDecoder {

    private final RsaJwtUtil rsaJwtUtil;
    private final TokenService tokenService;

    @Override
    public Jwt convert(@NotNull String jwtString) {
        Claims claims = rsaJwtUtil.parseToken(jwtString);
        tokenService.validTokenAndRejectOld(claims);
        return Jwt.withTokenValue(jwtString)
                .claims(c -> c.putAll(claims))
                .header("alg", "RS256")
                .header("typ", "JWT")
                .issuedAt(claims.getIssuedAt().toInstant())
                .expiresAt(claims.getExpiration().toInstant())
                .build();
    }


    @Override
    public Jwt decode(String token) throws JwtException {
        return this.convert(token);
    }
}
