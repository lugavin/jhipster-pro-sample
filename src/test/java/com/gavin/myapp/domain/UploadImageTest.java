package com.gavin.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadImage.class);
        UploadImage uploadImage1 = new UploadImage();
        uploadImage1.setId(1L);
        UploadImage uploadImage2 = new UploadImage();
        uploadImage2.setId(uploadImage1.getId());
        assertThat(uploadImage1).isEqualTo(uploadImage2);
        uploadImage2.setId(2L);
        assertThat(uploadImage1).isNotEqualTo(uploadImage2);
        uploadImage1.setId(null);
        assertThat(uploadImage1).isNotEqualTo(uploadImage2);
    }
}
