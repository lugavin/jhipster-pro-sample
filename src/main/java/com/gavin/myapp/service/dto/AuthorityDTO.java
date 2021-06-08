package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * A DTO for the {@link com.gavin.myapp.domain.Authority} entity.
 */
@ApiModel(description = "角色\n采用自分组的形式,采用向下包含关系，选中本节点继承父层并包含本节点内容及其所有子节点内容。")
public class AuthorityDTO implements Serializable {

    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 角色代号
     */
    @ApiModelProperty(value = "角色代号")
    private String code;

    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String info;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer order;

    /**
     * 展示
     */
    @ApiModelProperty(value = "展示")
    private Boolean display;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private LinkedHashSet<AuthorityDTO> children = new LinkedHashSet<>();

    /**
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private LinkedHashSet<UserDTO> users = new LinkedHashSet<>();

    /**
     * 可视权限
     */
    @ApiModelProperty(value = "可视权限")
    private LinkedHashSet<ViewPermissionDTO> viewPermissions = new LinkedHashSet<>();

    /**
     * 上级
     */
    @ApiModelProperty(value = "上级")
    private AuthoritySimpleDTO parent;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public LinkedHashSet<AuthorityDTO> getChildren() {
        return this.children;
    }

    public void setChildren(LinkedHashSet<AuthorityDTO> children) {
        this.children = children;
    }

    public LinkedHashSet<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(LinkedHashSet<UserDTO> users) {
        this.users = users;
    }

    public LinkedHashSet<ViewPermissionDTO> getViewPermissions() {
        return viewPermissions;
    }

    public void setViewPermissions(LinkedHashSet<ViewPermissionDTO> viewPermissions) {
        this.viewPermissions = viewPermissions;
    }

    public AuthoritySimpleDTO getParent() {
        return parent;
    }

    public void setParent(AuthoritySimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if (authorityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", info='" + getInfo() + "'" +
            ", order=" + getOrder() +
            ", display='" + getDisplay() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
