package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.AdministrativeDivisionDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdministrativeDivision} and its DTO {@link AdministrativeDivisionDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdministrativeDivisionSimpleMapper.class })
public interface AdministrativeDivisionMapper extends EntityMapper<AdministrativeDivisionDTO, AdministrativeDivision> {
    @Mapping(target = "children", source = "children", qualifiedByName = "nameList")
    @Mapping(target = "parent", source = "parent")
    AdministrativeDivisionDTO toDto(AdministrativeDivision administrativeDivision);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    AdministrativeDivision toEntity(AdministrativeDivisionDTO administrativeDivisionDTO);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<AdministrativeDivisionDTO> toDtoNameList(List<AdministrativeDivision> administrativeDivision);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AdministrativeDivisionDTO toDtoName(AdministrativeDivision administrativeDivision);
}
