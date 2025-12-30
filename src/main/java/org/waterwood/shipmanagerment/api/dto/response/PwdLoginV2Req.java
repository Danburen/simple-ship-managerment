package org.waterwood.shipmanagerment.api.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.waterwood.shipmanagerment.infrastructure.validation.StrongPassword;
import org.waterwood.shipmanagerment.infrastructure.validation.Username;

@Data
@AllArgsConstructor
public class PwdLoginV2Req {
    @Username
    @Size(min = 4, max = 16)
    private String username;
    @StrongPassword
    @Size(min = 8, max = 16)
    private String password;
    @NotEmpty
    private String cfTurnstileToken;
}
