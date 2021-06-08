package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * 通用条件
 */

@TableName(value = "common_condition")
public class CommonCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "name")
    private String name;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
    private ZonedDateTime lastModifiedTime;

    @TableField(value = "common_table_id")
    private Long commonTableId;

    /**
     * 条件项目
     */
    @TableField(exist = false)
    @BindEntityList(entity = CommonConditionItem.class, condition = "id=common_condition_id")
    @JsonIgnoreProperties(value = { "commonCondition" }, allowSetters = true)
    private List<CommonConditionItem> items = new ArrayList<>();

    /**
     * 所属模型
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonTable.class, condition = "this.common_table_id=id")
    @JsonIgnoreProperties(value = { "commonTableFields", "relationships", "metaModel", "creator", "businessType" }, allowSetters = true)
    private CommonTable commonTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommonCondition id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CommonCondition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CommonCondition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public CommonCondition lastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public List<CommonConditionItem> getItems() {
        return this.items;
    }

    public CommonCondition items(List<CommonConditionItem> commonConditionItems) {
        this.setItems(commonConditionItems);
        return this;
    }

    public CommonCondition addItems(CommonConditionItem commonConditionItem) {
        this.items.add(commonConditionItem);
        commonConditionItem.setCommonCondition(this);
        return this;
    }

    public CommonCondition removeItems(CommonConditionItem commonConditionItem) {
        this.items.remove(commonConditionItem);
        commonConditionItem.setCommonCondition(null);
        return this;
    }

    public void setItems(List<CommonConditionItem> commonConditionItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCommonCondition(null));
        }
        if (commonConditionItems != null) {
            commonConditionItems.forEach(i -> i.setCommonCondition(this));
        }
        this.items = commonConditionItems;
    }

    public CommonTable getCommonTable() {
        return this.commonTable;
    }

    public CommonCondition commonTable(CommonTable commonTable) {
        this.setCommonTable(commonTable);
        return this;
    }

    public void setCommonTable(CommonTable commonTable) {
        this.commonTable = commonTable;
    }

    public Long getCommonTableId() {
        return commonTableId;
    }

    public void setCommonTableId(Long commonTableId) {
        this.commonTableId = commonTableId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonCondition)) {
            return false;
        }
        return id != null && id.equals(((CommonCondition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonCondition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lastModifiedTime='" + getLastModifiedTime() + "'" +
            "}";
    }
}
