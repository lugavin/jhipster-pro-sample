package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.CommonConditionItemDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonConditionItem} and its DTO {@link CommonConditionItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommonConditionMapper.class })
public interface CommonConditionItemMapper extends EntityMapper<CommonConditionItemDTO, CommonConditionItem> {
    @Mapping(target = "commonCondition", source = "commonCondition", qualifiedByName = "name")
    CommonConditionItemDTO toDto(CommonConditionItem commonConditionItem);

    @Named("fieldNameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fieldName", source = "fieldName")
    ArrayList<CommonConditionItemDTO> toDtoFieldNameList(List<CommonConditionItem> commonConditionItem);
}
