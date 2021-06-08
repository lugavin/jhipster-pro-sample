package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.BindEntity;
import com.diboot.core.binding.annotation.BindEntityList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * 采用自分组的形式,采用向下包含关系，选中本节点继承父层并包含本节点内容及其所有子节点内容。
 */

@TableName(value = "jhi_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色代号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 信息
     */
    @TableField(value = "info")
    private String info;

    /**
     * 排序
     */
    @TableField(value = "`order`")
    private Integer order;

    /**
     * 展示
     */
    @TableField(value = "display")
    private Boolean display;

    /**
     * 父节点Id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = Authority.class, condition = "this.id=parent_id")
    private List<Authority> children = new ArrayList<>();

    /**
     * 用户
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "this.id=jhi_user_authority.authority_id AND jhi_user_authority.user_id=id")
    private List<User> users = new ArrayList<>();

    /**
     * 可视权限
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = ViewPermission.class,
        condition = "this.id=rel_jhi_authority__view_permissions.authority_id AND rel_jhi_authority__view_permissions.view_permissions_id=id"
    )
    private List<ViewPermission> viewPermissions = new ArrayList<>();

    /**
     * API权限
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = ViewPermission.class,
        condition = "this.id=rel_jhi_authority__api_permissions.authority_id AND rel_jhi_authority__api_permissions.api_permissions_id=id"
    )
    private List<ApiPermission> apiPermissions = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = Authority.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties("children")
    private Authority parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Authority name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Authority code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public Authority info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getOrder() {
        return order;
    }

    public Authority order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getDisplay() {
        return display;
    }

    public Authority display(Boolean display) {
        this.display = display;
        return this;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public List<Authority> getChildren() {
        return children;
    }

    public Authority children(List<Authority> authorities) {
        this.children = authorities;
        return this;
    }

    public Authority addChildren(Authority authority) {
        this.children.add(authority);
        authority.setParent(this);
        return this;
    }

    public Authority removeChildren(Authority authority) {
        this.children.remove(authority);
        authority.setParent(null);
        return this;
    }

    public void setChildren(List<Authority> children) {
        this.children = children;
    }

    public List<User> getUsers() {
        return users;
    }

    public Authority users(List<User> users) {
        this.users = users;
        return this;
    }

    public Authority addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public Authority removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ViewPermission> getViewPermissions() {
        return viewPermissions;
    }

    public Authority viewPermissions(List<ViewPermission> viewPermissions) {
        this.viewPermissions = viewPermissions;
        return this;
    }

    public Authority addViewPermissions(ViewPermission viewPermission) {
        this.viewPermissions.add(viewPermission);
        viewPermission.getAuthorities().add(this);
        return this;
    }

    public Authority removeViewPermissions(ViewPermission viewPermission) {
        this.viewPermissions.remove(viewPermission);
        viewPermission.getAuthorities().remove(this);
        return this;
    }

    public void setViewPermissions(List<ViewPermission> viewPermissions) {
        this.viewPermissions = viewPermissions;
    }

    public List<ApiPermission> getApiPermissions() {
        return apiPermissions;
    }

    public void setApiPermissions(List<ApiPermission> apiPermissions) {
        this.apiPermissions = apiPermissions;
    }

    public Authority apiPermissions(List<ApiPermission> apiPermissions) {
        this.apiPermissions = apiPermissions;
        return this;
    }

    public Authority addApiPermissions(ApiPermission apiPermission) {
        this.apiPermissions.add(apiPermission);
        apiPermission.getAuthorities().add(this);
        return this;
    }

    public Authority removeApiPermissions(ApiPermission apiPermission) {
        this.apiPermissions.remove(apiPermission);
        apiPermission.getAuthorities().remove(this);
        return this;
    }

    public Authority getParent() {
        return parent;
    }

    public Authority parent(Authority parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Authority authority) {
        this.parent = authority;
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
        if (!(o instanceof Authority)) {
            return false;
        }
        return id != null && id.equals(((Authority) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", info='" + getInfo() + "'" +
            ", order=" + getOrder() +
            ", display='" + getDisplay() + "'" +
            "}";
    }
}
