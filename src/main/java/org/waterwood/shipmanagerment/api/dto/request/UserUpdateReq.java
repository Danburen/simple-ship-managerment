package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateReq {
    @Size(max = 50, message = "昵称不能超过50个字符")
    private String nickname;
}
