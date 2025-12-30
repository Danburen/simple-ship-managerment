package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.request.VoyageRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.VoyageResponse;
import org.waterwood.shipmanagerment.entity.Voyage;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.VoyageSpec;
import org.waterwood.shipmanagerment.service.VoyageService;

import java.util.List;

@RestController
@RequestMapping("/api/voyage")
public class VoyageController {
    private final VoyageService voyageService;

    public VoyageController(VoyageService voyageService) {
        this.voyageService = voyageService;
    }

    @Operation(summary = "获取航行记录列表")
    @GetMapping("/list")
    public ApiResponse<Page<VoyageResponse>> listVoyages(
            @RequestParam(required = false) Long shipId,
            @RequestParam(required = false) Long departurePortId,
            @RequestParam(required = false) Long arrivalPortId,
            @RequestParam(required = false) String status,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Voyage> voyages = voyageService.listVoyages(VoyageSpec.of(shipId, departurePortId, arrivalPortId,  status), pageable);
        Page<VoyageResponse> voyageResponses = voyages.map(voyageService::toVoyageResponse);
        return ApiResponse.success(voyageResponses);
    }

    @Operation(summary = "获取航行记录详情")
    @GetMapping("/{id}")
    public ApiResponse<VoyageResponse> getVoyageById(@PathVariable Long id) {
        Voyage voyage = voyageService.getVoyageById(id);
        VoyageResponse voyageResponse = voyageService.toVoyageResponse(voyage);
        return ApiResponse.success(voyageResponse);
    }

    @Operation(summary = "新增航行记录")
    @PostMapping
    public ApiResponse<VoyageResponse> createVoyage(@RequestBody VoyageRequest voyageRequest) {
        Voyage voyage = voyageService.createVoyage(voyageRequest);
        VoyageResponse voyageResponse = voyageService.toVoyageResponse(voyage);
        return ApiResponse.success(voyageResponse);
    }

    @Operation(summary = "更新航行记录")
    @PutMapping("/{id}")
    public ApiResponse<VoyageResponse> updateVoyage(@PathVariable Long id, @RequestBody VoyageRequest voyageRequest) {
        Voyage voyage = voyageService.updateVoyage(id, voyageRequest);
        VoyageResponse voyageResponse = voyageService.toVoyageResponse(voyage);
        return ApiResponse.success(voyageResponse);
    }

    @Operation(summary = "删除航行记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVoyage(@PathVariable Long id) {
        voyageService.deleteVoyage(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取航行记录选项列表")
    @GetMapping("/options")
    public ApiResponse<List<DictOptionDTO>> getVoyageOptions() {
        return ApiResponse.success(voyageService.getVoyageOptions());
    }

    @Operation(summary = "获取航行记录状态选项列表")
    @GetMapping("/status/options")
    public ApiResponse<List<String>> getVoyageStatusOptions(){
        return ApiResponse.success(voyageService.getVoyageStatusOptions());
    }
}