package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonTable.class);
        CommonTable commonTable1 = new CommonTable();
        commonTable1.setId(1L);
        CommonTable commonTable2 = new CommonTable();
        commonTable2.setId(commonTable1.getId());
        assertThat(commonTable1).isEqualTo(commonTable2);
        commonTable2.setId(2L);
        assertThat(commonTable1).isNotEqualTo(commonTable2);
        commonTable1.setId(null);
        assertThat(commonTable1).isNotEqualTo(commonTable2);
    }
}
