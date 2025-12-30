package org.waterwood.shipmanagerment.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;
import org.waterwood.shipmanagerment.infrastructure.component.RedisHelperHolder;
import org.waterwood.shipmanagerment.infrastructure.exception.BizException;
import org.waterwood.shipmanagerment.service.CaptchaService;
import org.waterwood.shipmanagerment.service.RedisKeyBuilder;
import org.waterwood.shipmanagerment.service.LineCaptchaResult;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final RedisHelperHolder redisHelper;
    @Override
    public LineCaptchaResult generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 30, 4, 10);
        String key = UUID.randomUUID().toString();
        String code = captcha.getCode();
        redisHelper.set(RedisKeyBuilder.captcha(key), code, Duration.ofMinutes(5));
        return new LineCaptchaResult(key, captcha);
    }

    @Override
    public void VerifyCaptcha(String code, String key) {
        log.info("VerifyCaptcha. key: {}, code: {}", key, code);
        if(key == null || code == null)
        {
            throw new BizException(ResponseCode.CAPTCHA_CODE_INCORRECT);
        }
        if(! redisHelper.validateAndRemove(RedisKeyBuilder.captcha(key), code)){
            throw new BizException(ResponseCode.CAPTCHA_CODE_INCORRECT);
        }
    }
}
