package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.UReportFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UReportFile} and its DTO {@link UReportFileDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommonTableMapper.class })
public interface UReportFileMapper extends EntityMapper<UReportFileDTO, UReportFile> {
    @Mapping(target = "commonTable", source = "commonTable", qualifiedByName = "name")
    UReportFileDTO toDto(UReportFile uReportFile);
}
