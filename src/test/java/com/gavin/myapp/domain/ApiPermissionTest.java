package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApiPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiPermission.class);
        ApiPermission apiPermission1 = new ApiPermission();
        apiPermission1.setId(1L);
        ApiPermission apiPermission2 = new ApiPermission();
        apiPermission2.setId(apiPermission1.getId());
        assertThat(apiPermission1).isEqualTo(apiPermission2);
        apiPermission2.setId(2L);
        assertThat(apiPermission1).isNotEqualTo(apiPermission2);
        apiPermission1.setId(null);
        assertThat(apiPermission1).isNotEqualTo(apiPermission2);
    }
}
