package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.GpsInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GpsInfo} and its DTO {@link GpsInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GpsInfoMapper extends EntityMapper<GpsInfoDTO, GpsInfo> {}
