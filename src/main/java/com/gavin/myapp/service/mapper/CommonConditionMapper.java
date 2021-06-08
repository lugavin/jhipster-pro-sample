package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.CommonConditionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonCondition} and its DTO {@link CommonConditionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommonConditionItemMapper.class, CommonTableMapper.class })
public interface CommonConditionMapper extends EntityMapper<CommonConditionDTO, CommonCondition> {
    @Mapping(target = "items", source = "items", qualifiedByName = "fieldNameList")
    @Mapping(target = "commonTable", source = "commonTable", qualifiedByName = "name")
    CommonConditionDTO toDto(CommonCondition commonCondition);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "removeItems", ignore = true)
    CommonCondition toEntity(CommonConditionDTO commonConditionDTO);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CommonConditionDTO toDtoName(CommonCondition commonCondition);
}
