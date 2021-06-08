package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GpsInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GpsInfoDTO.class);
        GpsInfoDTO gpsInfoDTO1 = new GpsInfoDTO();
        gpsInfoDTO1.setId(1L);
        GpsInfoDTO gpsInfoDTO2 = new GpsInfoDTO();
        assertThat(gpsInfoDTO1).isNotEqualTo(gpsInfoDTO2);
        gpsInfoDTO2.setId(gpsInfoDTO1.getId());
        assertThat(gpsInfoDTO1).isEqualTo(gpsInfoDTO2);
        gpsInfoDTO2.setId(2L);
        assertThat(gpsInfoDTO1).isNotEqualTo(gpsInfoDTO2);
        gpsInfoDTO1.setId(null);
        assertThat(gpsInfoDTO1).isNotEqualTo(gpsInfoDTO2);
    }
}
