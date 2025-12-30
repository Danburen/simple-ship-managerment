package org.waterwood.shipmanagerment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.api.dto.request.PortRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.PortResponse;
import org.waterwood.shipmanagerment.entity.Port;

import java.util.List;

public interface PortService {
    Port createPort(PortRequest portRequest);

    Port getPortById(Long id);

    Port updatePort(Long id, PortRequest portRequest);

    void deletePort(Long id);

    Page<Port> listPorts(Specification<Port> spec, Pageable pageable);

    Page<Port> listPorts(Pageable pageable);

    List<DictOptionDTO> getPortOptions();

    PortResponse toPortResponse(Port port);
}
