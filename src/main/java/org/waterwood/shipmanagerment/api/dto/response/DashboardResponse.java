package org.waterwood.shipmanagerment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仪表盘响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    
    /**
     * 船舶总数
     */
    private Long shipCount;
    
    /**
     * 港口总数
     */
    private Long portCount;
    
    /**
     * 维护记录总数
     */
    private Long maintenanceCount;
    
    /**
     * 航行记录总数
     */
    private Long voyageCount;
}
