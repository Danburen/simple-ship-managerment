package org.waterwood.shipmanagerment.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestProcessor {
    public static String getRemoteIp(HttpServletRequest req) {
        String remoteip = req.getHeader("CF-Connecting-IP");
        if (remoteip == null) {
            remoteip = req.getHeader("X-Forwarded-For");
        }
        if (remoteip == null) {
            remoteip = req.getRemoteAddr();
        }
        return remoteip;
    }
}
