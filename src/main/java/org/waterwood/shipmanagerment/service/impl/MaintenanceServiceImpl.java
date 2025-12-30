package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.dto.request.MaintenanceRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.MaintenanceResponse;
import org.waterwood.shipmanagerment.entity.Maintenance;
import org.waterwood.shipmanagerment.entity.Ship;
import org.waterwood.shipmanagerment.infrastructure.mapper.MaintenanceMapper;
import org.waterwood.shipmanagerment.infrastructure.repository.MaintenanceRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.ShipRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.MaintenanceSpec;
import org.waterwood.shipmanagerment.service.MaintenanceService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final ShipRepository shipRepository;
    private final MaintenanceMapper maintenanceMapper;

    public Maintenance createMaintenance(MaintenanceRequest maintenanceRequest) {
        Ship ship = (Ship)shipRepository.findById(maintenanceRequest.getShipId()).orElseThrow(() -> new RuntimeException("船舶不存在: " + maintenanceRequest.getShipId()));
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceRequest);
        maintenance.setShip(ship);
        return (Maintenance)maintenanceRepository.save(maintenance);
    }

    public Maintenance getMaintenanceById(Long id) {
        return (Maintenance)maintenanceRepository.findById(id).orElseThrow(() -> new RuntimeException("维护记录不存在: " + id));
    }

    public Maintenance updateMaintenance(Long id, MaintenanceRequest maintenanceRequest) {
        Maintenance existingMaintenance = getMaintenanceById(id);
        if (maintenanceRequest.getShipId() != null) {
            Ship ship = (Ship)shipRepository.findById(maintenanceRequest.getShipId()).orElseThrow(() -> new RuntimeException("船舶不存在: " + maintenanceRequest.getShipId()));
            existingMaintenance.setShip(ship);
        }

        maintenanceMapper.updateMaintenanceFromRequest(maintenanceRequest, existingMaintenance);
        return (Maintenance)maintenanceRepository.save(existingMaintenance);
    }

    public void deleteMaintenance(Long id) {
        Maintenance maintenance = getMaintenanceById(id);
        maintenance.setIsDeleted(true);
        maintenanceRepository.save(maintenance);
    }

    public Page<Maintenance> listMaintenances(Specification<Maintenance> spec, Pageable pageable) {
        return maintenanceRepository.findAll(spec, pageable);
    }

    @Override
    public List<DictOptionDTO> getMaintenanceOptions() {
        return maintenanceRepository.findAll(MaintenanceSpec.of(null,null, null, null))
                .stream()
                .map(maintenance -> new DictOptionDTO(maintenance.getId(), "维护记录 " + maintenance.getId(), null))
                .collect(Collectors.toList());
    }

    public MaintenanceResponse toMaintenanceResponse(Maintenance maintenance) {
        return maintenanceMapper.toResponse(maintenance);
    }

    @Override
    public List<String> getMaintenanceStatusOptions() {
        return List.of(
                "待维护",
                "维护中",
                "已完成"
        );
    }
}
