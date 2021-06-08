package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.CommonFieldType;
import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
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

 * {@link com.gavin.myapp.domain.CommonTableField}的DTO。
 */
@ApiModel(description = "模型字段")
public class CommonTableFieldDTO implements Serializable {

    private Long id;

    /**
     * 标题
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 属性名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "属性名称", required = true)
    private String entityFieldName;

    /**
     * 数据类型
     */
    @NotNull
    @ApiModelProperty(value = "数据类型", required = true)
    private CommonFieldType type;

    /**
     * 字段名称
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "字段名称", required = true)
    private String tableColumnName;

    /**
     * 列宽
     */
    @Min(value = 0)
    @Max(value = 1200)
    @ApiModelProperty(value = "列宽")
    private Integer columnWidth;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer order;

    /**
     * 行内编辑
     */
    @ApiModelProperty(value = "行内编辑")
    private Boolean editInList;

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
     * 可过滤
     */
    @ApiModelProperty(value = "可过滤")
    private Boolean enableFilter;

    /**
     * 验证规则
     */
    @Size(max = 800)
    @ApiModelProperty(value = "验证规则")
    private String validateRules;

    /**
     * 显示在过滤树
     */
    @ApiModelProperty(value = "显示在过滤树")
    private Boolean showInFilterTree;

    /**
     * 列固定
     */
    @ApiModelProperty(value = "列固定")
    private FixedType fixed;

    /**
     * 可排序
     */
    @ApiModelProperty(value = "可排序")
    private Boolean sortable;

    /**
     * 树形标识
     */
    @ApiModelProperty(value = "树形标识")
    private Boolean treeIndicator;

    /**
     * 前端只读
     */
    @ApiModelProperty(value = "前端只读")
    private Boolean clientReadOnly;

    /**
     * 值范围
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "值范围")
    private String fieldValues;

    /**
     * 必填
     */
    @ApiModelProperty(value = "必填")
    private Boolean notNull;

    /**
     * 系统字段
     */
    @ApiModelProperty(value = "系统字段")
    private Boolean system;

    /**
     * 字段说明
     */
    @Size(max = 200)
    @ApiModelProperty(value = "字段说明")
    private String help;

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
     * 空值隐藏
     */
    @ApiModelProperty(value = "空值隐藏")
    private Boolean nullHideInForm;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntityFieldName() {
        return entityFieldName;
    }

    public void setEntityFieldName(String entityFieldName) {
        this.entityFieldName = entityFieldName;
    }

    public CommonFieldType getType() {
        return type;
    }

    public void setType(CommonFieldType type) {
        this.type = type;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
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

    public Boolean getEditInList() {
        return editInList;
    }

    public void setEditInList(Boolean editInList) {
        this.editInList = editInList;
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

    public Boolean getEnableFilter() {
        return enableFilter;
    }

    public void setEnableFilter(Boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    public String getValidateRules() {
        return validateRules;
    }

    public void setValidateRules(String validateRules) {
        this.validateRules = validateRules;
    }

    public Boolean getShowInFilterTree() {
        return showInFilterTree;
    }

    public void setShowInFilterTree(Boolean showInFilterTree) {
        this.showInFilterTree = showInFilterTree;
    }

    public FixedType getFixed() {
        return fixed;
    }

    public void setFixed(FixedType fixed) {
        this.fixed = fixed;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getTreeIndicator() {
        return treeIndicator;
    }

    public void setTreeIndicator(Boolean treeIndicator) {
        this.treeIndicator = treeIndicator;
    }

    public Boolean getClientReadOnly() {
        return clientReadOnly;
    }

    public void setClientReadOnly(Boolean clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
    }

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
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

    public Boolean getNullHideInForm() {
        return nullHideInForm;
    }

    public void setNullHideInForm(Boolean nullHideInForm) {
        this.nullHideInForm = nullHideInForm;
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
        if (!(o instanceof CommonTableFieldDTO)) {
            return false;
        }

        CommonTableFieldDTO commonTableFieldDTO = (CommonTableFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonTableFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableFieldDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", entityFieldName='" + getEntityFieldName() + "'" +
            ", type='" + getType() + "'" +
            ", tableColumnName='" + getTableColumnName() + "'" +
            ", columnWidth=" + getColumnWidth() +
            ", order=" + getOrder() +
            ", editInList='" + getEditInList() + "'" +
            ", hideInList='" + getHideInList() + "'" +
            ", hideInForm='" + getHideInForm() + "'" +
            ", enableFilter='" + getEnableFilter() + "'" +
            ", validateRules='" + getValidateRules() + "'" +
            ", showInFilterTree='" + getShowInFilterTree() + "'" +
            ", fixed='" + getFixed() + "'" +
            ", sortable='" + getSortable() + "'" +
            ", treeIndicator='" + getTreeIndicator() + "'" +
            ", clientReadOnly='" + getClientReadOnly() + "'" +
            ", fieldValues='" + getFieldValues() + "'" +
            ", notNull='" + getNotNull() + "'" +
            ", system='" + getSystem() + "'" +
            ", help='" + getHelp() + "'" +
            ", fontColor='" + getFontColor() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            ", nullHideInForm='" + getNullHideInForm() + "'" +
            ", endUsed='" + getEndUsed() + "'" +
            ", options='" + getOptions() + "'" +
            ", metaModel=" + getMetaModel() +
            ", commonTable=" + getCommonTable() +
            "}";
    }
}
