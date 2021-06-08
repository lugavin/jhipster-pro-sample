package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonConditionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonConditionDTO.class);
        CommonConditionDTO commonConditionDTO1 = new CommonConditionDTO();
        commonConditionDTO1.setId(1L);
        CommonConditionDTO commonConditionDTO2 = new CommonConditionDTO();
        assertThat(commonConditionDTO1).isNotEqualTo(commonConditionDTO2);
        commonConditionDTO2.setId(commonConditionDTO1.getId());
        assertThat(commonConditionDTO1).isEqualTo(commonConditionDTO2);
        commonConditionDTO2.setId(2L);
        assertThat(commonConditionDTO1).isNotEqualTo(commonConditionDTO2);
        commonConditionDTO1.setId(null);
        assertThat(commonConditionDTO1).isNotEqualTo(commonConditionDTO2);
    }
}
