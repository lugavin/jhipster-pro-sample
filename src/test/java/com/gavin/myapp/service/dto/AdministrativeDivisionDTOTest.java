package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministrativeDivisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministrativeDivisionDTO.class);
        AdministrativeDivisionDTO administrativeDivisionDTO1 = new AdministrativeDivisionDTO();
        administrativeDivisionDTO1.setId(1L);
        AdministrativeDivisionDTO administrativeDivisionDTO2 = new AdministrativeDivisionDTO();
        assertThat(administrativeDivisionDTO1).isNotEqualTo(administrativeDivisionDTO2);
        administrativeDivisionDTO2.setId(administrativeDivisionDTO1.getId());
        assertThat(administrativeDivisionDTO1).isEqualTo(administrativeDivisionDTO2);
        administrativeDivisionDTO2.setId(2L);
        assertThat(administrativeDivisionDTO1).isNotEqualTo(administrativeDivisionDTO2);
        administrativeDivisionDTO1.setId(null);
        assertThat(administrativeDivisionDTO1).isNotEqualTo(administrativeDivisionDTO2);
    }
}
