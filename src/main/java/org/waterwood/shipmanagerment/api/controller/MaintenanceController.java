package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.request.MaintenanceRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.MaintenanceResponse;
import org.waterwood.shipmanagerment.entity.Maintenance;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.MaintenanceSpec;
import org.waterwood.shipmanagerment.service.MaintenanceService;
import org.waterwood.shipmanagerment.service.ShipService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;


    @Operation(summary = "获取维护记录列表")
    @GetMapping("/list")
    public ApiResponse<Page<MaintenanceResponse>> listMaintenances(
            @RequestParam(required = false) Long shipId,
            @RequestParam(required = false) String shipName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Maintenance> maintenances = maintenanceService.listMaintenances(
                MaintenanceSpec.of(shipId, shipName, description, status),
                pageable
        );
        Page<MaintenanceResponse> maintenanceResponses = maintenances.map(maintenanceService::toMaintenanceResponse);
        return ApiResponse.success(maintenanceResponses);
    }

    @Operation(summary = "获取维护记录详情")
    @GetMapping("/{id}")
    public ApiResponse<MaintenanceResponse> getMaintenanceById(@PathVariable Long id) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id);
        MaintenanceResponse maintenanceResponse = maintenanceService.toMaintenanceResponse(maintenance);
        return ApiResponse.success(maintenanceResponse);
    }

    @Operation(summary = "新增维护记录")
    @PostMapping
    public ApiResponse<MaintenanceResponse> createMaintenance(@RequestBody MaintenanceRequest maintenanceRequest) {
        Maintenance maintenance = maintenanceService.createMaintenance(maintenanceRequest);
        MaintenanceResponse maintenanceResponse = maintenanceService.toMaintenanceResponse(maintenance);
        return ApiResponse.success(maintenanceResponse);
    }

    @Operation(summary = "更新维护记录")
    @PutMapping("/{id}")
    public ApiResponse<MaintenanceResponse> updateMaintenance(@PathVariable Long id, @RequestBody MaintenanceRequest maintenanceRequest) {
        Maintenance maintenance = maintenanceService.updateMaintenance(id, maintenanceRequest);
        MaintenanceResponse maintenanceResponse = maintenanceService.toMaintenanceResponse(maintenance);
        return ApiResponse.success(maintenanceResponse);
    }

    @Operation(summary = "删除维护记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取维护记录选项列表")
    @GetMapping("/options")
    public ApiResponse<List<DictOptionDTO>> getMaintenanceOptions() {
        return ApiResponse.success(maintenanceService.getMaintenanceOptions());
    }

    @Operation(summary = "获取维护记录状态选项列表")
    @GetMapping("/status/options")
    public ApiResponse<List<String>> getMaintenanceStatusOptions(){
        return ApiResponse.success(maintenanceService.getMaintenanceStatusOptions());
    }
}