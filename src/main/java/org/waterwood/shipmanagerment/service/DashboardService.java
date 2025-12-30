package org.waterwood.shipmanagerment.service;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {
    
    /**
     * 获取船舶总数
     * @return 船舶总数
     */
    Long getShipCount();
    
    /**
     * 获取港口总数
     * @return 港口总数
     */
    Long getPortCount();
    
    /**
     * 获取维护记录总数
     * @return 维护记录总数
     */
    Long getMaintenanceCount();
    
    /**
     * 获取航行记录总数
     * @return 航行记录总数
     */
    Long getVoyageCount();
}
