package org.waterwood.shipmanagerment.service;

import cn.hutool.captcha.LineCaptcha;

public record LineCaptchaResult (String key, LineCaptcha captcha){
}
