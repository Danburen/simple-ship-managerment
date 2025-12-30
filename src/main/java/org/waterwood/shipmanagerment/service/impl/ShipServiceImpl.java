package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.dto.request.ShipRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.ShipResponse;
import org.waterwood.shipmanagerment.entity.Ship;
import org.waterwood.shipmanagerment.infrastructure.mapper.ShipMapper;
import org.waterwood.shipmanagerment.infrastructure.repository.ShipRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.ShipSpec;
import org.waterwood.shipmanagerment.service.ShipService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;
    private final ShipMapper shipMapper;
    public Ship createShip(ShipRequest shipRequest) {
        Ship ship = this.shipMapper.toEntity(shipRequest);
        return (Ship)this.shipRepository.save(ship);
    }

    public Ship getShipById(Long id) {
        return (Ship)this.shipRepository.findById(id).orElseThrow(() -> new RuntimeException("船舶不存在: " + id));
    }

    public Ship updateShip(Long id, ShipRequest shipRequest) {
        Ship ship = this.getShipById(id);
        this.shipMapper.updateShipFromRequest(shipRequest, ship);
        return (Ship)this.shipRepository.save(ship);
    }

    public void deleteShip(Long id) {
        Ship ship = this.getShipById(id);
        ship.setIsDeleted(true);
        this.shipRepository.save(ship);
    }

    public Page<Ship> listShips(Specification<Ship> spec, Pageable pageable) {
        return this.shipRepository.findAll(spec, pageable);
    }

    @Override
    public List<DictOptionDTO> getShipOptions() {
        return this.shipRepository.findAll(ShipSpec.of(null, null, null))
                .stream()
                .map(ship -> new DictOptionDTO(ship.getId(), ship.getName(), ship.getType()))
                .collect(Collectors.toList());
    }

    public ShipResponse toShipResponse(Ship ship) {
        return this.shipMapper.toResponse(ship);
    }

    @Override
    public List<String> getShipStatusOptions() {
        return List.of(
                "正常",
                "维修中",
                "停运",
                "已退役"
        );
    }

}
