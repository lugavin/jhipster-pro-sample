package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataDictionaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataDictionaryDTO.class);
        DataDictionaryDTO dataDictionaryDTO1 = new DataDictionaryDTO();
        dataDictionaryDTO1.setId(1L);
        DataDictionaryDTO dataDictionaryDTO2 = new DataDictionaryDTO();
        assertThat(dataDictionaryDTO1).isNotEqualTo(dataDictionaryDTO2);
        dataDictionaryDTO2.setId(dataDictionaryDTO1.getId());
        assertThat(dataDictionaryDTO1).isEqualTo(dataDictionaryDTO2);
        dataDictionaryDTO2.setId(2L);
        assertThat(dataDictionaryDTO1).isNotEqualTo(dataDictionaryDTO2);
        dataDictionaryDTO1.setId(null);
        assertThat(dataDictionaryDTO1).isNotEqualTo(dataDictionaryDTO2);
    }
}
