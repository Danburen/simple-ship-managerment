package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShipRequest {
    @NotNull(message = "船舶名称不能为空")
    @Size(max = 100, message = "船舶名称不能超过100个字符")
    private String name;

    @Size(max = 50, message = "船舶类型不能超过50个字符")
    private String type;

    @Size(max = 50, message = "船舶状态不能超过50个字符")
    private String status;
}