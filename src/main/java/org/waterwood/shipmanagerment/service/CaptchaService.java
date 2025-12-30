package org.waterwood.shipmanagerment.service;

import org.waterwood.shipmanagerment.infrastructure.exception.BizException;

/**
 * A class to generate captcha and verify captcha codes
 * use hutools
 */
public interface CaptchaService {
    LineCaptchaResult generateCaptcha();

    /**
     * Verify a captcha code with the store captcha key in redis
     *
     * @param code captcha code
     * @param key  captcha key
     * @throws BizException if the code is incorrect
     */
    void VerifyCaptcha(String code, String key) throws BizException;
}
