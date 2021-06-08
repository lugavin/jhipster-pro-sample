package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.domain.enumeration.SourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.CommonTableRelationship}的DTO。
 */
@ApiModel(description = "模型关系")
public class CommonTableRelationshipDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 关系类型
     */
    @NotNull
    @ApiModelProperty(value = "关系类型", required = true)
    private RelationshipType relationshipType;

    /**
     * 来源类型
     */
    @NotNull
    @ApiModelProperty(value = "来源类型", required = true)
    private SourceType sourceType;

    /**
     * 关联表显示字段
     */
    @Size(max = 100)
    @ApiModelProperty(value = "关联表显示字段")
    private String otherEntityField;

    /**
     * 关联实体名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "关联实体名称", required = true)
    private String otherEntityName;

    /**
     * 关系属性名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "关系属性名称", required = true)
    private String relationshipName;

    /**
     * 对方属性名称
     */
    @Size(max = 100)
    @ApiModelProperty(value = "对方属性名称")
    private String otherEntityRelationshipName;

    /**
     * 列宽
     */
    @ApiModelProperty(value = "列宽")
    private Integer columnWidth;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer order;

    /**
     * 列固定
     */
    @ApiModelProperty(value = "列固定")
    private FixedType fixed;

    /**
     * 行内编辑
     */
    @ApiModelProperty(value = "行内编辑")
    private Boolean editInList;

    /**
     * 可过滤
     */
    @ApiModelProperty(value = "可过滤")
    private Boolean enableFilter;

    /**
     * 列表隐藏
     */
    @ApiModelProperty(value = "列表隐藏")
    private Boolean hideInList;

    /**
     * 表单隐藏
     */
    @ApiModelProperty(value = "表单隐藏")
    private Boolean hideInForm;

    /**
     * 系统定义
     */
    @ApiModelProperty(value = "系统定义")
    private Boolean system;

    /**
     * 字体颜色
     */
    @Size(max = 80)
    @ApiModelProperty(value = "字体颜色")
    private String fontColor;

    /**
     * 列背景色
     */
    @Size(max = 80)
    @ApiModelProperty(value = "列背景色")
    private String backgroundColor;

    /**
     * 详细字段说明
     */
    @Size(max = 200)
    @ApiModelProperty(value = "详细字段说明")
    private String help;

    /**
     * 是否维护端
     */
    @ApiModelProperty(value = "是否维护端")
    private Boolean ownerSide;

    /**
     * 数据源名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "数据源名称", required = true)
    private String dataName;

    /**
     * Web控件类型
     */
    @Size(max = 100)
    @ApiModelProperty(value = "Web控件类型")
    private String webComponentType;

    /**
     * 是否树形实体
     */
    @ApiModelProperty(value = "是否树形实体")
    private Boolean otherEntityIsTree;

    /**
     * 显示在过滤树
     */
    @ApiModelProperty(value = "显示在过滤树")
    private Boolean showInFilterTree;

    /**
     * 字典表代码
     */
    @Size(max = 100)
    @ApiModelProperty(value = "字典表代码")
    private String dataDictionaryCode;

    /**
     * 前端只读
     */
    @ApiModelProperty(value = "前端只读")
    private Boolean clientReadOnly;

    /**
     * 前端用法
     */
    @ApiModelProperty(value = "前端用法")
    private EndUsedType endUsed;

    /**
     * 关系配置项
     */
    @ApiModelProperty(value = "关系配置项")
    private String options;

    private CommonTableDTO relationEntity;

    private DataDictionaryDTO dataDictionaryNode;

    private CommonTableDTO metaModel;

    private CommonTableDTO commonTable;

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

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getOtherEntityField() {
        return otherEntityField;
    }

    public void setOtherEntityField(String otherEntityField) {
        this.otherEntityField = otherEntityField;
    }

    public String getOtherEntityName() {
        return otherEntityName;
    }

    public void setOtherEntityName(String otherEntityName) {
        this.otherEntityName = otherEntityName;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getOtherEntityRelationshipName() {
        return otherEntityRelationshipName;
    }

    public void setOtherEntityRelationshipName(String otherEntityRelationshipName) {
        this.otherEntityRelationshipName = otherEntityRelationshipName;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public FixedType getFixed() {
        return fixed;
    }

    public void setFixed(FixedType fixed) {
        this.fixed = fixed;
    }

    public Boolean getEditInList() {
        return editInList;
    }

    public void setEditInList(Boolean editInList) {
        this.editInList = editInList;
    }

    public Boolean getEnableFilter() {
        return enableFilter;
    }

    public void setEnableFilter(Boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    public Boolean getHideInList() {
        return hideInList;
    }

    public void setHideInList(Boolean hideInList) {
        this.hideInList = hideInList;
    }

    public Boolean getHideInForm() {
        return hideInForm;
    }

    public void setHideInForm(Boolean hideInForm) {
        this.hideInForm = hideInForm;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public Boolean getOwnerSide() {
        return ownerSide;
    }

    public void setOwnerSide(Boolean ownerSide) {
        this.ownerSide = ownerSide;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getWebComponentType() {
        return webComponentType;
    }

    public void setWebComponentType(String webComponentType) {
        this.webComponentType = webComponentType;
    }

    public Boolean getOtherEntityIsTree() {
        return otherEntityIsTree;
    }

    public void setOtherEntityIsTree(Boolean otherEntityIsTree) {
        this.otherEntityIsTree = otherEntityIsTree;
    }

    public Boolean getShowInFilterTree() {
        return showInFilterTree;
    }

    public void setShowInFilterTree(Boolean showInFilterTree) {
        this.showInFilterTree = showInFilterTree;
    }

    public String getDataDictionaryCode() {
        return dataDictionaryCode;
    }

    public void setDataDictionaryCode(String dataDictionaryCode) {
        this.dataDictionaryCode = dataDictionaryCode;
    }

    public Boolean getClientReadOnly() {
        return clientReadOnly;
    }

    public void setClientReadOnly(Boolean clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
    }

    public EndUsedType getEndUsed() {
        return endUsed;
    }

    public void setEndUsed(EndUsedType endUsed) {
        this.endUsed = endUsed;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public CommonTableDTO getRelationEntity() {
        return relationEntity;
    }

    public void setRelationEntity(CommonTableDTO relationEntity) {
        this.relationEntity = relationEntity;
    }

    public DataDictionaryDTO getDataDictionaryNode() {
        return dataDictionaryNode;
    }

    public void setDataDictionaryNode(DataDictionaryDTO dataDictionaryNode) {
        this.dataDictionaryNode = dataDictionaryNode;
    }

    public CommonTableDTO getMetaModel() {
        return metaModel;
    }

    public void setMetaModel(CommonTableDTO metaModel) {
        this.metaModel = metaModel;
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
        if (!(o instanceof CommonTableRelationshipDTO)) {
            return false;
        }

        CommonTableRelationshipDTO commonTableRelationshipDTO = (CommonTableRelationshipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonTableRelationshipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableRelationshipDTO{" +
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
            ", relationEntity=" + getRelationEntity() +
            ", dataDictionaryNode=" + getDataDictionaryNode() +
            ", metaModel=" + getMetaModel() +
            ", commonTable=" + getCommonTable() +
            "}";
    }
}
