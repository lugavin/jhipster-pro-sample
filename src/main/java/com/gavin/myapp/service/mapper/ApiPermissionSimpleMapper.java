package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.ApiPermissionSimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApiPermission} and its DTO {@link ApiPermissionSimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiPermissionSimpleMapper extends EntityMapper<ApiPermissionSimpleDTO, ApiPermission> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ApiPermissionSimpleDTO toDto(ApiPermission apiPermission);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ApiPermission toEntity(ApiPermissionSimpleDTO apiPermissionDTO);
}
