package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.request.PwdLoginReq;
import org.waterwood.shipmanagerment.api.dto.request.RegisterReq;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;
import org.waterwood.shipmanagerment.infrastructure.utils.UserContext;
import org.waterwood.shipmanagerment.service.AuthService;
import org.waterwood.shipmanagerment.service.CaptchaService;
import org.waterwood.shipmanagerment.service.LineCaptchaResult;
import org.waterwood.shipmanagerment.infrastructure.utils.CookieUtils;

import java.io.IOException;

import static org.waterwood.shipmanagerment.infrastructure.constants.KeyConstant.COOKIE_CAPTCHA_KEY;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CaptchaService captchaService;
    private final AuthService authService;

    public AuthController(CaptchaService captchaService, AuthService authService) {
        this.captchaService = captchaService;
        this.authService = authService;
    }

    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse res) throws IOException {
        LineCaptchaResult result = captchaService.generateCaptcha();
        Cookie cookie = new Cookie(COOKIE_CAPTCHA_KEY,result.key());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(120);
        res.addCookie(cookie);
        res.setContentType("image/jpeg");
        res.setHeader("Pragma", "No-cache");
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setDateHeader("Expires", 0);
        result.captcha().write(res.getOutputStream());
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterReq dto, HttpServletRequest req){
        String key =  CookieUtils.getCookie(req, COOKIE_CAPTCHA_KEY);
        log.info("key: {}", key);
        authService.register(dto, key);
        return ApiResponse.success();
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<LoginRes> login(@Valid @RequestBody PwdLoginReq dto, HttpServletRequest req){
        String key =  CookieUtils.getCookie(req, COOKIE_CAPTCHA_KEY);
        return ApiResponse.success(authService.loginByPwd(dto, key));
    }

    public ApiResponse<Void> logout(@Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        authService.logout(ctx.getUserId());
        return ApiResponse.success();
    }
}
