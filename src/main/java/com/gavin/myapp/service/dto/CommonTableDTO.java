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

 * {@link com.gavin.myapp.domain.CommonTable}的DTO。
 */
@ApiModel(description = "关系模型")
public class CommonTableDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 80)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 实体名称
     */
    @NotNull
    @Size(max = 80)
    @ApiModelProperty(value = "实体名称", required = true)
    private String entityName;

    /**
     * 数据库表名
     */
    @NotNull
    @Size(max = 80)
    @ApiModelProperty(value = "数据库表名", required = true)
    private String tableName;

    /**
     * 系统表
     */
    @ApiModelProperty(value = "系统表")
    private Boolean system;

    /**
     * 类名
     */
    @NotNull
    @Size(max = 80)
    @ApiModelProperty(value = "类名", required = true)
    private String clazzName;

    /**
     * 是否生成
     */
    @ApiModelProperty(value = "是否生成")
    private Boolean generated;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private ZonedDateTime creatAt;

    /**
     * 生成表时间
     */
    @ApiModelProperty(value = "生成表时间")
    private ZonedDateTime generateAt;

    /**
     * 编译时间
     */
    @ApiModelProperty(value = "编译时间")
    private ZonedDateTime generateClassAt;

    /**
     * 表说明
     */
    @Size(max = 200)
    @ApiModelProperty(value = "表说明")
    private String description;

    /**
     * 树形表
     */
    @ApiModelProperty(value = "树形表")
    private Boolean treeTable;

    /**
     * 来源Id
     */
    @ApiModelProperty(value = "来源Id")
    private Long baseTableId;

    /**
     * 操作栏宽度
     */
    @ApiModelProperty(value = "操作栏宽度")
    private Integer recordActionWidth;

    /**
     * 前端列表配置
     */
    @ApiModelProperty(value = "前端列表配置")
    private String listConfig;

    /**
     * 前端表单配置
     */
    @ApiModelProperty(value = "前端表单配置")
    private String formConfig;

    /**
     * 弹窗编辑
     */
    @ApiModelProperty(value = "弹窗编辑")
    private Boolean editInModal;

    private List<CommonTableFieldDTO> commonTableFields = new ArrayList<>();

    private List<CommonTableRelationshipDTO> relationships = new ArrayList<>();

    private CommonTableSimpleDTO metaModel;

    private UserDTO creator;

    private BusinessTypeDTO businessType;

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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public Boolean getGenerated() {
        return generated;
    }

    public void setGenerated(Boolean generated) {
        this.generated = generated;
    }

    public ZonedDateTime getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(ZonedDateTime creatAt) {
        this.creatAt = creatAt;
    }

    public ZonedDateTime getGenerateAt() {
        return generateAt;
    }

    public void setGenerateAt(ZonedDateTime generateAt) {
        this.generateAt = generateAt;
    }

    public ZonedDateTime getGenerateClassAt() {
        return generateClassAt;
    }

    public void setGenerateClassAt(ZonedDateTime generateClassAt) {
        this.generateClassAt = generateClassAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTreeTable() {
        return treeTable;
    }

    public void setTreeTable(Boolean treeTable) {
        this.treeTable = treeTable;
    }

    public Long getBaseTableId() {
        return baseTableId;
    }

    public void setBaseTableId(Long baseTableId) {
        this.baseTableId = baseTableId;
    }

    public Integer getRecordActionWidth() {
        return recordActionWidth;
    }

    public void setRecordActionWidth(Integer recordActionWidth) {
        this.recordActionWidth = recordActionWidth;
    }

    public String getListConfig() {
        return listConfig;
    }

    public void setListConfig(String listConfig) {
        this.listConfig = listConfig;
    }

    public String getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(String formConfig) {
        this.formConfig = formConfig;
    }

    public Boolean getEditInModal() {
        return editInModal;
    }

    public void setEditInModal(Boolean editInModal) {
        this.editInModal = editInModal;
    }

    public List<CommonTableFieldDTO> getCommonTableFields() {
        return commonTableFields;
    }

    public void setCommonTableFields(List<CommonTableFieldDTO> commonTableFields) {
        this.commonTableFields = commonTableFields;
    }

    public List<CommonTableRelationshipDTO> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<CommonTableRelationshipDTO> relationships) {
        this.relationships = relationships;
    }

    public CommonTableSimpleDTO getMetaModel() {
        return metaModel;
    }

    public void setMetaModel(CommonTableSimpleDTO metaModel) {
        this.metaModel = metaModel;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public BusinessTypeDTO getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeDTO businessType) {
        this.businessType = businessType;
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

        CommonTableDTO commonTableDTO = (CommonTableDTO) o;
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
            ", commonTableFields=" + getCommonTableFields() +
            ", relationships=" + getRelationships() +
            ", metaModel=" + getMetaModel() +
            ", creator=" + getCreator() +
            ", businessType=" + getBusinessType() +
            "}";
    }
}
