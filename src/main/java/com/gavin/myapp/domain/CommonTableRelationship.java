package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.domain.enumeration.SourceType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;

/**
 * 模型关系
 */

@TableName(value = "common_table_relationship")
public class CommonTableRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 100)
    @TableField(value = "name")
    private String name;

    /**
     * 关系类型
     */
    @NotNull
    @TableField(value = "relationship_type")
    private RelationshipType relationshipType;

    /**
     * 来源类型
     */
    @NotNull
    @TableField(value = "source_type")
    private SourceType sourceType;

    /**
     * 关联表显示字段
     */
    @Size(max = 100)
    @TableField(value = "other_entity_field")
    private String otherEntityField;

    /**
     * 关联实体名称
     */
    @NotNull
    @Size(max = 100)
    @TableField(value = "other_entity_name")
    private String otherEntityName;

    /**
     * 关系属性名称
     */
    @NotNull
    @Size(max = 100)
    @TableField(value = "relationship_name")
    private String relationshipName;

    /**
     * 对方属性名称
     */
    @Size(max = 100)
    @TableField(value = "other_entity_relationship_name")
    private String otherEntityRelationshipName;

    /**
     * 列宽
     */
    @TableField(value = "column_width")
    private Integer columnWidth;

    /**
     * 显示顺序
     */
    @TableField(value = "`order`")
    private Integer order;

    /**
     * 列固定
     */
    @TableField(value = "fixed")
    private FixedType fixed;

    /**
     * 行内编辑
     */
    @TableField(value = "edit_in_list")
    private Boolean editInList;

    /**
     * 可过滤
     */
    @TableField(value = "enable_filter")
    private Boolean enableFilter;

    /**
     * 列表隐藏
     */
    @TableField(value = "hide_in_list")
    private Boolean hideInList;

    /**
     * 表单隐藏
     */
    @TableField(value = "hide_in_form")
    private Boolean hideInForm;

    /**
     * 系统定义
     */
    @TableField(value = "`system`")
    private Boolean system;

    /**
     * 字体颜色
     */
    @Size(max = 80)
    @TableField(value = "font_color")
    private String fontColor;

    /**
     * 列背景色
     */
    @Size(max = 80)
    @TableField(value = "background_color")
    private String backgroundColor;

    /**
     * 详细字段说明
     */
    @Size(max = 200)
    @TableField(value = "help")
    private String help;

    /**
     * 是否维护端
     */
    @TableField(value = "owner_side")
    private Boolean ownerSide;

    /**
     * 数据源名称
     */
    @NotNull
    @Size(max = 100)
    @TableField(value = "data_name")
    private String dataName;

    /**
     * Web控件类型
     */
    @Size(max = 100)
    @TableField(value = "web_component_type")
    private String webComponentType;

    /**
     * 是否树形实体
     */
    @TableField(value = "other_entity_is_tree")
    private Boolean otherEntityIsTree;

    /**
     * 显示在过滤树
     */
    @TableField(value = "show_in_filter_tree")
    private Boolean showInFilterTree;

    /**
     * 字典表代码
     */
    @Size(max = 100)
    @TableField(value = "data_dictionary_code")
    private String dataDictionaryCode;

    /**
     * 前端只读
     */
    @TableField(value = "client_read_only")
    private Boolean clientReadOnly;

    /**
     * 前端用法
     */
    @TableField(value = "end_used")
    private EndUsedType endUsed;

    /**
     * 关系配置项
     */
    @TableField(value = "options")
    private String options;

    @BindExtData
    @TableField(exist = false)
    private Map<String, Object> extData = new HashMap<>();

    @TableField(value = "relation_entity_id")
    private Long relationEntityId;

    @TableField(value = "data_dictionary_node_id")
    private Long dataDictionaryNodeId;

    @TableField(value = "meta_model_id")
    private Long metaModelId;

    @TableField(value = "common_table_id")
    private Long commonTableId;

    /**
     * 关联实体
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonTable.class, condition = "this.relation_entity_id=id")
    @JsonIgnoreProperties(value = { "commonTableFields", "relationships", "metaModel", "creator", "businessType" }, allowSetters = true)
    private CommonTable relationEntity;

    /**
     * 字典表节点
     */
    @TableField(exist = false)
    @BindEntity(entity = DataDictionary.class, condition = "this.data_dictionary_node_id=id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private DataDictionary dataDictionaryNode;

    /**
     * 元模型
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonTable.class, condition = "this.meta_model_id=id")
    @JsonIgnoreProperties(value = { "commonTableFields", "relationships", "metaModel", "creator", "businessType" }, allowSetters = true)
    private CommonTable metaModel;

    /**
     * 所属表
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

    public CommonTableRelationship id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CommonTableRelationship name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelationshipType getRelationshipType() {
        return this.relationshipType;
    }

    public CommonTableRelationship relationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
        return this;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public SourceType getSourceType() {
        return this.sourceType;
    }

    public CommonTableRelationship sourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getOtherEntityField() {
        return this.otherEntityField;
    }

    public CommonTableRelationship otherEntityField(String otherEntityField) {
        this.otherEntityField = otherEntityField;
        return this;
    }

    public void setOtherEntityField(String otherEntityField) {
        this.otherEntityField = otherEntityField;
    }

    public String getOtherEntityName() {
        return this.otherEntityName;
    }

    public CommonTableRelationship otherEntityName(String otherEntityName) {
        this.otherEntityName = otherEntityName;
        return this;
    }

    public void setOtherEntityName(String otherEntityName) {
        this.otherEntityName = otherEntityName;
    }

    public String getRelationshipName() {
        return this.relationshipName;
    }

    public CommonTableRelationship relationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
        return this;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getOtherEntityRelationshipName() {
        return this.otherEntityRelationshipName;
    }

    public CommonTableRelationship otherEntityRelationshipName(String otherEntityRelationshipName) {
        this.otherEntityRelationshipName = otherEntityRelationshipName;
        return this;
    }

    public void setOtherEntityRelationshipName(String otherEntityRelationshipName) {
        this.otherEntityRelationshipName = otherEntityRelationshipName;
    }

    public Integer getColumnWidth() {
        return this.columnWidth;
    }

    public CommonTableRelationship columnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
        return this;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public Integer getOrder() {
        return this.order;
    }

    public CommonTableRelationship order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public FixedType getFixed() {
        return this.fixed;
    }

    public CommonTableRelationship fixed(FixedType fixed) {
        this.fixed = fixed;
        return this;
    }

    public void setFixed(FixedType fixed) {
        this.fixed = fixed;
    }

    public Boolean getEditInList() {
        return this.editInList;
    }

    public CommonTableRelationship editInList(Boolean editInList) {
        this.editInList = editInList;
        return this;
    }

    public void setEditInList(Boolean editInList) {
        this.editInList = editInList;
    }

    public Boolean getEnableFilter() {
        return this.enableFilter;
    }

    public CommonTableRelationship enableFilter(Boolean enableFilter) {
        this.enableFilter = enableFilter;
        return this;
    }

    public void setEnableFilter(Boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    public Boolean getHideInList() {
        return this.hideInList;
    }

    public CommonTableRelationship hideInList(Boolean hideInList) {
        this.hideInList = hideInList;
        return this;
    }

    public void setHideInList(Boolean hideInList) {
        this.hideInList = hideInList;
    }

    public Boolean getHideInForm() {
        return this.hideInForm;
    }

    public CommonTableRelationship hideInForm(Boolean hideInForm) {
        this.hideInForm = hideInForm;
        return this;
    }

    public void setHideInForm(Boolean hideInForm) {
        this.hideInForm = hideInForm;
    }

    public Boolean getSystem() {
        return this.system;
    }

    public CommonTableRelationship system(Boolean system) {
        this.system = system;
        return this;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getFontColor() {
        return this.fontColor;
    }

    public CommonTableRelationship fontColor(String fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public CommonTableRelationship backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getHelp() {
        return this.help;
    }

    public CommonTableRelationship help(String help) {
        this.help = help;
        return this;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public Boolean getOwnerSide() {
        return this.ownerSide;
    }

    public CommonTableRelationship ownerSide(Boolean ownerSide) {
        this.ownerSide = ownerSide;
        return this;
    }

    public void setOwnerSide(Boolean ownerSide) {
        this.ownerSide = ownerSide;
    }

    public String getDataName() {
        return this.dataName;
    }

    public CommonTableRelationship dataName(String dataName) {
        this.dataName = dataName;
        return this;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getWebComponentType() {
        return this.webComponentType;
    }

    public CommonTableRelationship webComponentType(String webComponentType) {
        this.webComponentType = webComponentType;
        return this;
    }

    public void setWebComponentType(String webComponentType) {
        this.webComponentType = webComponentType;
    }

    public Boolean getOtherEntityIsTree() {
        return this.otherEntityIsTree;
    }

    public CommonTableRelationship otherEntityIsTree(Boolean otherEntityIsTree) {
        this.otherEntityIsTree = otherEntityIsTree;
        return this;
    }

    public void setOtherEntityIsTree(Boolean otherEntityIsTree) {
        this.otherEntityIsTree = otherEntityIsTree;
    }

    public Boolean getShowInFilterTree() {
        return this.showInFilterTree;
    }

    public CommonTableRelationship showInFilterTree(Boolean showInFilterTree) {
        this.showInFilterTree = showInFilterTree;
        return this;
    }

    public void setShowInFilterTree(Boolean showInFilterTree) {
        this.showInFilterTree = showInFilterTree;
    }

    public String getDataDictionaryCode() {
        return this.dataDictionaryCode;
    }

    public CommonTableRelationship dataDictionaryCode(String dataDictionaryCode) {
        this.dataDictionaryCode = dataDictionaryCode;
        return this;
    }

    public void setDataDictionaryCode(String dataDictionaryCode) {
        this.dataDictionaryCode = dataDictionaryCode;
    }

    public Boolean getClientReadOnly() {
        return this.clientReadOnly;
    }

    public CommonTableRelationship clientReadOnly(Boolean clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
        return this;
    }

    public void setClientReadOnly(Boolean clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
    }

    public EndUsedType getEndUsed() {
        return this.endUsed;
    }

    public CommonTableRelationship endUsed(EndUsedType endUsed) {
        this.endUsed = endUsed;
        return this;
    }

    public void setEndUsed(EndUsedType endUsed) {
        this.endUsed = endUsed;
    }

    public String getOptions() {
        return this.options;
    }

    public CommonTableRelationship options(String options) {
        this.options = options;
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public CommonTable getRelationEntity() {
        return this.relationEntity;
    }

    public CommonTableRelationship relationEntity(CommonTable commonTable) {
        this.setRelationEntity(commonTable);
        return this;
    }

    public void setRelationEntity(CommonTable commonTable) {
        this.relationEntity = commonTable;
    }

    public DataDictionary getDataDictionaryNode() {
        return this.dataDictionaryNode;
    }

    public CommonTableRelationship dataDictionaryNode(DataDictionary dataDictionary) {
        this.setDataDictionaryNode(dataDictionary);
        return this;
    }

    public void setDataDictionaryNode(DataDictionary dataDictionary) {
        this.dataDictionaryNode = dataDictionary;
    }

    public CommonTable getMetaModel() {
        return this.metaModel;
    }

    public CommonTableRelationship metaModel(CommonTable commonTable) {
        this.setMetaModel(commonTable);
        return this;
    }

    public void setMetaModel(CommonTable commonTable) {
        this.metaModel = commonTable;
    }

    public CommonTable getCommonTable() {
        return this.commonTable;
    }

    public CommonTableRelationship commonTable(CommonTable commonTable) {
        this.setCommonTable(commonTable);
        return this;
    }

    public void setCommonTable(CommonTable commonTable) {
        this.commonTable = commonTable;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

    public CommonTableRelationship extData(Map<String, Object> extData) {
        this.extData = extData;
        return this;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public Long getRelationEntityId() {
        return relationEntityId;
    }

    public void setRelationEntityId(Long relationEntityId) {
        this.relationEntityId = relationEntityId;
    }

    public Long getDataDictionaryNodeId() {
        return dataDictionaryNodeId;
    }

    public void setDataDictionaryNodeId(Long dataDictionaryNodeId) {
        this.dataDictionaryNodeId = dataDictionaryNodeId;
    }

    public Long getMetaModelId() {
        return metaModelId;
    }

    public void setMetaModelId(Long metaModelId) {
        this.metaModelId = metaModelId;
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
        if (!(o instanceof CommonTableRelationship)) {
            return false;
        }
        return id != null && id.equals(((CommonTableRelationship) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableRelationship{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", relationshipType='" + getRelationshipType() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", otherEntityField='" + getOtherEntityField() + "'" +
            ", otherEntityName='" + getOtherEntityName() + "'" +
            ", relationshipName='" + getRelationshipName() + "'" +
            ", otherEntityRelationshipName='" + getOtherEntityRelationshipName() + "'" +
            ", columnWidth=" + getColumnWidth() +
            ", order=" + getOrder() +
            ", fixed='" + getFixed() + "'" +
            ", editInList='" + getEditInList() + "'" +
            ", enableFilter='" + getEnableFilter() + "'" +
            ", hideInList='" + getHideInList() + "'" +
            ", hideInForm='" + getHideInForm() + "'" +
            ", system='" + getSystem() + "'" +
            ", fontColor='" + getFontColor() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            ", help='" + getHelp() + "'" +
            ", ownerSide='" + getOwnerSide() + "'" +
            ", dataName='" + getDataName() + "'" +
            ", webComponentType='" + getWebComponentType() + "'" +
            ", otherEntityIsTree='" + getOtherEntityIsTree() + "'" +
            ", showInFilterTree='" + getShowInFilterTree() + "'" +
            ", dataDictionaryCode='" + getDataDictionaryCode() + "'" +
            ", clientReadOnly='" + getClientReadOnly() + "'" +
            ", endUsed='" + getEndUsed() + "'" +
            ", options='" + getOptions() + "'" +
            "}";
    }
}
