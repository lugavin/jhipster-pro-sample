package com.gavin.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gavin.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadImageDTO.class);
        UploadImageDTO uploadImageDTO1 = new UploadImageDTO();
        uploadImageDTO1.setId(1L);
        UploadImageDTO uploadImageDTO2 = new UploadImageDTO();
        assertThat(uploadImageDTO1).isNotEqualTo(uploadImageDTO2);
        uploadImageDTO2.setId(uploadImageDTO1.getId());
        assertThat(uploadImageDTO1).isEqualTo(uploadImageDTO2);
        uploadImageDTO2.setId(2L);
        assertThat(uploadImageDTO1).isNotEqualTo(uploadImageDTO2);
        uploadImageDTO1.setId(null);
        assertThat(uploadImageDTO1).isNotEqualTo(uploadImageDTO2);
    }
}
