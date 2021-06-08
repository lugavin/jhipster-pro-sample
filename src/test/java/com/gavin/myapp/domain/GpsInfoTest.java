package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GpsInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GpsInfo.class);
        GpsInfo gpsInfo1 = new GpsInfo();
        gpsInfo1.setId(1L);
        GpsInfo gpsInfo2 = new GpsInfo();
        gpsInfo2.setId(gpsInfo1.getId());
        assertThat(gpsInfo1).isEqualTo(gpsInfo2);
        gpsInfo2.setId(2L);
        assertThat(gpsInfo1).isNotEqualTo(gpsInfo2);
        gpsInfo1.setId(null);
        assertThat(gpsInfo1).isNotEqualTo(gpsInfo2);
    }
}
