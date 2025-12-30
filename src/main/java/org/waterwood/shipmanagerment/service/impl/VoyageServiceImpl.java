package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.dto.request.VoyageRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.VoyageResponse;
import org.waterwood.shipmanagerment.entity.Port;
import org.waterwood.shipmanagerment.entity.Ship;
import org.waterwood.shipmanagerment.entity.Voyage;
import org.waterwood.shipmanagerment.infrastructure.mapper.VoyageMapper;
import org.waterwood.shipmanagerment.infrastructure.repository.PortRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.ShipRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.VoyageRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.VoyageSpec;
import org.waterwood.shipmanagerment.service.VoyageService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoyageServiceImpl implements VoyageService {
    private final VoyageRepository voyageRepository;
    private final ShipRepository shipRepository;
    private final PortRepository portRepository;
    private final VoyageMapper voyageMapper;
    public Voyage createVoyage(VoyageRequest voyageRequest) {
        Ship ship = shipRepository.findById(voyageRequest.getShipId()).orElseThrow(() -> new RuntimeException("船舶不存在: " + voyageRequest.getShipId()));
        Port departurePort = portRepository.findById(voyageRequest.getDeparturePortId()).orElseThrow(() -> new RuntimeException("出发港口不存在: " + voyageRequest.getDeparturePortId()));
        Port arrivalPort = portRepository.findById(voyageRequest.getArrivalPortId()).orElseThrow(() -> new RuntimeException("到达港口不存在: " + voyageRequest.getArrivalPortId()));
        Voyage voyage = voyageMapper.toEntity(voyageRequest);
        voyage.setShip(ship);
        voyage.setDeparturePort(departurePort);
        voyage.setArrivalPort(arrivalPort);
        return voyageRepository.save(voyage);
    }

    public Voyage getVoyageById(Long id) {
        return voyageRepository.findById(id).orElseThrow(() -> new RuntimeException("航行记录不存在: " + id));
    }

    public Voyage updateVoyage(Long id, VoyageRequest voyageRequest) {
        Voyage existingVoyage = getVoyageById(id);
        if (voyageRequest.getShipId() != null) {
            Ship ship = shipRepository.findById(voyageRequest.getShipId()).orElseThrow(() -> new RuntimeException("船舶不存在: " + voyageRequest.getShipId()));
            existingVoyage.setShip(ship);
        }

        if (voyageRequest.getDeparturePortId() != null) {
            Port departurePort = portRepository.findById(voyageRequest.getDeparturePortId()).orElseThrow(() -> new RuntimeException("出发港口不存在: " + voyageRequest.getDeparturePortId()));
            existingVoyage.setDeparturePort(departurePort);
        }

        if (voyageRequest.getArrivalPortId() != null) {
            Port arrivalPort = portRepository.findById(voyageRequest.getArrivalPortId()).orElseThrow(() -> new RuntimeException("到达港口不存在: " + voyageRequest.getArrivalPortId()));
            existingVoyage.setArrivalPort(arrivalPort);
        }

        voyageMapper.updateVoyageFromRequest(voyageRequest, existingVoyage);
        return voyageRepository.save(existingVoyage);
    }

    public void deleteVoyage(Long id) {
        Voyage voyage = getVoyageById(id);
        voyage.setIsDeleted(true);
        voyageRepository.save(voyage);
    }

    public Page<Voyage> listVoyages(Specification<Voyage> spec, Pageable pageable) {
        return voyageRepository.findAll(spec, pageable);
    }

    @Override
    public List<DictOptionDTO> getVoyageOptions() {
        return voyageRepository.findAll(VoyageSpec.of(null, null, null, null))
                .stream()
                .map(voyage -> new DictOptionDTO(voyage.getId(), "航行记录 " + voyage.getId(), null))
                .collect(Collectors.toList());
    }

    public VoyageResponse toVoyageResponse(Voyage voyage) {
        return voyageMapper.toResponse(voyage);
    }

    @Override
    public List<String> getVoyageStatusOptions() {
        return List.of(
                "计划中",
                "航行中",
                "已到达",
                "已完成"
        );
    }
}
