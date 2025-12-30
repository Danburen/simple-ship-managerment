package org.waterwood.shipmanagerment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
public class LoginRes implements Serializable {
    private String token;
    private Instant expireAt;
}
