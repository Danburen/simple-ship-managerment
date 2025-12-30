package org.waterwood.shipmanagerment.service;

import org.waterwood.shipmanagerment.infrastructure.utils.StringUtil;

import static org.waterwood.shipmanagerment.infrastructure.constants.KeyConstant.*;

public final class RedisKeyBuilder {
    public static String user(long userId){
        return USER + ":" + userId;
    }

    public static String userTokenJti(long userId){
        return user(userId) + ":" + JTI;
    }

    public static String captcha(String value){
        return VERIFY + ":" + CAPTCHA + ":" + value;
    }

    public static String fs(){
        return "cloud:fs";
    }

    public static String buildKey(String... segments) {
        return StringUtil.buildPath(':', segments);
    }
}
