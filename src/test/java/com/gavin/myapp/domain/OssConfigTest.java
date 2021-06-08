package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OssConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OssConfig.class);
        OssConfig ossConfig1 = new OssConfig();
        ossConfig1.setId(1L);
        OssConfig ossConfig2 = new OssConfig();
        ossConfig2.setId(ossConfig1.getId());
        assertThat(ossConfig1).isEqualTo(ossConfig2);
        ossConfig2.setId(2L);
        assertThat(ossConfig1).isNotEqualTo(ossConfig2);
        ossConfig1.setId(null);
        assertThat(ossConfig1).isNotEqualTo(ossConfig2);
    }
}
