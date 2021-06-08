package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonTableFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonTableFieldDTO.class);
        CommonTableFieldDTO commonTableFieldDTO1 = new CommonTableFieldDTO();
        commonTableFieldDTO1.setId(1L);
        CommonTableFieldDTO commonTableFieldDTO2 = new CommonTableFieldDTO();
        assertThat(commonTableFieldDTO1).isNotEqualTo(commonTableFieldDTO2);
        commonTableFieldDTO2.setId(commonTableFieldDTO1.getId());
        assertThat(commonTableFieldDTO1).isEqualTo(commonTableFieldDTO2);
        commonTableFieldDTO2.setId(2L);
        assertThat(commonTableFieldDTO1).isNotEqualTo(commonTableFieldDTO2);
        commonTableFieldDTO1.setId(null);
        assertThat(commonTableFieldDTO1).isNotEqualTo(commonTableFieldDTO2);
    }
}
