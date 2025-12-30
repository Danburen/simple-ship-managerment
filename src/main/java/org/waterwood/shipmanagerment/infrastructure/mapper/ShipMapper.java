package org.waterwood.shipmanagerment.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.waterwood.shipmanagerment.api.dto.request.ShipRequest;
import org.waterwood.shipmanagerment.api.dto.response.ShipResponse;
import org.waterwood.shipmanagerment.entity.Ship;

@Mapper(componentModel = "spring")
public interface ShipMapper {
    Ship toEntity(ShipRequest request);
    void updateShipFromRequest(ShipRequest request, @MappingTarget Ship ship);
    ShipResponse toResponse(Ship ship);
}
