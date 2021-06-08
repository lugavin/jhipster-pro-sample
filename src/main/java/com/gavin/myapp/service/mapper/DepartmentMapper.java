package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.DepartmentDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentSimpleMapper.class, AuthorityMapper.class })
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "children", source = "children", qualifiedByName = "nameList")
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "nameList")
    @Mapping(target = "parent", source = "parent")
    DepartmentDTO toDto(Department department);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    @Mapping(target = "removeAuthorities", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<DepartmentDTO> toDtoNameList(List<Department> department);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoName(Department department);
}
