package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.infrastructure.repository.MaintenanceRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.PortRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.ShipRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.VoyageRepository;
import org.waterwood.shipmanagerment.service.DashboardService;

/**
 * 仪表盘服务实现类
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ShipRepository shipRepository;
    private final PortRepository portRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final VoyageRepository voyageRepository;

    @Override
    public Long getShipCount() {
        return shipRepository.count();
    }

    @Override
    public Long getPortCount() {
        return portRepository.count();
    }

    @Override
    public Long getMaintenanceCount() {
        return maintenanceRepository.count();
    }

    @Override
    public Long getVoyageCount() {
        return voyageRepository.count();
    }
}
