package org.waterwood.shipmanagerment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.api.dto.request.VoyageRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.VoyageResponse;
import org.waterwood.shipmanagerment.entity.Voyage;

import java.util.List;

public interface VoyageService {
    Voyage createVoyage(VoyageRequest voyageRequest);

    Voyage getVoyageById(Long id);

    Voyage updateVoyage(Long id, VoyageRequest voyageRequest);

    void deleteVoyage(Long id);

    Page<Voyage> listVoyages(Specification<Voyage> spec, Pageable pageable);

    List<DictOptionDTO> getVoyageOptions();

    VoyageResponse toVoyageResponse(Voyage voyage);

    List<String> getVoyageStatusOptions();
}
