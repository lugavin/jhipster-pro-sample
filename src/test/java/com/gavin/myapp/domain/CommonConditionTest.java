package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonConditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonCondition.class);
        CommonCondition commonCondition1 = new CommonCondition();
        commonCondition1.setId(1L);
        CommonCondition commonCondition2 = new CommonCondition();
        commonCondition2.setId(commonCondition1.getId());
        assertThat(commonCondition1).isEqualTo(commonCondition2);
        commonCondition2.setId(2L);
        assertThat(commonCondition1).isNotEqualTo(commonCondition2);
        commonCondition1.setId(null);
        assertThat(commonCondition1).isNotEqualTo(commonCondition2);
    }
}
