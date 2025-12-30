package org.waterwood.shipmanagerment.api.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class VoyageResponse implements Serializable {
    private Long id;
    private ShipResponse ship;
    private PortResponse departurePort;
    private PortResponse arrivalPort;
    private Instant departureTime;
    private Instant arrivalTime;
    private String status;
    private String log;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
}