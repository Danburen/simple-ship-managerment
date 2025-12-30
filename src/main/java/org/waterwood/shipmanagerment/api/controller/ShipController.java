package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.request.ShipRequest;
import org.waterwood.shipmanagerment.api.dto.response.DictOptionDTO;
import org.waterwood.shipmanagerment.api.dto.response.ShipResponse;
import org.waterwood.shipmanagerment.entity.Ship;
import org.waterwood.shipmanagerment.infrastructure.repository.specification.ShipSpec;
import org.waterwood.shipmanagerment.service.ShipService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ship")
public class ShipController {
    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @Operation(summary = "获取船舶列表")
    @GetMapping("/list")
    public ApiResponse<Page<ShipResponse>> listShips(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Ship> ships = shipService.listShips(ShipSpec.of( name, type, status), pageable);
        Page<ShipResponse> shipResponses = ships.map(shipService::toShipResponse);
        return ApiResponse.success(shipResponses);
    }

    @Operation(summary = "获取船舶详情")
    @GetMapping("/{id}")
    public ApiResponse<ShipResponse> getShipById(@PathVariable Long id) {
        Ship ship = shipService.getShipById(id);
        ShipResponse shipResponse = shipService.toShipResponse(ship);
        return ApiResponse.success(shipResponse);
    }

    @Operation(summary = "新增船舶")
    @PostMapping
    public ApiResponse<ShipResponse> createShip(@RequestBody ShipRequest shipRequest) {
        Ship ship = shipService.createShip(shipRequest);
        ShipResponse shipResponse = shipService.toShipResponse(ship);
        return ApiResponse.success(shipResponse);
    }

    @Operation(summary = "更新船舶信息")
    @PutMapping("/{id}")
    public ApiResponse<ShipResponse> updateShip(@PathVariable Long id, @RequestBody ShipRequest shipRequest) {
        Ship ship = shipService.updateShip(id, shipRequest);
        ShipResponse shipResponse = shipService.toShipResponse(ship);
        return ApiResponse.success(shipResponse);
    }

    @Operation(summary = "删除船舶")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteShip(@PathVariable Long id) {
        shipService.deleteShip(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取船舶选项列表")
    @GetMapping("/options")
    public ApiResponse<List<DictOptionDTO>> getShipOptions() {
        return ApiResponse.success(shipService.getShipOptions());
    }

    @Operation(summary = "获取船舶状态选项列表")
    @GetMapping("/status/options")
    public ApiResponse<List<String>> getShipStatusOptions() {
        return ApiResponse.success(shipService.getShipStatusOptions());
    }
}