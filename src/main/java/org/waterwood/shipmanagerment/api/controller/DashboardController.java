package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.dto.response.DashboardResponse;
import org.waterwood.shipmanagerment.service.DashboardService;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "获取仪表盘统计数据")
    @GetMapping
    public ApiResponse<DashboardResponse> getDashboardData() {
        DashboardResponse dashboardResponse = new DashboardResponse(
                dashboardService.getShipCount(),
                dashboardService.getPortCount(),
                dashboardService.getMaintenanceCount(),
                dashboardService.getVoyageCount()
        );
        return ApiResponse.success(dashboardResponse);
    }
}
