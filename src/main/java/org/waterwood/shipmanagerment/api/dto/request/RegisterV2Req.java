package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.waterwood.shipmanagerment.infrastructure.validation.StrongPassword;
import org.waterwood.shipmanagerment.infrastructure.validation.Username;

@Data
public class RegisterV2Req {
    @Size(min = 4, max = 16)
    @Username
    private String username;
    @Size(min = 6, max = 16)
    @StrongPassword
    private String password;
    @NotEmpty
    private String confirmPwd;
    @Size(min = 2 , max = 16)
    private String nickname;
    @NotEmpty
    private String cfTurnstileToken;
}
