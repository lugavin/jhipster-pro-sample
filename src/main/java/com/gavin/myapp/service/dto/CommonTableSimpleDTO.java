package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * A DTO for the {@link com.gavin.myapp.domain.CommonTable} entity.
 */
@ApiModel(description = "关系模型")
public class CommonTableSimpleDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 80)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    private Map<String, Object> extData = new HashMap<>();

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonTableDTO)) {
            return false;
        }

        CommonTableSimpleDTO commonTableDTO = (CommonTableSimpleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
