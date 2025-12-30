package org.waterwood.shipmanagerment.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.waterwood.shipmanagerment.api.dto.request.VoyageRequest;
import org.waterwood.shipmanagerment.api.dto.response.VoyageResponse;
import org.waterwood.shipmanagerment.entity.Voyage;

@Mapper(componentModel = "spring")
public interface VoyageMapper {
    
    // 将请求DTO转换为实体
    Voyage toEntity(VoyageRequest request);
    
    // 从请求更新现有实体
    void updateVoyageFromRequest(VoyageRequest request, @MappingTarget Voyage voyage);
    
    // 将实体转换为响应DTO
    VoyageResponse toResponse(Voyage voyage);
}
