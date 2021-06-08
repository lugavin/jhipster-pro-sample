package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonConditionItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonConditionItemDTO.class);
        CommonConditionItemDTO commonConditionItemDTO1 = new CommonConditionItemDTO();
        commonConditionItemDTO1.setId(1L);
        CommonConditionItemDTO commonConditionItemDTO2 = new CommonConditionItemDTO();
        assertThat(commonConditionItemDTO1).isNotEqualTo(commonConditionItemDTO2);
        commonConditionItemDTO2.setId(commonConditionItemDTO1.getId());
        assertThat(commonConditionItemDTO1).isEqualTo(commonConditionItemDTO2);
        commonConditionItemDTO2.setId(2L);
        assertThat(commonConditionItemDTO1).isNotEqualTo(commonConditionItemDTO2);
        commonConditionItemDTO1.setId(null);
        assertThat(commonConditionItemDTO1).isNotEqualTo(commonConditionItemDTO2);
    }
}
