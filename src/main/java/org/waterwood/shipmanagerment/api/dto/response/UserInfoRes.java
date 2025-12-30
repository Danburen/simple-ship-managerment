package org.waterwood.shipmanagerment.api.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.waterwood.shipmanagerment.entity.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRes implements Serializable {
    private Long id;
    @NotNull
    @Size(max = 30)
    private String username;
    private CloudResPresignedUrlResp avatar;
    private Instant createdAt;
    @NotNull
    @Size(max = 30)
    private String nickname;
}