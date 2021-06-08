package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;

/**
 * 关系模型
 */

@TableName(value = "common_table")
public class CommonTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 80)
    @TableField(value = "name")
    private String name;

    /**
     * 实体名称
     */
    @NotNull
    @Size(max = 80)
    @TableField(value = "entity_name")
    private String entityName;

    /**
     * 数据库表名
     */
    @NotNull
    @Size(max = 80)
    @TableField(value = "table_name")
    private String tableName;

    /**
     * 系统表
     */
    @TableField(value = "`system`")
    private Boolean system;

    /**
     * 类名
     */
    @NotNull
    @Size(max = 80)
    @TableField(value = "clazz_name")
    private String clazzName;

    /**
     * 是否生成
     */
    @TableField(value = "`generated`")
    private Boolean generated;

    /**
     * 创建时间
     */
    @TableField(value = "creat_at")
    private ZonedDateTime creatAt;

    /**
     * 生成表时间
     */
    @TableField(value = "generate_at")
    private ZonedDateTime generateAt;

    /**
     * 编译时间
     */
    @TableField(value = "generate_class_at")
    private ZonedDateTime generateClassAt;

    /**
     * 表说明
     */
    @Size(max = 200)
    @TableField(value = "description")
    private String description;

    /**
     * 树形表
     */
    @TableField(value = "tree_table")
    private Boolean treeTable;

    /**
     * 来源Id
     */
    @TableField(value = "base_table_id")
    private Long baseTableId;

    /**
     * 操作栏宽度
     */
    @TableField(value = "record_action_width")
    private Integer recordActionWidth;

    /**
     * 前端列表配置
     */
    @TableField(value = "list_config")
    private String listConfig;

    /**
     * 前端表单配置
     */
    @TableField(value = "form_config")
    private String formConfig;

    /**
     * 弹窗编辑
     */
    @TableField(value = "edit_in_modal")
    private Boolean editInModal;

    @BindExtData
    @TableField(exist = false)
    private Map<String, Object> extData = new HashMap<>();

    @TableField(value = "meta_model_id")
    private Long metaModelId;

    @TableField(value = "creator_id")
    private Long creatorId;

    @TableField(value = "business_type_id")
    private Long businessTypeId;

    /**
     * 字段
     */
    @TableField(exist = false)
    @BindEntityList(entity = CommonTableField.class, condition = "id=common_table_id")
    @JsonIgnoreProperties(value = { "metaModel", "commonTable" }, allowSetters = true)
    private List<CommonTableField> commonTableFields = new ArrayList<>();

    /**
     * 关系
     */
    @TableField(exist = false)
    @BindEntityList(entity = CommonTableRelationship.class, condition = "id=common_table_id")
    @JsonIgnoreProperties(value = { "relationEntity", "dataDictionaryNode", "metaModel", "commonTable" }, allowSetters = true)
    private List<CommonTableRelationship> relationships = new ArrayList<>();

    /**
     * 元模型
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonTable.class, condition = "this.meta_model_id=id")
    @JsonIgnoreProperties(value = { "commonTableFields", "relationships", "metaModel", "creator", "businessType" }, allowSetters = true)
    private CommonTable metaModel;

    /**
     * 创建人
     */
    @TableField(exist = false)
    @BindEntity(entity = User.class, condition = "this.creator_id=id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private User creator;

    /**
     * 业务类型
     */
    @TableField(exist = false)
    @BindEntity(entity = BusinessType.class, condition = "this.business_type_id=id")
    private BusinessType businessType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommonTable id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CommonTable name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public CommonTable entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public CommonTable tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getSystem() {
        return this.system;
    }

    public CommonTable system(Boolean system) {
        this.system = system;
        return this;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getClazzName() {
        return this.clazzName;
    }

    public CommonTable clazzName(String clazzName) {
        this.clazzName = clazzName;
        return this;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public Boolean getGenerated() {
        return this.generated;
    }

    public CommonTable generated(Boolean generated) {
        this.generated = generated;
        return this;
    }

    public void setGenerated(Boolean generated) {
        this.generated = generated;
    }

    public ZonedDateTime getCreatAt() {
        return this.creatAt;
    }

    public CommonTable creatAt(ZonedDateTime creatAt) {
        this.creatAt = creatAt;
        return this;
    }

    public void setCreatAt(ZonedDateTime creatAt) {
        this.creatAt = creatAt;
    }

    public ZonedDateTime getGenerateAt() {
        return this.generateAt;
    }

    public CommonTable generateAt(ZonedDateTime generateAt) {
        this.generateAt = generateAt;
        return this;
    }

    public void setGenerateAt(ZonedDateTime generateAt) {
        this.generateAt = generateAt;
    }

    public ZonedDateTime getGenerateClassAt() {
        return this.generateClassAt;
    }

    public CommonTable generateClassAt(ZonedDateTime generateClassAt) {
        this.generateClassAt = generateClassAt;
        return this;
    }

    public void setGenerateClassAt(ZonedDateTime generateClassAt) {
        this.generateClassAt = generateClassAt;
    }

    public String getDescription() {
        return this.description;
    }

    public CommonTable description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTreeTable() {
        return this.treeTable;
    }

    public CommonTable treeTable(Boolean treeTable) {
        this.treeTable = treeTable;
        return this;
    }

    public void setTreeTable(Boolean treeTable) {
        this.treeTable = treeTable;
    }

    public Long getBaseTableId() {
        return this.baseTableId;
    }

    public CommonTable baseTableId(Long baseTableId) {
        this.baseTableId = baseTableId;
        return this;
    }

    public void setBaseTableId(Long baseTableId) {
        this.baseTableId = baseTableId;
    }

    public Integer getRecordActionWidth() {
        return this.recordActionWidth;
    }

    public CommonTable recordActionWidth(Integer recordActionWidth) {
        this.recordActionWidth = recordActionWidth;
        return this;
    }

    public void setRecordActionWidth(Integer recordActionWidth) {
        this.recordActionWidth = recordActionWidth;
    }

    public String getListConfig() {
        return this.listConfig;
    }

    public CommonTable listConfig(String listConfig) {
        this.listConfig = listConfig;
        return this;
    }

    public void setListConfig(String listConfig) {
        this.listConfig = listConfig;
    }

    public String getFormConfig() {
        return this.formConfig;
    }

    public CommonTable formConfig(String formConfig) {
        this.formConfig = formConfig;
        return this;
    }

    public void setFormConfig(String formConfig) {
        this.formConfig = formConfig;
    }

    public Boolean getEditInModal() {
        return this.editInModal;
    }

    public CommonTable editInModal(Boolean editInModal) {
        this.editInModal = editInModal;
        return this;
    }

    public void setEditInModal(Boolean editInModal) {
        this.editInModal = editInModal;
    }

    public List<CommonTableField> getCommonTableFields() {
        return this.commonTableFields;
    }

    public CommonTable commonTableFields(List<CommonTableField> commonTableFields) {
        this.setCommonTableFields(commonTableFields);
        return this;
    }

    public CommonTable addCommonTableFields(CommonTableField commonTableField) {
        this.commonTableFields.add(commonTableField);
        commonTableField.setCommonTable(this);
        return this;
    }

    public CommonTable removeCommonTableFields(CommonTableField commonTableField) {
        this.commonTableFields.remove(commonTableField);
        commonTableField.setCommonTable(null);
        return this;
    }

    public void setCommonTableFields(List<CommonTableField> commonTableFields) {
        if (this.commonTableFields != null) {
            this.commonTableFields.forEach(i -> i.setCommonTable(null));
        }
        if (commonTableFields != null) {
            commonTableFields.forEach(i -> i.setCommonTable(this));
        }
        this.commonTableFields = commonTableFields;
    }

    public List<CommonTableRelationship> getRelationships() {
        return this.relationships;
    }

    public CommonTable relationships(List<CommonTableRelationship> commonTableRelationships) {
        this.setRelationships(commonTableRelationships);
        return this;
    }

    public CommonTable addRelationships(CommonTableRelationship commonTableRelationship) {
        this.relationships.add(commonTableRelationship);
        commonTableRelationship.setCommonTable(this);
        return this;
    }

    public CommonTable removeRelationships(CommonTableRelationship commonTableRelationship) {
        this.relationships.remove(commonTableRelationship);
        commonTableRelationship.setCommonTable(null);
        return this;
    }

    public void setRelationships(List<CommonTableRelationship> commonTableRelationships) {
        if (this.relationships != null) {
            this.relationships.forEach(i -> i.setCommonTable(null));
        }
        if (commonTableRelationships != null) {
            commonTableRelationships.forEach(i -> i.setCommonTable(this));
        }
        this.relationships = commonTableRelationships;
    }

    public CommonTable getMetaModel() {
        return this.metaModel;
    }

    public CommonTable metaModel(CommonTable commonTable) {
        this.setMetaModel(commonTable);
        return this;
    }

    public void setMetaModel(CommonTable commonTable) {
        this.metaModel = commonTable;
    }

    public User getCreator() {
        return this.creator;
    }

    public CommonTable creator(User user) {
        this.setCreator(user);
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public BusinessType getBusinessType() {
        return this.businessType;
    }

    public CommonTable businessType(BusinessType businessType) {
        this.setBusinessType(businessType);
        return this;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

    public CommonTable extData(Map<String, Object> extData) {
        this.extData = extData;
        return this;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public Long getMetaModelId() {
        return metaModelId;
    }

    public void setMetaModelId(Long metaModelId) {
        this.metaModelId = metaModelId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonTable)) {
            return false;
        }
        return id != null && id.equals(((CommonTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTable{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", system='" + getSystem() + "'" +
            ", clazzName='" + getClazzName() + "'" +
            ", generated='" + getGenerated() + "'" +
            ", creatAt='" + getCreatAt() + "'" +
            ", generateAt='" + getGenerateAt() + "'" +
            ", generateClassAt='" + getGenerateClassAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", treeTable='" + getTreeTable() + "'" +
            ", baseTableId=" + getBaseTableId() +
            ", recordActionWidth=" + getRecordActionWidth() +
            ", listConfig='" + getListConfig() + "'" +
            ", formConfig='" + getFormConfig() + "'" +
            ", editInModal='" + getEditInModal() + "'" +
            "}";
    }
}
