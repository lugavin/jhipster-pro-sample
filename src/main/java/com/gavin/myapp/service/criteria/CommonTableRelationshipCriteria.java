package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.domain.enumeration.SourceType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gavin.myapp.domain.CommonTableRelationship} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.CommonTableRelationshipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-table-relationships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommonTableRelationshipCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering RelationshipType
     */
    public static class RelationshipTypeFilter extends Filter<RelationshipType> {

        public RelationshipTypeFilter() {}

        public RelationshipTypeFilter(RelationshipTypeFilter filter) {
            super(filter);
        }

        @Override
        public RelationshipTypeFilter copy() {
            return new RelationshipTypeFilter(this);
        }
    }

    /**
     * Class for filtering SourceType
     */
    public static class SourceTypeFilter extends Filter<SourceType> {

        public SourceTypeFilter() {}

        public SourceTypeFilter(SourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public SourceTypeFilter copy() {
            return new SourceTypeFilter(this);
        }
    }

    /**
     * Class for filtering FixedType
     */
    public static class FixedTypeFilter extends Filter<FixedType> {

        public FixedTypeFilter() {}

        public FixedTypeFilter(FixedTypeFilter filter) {
            super(filter);
        }

        @Override
        public FixedTypeFilter copy() {
            return new FixedTypeFilter(this);
        }
    }

    /**
     * Class for filtering EndUsedType
     */
    public static class EndUsedTypeFilter extends Filter<EndUsedType> {

        public EndUsedTypeFilter() {}

        public EndUsedTypeFilter(EndUsedTypeFilter filter) {
            super(filter);
        }

        @Override
        public EndUsedTypeFilter copy() {
            return new EndUsedTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private RelationshipTypeFilter relationshipType;

    private SourceTypeFilter sourceType;

    private StringFilter otherEntityField;

    private StringFilter otherEntityName;

    private StringFilter relationshipName;

    private StringFilter otherEntityRelationshipName;

    private IntegerFilter columnWidth;

    private IntegerFilter order;

    private FixedTypeFilter fixed;

    private BooleanFilter editInList;

    private BooleanFilter enableFilter;

    private BooleanFilter hideInList;

    private BooleanFilter hideInForm;

    private BooleanFilter system;

    private StringFilter fontColor;

    private StringFilter backgroundColor;

    private StringFilter help;

    private BooleanFilter ownerSide;

    private StringFilter dataName;

    private StringFilter webComponentType;

    private BooleanFilter otherEntityIsTree;

    private BooleanFilter showInFilterTree;

    private StringFilter dataDictionaryCode;

    private BooleanFilter clientReadOnly;

    private EndUsedTypeFilter endUsed;

    private StringFilter options;

    private LongFilter relationEntityId;

    private StringFilter relationEntityName;

    private LongFilter dataDictionaryNodeId;

    private StringFilter dataDictionaryNodeName;

    private LongFilter metaModelId;

    private StringFilter metaModelName;

    private LongFilter commonTableId;

    private StringFilter commonTableName;

    public CommonTableRelationshipCriteria() {}

    public CommonTableRelationshipCriteria(CommonTableRelationshipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.relationshipType = other.relationshipType == null ? null : other.relationshipType.copy();
        this.sourceType = other.sourceType == null ? null : other.sourceType.copy();
        this.otherEntityField = other.otherEntityField == null ? null : other.otherEntityField.copy();
        this.otherEntityName = other.otherEntityName == null ? null : other.otherEntityName.copy();
        this.relationshipName = other.relationshipName == null ? null : other.relationshipName.copy();
        this.otherEntityRelationshipName = other.otherEntityRelationshipName == null ? null : other.otherEntityRelationshipName.copy();
        this.columnWidth = other.columnWidth == null ? null : other.columnWidth.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.fixed = other.fixed == null ? null : other.fixed.copy();
        this.editInList = other.editInList == null ? null : other.editInList.copy();
        this.enableFilter = other.enableFilter == null ? null : other.enableFilter.copy();
        this.hideInList = other.hideInList == null ? null : other.hideInList.copy();
        this.hideInForm = other.hideInForm == null ? null : other.hideInForm.copy();
        this.system = other.system == null ? null : other.system.copy();
        this.fontColor = other.fontColor == null ? null : other.fontColor.copy();
        this.backgroundColor = other.backgroundColor == null ? null : other.backgroundColor.copy();
        this.help = other.help == null ? null : other.help.copy();
        this.ownerSide = other.ownerSide == null ? null : other.ownerSide.copy();
        this.dataName = other.dataName == null ? null : other.dataName.copy();
        this.webComponentType = other.webComponentType == null ? null : other.webComponentType.copy();
        this.otherEntityIsTree = other.otherEntityIsTree == null ? null : other.otherEntityIsTree.copy();
        this.showInFilterTree = other.showInFilterTree == null ? null : other.showInFilterTree.copy();
        this.dataDictionaryCode = other.dataDictionaryCode == null ? null : other.dataDictionaryCode.copy();
        this.clientReadOnly = other.clientReadOnly == null ? null : other.clientReadOnly.copy();
        this.endUsed = other.endUsed == null ? null : other.endUsed.copy();
        this.options = other.options == null ? null : other.options.copy();
        this.relationEntityId = other.relationEntityId == null ? null : other.relationEntityId.copy();
        this.relationEntityName = other.relationEntityName == null ? null : other.relationEntityName.copy();
        this.dataDictionaryNodeId = other.dataDictionaryNodeId == null ? null : other.dataDictionaryNodeId.copy();
        this.dataDictionaryNodeName = other.dataDictionaryNodeName == null ? null : other.dataDictionaryNodeName.copy();
        this.metaModelId = other.metaModelId == null ? null : other.metaModelId.copy();
        this.metaModelName = other.metaModelName == null ? null : other.metaModelName.copy();
        this.commonTableId = other.commonTableId == null ? null : other.commonTableId.copy();
        this.commonTableName = other.commonTableName == null ? null : other.commonTableName.copy();
    }

    @Override
    public CommonTableRelationshipCriteria copy() {
        return new CommonTableRelationshipCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public RelationshipTypeFilter getRelationshipType() {
        return relationshipType;
    }

    public RelationshipTypeFilter relationshipType() {
        if (relationshipType == null) {
            relationshipType = new RelationshipTypeFilter();
        }
        return relationshipType;
    }

    public void setRelationshipType(RelationshipTypeFilter relationshipType) {
        this.relationshipType = relationshipType;
    }

    public SourceTypeFilter getSourceType() {
        return sourceType;
    }

    public SourceTypeFilter sourceType() {
        if (sourceType == null) {
            sourceType = new SourceTypeFilter();
        }
        return sourceType;
    }

    public void setSourceType(SourceTypeFilter sourceType) {
        this.sourceType = sourceType;
    }

    public StringFilter getOtherEntityField() {
        return otherEntityField;
    }

    public StringFilter otherEntityField() {
        if (otherEntityField == null) {
            otherEntityField = new StringFilter();
        }
        return otherEntityField;
    }

    public void setOtherEntityField(StringFilter otherEntityField) {
        this.otherEntityField = otherEntityField;
    }

    public StringFilter getOtherEntityName() {
        return otherEntityName;
    }

    public StringFilter otherEntityName() {
        if (otherEntityName == null) {
            otherEntityName = new StringFilter();
        }
        return otherEntityName;
    }

    public void setOtherEntityName(StringFilter otherEntityName) {
        this.otherEntityName = otherEntityName;
    }

    public StringFilter getRelationshipName() {
        return relationshipName;
    }

    public StringFilter relationshipName() {
        if (relationshipName == null) {
            relationshipName = new StringFilter();
        }
        return relationshipName;
    }

    public void setRelationshipName(StringFilter relationshipName) {
        this.relationshipName = relationshipName;
    }

    public StringFilter getOtherEntityRelationshipName() {
        return otherEntityRelationshipName;
    }

    public StringFilter otherEntityRelationshipName() {
        if (otherEntityRelationshipName == null) {
            otherEntityRelationshipName = new StringFilter();
        }
        return otherEntityRelationshipName;
    }

    public void setOtherEntityRelationshipName(StringFilter otherEntityRelationshipName) {
        this.otherEntityRelationshipName = otherEntityRelationshipName;
    }

    public IntegerFilter getColumnWidth() {
        return columnWidth;
    }

    public IntegerFilter columnWidth() {
        if (columnWidth == null) {
            columnWidth = new IntegerFilter();
        }
        return columnWidth;
    }

    public void setColumnWidth(IntegerFilter columnWidth) {
        this.columnWidth = columnWidth;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public FixedTypeFilter getFixed() {
        return fixed;
    }

    public FixedTypeFilter fixed() {
        if (fixed == null) {
            fixed = new FixedTypeFilter();
        }
        return fixed;
    }

    public void setFixed(FixedTypeFilter fixed) {
        this.fixed = fixed;
    }

    public BooleanFilter getEditInList() {
        return editInList;
    }

    public BooleanFilter editInList() {
        if (editInList == null) {
            editInList = new BooleanFilter();
        }
        return editInList;
    }

    public void setEditInList(BooleanFilter editInList) {
        this.editInList = editInList;
    }

    public BooleanFilter getEnableFilter() {
        return enableFilter;
    }

    public BooleanFilter enableFilter() {
        if (enableFilter == null) {
            enableFilter = new BooleanFilter();
        }
        return enableFilter;
    }

    public void setEnableFilter(BooleanFilter enableFilter) {
        this.enableFilter = enableFilter;
    }

    public BooleanFilter getHideInList() {
        return hideInList;
    }

    public BooleanFilter hideInList() {
        if (hideInList == null) {
            hideInList = new BooleanFilter();
        }
        return hideInList;
    }

    public void setHideInList(BooleanFilter hideInList) {
        this.hideInList = hideInList;
    }

    public BooleanFilter getHideInForm() {
        return hideInForm;
    }

    public BooleanFilter hideInForm() {
        if (hideInForm == null) {
            hideInForm = new BooleanFilter();
        }
        return hideInForm;
    }

    public void setHideInForm(BooleanFilter hideInForm) {
        this.hideInForm = hideInForm;
    }

    public BooleanFilter getSystem() {
        return system;
    }

    public BooleanFilter system() {
        if (system == null) {
            system = new BooleanFilter();
        }
        return system;
    }

    public void setSystem(BooleanFilter system) {
        this.system = system;
    }

    public StringFilter getFontColor() {
        return fontColor;
    }

    public StringFilter fontColor() {
        if (fontColor == null) {
            fontColor = new StringFilter();
        }
        return fontColor;
    }

    public void setFontColor(StringFilter fontColor) {
        this.fontColor = fontColor;
    }

    public StringFilter getBackgroundColor() {
        return backgroundColor;
    }

    public StringFilter backgroundColor() {
        if (backgroundColor == null) {
            backgroundColor = new StringFilter();
        }
        return backgroundColor;
    }

    public void setBackgroundColor(StringFilter backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public StringFilter getHelp() {
        return help;
    }

    public StringFilter help() {
        if (help == null) {
            help = new StringFilter();
        }
        return help;
    }

    public void setHelp(StringFilter help) {
        this.help = help;
    }

    public BooleanFilter getOwnerSide() {
        return ownerSide;
    }

    public BooleanFilter ownerSide() {
        if (ownerSide == null) {
            ownerSide = new BooleanFilter();
        }
        return ownerSide;
    }

    public void setOwnerSide(BooleanFilter ownerSide) {
        this.ownerSide = ownerSide;
    }

    public StringFilter getDataName() {
        return dataName;
    }

    public StringFilter dataName() {
        if (dataName == null) {
            dataName = new StringFilter();
        }
        return dataName;
    }

    public void setDataName(StringFilter dataName) {
        this.dataName = dataName;
    }

    public StringFilter getWebComponentType() {
        return webComponentType;
    }

    public StringFilter webComponentType() {
        if (webComponentType == null) {
            webComponentType = new StringFilter();
        }
        return webComponentType;
    }

    public void setWebComponentType(StringFilter webComponentType) {
        this.webComponentType = webComponentType;
    }

    public BooleanFilter getOtherEntityIsTree() {
        return otherEntityIsTree;
    }

    public BooleanFilter otherEntityIsTree() {
        if (otherEntityIsTree == null) {
            otherEntityIsTree = new BooleanFilter();
        }
        return otherEntityIsTree;
    }

    public void setOtherEntityIsTree(BooleanFilter otherEntityIsTree) {
        this.otherEntityIsTree = otherEntityIsTree;
    }

    public BooleanFilter getShowInFilterTree() {
        return showInFilterTree;
    }

    public BooleanFilter showInFilterTree() {
        if (showInFilterTree == null) {
            showInFilterTree = new BooleanFilter();
        }
        return showInFilterTree;
    }

    public void setShowInFilterTree(BooleanFilter showInFilterTree) {
        this.showInFilterTree = showInFilterTree;
    }

    public StringFilter getDataDictionaryCode() {
        return dataDictionaryCode;
    }

    public StringFilter dataDictionaryCode() {
        if (dataDictionaryCode == null) {
            dataDictionaryCode = new StringFilter();
        }
        return dataDictionaryCode;
    }

    public void setDataDictionaryCode(StringFilter dataDictionaryCode) {
        this.dataDictionaryCode = dataDictionaryCode;
    }

    public BooleanFilter getClientReadOnly() {
        return clientReadOnly;
    }

    public BooleanFilter clientReadOnly() {
        if (clientReadOnly == null) {
            clientReadOnly = new BooleanFilter();
        }
        return clientReadOnly;
    }

    public void setClientReadOnly(BooleanFilter clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
    }

    public EndUsedTypeFilter getEndUsed() {
        return endUsed;
    }

    public EndUsedTypeFilter endUsed() {
        if (endUsed == null) {
            endUsed = new EndUsedTypeFilter();
        }
        return endUsed;
    }

    public void setEndUsed(EndUsedTypeFilter endUsed) {
        this.endUsed = endUsed;
    }

    public StringFilter getOptions() {
        return options;
    }

    public StringFilter options() {
        if (options == null) {
            options = new StringFilter();
        }
        return options;
    }

    public void setOptions(StringFilter options) {
        this.options = options;
    }

    public LongFilter getRelationEntityId() {
        return relationEntityId;
    }

    public LongFilter relationEntityId() {
        if (relationEntityId == null) {
            relationEntityId = new LongFilter();
        }
        return relationEntityId;
    }

    public void setRelationEntityId(LongFilter relationEntityId) {
        this.relationEntityId = relationEntityId;
    }

    public StringFilter getRelationEntityName() {
        return relationEntityName;
    }

    public StringFilter relationEntityName() {
        if (relationEntityName == null) {
            relationEntityName = new StringFilter();
        }
        return relationEntityName;
    }

    public void setRelationEntityName(StringFilter relationEntityName) {
        this.relationEntityName = relationEntityName;
    }

    public LongFilter getDataDictionaryNodeId() {
        return dataDictionaryNodeId;
    }

    public LongFilter dataDictionaryNodeId() {
        if (dataDictionaryNodeId == null) {
            dataDictionaryNodeId = new LongFilter();
        }
        return dataDictionaryNodeId;
    }

    public void setDataDictionaryNodeId(LongFilter dataDictionaryNodeId) {
        this.dataDictionaryNodeId = dataDictionaryNodeId;
    }

    public StringFilter getDataDictionaryNodeName() {
        return dataDictionaryNodeName;
    }

    public StringFilter dataDictionaryNodeName() {
        if (dataDictionaryNodeName == null) {
            dataDictionaryNodeName = new StringFilter();
        }
        return dataDictionaryNodeName;
    }

    public void setDataDictionaryNodeName(StringFilter dataDictionaryNodeName) {
        this.dataDictionaryNodeName = dataDictionaryNodeName;
    }

    public LongFilter getMetaModelId() {
        return metaModelId;
    }

    public LongFilter metaModelId() {
        if (metaModelId == null) {
            metaModelId = new LongFilter();
        }
        return metaModelId;
    }

    public void setMetaModelId(LongFilter metaModelId) {
        this.metaModelId = metaModelId;
    }

    public StringFilter getMetaModelName() {
        return metaModelName;
    }

    public StringFilter metaModelName() {
        if (metaModelName == null) {
            metaModelName = new StringFilter();
        }
        return metaModelName;
    }

    public void setMetaModelName(StringFilter metaModelName) {
        this.metaModelName = metaModelName;
    }

    public LongFilter getCommonTableId() {
        return commonTableId;
    }

    public LongFilter commonTableId() {
        if (commonTableId == null) {
            commonTableId = new LongFilter();
        }
        return commonTableId;
    }

    public void setCommonTableId(LongFilter commonTableId) {
        this.commonTableId = commonTableId;
    }

    public StringFilter getCommonTableName() {
        return commonTableName;
    }

    public StringFilter commonTableName() {
        if (commonTableName == null) {
            commonTableName = new StringFilter();
        }
        return commonTableName;
    }

    public void setCommonTableName(StringFilter commonTableName) {
        this.commonTableName = commonTableName;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommonTableRelationshipCriteria that = (CommonTableRelationshipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(relationshipType, that.relationshipType) &&
            Objects.equals(sourceType, that.sourceType) &&
            Objects.equals(otherEntityField, that.otherEntityField) &&
            Objects.equals(otherEntityName, that.otherEntityName) &&
            Objects.equals(relationshipName, that.relationshipName) &&
            Objects.equals(otherEntityRelationshipName, that.otherEntityRelationshipName) &&
            Objects.equals(columnWidth, that.columnWidth) &&
            Objects.equals(order, that.order) &&
            Objects.equals(fixed, that.fixed) &&
            Objects.equals(editInList, that.editInList) &&
            Objects.equals(enableFilter, that.enableFilter) &&
            Objects.equals(hideInList, that.hideInList) &&
            Objects.equals(hideInForm, that.hideInForm) &&
            Objects.equals(system, that.system) &&
            Objects.equals(fontColor, that.fontColor) &&
            Objects.equals(backgroundColor, that.backgroundColor) &&
            Objects.equals(help, that.help) &&
            Objects.equals(ownerSide, that.ownerSide) &&
            Objects.equals(dataName, that.dataName) &&
            Objects.equals(webComponentType, that.webComponentType) &&
            Objects.equals(otherEntityIsTree, that.otherEntityIsTree) &&
            Objects.equals(showInFilterTree, that.showInFilterTree) &&
            Objects.equals(dataDictionaryCode, that.dataDictionaryCode) &&
            Objects.equals(clientReadOnly, that.clientReadOnly) &&
            Objects.equals(endUsed, that.endUsed) &&
            Objects.equals(options, that.options) &&
            Objects.equals(relationEntityId, that.relationEntityId) &&
            Objects.equals(relationEntityName, that.relationEntityName) &&
            Objects.equals(dataDictionaryNodeId, that.dataDictionaryNodeId) &&
            Objects.equals(dataDictionaryNodeName, that.dataDictionaryNodeName) &&
            Objects.equals(metaModelId, that.metaModelId) &&
            Objects.equals(metaModelName, that.metaModelName) &&
            Objects.equals(commonTableId, that.commonTableId) &&
            Objects.equals(commonTableName, that.commonTableName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            relationshipType,
            sourceType,
            otherEntityField,
            otherEntityName,
            relationshipName,
            otherEntityRelationshipName,
            columnWidth,
            order,
            fixed,
            editInList,
            enableFilter,
            hideInList,
            hideInForm,
            system,
            fontColor,
            backgroundColor,
            help,
            ownerSide,
            dataName,
            webComponentType,
            otherEntityIsTree,
            showInFilterTree,
            dataDictionaryCode,
            clientReadOnly,
            endUsed,
            options,
            relationEntityId,
            relationEntityName,
            dataDictionaryNodeId,
            dataDictionaryNodeName,
            metaModelId,
            metaModelName,
            commonTableId,
            commonTableName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableRelationshipCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (relationshipType != null ? "relationshipType=" + relationshipType + ", " : "") +
                (sourceType != null ? "sourceType=" + sourceType + ", " : "") +
                (otherEntityField != null ? "otherEntityField=" + otherEntityField + ", " : "") +
                (otherEntityName != null ? "otherEntityName=" + otherEntityName + ", " : "") +
                (relationshipName != null ? "relationshipName=" + relationshipName + ", " : "") +
                (otherEntityRelationshipName != null ? "otherEntityRelationshipName=" + otherEntityRelationshipName + ", " : "") +
                (columnWidth != null ? "columnWidth=" + columnWidth + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (fixed != null ? "fixed=" + fixed + ", " : "") +
                (editInList != null ? "editInList=" + editInList + ", " : "") +
                (enableFilter != null ? "enableFilter=" + enableFilter + ", " : "") +
                (hideInList != null ? "hideInList=" + hideInList + ", " : "") +
                (hideInForm != null ? "hideInForm=" + hideInForm + ", " : "") +
                (system != null ? "system=" + system + ", " : "") +
                (fontColor != null ? "fontColor=" + fontColor + ", " : "") +
                (backgroundColor != null ? "backgroundColor=" + backgroundColor + ", " : "") +
                (help != null ? "help=" + help + ", " : "") +
                (ownerSide != null ? "ownerSide=" + ownerSide + ", " : "") +
                (dataName != null ? "dataName=" + dataName + ", " : "") +
                (webComponentType != null ? "webComponentType=" + webComponentType + ", " : "") +
                (otherEntityIsTree != null ? "otherEntityIsTree=" + otherEntityIsTree + ", " : "") +
                (showInFilterTree != null ? "showInFilterTree=" + showInFilterTree + ", " : "") +
                (dataDictionaryCode != null ? "dataDictionaryCode=" + dataDictionaryCode + ", " : "") +
                (clientReadOnly != null ? "clientReadOnly=" + clientReadOnly + ", " : "") +
                (endUsed != null ? "endUsed=" + endUsed + ", " : "") +
                (options != null ? "options=" + options + ", " : "") +
                (relationEntityId != null ? "relationEntityId=" + relationEntityId + ", " : "") +
                (relationEntityName != null ? "relationEntityName=" + relationEntityName + ", " : "") +
                (dataDictionaryNodeId != null ? "dataDictionaryNodeId=" + dataDictionaryNodeId + ", " : "") +
                (dataDictionaryNodeName != null ? "dataDictionaryNodeName=" + dataDictionaryNodeName + ", " : "") +
                (metaModelId != null ? "metaModelId=" + metaModelId + ", " : "") +
                (metaModelName != null ? "metaModelName=" + metaModelName + ", " : "") +
                (commonTableId != null ? "commonTableId=" + commonTableId + ", " : "") +
                (commonTableName != null ? "commonTableName=" + commonTableName + ", " : "") +
            "}";
    }
}
