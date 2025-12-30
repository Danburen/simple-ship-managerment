package org.waterwood.shipmanagerment.infrastructure.component;

import io.jsonwebtoken.Claims;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.waterwood.shipmanagerment.infrastructure.utils.UserContext;

@Component
public class UserJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Long userId = Long.valueOf(source.getClaimAsString(Claims.SUBJECT));
        UserContext ctx = UserContext.of(userId);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ctx, null, ctx.getAuthorities());
        auth.setDetails(source);
        return auth;
    }
}
