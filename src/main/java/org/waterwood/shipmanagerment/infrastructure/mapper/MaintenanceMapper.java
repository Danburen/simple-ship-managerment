package org.waterwood.shipmanagerment.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.waterwood.shipmanagerment.api.dto.request.MaintenanceRequest;
import org.waterwood.shipmanagerment.api.dto.response.MaintenanceResponse;
import org.waterwood.shipmanagerment.entity.Maintenance;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    
    // 将请求DTO转换为实体
    Maintenance toEntity(MaintenanceRequest request);
    
    // 从请求更新现有实体
    void updateMaintenanceFromRequest(MaintenanceRequest request, @MappingTarget Maintenance maintenance);
    
    // 将实体转换为响应DTO
    @Mapping(source = "ship", target = "ship")
    MaintenanceResponse toResponse(Maintenance maintenance);
}
