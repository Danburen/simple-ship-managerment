package org.waterwood.shipmanagerment.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MaintenanceRequest {
    @NotNull(message = "船舶ID不能为空")
    private Long shipId;
    
    private Instant maintenanceDate;
    private String description;
    private BigDecimal cost;
}