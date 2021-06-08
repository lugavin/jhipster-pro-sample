package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.ResourceCategoryDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceCategory} and its DTO {@link ResourceCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { ResourceCategorySimpleMapper.class })
public interface ResourceCategoryMapper extends EntityMapper<ResourceCategoryDTO, ResourceCategory> {
    @Mapping(target = "children", source = "children", qualifiedByName = "titleList")
    @Mapping(target = "parent", source = "parent")
    ResourceCategoryDTO toDto(ResourceCategory resourceCategory);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    ResourceCategory toEntity(ResourceCategoryDTO resourceCategoryDTO);

    @Named("title")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ResourceCategoryDTO toDtoTitle(ResourceCategory resourceCategory);

    @Named("titleList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ArrayList<ResourceCategoryDTO> toDtoTitleList(List<ResourceCategory> resourceCategory);
}
