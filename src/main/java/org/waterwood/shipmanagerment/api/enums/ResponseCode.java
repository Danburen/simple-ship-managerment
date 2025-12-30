package org.waterwood.shipmanagerment.api.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {
    CAPTCHA_CODE_INCORRECT("verify.captcha_code.incorrect", "图形验证不正确"),
    USER_ALREADY_EXISTS("user.already_exists", "用户已存在。"),
    PASSWORD_NOT_MATCH("valid.password_not_match", "两次密码不一致"),
    USER_NOT_FOUND("user.not_found","用户不存在" ),
    USERNAME_OR_PASSWORD_INCORRECT("verify.username_or_password_incorrect", "用户名或密码不正确"),
    TOKEN_INVALID("token.invalid_or_expired", "登陆凭证过期，请重新登陆。"),
    INTERNAL_SERVER_ERROR("http.internal_server_error", "服务器内部错误"),
    UNKNOWN_ERROR("unknown", "未知错误"),
    FORBIDDEN("http.forbidden", "服务器拒绝访问"),
    VALIDATION_ERROR("valid.error", "参数错误"),
    VALIDATION_ENUM_NOT_SUPPORT("valid.enum_not_support", "枚举 {field} 不支持 {value}, 可用枚举 {availableValues}"),
    PASSWORD_INCORRECT("verify.password_incorrect", "密码不正确"),
    NEED_FILE_TYPE("valid.file_type_required", "需要指定的文件类型。");
    private final String code;
    private final String message;
    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
