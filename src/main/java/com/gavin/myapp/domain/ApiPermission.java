package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gavin.myapp.domain.enumeration.ApiPermissionType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * API权限
 * 菜单或按钮下有相关的api权限
 */

@TableName(value = "api_permission")
public class ApiPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    @TableField(value = "service_name")
    private String serviceName;

    /**
     * 权限名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 权限代码(ROLE_开头)
     */
    @TableField(value = "code")
    private String code;

    /**
     * 权限描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 类型
     */
    @TableField(value = "type")
    private ApiPermissionType type;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * url 地址
     */
    @TableField(value = "url")
    private String url;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = ApiPermission.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ApiPermission> children = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ApiPermission.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private ApiPermission parent;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        condition = "this.id=rel_jhi_authority__api_permissions.authority_id AND rel_jhi_authority__api_permissions.api_permissions_id=id"
    )
    @JsonIgnoreProperties(value = { "departments", "apiPermissions", "viewPermissions" }, allowSetters = true)
    private List<Authority> authorities = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiPermission id(Long id) {
        this.id = id;
        return this;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public ApiPermission serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getName() {
        return this.name;
    }

    public ApiPermission name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public ApiPermission code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public ApiPermission description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiPermissionType getType() {
        return this.type;
    }

    public ApiPermission type(ApiPermissionType type) {
        this.type = type;
        return this;
    }

    public void setType(ApiPermissionType type) {
        this.type = type;
    }

    public String getMethod() {
        return this.method;
    }

    public ApiPermission method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return this.url;
    }

    public ApiPermission url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ApiPermission> getChildren() {
        return this.children;
    }

    public ApiPermission children(List<ApiPermission> apiPermissions) {
        this.setChildren(apiPermissions);
        return this;
    }

    public ApiPermission addChildren(ApiPermission apiPermission) {
        this.children.add(apiPermission);
        apiPermission.setParent(this);
        return this;
    }

    public ApiPermission removeChildren(ApiPermission apiPermission) {
        this.children.remove(apiPermission);
        apiPermission.setParent(null);
        return this;
    }

    public void setChildren(List<ApiPermission> apiPermissions) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (apiPermissions != null) {
            apiPermissions.forEach(i -> i.setParent(this));
        }
        this.children = apiPermissions;
    }

    public ApiPermission getParent() {
        return this.parent;
    }

    public ApiPermission parent(ApiPermission apiPermission) {
        this.setParent(apiPermission);
        return this;
    }

    public void setParent(ApiPermission apiPermission) {
        this.parent = apiPermission;
    }

    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public ApiPermission authorities(List<Authority> authorities) {
        this.setAuthorities(authorities);
        return this;
    }

    public ApiPermission addAuthorities(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public ApiPermission removeAuthorities(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }

    public void setAuthorities(List<Authority> authorities) {
        if (this.authorities != null) {
            this.authorities.forEach(i -> i.removeApiPermissions(this));
        }
        if (authorities != null) {
            authorities.forEach(i -> i.addApiPermissions(this));
        }
        this.authorities = authorities;
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
        if (!(o instanceof ApiPermission)) {
            return false;
        }
        return id != null && id.equals(((ApiPermission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiPermission{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", method='" + getMethod() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
