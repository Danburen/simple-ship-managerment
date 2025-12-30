package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.dto.request.PortRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.PortResponse;
import org.waterwood.shipmanagerment.entity.Port;
import org.waterwood.shipmanagerment.infrastructure.mapper.PortMapper;
import org.waterwood.shipmanagerment.infrastructure.repository.PortRepository;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.PortSpec;
import org.waterwood.shipmanagerment.service.PortService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortServiceImpl implements PortService {
    private final PortRepository portRepository;
    private final PortMapper portMapper;

    public Port createPort(PortRequest portRequest) {
        Port port = this.portMapper.toEntity(portRequest);
        return (Port)this.portRepository.save(port);
    }

    public Port getPortById(Long id) {
        return (Port)this.portRepository.findById(id).orElseThrow(() -> new RuntimeException("港口不存在: " + id));
    }

    public Port updatePort(Long id, PortRequest portRequest) {
        Port port = this.getPortById(id);
        this.portMapper.updatePortFromRequest(portRequest, port);
        return (Port)this.portRepository.save(port);
    }

    public void deletePort(Long id) {
        Port port = this.getPortById(id);
        port.setIsDeleted(true);
        this.portRepository.save(port);
    }

    public Page<Port> listPorts(Specification<Port> spec, Pageable pageable) {
        return this.portRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Port> listPorts(Pageable pageable) {
        return this.portRepository.findAll(PortSpec.of(null, null), pageable);
    }

    @Override
    public List<DictOptionDTO> getPortOptions() {
        return this.portRepository.findAll(PortSpec.of(null,  null))
                .stream()
                .map(port -> new DictOptionDTO(port.getId(), port.getName(), port.getLocation()))
                .collect(Collectors.toList());
    }

    public PortResponse toPortResponse(Port port) {
        return this.portMapper.toResponse(port);
    }
}
