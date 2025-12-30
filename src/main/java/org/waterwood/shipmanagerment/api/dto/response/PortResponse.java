package org.waterwood.shipmanagerment.api.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class PortResponse implements Serializable {
    private Long id;
    private String name;
    private String location;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
}