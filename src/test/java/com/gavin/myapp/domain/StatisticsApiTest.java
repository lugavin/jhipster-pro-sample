package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatisticsApiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatisticsApi.class);
        StatisticsApi statisticsApi1 = new StatisticsApi();
        statisticsApi1.setId(1L);
        StatisticsApi statisticsApi2 = new StatisticsApi();
        statisticsApi2.setId(statisticsApi1.getId());
        assertThat(statisticsApi1).isEqualTo(statisticsApi2);
        statisticsApi2.setId(2L);
        assertThat(statisticsApi1).isNotEqualTo(statisticsApi2);
        statisticsApi1.setId(null);
        assertThat(statisticsApi1).isNotEqualTo(statisticsApi2);
    }
}
