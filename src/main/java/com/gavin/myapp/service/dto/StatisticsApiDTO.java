package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.StatSourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.StatisticsApi}的DTO。
 */
@ApiModel(description = "统计Api")
public class StatisticsApiDTO implements Serializable {

    private Long id;

    /**
     * 标题
     */
    @Size(max = 200)
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * ApiKey
     */

    @ApiModelProperty(value = "ApiKey")
    private String apiKey;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private ZonedDateTime updateAt;

    /**
     * 来源类型
     */
    @ApiModelProperty(value = "来源类型")
    private StatSourceType sourceType;

    /**
     * 主体内容
     */
    @ApiModelProperty(value = "主体内容")
    private String apiBody;

    /**
     * 可能存放的结果
     */
    @ApiModelProperty(value = "可能存放的结果")
    private String result;

    /**
     * 更新间隔(秒)
     */
    @ApiModelProperty(value = "更新间隔(秒)")
    private Integer updateInterval;

    /**
     * 最新运行时间
     */
    @ApiModelProperty(value = "最新运行时间")
    private ZonedDateTime lastSQLRunTime;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean enable;

    private CommonTableDTO commonTable;

    private UserDTO creator;

    private UserDTO modifier;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public StatSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(StatSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getApiBody() {
        return apiBody;
    }

    public void setApiBody(String apiBody) {
        this.apiBody = apiBody;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public ZonedDateTime getLastSQLRunTime() {
        return lastSQLRunTime;
    }

    public void setLastSQLRunTime(ZonedDateTime lastSQLRunTime) {
        this.lastSQLRunTime = lastSQLRunTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public CommonTableDTO getCommonTable() {
        return commonTable;
    }

    public void setCommonTable(CommonTableDTO commonTable) {
        this.commonTable = commonTable;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public UserDTO getModifier() {
        return modifier;
    }

    public void setModifier(UserDTO modifier) {
        this.modifier = modifier;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatisticsApiDTO)) {
            return false;
        }

        StatisticsApiDTO statisticsApiDTO = (StatisticsApiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statisticsApiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatisticsApiDTO{" +
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
            ", commonTable=" + getCommonTable() +
            ", creator=" + getCreator() +
            ", modifier=" + getModifier() +
            "}";
    }
}
