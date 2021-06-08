package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataDictionaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataDictionary.class);
        DataDictionary dataDictionary1 = new DataDictionary();
        dataDictionary1.setId(1L);
        DataDictionary dataDictionary2 = new DataDictionary();
        dataDictionary2.setId(dataDictionary1.getId());
        assertThat(dataDictionary1).isEqualTo(dataDictionary2);
        dataDictionary2.setId(2L);
        assertThat(dataDictionary1).isNotEqualTo(dataDictionary2);
        dataDictionary1.setId(null);
        assertThat(dataDictionary1).isNotEqualTo(dataDictionary2);
    }
}
