package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.Position}的DTO。
 */
@ApiModel(description = "岗位\n@author jhipster.pro")
public class PositionDTO implements Serializable {

    private Long id;

    /**
     * 岗位代码
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "岗位代码", required = true)
    private String code;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    /**
     * 描述
     */
    @Size(max = 200)
    @ApiModelProperty(value = "描述")
    private String description;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionDTO)) {
            return false;
        }

        PositionDTO positionDTO = (PositionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortNo=" + getSortNo() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
