package org.waterwood.shipmanagerment.infrastructure.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Slf4j
public final class CookieUtils {
    public static @Nullable String getCookie(HttpServletRequest req, String Key) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(Key))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
