package org.waterwood.shipmanagerment.service;

public interface TurnstileService {
    TurnstileResponse validateToken(String token, String remoteIp);
}
