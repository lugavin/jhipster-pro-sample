package com.gavin.myapp.service.criteria;

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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.gavin.myapp.domain.CommonTable} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.CommonTableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-tables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommonTableCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter entityName;

    private StringFilter tableName;

    private BooleanFilter system;

    private StringFilter clazzName;

    private BooleanFilter generated;

    private ZonedDateTimeFilter creatAt;

    private ZonedDateTimeFilter generateAt;

    private ZonedDateTimeFilter generateClassAt;

    private StringFilter description;

    private BooleanFilter treeTable;

    private LongFilter baseTableId;

    private IntegerFilter recordActionWidth;

    private BooleanFilter editInModal;

    private LongFilter commonTableFieldsId;

    private StringFilter commonTableFieldsTitle;

    private LongFilter relationshipsId;

    private StringFilter relationshipsName;

    private LongFilter metaModelId;

    private StringFilter metaModelName;

    private LongFilter creatorId;

    private StringFilter creatorLogin;

    private LongFilter businessTypeId;

    private StringFilter businessTypeName;

    public CommonTableCriteria() {}

    public CommonTableCriteria(CommonTableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.entityName = other.entityName == null ? null : other.entityName.copy();
        this.tableName = other.tableName == null ? null : other.tableName.copy();
        this.system = other.system == null ? null : other.system.copy();
        this.clazzName = other.clazzName == null ? null : other.clazzName.copy();
        this.generated = other.generated == null ? null : other.generated.copy();
        this.creatAt = other.creatAt == null ? null : other.creatAt.copy();
        this.generateAt = other.generateAt == null ? null : other.generateAt.copy();
        this.generateClassAt = other.generateClassAt == null ? null : other.generateClassAt.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.treeTable = other.treeTable == null ? null : other.treeTable.copy();
        this.baseTableId = other.baseTableId == null ? null : other.baseTableId.copy();
        this.recordActionWidth = other.recordActionWidth == null ? null : other.recordActionWidth.copy();
        this.editInModal = other.editInModal == null ? null : other.editInModal.copy();
        this.commonTableFieldsId = other.commonTableFieldsId == null ? null : other.commonTableFieldsId.copy();
        this.commonTableFieldsTitle = other.commonTableFieldsTitle == null ? null : other.commonTableFieldsTitle.copy();
        this.relationshipsId = other.relationshipsId == null ? null : other.relationshipsId.copy();
        this.relationshipsName = other.relationshipsName == null ? null : other.relationshipsName.copy();
        this.metaModelId = other.metaModelId == null ? null : other.metaModelId.copy();
        this.metaModelName = other.metaModelName == null ? null : other.metaModelName.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.creatorLogin = other.creatorLogin == null ? null : other.creatorLogin.copy();
        this.businessTypeId = other.businessTypeId == null ? null : other.businessTypeId.copy();
        this.businessTypeName = other.businessTypeName == null ? null : other.businessTypeName.copy();
    }

    @Override
    public CommonTableCriteria copy() {
        return new CommonTableCriteria(this);
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

    public StringFilter getEntityName() {
        return entityName;
    }

    public StringFilter entityName() {
        if (entityName == null) {
            entityName = new StringFilter();
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public StringFilter getTableName() {
        return tableName;
    }

    public StringFilter tableName() {
        if (tableName == null) {
            tableName = new StringFilter();
        }
        return tableName;
    }

    public void setTableName(StringFilter tableName) {
        this.tableName = tableName;
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

    public StringFilter getClazzName() {
        return clazzName;
    }

    public StringFilter clazzName() {
        if (clazzName == null) {
            clazzName = new StringFilter();
        }
        return clazzName;
    }

    public void setClazzName(StringFilter clazzName) {
        this.clazzName = clazzName;
    }

    public BooleanFilter getGenerated() {
        return generated;
    }

    public BooleanFilter generated() {
        if (generated == null) {
            generated = new BooleanFilter();
        }
        return generated;
    }

    public void setGenerated(BooleanFilter generated) {
        this.generated = generated;
    }

    public ZonedDateTimeFilter getCreatAt() {
        return creatAt;
    }

    public ZonedDateTimeFilter creatAt() {
        if (creatAt == null) {
            creatAt = new ZonedDateTimeFilter();
        }
        return creatAt;
    }

    public void setCreatAt(ZonedDateTimeFilter creatAt) {
        this.creatAt = creatAt;
    }

    public ZonedDateTimeFilter getGenerateAt() {
        return generateAt;
    }

    public ZonedDateTimeFilter generateAt() {
        if (generateAt == null) {
            generateAt = new ZonedDateTimeFilter();
        }
        return generateAt;
    }

    public void setGenerateAt(ZonedDateTimeFilter generateAt) {
        this.generateAt = generateAt;
    }

    public ZonedDateTimeFilter getGenerateClassAt() {
        return generateClassAt;
    }

    public ZonedDateTimeFilter generateClassAt() {
        if (generateClassAt == null) {
            generateClassAt = new ZonedDateTimeFilter();
        }
        return generateClassAt;
    }

    public void setGenerateClassAt(ZonedDateTimeFilter generateClassAt) {
        this.generateClassAt = generateClassAt;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getTreeTable() {
        return treeTable;
    }

    public BooleanFilter treeTable() {
        if (treeTable == null) {
            treeTable = new BooleanFilter();
        }
        return treeTable;
    }

    public void setTreeTable(BooleanFilter treeTable) {
        this.treeTable = treeTable;
    }

    public LongFilter getBaseTableId() {
        return baseTableId;
    }

    public LongFilter baseTableId() {
        if (baseTableId == null) {
            baseTableId = new LongFilter();
        }
        return baseTableId;
    }

    public void setBaseTableId(LongFilter baseTableId) {
        this.baseTableId = baseTableId;
    }

    public IntegerFilter getRecordActionWidth() {
        return recordActionWidth;
    }

    public IntegerFilter recordActionWidth() {
        if (recordActionWidth == null) {
            recordActionWidth = new IntegerFilter();
        }
        return recordActionWidth;
    }

    public void setRecordActionWidth(IntegerFilter recordActionWidth) {
        this.recordActionWidth = recordActionWidth;
    }

    public BooleanFilter getEditInModal() {
        return editInModal;
    }

    public BooleanFilter editInModal() {
        if (editInModal == null) {
            editInModal = new BooleanFilter();
        }
        return editInModal;
    }

    public void setEditInModal(BooleanFilter editInModal) {
        this.editInModal = editInModal;
    }

    public LongFilter getCommonTableFieldsId() {
        return commonTableFieldsId;
    }

    public LongFilter commonTableFieldsId() {
        if (commonTableFieldsId == null) {
            commonTableFieldsId = new LongFilter();
        }
        return commonTableFieldsId;
    }

    public void setCommonTableFieldsId(LongFilter commonTableFieldsId) {
        this.commonTableFieldsId = commonTableFieldsId;
    }

    public StringFilter getCommonTableFieldsTitle() {
        return commonTableFieldsTitle;
    }

    public StringFilter commonTableFieldsTitle() {
        if (commonTableFieldsTitle == null) {
            commonTableFieldsTitle = new StringFilter();
        }
        return commonTableFieldsTitle;
    }

    public void setCommonTableFieldsTitle(StringFilter commonTableFieldsTitle) {
        this.commonTableFieldsTitle = commonTableFieldsTitle;
    }

    public LongFilter getRelationshipsId() {
        return relationshipsId;
    }

    public LongFilter relationshipsId() {
        if (relationshipsId == null) {
            relationshipsId = new LongFilter();
        }
        return relationshipsId;
    }

    public void setRelationshipsId(LongFilter relationshipsId) {
        this.relationshipsId = relationshipsId;
    }

    public StringFilter getRelationshipsName() {
        return relationshipsName;
    }

    public StringFilter relationshipsName() {
        if (relationshipsName == null) {
            relationshipsName = new StringFilter();
        }
        return relationshipsName;
    }

    public void setRelationshipsName(StringFilter relationshipsName) {
        this.relationshipsName = relationshipsName;
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

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public LongFilter creatorId() {
        if (creatorId == null) {
            creatorId = new LongFilter();
        }
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public StringFilter getCreatorLogin() {
        return creatorLogin;
    }

    public StringFilter creatorLogin() {
        if (creatorLogin == null) {
            creatorLogin = new StringFilter();
        }
        return creatorLogin;
    }

    public void setCreatorLogin(StringFilter creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public LongFilter getBusinessTypeId() {
        return businessTypeId;
    }

    public LongFilter businessTypeId() {
        if (businessTypeId == null) {
            businessTypeId = new LongFilter();
        }
        return businessTypeId;
    }

    public void setBusinessTypeId(LongFilter businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public StringFilter getBusinessTypeName() {
        return businessTypeName;
    }

    public StringFilter businessTypeName() {
        if (businessTypeName == null) {
            businessTypeName = new StringFilter();
        }
        return businessTypeName;
    }

    public void setBusinessTypeName(StringFilter businessTypeName) {
        this.businessTypeName = businessTypeName;
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
        final CommonTableCriteria that = (CommonTableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(tableName, that.tableName) &&
            Objects.equals(system, that.system) &&
            Objects.equals(clazzName, that.clazzName) &&
            Objects.equals(generated, that.generated) &&
            Objects.equals(creatAt, that.creatAt) &&
            Objects.equals(generateAt, that.generateAt) &&
            Objects.equals(generateClassAt, that.generateClassAt) &&
            Objects.equals(description, that.description) &&
            Objects.equals(treeTable, that.treeTable) &&
            Objects.equals(baseTableId, that.baseTableId) &&
            Objects.equals(recordActionWidth, that.recordActionWidth) &&
            Objects.equals(editInModal, that.editInModal) &&
            Objects.equals(commonTableFieldsId, that.commonTableFieldsId) &&
            Objects.equals(commonTableFieldsTitle, that.commonTableFieldsTitle) &&
            Objects.equals(relationshipsId, that.relationshipsId) &&
            Objects.equals(relationshipsName, that.relationshipsName) &&
            Objects.equals(metaModelId, that.metaModelId) &&
            Objects.equals(metaModelName, that.metaModelName) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(creatorLogin, that.creatorLogin) &&
            Objects.equals(businessTypeId, that.businessTypeId) &&
            Objects.equals(businessTypeName, that.businessTypeName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            entityName,
            tableName,
            system,
            clazzName,
            generated,
            creatAt,
            generateAt,
            generateClassAt,
            description,
            treeTable,
            baseTableId,
            recordActionWidth,
            editInModal,
            commonTableFieldsId,
            commonTableFieldsTitle,
            relationshipsId,
            relationshipsName,
            metaModelId,
            metaModelName,
            creatorId,
            creatorLogin,
            businessTypeId,
            businessTypeName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonTableCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (entityName != null ? "entityName=" + entityName + ", " : "") +
                (tableName != null ? "tableName=" + tableName + ", " : "") +
                (system != null ? "system=" + system + ", " : "") +
                (clazzName != null ? "clazzName=" + clazzName + ", " : "") +
                (generated != null ? "generated=" + generated + ", " : "") +
                (creatAt != null ? "creatAt=" + creatAt + ", " : "") +
                (generateAt != null ? "generateAt=" + generateAt + ", " : "") +
                (generateClassAt != null ? "generateClassAt=" + generateClassAt + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (treeTable != null ? "treeTable=" + treeTable + ", " : "") +
                (baseTableId != null ? "baseTableId=" + baseTableId + ", " : "") +
                (recordActionWidth != null ? "recordActionWidth=" + recordActionWidth + ", " : "") +
                (editInModal != null ? "editInModal=" + editInModal + ", " : "") +
                (commonTableFieldsId != null ? "commonTableFieldsId=" + commonTableFieldsId + ", " : "") +
                (commonTableFieldsTitle != null ? "commonTableFieldsTitle=" + commonTableFieldsTitle + ", " : "") +
                (relationshipsId != null ? "relationshipsId=" + relationshipsId + ", " : "") +
                (relationshipsName != null ? "relationshipsName=" + relationshipsName + ", " : "") +
                (metaModelId != null ? "metaModelId=" + metaModelId + ", " : "") +
                (metaModelName != null ? "metaModelName=" + metaModelName + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (creatorLogin != null ? "creatorLogin=" + creatorLogin + ", " : "") +
                (businessTypeId != null ? "businessTypeId=" + businessTypeId + ", " : "") +
                (businessTypeName != null ? "businessTypeName=" + businessTypeName + ", " : "") +
            "}";
    }
}
