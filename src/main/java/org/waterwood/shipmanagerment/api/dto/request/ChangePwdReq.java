package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.waterwood.shipmanagerment.infrastructure.validation.StrongPassword;

/**
 * 修改密码请求DTO
 */
@Data
public class ChangePwdReq {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 16)
    @StrongPassword
    private String newPassword;
    @Size(min = 8, max = 16)
    private String confirmPassword;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
