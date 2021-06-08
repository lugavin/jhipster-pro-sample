package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gavin.myapp.domain.enumeration.TargetType;
import com.gavin.myapp.domain.enumeration.ViewPermissionType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 可视权限
 * 权限分为菜单权限、按钮权限等\n
 */

@TableName(value = "view_permission")
public class ViewPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField(value = "text")
    private String text;

    /**
     * i18n主键
     */
    @TableField(value = "i_18_n")
    private String i18n;

    /**
     * 显示分组名
     */
    @TableField(value = "`group`")
    private Boolean group;

    /**
     * 路由
     */
    @TableField(value = "link")
    private String link;

    /**
     * 外部链接
     */
    @TableField(value = "external_link")
    private String externalLink;

    /**
     * 链接目标
     */
    @TableField(value = "target")
    private TargetType target;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 禁用菜单
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 隐藏菜单
     */
    @TableField(value = "hide")
    private Boolean hide;

    /**
     * 隐藏面包屑
     */
    @TableField(value = "hide_in_breadcrumb")
    private Boolean hideInBreadcrumb;

    /**
     * 快捷菜单项
     */
    @TableField(value = "shortcut")
    private Boolean shortcut;

    /**
     * 菜单根节点
     */
    @TableField(value = "shortcut_root")
    private Boolean shortcutRoot;

    /**
     * 允许复用
     */
    @TableField(value = "reuse")
    private Boolean reuse;

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
     * 权限类型
     */
    @TableField(value = "type")
    private ViewPermissionType type;

    /**
     * 排序
     */
    @TableField(value = "`order`")
    private Integer order;

    /**
     * api权限标识串
     */
    @TableField(value = "api_permission_codes")
    private String apiPermissionCodes;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = ViewPermission.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ViewPermission> children = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ViewPermission.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private ViewPermission parent;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        condition = "this.id=rel_jhi_authority__view_permissions.authority_id AND rel_jhi_authority__view_permissions.view_permissions_id=id"
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

    public ViewPermission id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public ViewPermission text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String geti18n() {
        return this.i18n;
    }

    public ViewPermission i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public Boolean getGroup() {
        return this.group;
    }

    public ViewPermission group(Boolean group) {
        this.group = group;
        return this;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public String getLink() {
        return this.link;
    }

    public ViewPermission link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExternalLink() {
        return this.externalLink;
    }

    public ViewPermission externalLink(String externalLink) {
        this.externalLink = externalLink;
        return this;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public TargetType getTarget() {
        return this.target;
    }

    public ViewPermission target(TargetType target) {
        this.target = target;
        return this;
    }

    public void setTarget(TargetType target) {
        this.target = target;
    }

    public String getIcon() {
        return this.icon;
    }

    public ViewPermission icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public ViewPermission disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public ViewPermission hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Boolean getHideInBreadcrumb() {
        return this.hideInBreadcrumb;
    }

    public ViewPermission hideInBreadcrumb(Boolean hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
        return this;
    }

    public void setHideInBreadcrumb(Boolean hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
    }

    public Boolean getShortcut() {
        return this.shortcut;
    }

    public ViewPermission shortcut(Boolean shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public void setShortcut(Boolean shortcut) {
        this.shortcut = shortcut;
    }

    public Boolean getShortcutRoot() {
        return this.shortcutRoot;
    }

    public ViewPermission shortcutRoot(Boolean shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
        return this;
    }

    public void setShortcutRoot(Boolean shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
    }

    public Boolean getReuse() {
        return this.reuse;
    }

    public ViewPermission reuse(Boolean reuse) {
        this.reuse = reuse;
        return this;
    }

    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }

    public String getCode() {
        return this.code;
    }

    public ViewPermission code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public ViewPermission description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ViewPermissionType getType() {
        return this.type;
    }

    public ViewPermission type(ViewPermissionType type) {
        this.type = type;
        return this;
    }

    public void setType(ViewPermissionType type) {
        this.type = type;
    }

    public Integer getOrder() {
        return this.order;
    }

    public ViewPermission order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getApiPermissionCodes() {
        return this.apiPermissionCodes;
    }

    public ViewPermission apiPermissionCodes(String apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
        return this;
    }

    public void setApiPermissionCodes(String apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
    }

    public List<ViewPermission> getChildren() {
        return this.children;
    }

    public ViewPermission children(List<ViewPermission> viewPermissions) {
        this.setChildren(viewPermissions);
        return this;
    }

    public ViewPermission addChildren(ViewPermission viewPermission) {
        this.children.add(viewPermission);
        viewPermission.setParent(this);
        return this;
    }

    public ViewPermission removeChildren(ViewPermission viewPermission) {
        this.children.remove(viewPermission);
        viewPermission.setParent(null);
        return this;
    }

    public void setChildren(List<ViewPermission> viewPermissions) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (viewPermissions != null) {
            viewPermissions.forEach(i -> i.setParent(this));
        }
        this.children = viewPermissions;
    }

    public ViewPermission getParent() {
        return this.parent;
    }

    public ViewPermission parent(ViewPermission viewPermission) {
        this.setParent(viewPermission);
        return this;
    }

    public void setParent(ViewPermission viewPermission) {
        this.parent = viewPermission;
    }

    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public ViewPermission authorities(List<Authority> authorities) {
        this.setAuthorities(authorities);
        return this;
    }

    public ViewPermission addAuthorities(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public ViewPermission removeAuthorities(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }

    public void setAuthorities(List<Authority> authorities) {
        if (this.authorities != null) {
            this.authorities.forEach(i -> i.removeViewPermissions(this));
        }
        if (authorities != null) {
            authorities.forEach(i -> i.addViewPermissions(this));
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
        if (!(o instanceof ViewPermission)) {
            return false;
        }
        return id != null && id.equals(((ViewPermission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermission{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", i18n='" + geti18n() + "'" +
            ", group='" + getGroup() + "'" +
            ", link='" + getLink() + "'" +
            ", externalLink='" + getExternalLink() + "'" +
            ", target='" + getTarget() + "'" +
            ", icon='" + getIcon() + "'" +
            ", disabled='" + getDisabled() + "'" +
            ", hide='" + getHide() + "'" +
            ", hideInBreadcrumb='" + getHideInBreadcrumb() + "'" +
            ", shortcut='" + getShortcut() + "'" +
            ", shortcutRoot='" + getShortcutRoot() + "'" +
            ", reuse='" + getReuse() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", order=" + getOrder() +
            ", apiPermissionCodes='" + getApiPermissionCodes() + "'" +
            "}";
    }
}
