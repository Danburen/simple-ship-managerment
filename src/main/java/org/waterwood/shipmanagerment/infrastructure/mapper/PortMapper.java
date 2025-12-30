package org.waterwood.shipmanagerment.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.waterwood.shipmanagerment.api.dto.request.PortRequest;
import org.waterwood.shipmanagerment.api.dto.response.PortResponse;
import org.waterwood.shipmanagerment.entity.Port;

@Mapper(componentModel = "spring")
public interface PortMapper {
    
    // 将请求DTO转换为实体
    Port toEntity(PortRequest request);
    
    // 从请求更新现有实体
    void updatePortFromRequest(PortRequest request, @MappingTarget Port port);
    
    // 将实体转换为响应DTO
    PortResponse toResponse(Port port);
}
