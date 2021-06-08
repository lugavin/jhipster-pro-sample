package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划
 */

@TableName(value = "administrative_division")
public class AdministrativeDivision implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 地区代码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 城市代码
     */
    @TableField(value = "city_code")
    private String cityCode;

    /**
     * 全名
     */
    @TableField(value = "merger_name")
    private String mergerName;

    /**
     * 短名称
     */
    @TableField(value = "short_name")
    private String shortName;

    /**
     * 邮政编码
     */
    @TableField(value = "zip_code")
    private String zipCode;

    /**
     * 行政区域等级（0: 省级 1:市级 2:县级 3:镇级 4:乡村级）
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 经度
     */
    @TableField(value = "lng")
    private Double lng;

    /**
     * 纬度
     */
    @TableField(value = "lat")
    private Double lat;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = AdministrativeDivision.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private List<AdministrativeDivision> children = new ArrayList<>();

    /**
     * 上级节点
     */
    @TableField(exist = false)
    @BindEntity(entity = AdministrativeDivision.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private AdministrativeDivision parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdministrativeDivision id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public AdministrativeDivision name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public AdministrativeDivision areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public AdministrativeDivision cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMergerName() {
        return this.mergerName;
    }

    public AdministrativeDivision mergerName(String mergerName) {
        this.mergerName = mergerName;
        return this;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public AdministrativeDivision shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public AdministrativeDivision zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getLevel() {
        return this.level;
    }

    public AdministrativeDivision level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getLng() {
        return this.lng;
    }

    public AdministrativeDivision lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return this.lat;
    }

    public AdministrativeDivision lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public List<AdministrativeDivision> getChildren() {
        return this.children;
    }

    public AdministrativeDivision children(List<AdministrativeDivision> administrativeDivisions) {
        this.setChildren(administrativeDivisions);
        return this;
    }

    public AdministrativeDivision addChildren(AdministrativeDivision administrativeDivision) {
        this.children.add(administrativeDivision);
        administrativeDivision.setParent(this);
        return this;
    }

    public AdministrativeDivision removeChildren(AdministrativeDivision administrativeDivision) {
        this.children.remove(administrativeDivision);
        administrativeDivision.setParent(null);
        return this;
    }

    public void setChildren(List<AdministrativeDivision> administrativeDivisions) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (administrativeDivisions != null) {
            administrativeDivisions.forEach(i -> i.setParent(this));
        }
        this.children = administrativeDivisions;
    }

    public AdministrativeDivision getParent() {
        return this.parent;
    }

    public AdministrativeDivision parent(AdministrativeDivision administrativeDivision) {
        this.setParent(administrativeDivision);
        return this;
    }

    public void setParent(AdministrativeDivision administrativeDivision) {
        this.parent = administrativeDivision;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdministrativeDivision)) {
            return false;
        }
        return id != null && id.equals(((AdministrativeDivision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministrativeDivision{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", areaCode='" + getAreaCode() + "'" +
            ", cityCode='" + getCityCode() + "'" +
            ", mergerName='" + getMergerName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", level=" + getLevel() +
            ", lng=" + getLng() +
            ", lat=" + getLat() +
            "}";
    }
}
