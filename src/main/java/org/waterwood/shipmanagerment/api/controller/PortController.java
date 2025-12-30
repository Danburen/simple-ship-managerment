package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.request.PortRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.PortResponse;
import org.waterwood.shipmanagerment.entity.Port;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.PortSpec;
import org.waterwood.shipmanagerment.service.PortService;

import java.util.List;

@RestController
@RequestMapping("/api/port")
public class PortController {
    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }

    @Operation(summary = "获取港口列表")
    @GetMapping("/list")
    public ApiResponse<Page<PortResponse>> listPorts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Port> ports = portService.listPorts(PortSpec.of(name, location), pageable);
        Page<PortResponse> portResponses = ports.map(portService::toPortResponse);
        return ApiResponse.success(portResponses);
    }

    @Operation(summary = "获取港口详情")
    @GetMapping("/{id}")
    public ApiResponse<PortResponse> getPortById(@PathVariable Long id) {
        Port port = portService.getPortById(id);
        PortResponse portResponse = portService.toPortResponse(port);
        return ApiResponse.success(portResponse);
    }

    @Operation(summary = "新增港口")
    @PostMapping
    public ApiResponse<PortResponse> createPort(@RequestBody PortRequest portRequest) {
        Port port = portService.createPort(portRequest);
        PortResponse portResponse = portService.toPortResponse(port);
        return ApiResponse.success(portResponse);
    }

    @Operation(summary = "更新港口信息")
    @PutMapping("/{id}")
    public ApiResponse<PortResponse> updatePort(@PathVariable Long id, @RequestBody PortRequest portRequest) {
        Port port = portService.updatePort(id, portRequest);
        PortResponse portResponse = portService.toPortResponse(port);
        return ApiResponse.success(portResponse);
    }

    @Operation(summary = "删除港口")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePort(@PathVariable Long id) {
        portService.deletePort(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取港口选项列表")
    @GetMapping("/options")
    public ApiResponse<List<DictOptionDTO>> getPortOptions() {
        return ApiResponse.success(portService.getPortOptions());
    }
}