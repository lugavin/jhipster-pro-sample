package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.CommonTableRelationshipDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonTableRelationship} and its DTO {@link CommonTableRelationshipDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommonTableMapper.class, DataDictionaryMapper.class })
public interface CommonTableRelationshipMapper extends EntityMapper<CommonTableRelationshipDTO, CommonTableRelationship> {
    @Mapping(target = "relationEntity", source = "relationEntity", qualifiedByName = "name")
    @Mapping(target = "dataDictionaryNode", source = "dataDictionaryNode", qualifiedByName = "name")
    @Mapping(target = "metaModel", source = "metaModel", qualifiedByName = "name")
    @Mapping(target = "commonTable", source = "commonTable", qualifiedByName = "name")
    CommonTableRelationshipDTO toDto(CommonTableRelationship commonTableRelationship);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<CommonTableRelationshipDTO> toDtoNameList(List<CommonTableRelationship> commonTableRelationship);
}
