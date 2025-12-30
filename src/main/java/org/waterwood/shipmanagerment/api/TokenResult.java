package org.waterwood.shipmanagerment.api;

/**
 * TokenResult
 *
 * @param token
 * @param expire
 */
public record TokenResult(String token, Long expire){}