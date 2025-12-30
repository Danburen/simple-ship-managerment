package org.waterwood.shipmanagerment.service;

import io.jsonwebtoken.Claims;
import org.waterwood.shipmanagerment.api.TokenResult;

/**
 * A service to manager token storage and retrieval.
 */
public interface TokenService {
    /**
     * Generate a jwt token with user id
     * @param id user id
     * @return jwt token
     */
    TokenResult generateToken(Long id);

    /**
     * Validate token and revoke the old one clean the cache jti memory.
     * @param claims jwt token
     * @throws org.waterwood.shipmanagerment.infrastructure.exception.AuthException if token is invalid
     */
    void validTokenAndRejectOld(Claims claims);

    /**
     * Revoke token
     * @param userId user id
     */
    void revokeToken(Long userId);
}
