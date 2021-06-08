package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadFile.class);
        UploadFile uploadFile1 = new UploadFile();
        uploadFile1.setId(1L);
        UploadFile uploadFile2 = new UploadFile();
        uploadFile2.setId(uploadFile1.getId());
        assertThat(uploadFile1).isEqualTo(uploadFile2);
        uploadFile2.setId(2L);
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);
        uploadFile1.setId(null);
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);
    }
}
