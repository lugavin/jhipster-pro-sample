package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.DataDictionarySimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataDictionary} and its DTO {@link DataDictionarySimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataDictionarySimpleMapper extends EntityMapper<DataDictionarySimpleDTO, DataDictionary> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DataDictionarySimpleDTO toDto(DataDictionary dataDictionary);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DataDictionary toEntity(DataDictionarySimpleDTO dataDictionaryDTO);
}
