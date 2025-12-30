package org.waterwood.shipmanagerment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.api.dto.request.ShipRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.ShipResponse;
import org.waterwood.shipmanagerment.entity.Ship;

import java.util.List;

public interface ShipService {
    Ship createShip(ShipRequest shipRequest);

    Ship getShipById(Long id);

    Ship updateShip(Long id, ShipRequest shipRequest);

    void deleteShip(Long id);

    Page<Ship> listShips(Specification<Ship> spec, Pageable pageable);

    List<DictOptionDTO> getShipOptions();

    ShipResponse toShipResponse(Ship ship);

    List<String> getShipStatusOptions();
}
