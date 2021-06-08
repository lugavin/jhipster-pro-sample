package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.ViewPermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ViewPermission} and its DTO {@link ViewPermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApiPermissionMapper.class, AuthorityMapper.class, ViewPermissionSimpleMapper.class })
public interface ViewPermissionMapper extends EntityMapper<ViewPermissionDTO, ViewPermission> {
    @Mapping(source = "parent", target = "parent")
    ViewPermissionDTO toDto(ViewPermission viewPermission);

    @Named("MapperNoChildren")
    @Mapping(source = "parent", target = "parent")
    @Mapping(target = "children", ignore = true)
    ViewPermissionDTO toNoChildrenDto(ViewPermission viewPermission);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    // @Mapping(target = "removeApiPermissions", ignore = true)
    // @Mapping(target = "roles", ignore = true)
    // @Mapping(target = "removeRoles", ignore = true)
    ViewPermission toEntity(ViewPermissionDTO viewPermissionDTO);

    default ViewPermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        ViewPermission viewPermission = new ViewPermission();
        viewPermission.setId(id);
        return viewPermission;
    }

    @Named("text")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    default ViewPermissionDTO toDtoText(ViewPermission viewPermission) {
        if (viewPermission == null) {
            return null;
        }
        ViewPermissionDTO viewPermissionDTO = new ViewPermissionDTO();
        viewPermissionDTO.setId(viewPermission.getId());
        viewPermissionDTO.setText(viewPermission.getText());
        return viewPermissionDTO;
    }
}
