package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.CommonCondition}的DTO。
 */
@ApiModel(description = "通用条件")
public class CommonConditionDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    private ZonedDateTime lastModifiedTime;

    private List<CommonConditionItemDTO> items = new ArrayList<>();

    private CommonTableDTO commonTable;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public List<CommonConditionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CommonConditionItemDTO> items) {
        this.items = items;
    }

    public CommonTableDTO getCommonTable() {
        return commonTable;
    }

    public void setCommonTable(CommonTableDTO commonTable) {
        this.commonTable = commonTable;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonConditionDTO)) {
            return false;
        }

        CommonConditionDTO commonConditionDTO = (CommonConditionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonConditionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonConditionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lastModifiedTime='" + getLastModifiedTime() + "'" +
            ", items=" + getItems() +
            ", commonTable=" + getCommonTable() +
            "}";
    }
}
