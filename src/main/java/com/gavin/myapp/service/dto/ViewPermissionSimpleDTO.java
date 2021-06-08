package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.TargetType;
import com.gavin.myapp.domain.enumeration.ViewPermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * A DTO for the {@link com.gavin.myapp.domain.ViewPermission} entity.
 */
@ApiModel(description = "可视权限\n权限分为菜单权限、按钮权限等\n")
public class ViewPermissionSimpleDTO implements Serializable {

    private Long id;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String text;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewPermissionDTO)) {
            return false;
        }

        ViewPermissionSimpleDTO viewPermissionDTO = (ViewPermissionSimpleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, viewPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermissionDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
