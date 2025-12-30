package org.waterwood.shipmanagerment.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.TokenResult;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;
import org.waterwood.shipmanagerment.infrastructure.component.RedisHelperHolder;
import org.waterwood.shipmanagerment.infrastructure.component.RsaJwtUtil;
import org.waterwood.shipmanagerment.infrastructure.exception.AuthException;
import org.waterwood.shipmanagerment.service.RedisKeyBuilder;
import org.waterwood.shipmanagerment.service.TokenService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RedisHelperHolder redisHelper;
    private final RsaJwtUtil rsaJwtUtil;
    @Value("${token.access.expire:43200}")
    private Long accessTokenExpire;

    @Override
    public TokenResult generateToken(Long id) {
        Map<String, String> claims = new HashMap<>();
        String jti = UUID.randomUUID().toString();
        claims.put(Claims.SUBJECT, id.toString());
        claims.put(Claims.ID, jti);
        redisHelper.set(
                RedisKeyBuilder.userTokenJti(id),
                jti,
                Duration.ofMinutes(accessTokenExpire)
        );
        return rsaJwtUtil.buildToken(claims, Duration.ofMinutes(accessTokenExpire));
    }

    @Override
    public void validTokenAndRejectOld(Claims claims) throws JwtException{
        String userId = claims.getSubject();
        String jti = claims.getId();
        String stored = redisHelper.getValue(
                RedisKeyBuilder.userTokenJti(Long.parseLong(userId))
        );
        // we do not remove the redis key here
        // because we need to check the token again in the next request.
        if(stored == null || ! stored.equals(jti)){
            throw new AuthException(ResponseCode.TOKEN_INVALID);
        }
    }

    @Override
    public void revokeToken(Long userId) {
        redisHelper.del(
                RedisKeyBuilder.userTokenJti(userId)
        );
    }
}
