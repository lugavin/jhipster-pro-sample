package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.StatSourceType;
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
 * Criteria class for the {@link com.gavin.myapp.domain.StatisticsApi} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.StatisticsApiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /statistics-apis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StatisticsApiCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering StatSourceType
     */
    public static class StatSourceTypeFilter extends Filter<StatSourceType> {

        public StatSourceTypeFilter() {}

        public StatSourceTypeFilter(StatSourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public StatSourceTypeFilter copy() {
            return new StatSourceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter apiKey;

    private ZonedDateTimeFilter createAt;

    private ZonedDateTimeFilter updateAt;

    private StatSourceTypeFilter sourceType;

    private IntegerFilter updateInterval;

    private ZonedDateTimeFilter lastSQLRunTime;

    private BooleanFilter enable;

    private LongFilter commonTableId;

    private StringFilter commonTableName;

    private LongFilter creatorId;

    private StringFilter creatorFirstName;

    private LongFilter modifierId;

    private StringFilter modifierFirstName;

    public StatisticsApiCriteria() {}

    public StatisticsApiCriteria(StatisticsApiCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.apiKey = other.apiKey == null ? null : other.apiKey.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
        this.updateAt = other.updateAt == null ? null : other.updateAt.copy();
        this.sourceType = other.sourceType == null ? null : other.sourceType.copy();
        this.updateInterval = other.updateInterval == null ? null : other.updateInterval.copy();
        this.lastSQLRunTime = other.lastSQLRunTime == null ? null : other.lastSQLRunTime.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.commonTableId = other.commonTableId == null ? null : other.commonTableId.copy();
        this.commonTableName = other.commonTableName == null ? null : other.commonTableName.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.creatorFirstName = other.creatorFirstName == null ? null : other.creatorFirstName.copy();
        this.modifierId = other.modifierId == null ? null : other.modifierId.copy();
        this.modifierFirstName = other.modifierFirstName == null ? null : other.modifierFirstName.copy();
    }

    @Override
    public StatisticsApiCriteria copy() {
        return new StatisticsApiCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getApiKey() {
        return apiKey;
    }

    public StringFilter apiKey() {
        if (apiKey == null) {
            apiKey = new StringFilter();
        }
        return apiKey;
    }

    public void setApiKey(StringFilter apiKey) {
        this.apiKey = apiKey;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            createAt = new ZonedDateTimeFilter();
        }
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTimeFilter getUpdateAt() {
        return updateAt;
    }

    public ZonedDateTimeFilter updateAt() {
        if (updateAt == null) {
            updateAt = new ZonedDateTimeFilter();
        }
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTimeFilter updateAt) {
        this.updateAt = updateAt;
    }

    public StatSourceTypeFilter getSourceType() {
        return sourceType;
    }

    public StatSourceTypeFilter sourceType() {
        if (sourceType == null) {
            sourceType = new StatSourceTypeFilter();
        }
        return sourceType;
    }

    public void setSourceType(StatSourceTypeFilter sourceType) {
        this.sourceType = sourceType;
    }

    public IntegerFilter getUpdateInterval() {
        return updateInterval;
    }

    public IntegerFilter updateInterval() {
        if (updateInterval == null) {
            updateInterval = new IntegerFilter();
        }
        return updateInterval;
    }

    public void setUpdateInterval(IntegerFilter updateInterval) {
        this.updateInterval = updateInterval;
    }

    public ZonedDateTimeFilter getLastSQLRunTime() {
        return lastSQLRunTime;
    }

    public ZonedDateTimeFilter lastSQLRunTime() {
        if (lastSQLRunTime == null) {
            lastSQLRunTime = new ZonedDateTimeFilter();
        }
        return lastSQLRunTime;
    }

    public void setLastSQLRunTime(ZonedDateTimeFilter lastSQLRunTime) {
        this.lastSQLRunTime = lastSQLRunTime;
    }

    public BooleanFilter getEnable() {
        return enable;
    }

    public BooleanFilter enable() {
        if (enable == null) {
            enable = new BooleanFilter();
        }
        return enable;
    }

    public void setEnable(BooleanFilter enable) {
        this.enable = enable;
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

    public StringFilter getCreatorFirstName() {
        return creatorFirstName;
    }

    public StringFilter creatorFirstName() {
        if (creatorFirstName == null) {
            creatorFirstName = new StringFilter();
        }
        return creatorFirstName;
    }

    public void setCreatorFirstName(StringFilter creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }

    public LongFilter getModifierId() {
        return modifierId;
    }

    public LongFilter modifierId() {
        if (modifierId == null) {
            modifierId = new LongFilter();
        }
        return modifierId;
    }

    public void setModifierId(LongFilter modifierId) {
        this.modifierId = modifierId;
    }

    public StringFilter getModifierFirstName() {
        return modifierFirstName;
    }

    public StringFilter modifierFirstName() {
        if (modifierFirstName == null) {
            modifierFirstName = new StringFilter();
        }
        return modifierFirstName;
    }

    public void setModifierFirstName(StringFilter modifierFirstName) {
        this.modifierFirstName = modifierFirstName;
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
        final StatisticsApiCriteria that = (StatisticsApiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(apiKey, that.apiKey) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(sourceType, that.sourceType) &&
            Objects.equals(updateInterval, that.updateInterval) &&
            Objects.equals(lastSQLRunTime, that.lastSQLRunTime) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(commonTableId, that.commonTableId) &&
            Objects.equals(commonTableName, that.commonTableName) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(creatorFirstName, that.creatorFirstName) &&
            Objects.equals(modifierId, that.modifierId) &&
            Objects.equals(modifierFirstName, that.modifierFirstName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            apiKey,
            createAt,
            updateAt,
            sourceType,
            updateInterval,
            lastSQLRunTime,
            enable,
            commonTableId,
            commonTableName,
            creatorId,
            creatorFirstName,
            modifierId,
            modifierFirstName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatisticsApiCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (apiKey != null ? "apiKey=" + apiKey + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (updateAt != null ? "updateAt=" + updateAt + ", " : "") +
                (sourceType != null ? "sourceType=" + sourceType + ", " : "") +
                (updateInterval != null ? "updateInterval=" + updateInterval + ", " : "") +
                (lastSQLRunTime != null ? "lastSQLRunTime=" + lastSQLRunTime + ", " : "") +
                (enable != null ? "enable=" + enable + ", " : "") +
                (commonTableId != null ? "commonTableId=" + commonTableId + ", " : "") +
                (commonTableName != null ? "commonTableName=" + commonTableName + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (creatorFirstName != null ? "creatorFirstName=" + creatorFirstName + ", " : "") +
                (modifierId != null ? "modifierId=" + modifierId + ", " : "") +
                (modifierFirstName != null ? "modifierFirstName=" + modifierFirstName + ", " : "") +
            "}";
    }
}
