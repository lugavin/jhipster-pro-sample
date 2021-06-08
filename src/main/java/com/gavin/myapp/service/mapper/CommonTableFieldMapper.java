package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonTableField} and its DTO {@link CommonTableFieldDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommonTableMapper.class })
public interface CommonTableFieldMapper extends EntityMapper<CommonTableFieldDTO, CommonTableField> {
    @Mapping(target = "metaModel", source = "metaModel", qualifiedByName = "name")
    @Mapping(target = "commonTable", source = "commonTable", qualifiedByName = "name")
    CommonTableFieldDTO toDto(CommonTableField commonTableField);

    @Named("titleList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ArrayList<CommonTableFieldDTO> toDtoTitleList(List<CommonTableField> commonTableField);
}
