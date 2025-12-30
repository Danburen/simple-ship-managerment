package org.waterwood.shipmanagerment.infrastructure.mapper;

import org.mapstruct.*;
import org.waterwood.shipmanagerment.api.dto.request.UserUpdateReq;
import org.waterwood.shipmanagerment.api.dto.response.UserInfoRes;
import org.waterwood.shipmanagerment.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserInfoRes userInfoRes);

    UserInfoRes toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserInfoRes userInfoRes, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserUpdateReq req, @MappingTarget User user);
}