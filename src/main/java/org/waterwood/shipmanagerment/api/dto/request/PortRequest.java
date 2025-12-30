package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PortRequest {
    @NotNull(message = "港口名称不能为空")
    @Size(max = 100, message = "港口名称不能超过100个字符")
    private String name;

    @Size(max = 255, message = "港口位置不能超过255个字符")
    private String location;
}