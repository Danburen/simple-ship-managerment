package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.waterwood.shipmanagerment.infrastructure.validation.StrongPassword;
import org.waterwood.shipmanagerment.infrastructure.validation.Username;

@Data
public class PwdLoginReq {
    @Username
    @Size(min = 4, max = 16)
    private String username;
    @StrongPassword
    @Size(min = 8, max = 16)
    private String password;
    @NotEmpty
    private String verifyCode;
}
