package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class VoyageRequest {
    @NotNull(message = "船舶ID不能为空")
    private Long shipId;

    @NotNull(message = "出发港口ID不能为空")
    private Long departurePortId;

    @NotNull(message = "到达港口ID不能为空")
    private Long arrivalPortId;

    private Instant departureTime;
    private Instant arrivalTime;
    private String log;
    private String status;
}