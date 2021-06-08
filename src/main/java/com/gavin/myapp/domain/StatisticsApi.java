package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gavin.myapp.domain.enumeration.StatSourceType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * 统计Api
 */

@TableName(value = "statistics_api")
public class StatisticsApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @Size(max = 200)
    @TableField(value = "title")
    private String title;

    /**
     * ApiKey
     */

    @TableField(value = "api_key")
    private String apiKey;

    /**
     * 创建时间
     */
    @TableField(value = "create_at", fill = FieldFill.INSERT)
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    private ZonedDateTime updateAt;

    /**
     * 来源类型
     */
    @TableField(value = "source_type")
    private StatSourceType sourceType;

    /**
     * 主体内容
     */
    @TableField(value = "api_body")
    private String apiBody;

    /**
     * 可能存放的结果
     */
    @TableField(value = "result")
    private String result;

    /**
     * 更新间隔(秒)
     */
    @TableField(value = "update_interval")
    private Integer updateInterval;

    /**
     * 最新运行时间
     */
    @TableField(value = "last_sql_run_time")
    private ZonedDateTime lastSQLRunTime;

    /**
     * 是否可用
     */
    @TableField(value = "enable")
    private Boolean enable;

    @TableField(value = "common_table_id")
    private Long commonTableId;

    @TableField(value = "creator_id")
    private Long creatorId;

    @TableField(value = "modifier_id")
    private Long modifierId;

    /**
     * 所属表
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonTable.class, condition = "this.common_table_id=id")
    @JsonIgnoreProperties(value = { "commonTableFields", "relationships", "metaModel", "creator", "businessType" }, allowSetters = true)
    private CommonTable commonTable;

    /**
     * 创建人
     */
    @TableField(exist = false)
    @BindEntity(entity = User.class, condition = "this.creator_id=id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private User creator;

    /**
     * 修改人
     */
    @TableField(exist = false)
    @BindEntity(entity = User.class, condition = "this.modifier_id=id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private User modifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatisticsApi id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public StatisticsApi title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public StatisticsApi apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public StatisticsApi createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public StatisticsApi updateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public StatSourceType getSourceType() {
        return this.sourceType;
    }

    public StatisticsApi sourceType(StatSourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public void setSourceType(StatSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getApiBody() {
        return this.apiBody;
    }

    public StatisticsApi apiBody(String apiBody) {
        this.apiBody = apiBody;
        return this;
    }

    public void setApiBody(String apiBody) {
        this.apiBody = apiBody;
    }

    public String getResult() {
        return this.result;
    }

    public StatisticsApi result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getUpdateInterval() {
        return this.updateInterval;
    }

    public StatisticsApi updateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public ZonedDateTime getLastSQLRunTime() {
        return this.lastSQLRunTime;
    }

    public StatisticsApi lastSQLRunTime(ZonedDateTime lastSQLRunTime) {
        this.lastSQLRunTime = lastSQLRunTime;
        return this;
    }

    public void setLastSQLRunTime(ZonedDateTime lastSQLRunTime) {
        this.lastSQLRunTime = lastSQLRunTime;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public StatisticsApi enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public CommonTable getCommonTable() {
        return this.commonTable;
    }

    public StatisticsApi commonTable(CommonTable commonTable) {
        this.setCommonTable(commonTable);
        return this;
    }

    public void setCommonTable(CommonTable commonTable) {
        this.commonTable = commonTable;
    }

    public User getCreator() {
        return this.creator;
    }

    public StatisticsApi creator(User user) {
        this.setCreator(user);
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public User getModifier() {
        return this.modifier;
    }

    public StatisticsApi modifier(User user) {
        this.setModifier(user);
        return this;
    }

    public void setModifier(User user) {
        this.modifier = user;
    }

    public Long getCommonTableId() {
        return commonTableId;
    }

    public void setCommonTableId(Long commonTableId) {
        this.commonTableId = commonTableId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatisticsApi)) {
            return false;
        }
        return id != null && id.equals(((StatisticsApi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatisticsApi{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", apiBody='" + getApiBody() + "'" +
            ", result='" + getResult() + "'" +
            ", updateInterval=" + getUpdateInterval() +
            ", lastSQLRunTime='" + getLastSQLRunTime() + "'" +
            ", enable='" + getEnable() + "'" +
            "}";
    }
}
