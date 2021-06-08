package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.SmsConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsConfig} and its DTO {@link SmsConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsConfigMapper extends EntityMapper<SmsConfigDTO, SmsConfig> {}
