package org.waterwood.shipmanagerment.api.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MaintenanceResponse implements Serializable {
    private Long id;
    private ShipResponse ship;
    private Instant maintenanceDate;
    private String description;
    private String status;
    private BigDecimal cost;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
}