package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新个人资料请求DTO
 */
@Data
public class UpdateProfileRequest {
    
    @Size(max = 30, message = "昵称不能超过30个字符")
    private String nickname;
    
    private String avatarUrl;
}
