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
import org.waterwood.shipmanagerment.api.dto.request.RegisterV2Req;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;
import org.waterwood.shipmanagerment.api.dto.response.PwdLoginV2Req;
import org.waterwood.shipmanagerment.infrastructure.utils.RequestProcessor;
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
    public ApiResponse<Void> register(@Valid @RequestBody RegisterV2Req dto, HttpServletRequest req){
        String key =  CookieUtils.getCookie(req, COOKIE_CAPTCHA_KEY);
        authService.tryRegister(dto, RequestProcessor.getRemoteIp(req));
        return ApiResponse.success();
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<LoginRes> login(@Valid @RequestBody PwdLoginV2Req dto, HttpServletRequest req){
        return ApiResponse.success(authService.tryLoginByPwd(dto, RequestProcessor.getRemoteIp(req)));
    }
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        authService.logout(ctx.getUserId());
        return ApiResponse.success();
    }
}
