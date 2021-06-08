package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.BusinessTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessType} and its DTO {@link BusinessTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessTypeMapper extends EntityMapper<BusinessTypeDTO, BusinessType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BusinessTypeDTO toDtoName(BusinessType businessType);
}
