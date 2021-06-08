package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * A DTO for the {@link com.gavin.myapp.domain.ResourceCategory} entity.
 */
@ApiModel(description = "资源分类")
public class ResourceCategorySimpleDTO implements Serializable {

    private Long id;

    /**
     * 标题
     */
    @Size(max = 40)
    @ApiModelProperty(value = "标题")
    private String title;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceCategoryDTO)) {
            return false;
        }

        ResourceCategorySimpleDTO resourceCategoryDTO = (ResourceCategorySimpleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
