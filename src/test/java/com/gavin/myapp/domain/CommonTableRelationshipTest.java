package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonTableRelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonTableRelationship.class);
        CommonTableRelationship commonTableRelationship1 = new CommonTableRelationship();
        commonTableRelationship1.setId(1L);
        CommonTableRelationship commonTableRelationship2 = new CommonTableRelationship();
        commonTableRelationship2.setId(commonTableRelationship1.getId());
        assertThat(commonTableRelationship1).isEqualTo(commonTableRelationship2);
        commonTableRelationship2.setId(2L);
        assertThat(commonTableRelationship1).isNotEqualTo(commonTableRelationship2);
        commonTableRelationship1.setId(null);
        assertThat(commonTableRelationship1).isNotEqualTo(commonTableRelationship2);
    }
}
