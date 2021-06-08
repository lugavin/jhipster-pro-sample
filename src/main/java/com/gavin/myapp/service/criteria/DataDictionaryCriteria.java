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

/**
 * Criteria class for the {@link com.gavin.myapp.domain.DataDictionary} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.DataDictionaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /data-dictionaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataDictionaryCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter description;

    private StringFilter fontColor;

    private StringFilter backgroundColor;

    private LongFilter childrenId;

    private StringFilter childrenName;

    private LongFilter parentId;

    private StringFilter parentName;

    public DataDictionaryCriteria() {}

    public DataDictionaryCriteria(DataDictionaryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.fontColor = other.fontColor == null ? null : other.fontColor.copy();
        this.backgroundColor = other.backgroundColor == null ? null : other.backgroundColor.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
    }

    @Override
    public DataDictionaryCriteria copy() {
        return new DataDictionaryCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            childrenName = new StringFilter();
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public StringFilter parentName() {
        if (parentName == null) {
            parentName = new StringFilter();
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
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
        final DataDictionaryCriteria that = (DataDictionaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fontColor, that.fontColor) &&
            Objects.equals(backgroundColor, that.backgroundColor) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, description, fontColor, backgroundColor, childrenId, childrenName, parentId, parentName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDictionaryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (fontColor != null ? "fontColor=" + fontColor + ", " : "") +
                (backgroundColor != null ? "backgroundColor=" + backgroundColor + ", " : "") +
                (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
                (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (parentName != null ? "parentName=" + parentName + ", " : "") +
            "}";
    }
}
