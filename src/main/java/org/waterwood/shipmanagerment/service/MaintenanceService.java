package org.waterwood.shipmanagerment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.api.dto.request.MaintenanceRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.MaintenanceResponse;
import org.waterwood.shipmanagerment.entity.Maintenance;

import java.util.List;

public interface MaintenanceService {
    Maintenance createMaintenance(MaintenanceRequest maintenanceRequest);

    Maintenance getMaintenanceById(Long id);

    Maintenance updateMaintenance(Long id, MaintenanceRequest maintenanceRequest);

    void deleteMaintenance(Long id);

    Page<Maintenance> listMaintenances(Specification<Maintenance> spec, Pageable pageable);

    List<DictOptionDTO> getMaintenanceOptions();

    MaintenanceResponse toMaintenanceResponse(Maintenance maintenance);
    List<String> getMaintenanceStatusOptions();
}
