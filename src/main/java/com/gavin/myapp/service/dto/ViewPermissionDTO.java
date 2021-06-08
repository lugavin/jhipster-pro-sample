package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.TargetType;
import com.gavin.myapp.domain.enumeration.ViewPermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.ViewPermission}的DTO。
 */
@ApiModel(description = "可视权限\n权限分为菜单权限、按钮权限等\n")
public class ViewPermissionDTO implements Serializable {

    private Long id;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String text;

    /**
     * i18n主键
     */
    @ApiModelProperty(value = "i18n主键")
    private String i18n;

    /**
     * 显示分组名
     */
    @ApiModelProperty(value = "显示分组名")
    private Boolean group;

    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String link;

    /**
     * 外部链接
     */
    @ApiModelProperty(value = "外部链接")
    private String externalLink;

    /**
     * 链接目标
     */
    @ApiModelProperty(value = "链接目标")
    private TargetType target;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 禁用菜单
     */
    @ApiModelProperty(value = "禁用菜单")
    private Boolean disabled;

    /**
     * 隐藏菜单
     */
    @ApiModelProperty(value = "隐藏菜单")
    private Boolean hide;

    /**
     * 隐藏面包屑
     */
    @ApiModelProperty(value = "隐藏面包屑")
    private Boolean hideInBreadcrumb;

    /**
     * 快捷菜单项
     */
    @ApiModelProperty(value = "快捷菜单项")
    private Boolean shortcut;

    /**
     * 菜单根节点
     */
    @ApiModelProperty(value = "菜单根节点")
    private Boolean shortcutRoot;

    /**
     * 允许复用
     */
    @ApiModelProperty(value = "允许复用")
    private Boolean reuse;

    /**
     * 权限代码(ROLE_开头)
     */
    @ApiModelProperty(value = "权限代码(ROLE_开头)")
    private String code;

    /**
     * 权限描述
     */
    @ApiModelProperty(value = "权限描述")
    private String description;

    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型")
    private ViewPermissionType type;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer order;

    /**
     * api权限标识串
     */
    @ApiModelProperty(value = "api权限标识串")
    private String apiPermissionCodes;

    private List<ViewPermissionDTO> children = new ArrayList<>();

    private ViewPermissionSimpleDTO parent;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String geti18n() {
        return i18n;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public TargetType getTarget() {
        return target;
    }

    public void setTarget(TargetType target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Boolean getHideInBreadcrumb() {
        return hideInBreadcrumb;
    }

    public void setHideInBreadcrumb(Boolean hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
    }

    public Boolean getShortcut() {
        return shortcut;
    }

    public void setShortcut(Boolean shortcut) {
        this.shortcut = shortcut;
    }

    public Boolean getShortcutRoot() {
        return shortcutRoot;
    }

    public void setShortcutRoot(Boolean shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
    }

    public Boolean getReuse() {
        return reuse;
    }

    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ViewPermissionType getType() {
        return type;
    }

    public void setType(ViewPermissionType type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getApiPermissionCodes() {
        return apiPermissionCodes;
    }

    public void setApiPermissionCodes(String apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
    }

    public List<ViewPermissionDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ViewPermissionDTO> children) {
        this.children = children;
    }

    public ViewPermissionSimpleDTO getParent() {
        return parent;
    }

    public void setParent(ViewPermissionSimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewPermissionDTO)) {
            return false;
        }

        ViewPermissionDTO viewPermissionDTO = (ViewPermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, viewPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermissionDTO{" +
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
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
