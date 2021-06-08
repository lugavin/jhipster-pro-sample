package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.TargetType;
import com.gavin.myapp.domain.enumeration.ViewPermissionType;
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
 * Criteria class for the {@link com.gavin.myapp.domain.ViewPermission} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.ViewPermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /view-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ViewPermissionCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering TargetType
     */
    public static class TargetTypeFilter extends Filter<TargetType> {

        public TargetTypeFilter() {}

        public TargetTypeFilter(TargetTypeFilter filter) {
            super(filter);
        }

        @Override
        public TargetTypeFilter copy() {
            return new TargetTypeFilter(this);
        }
    }

    /**
     * Class for filtering ViewPermissionType
     */
    public static class ViewPermissionTypeFilter extends Filter<ViewPermissionType> {

        public ViewPermissionTypeFilter() {}

        public ViewPermissionTypeFilter(ViewPermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ViewPermissionTypeFilter copy() {
            return new ViewPermissionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private StringFilter i18n;

    private BooleanFilter group;

    private StringFilter link;

    private StringFilter externalLink;

    private TargetTypeFilter target;

    private StringFilter icon;

    private BooleanFilter disabled;

    private BooleanFilter hide;

    private BooleanFilter hideInBreadcrumb;

    private BooleanFilter shortcut;

    private BooleanFilter shortcutRoot;

    private BooleanFilter reuse;

    private StringFilter code;

    private StringFilter description;

    private ViewPermissionTypeFilter type;

    private IntegerFilter order;

    private StringFilter apiPermissionCodes;

    private LongFilter childrenId;

    private StringFilter childrenText;

    private LongFilter parentId;

    private StringFilter parentText;

    private LongFilter authoritiesId;

    private StringFilter authoritiesName;

    public ViewPermissionCriteria() {}

    public ViewPermissionCriteria(ViewPermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.i18n = other.i18n == null ? null : other.i18n.copy();
        this.group = other.group == null ? null : other.group.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.externalLink = other.externalLink == null ? null : other.externalLink.copy();
        this.target = other.target == null ? null : other.target.copy();
        this.icon = other.icon == null ? null : other.icon.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.hide = other.hide == null ? null : other.hide.copy();
        this.hideInBreadcrumb = other.hideInBreadcrumb == null ? null : other.hideInBreadcrumb.copy();
        this.shortcut = other.shortcut == null ? null : other.shortcut.copy();
        this.shortcutRoot = other.shortcutRoot == null ? null : other.shortcutRoot.copy();
        this.reuse = other.reuse == null ? null : other.reuse.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.apiPermissionCodes = other.apiPermissionCodes == null ? null : other.apiPermissionCodes.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenText = other.childrenText == null ? null : other.childrenText.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentText = other.parentText == null ? null : other.parentText.copy();
        this.authoritiesId = other.authoritiesId == null ? null : other.authoritiesId.copy();
        this.authoritiesName = other.authoritiesName == null ? null : other.authoritiesName.copy();
    }

    @Override
    public ViewPermissionCriteria copy() {
        return new ViewPermissionCriteria(this);
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

    public StringFilter getText() {
        return text;
    }

    public StringFilter text() {
        if (text == null) {
            text = new StringFilter();
        }
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public StringFilter geti18n() {
        return i18n;
    }

    public StringFilter i18n() {
        if (i18n == null) {
            i18n = new StringFilter();
        }
        return i18n;
    }

    public void seti18n(StringFilter i18n) {
        this.i18n = i18n;
    }

    public BooleanFilter getGroup() {
        return group;
    }

    public BooleanFilter group() {
        if (group == null) {
            group = new BooleanFilter();
        }
        return group;
    }

    public void setGroup(BooleanFilter group) {
        this.group = group;
    }

    public StringFilter getLink() {
        return link;
    }

    public StringFilter link() {
        if (link == null) {
            link = new StringFilter();
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public StringFilter getExternalLink() {
        return externalLink;
    }

    public StringFilter externalLink() {
        if (externalLink == null) {
            externalLink = new StringFilter();
        }
        return externalLink;
    }

    public void setExternalLink(StringFilter externalLink) {
        this.externalLink = externalLink;
    }

    public TargetTypeFilter getTarget() {
        return target;
    }

    public TargetTypeFilter target() {
        if (target == null) {
            target = new TargetTypeFilter();
        }
        return target;
    }

    public void setTarget(TargetTypeFilter target) {
        this.target = target;
    }

    public StringFilter getIcon() {
        return icon;
    }

    public StringFilter icon() {
        if (icon == null) {
            icon = new StringFilter();
        }
        return icon;
    }

    public void setIcon(StringFilter icon) {
        this.icon = icon;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            disabled = new BooleanFilter();
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public BooleanFilter getHide() {
        return hide;
    }

    public BooleanFilter hide() {
        if (hide == null) {
            hide = new BooleanFilter();
        }
        return hide;
    }

    public void setHide(BooleanFilter hide) {
        this.hide = hide;
    }

    public BooleanFilter getHideInBreadcrumb() {
        return hideInBreadcrumb;
    }

    public BooleanFilter hideInBreadcrumb() {
        if (hideInBreadcrumb == null) {
            hideInBreadcrumb = new BooleanFilter();
        }
        return hideInBreadcrumb;
    }

    public void setHideInBreadcrumb(BooleanFilter hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
    }

    public BooleanFilter getShortcut() {
        return shortcut;
    }

    public BooleanFilter shortcut() {
        if (shortcut == null) {
            shortcut = new BooleanFilter();
        }
        return shortcut;
    }

    public void setShortcut(BooleanFilter shortcut) {
        this.shortcut = shortcut;
    }

    public BooleanFilter getShortcutRoot() {
        return shortcutRoot;
    }

    public BooleanFilter shortcutRoot() {
        if (shortcutRoot == null) {
            shortcutRoot = new BooleanFilter();
        }
        return shortcutRoot;
    }

    public void setShortcutRoot(BooleanFilter shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
    }

    public BooleanFilter getReuse() {
        return reuse;
    }

    public BooleanFilter reuse() {
        if (reuse == null) {
            reuse = new BooleanFilter();
        }
        return reuse;
    }

    public void setReuse(BooleanFilter reuse) {
        this.reuse = reuse;
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

    public ViewPermissionTypeFilter getType() {
        return type;
    }

    public ViewPermissionTypeFilter type() {
        if (type == null) {
            type = new ViewPermissionTypeFilter();
        }
        return type;
    }

    public void setType(ViewPermissionTypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public StringFilter getApiPermissionCodes() {
        return apiPermissionCodes;
    }

    public StringFilter apiPermissionCodes() {
        if (apiPermissionCodes == null) {
            apiPermissionCodes = new StringFilter();
        }
        return apiPermissionCodes;
    }

    public void setApiPermissionCodes(StringFilter apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
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

    public StringFilter getChildrenText() {
        return childrenText;
    }

    public StringFilter childrenText() {
        if (childrenText == null) {
            childrenText = new StringFilter();
        }
        return childrenText;
    }

    public void setChildrenText(StringFilter childrenText) {
        this.childrenText = childrenText;
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

    public StringFilter getParentText() {
        return parentText;
    }

    public StringFilter parentText() {
        if (parentText == null) {
            parentText = new StringFilter();
        }
        return parentText;
    }

    public void setParentText(StringFilter parentText) {
        this.parentText = parentText;
    }

    public LongFilter getAuthoritiesId() {
        return authoritiesId;
    }

    public LongFilter authoritiesId() {
        if (authoritiesId == null) {
            authoritiesId = new LongFilter();
        }
        return authoritiesId;
    }

    public void setAuthoritiesId(LongFilter authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public StringFilter getAuthoritiesName() {
        return authoritiesName;
    }

    public StringFilter authoritiesName() {
        if (authoritiesName == null) {
            authoritiesName = new StringFilter();
        }
        return authoritiesName;
    }

    public void setAuthoritiesName(StringFilter authoritiesName) {
        this.authoritiesName = authoritiesName;
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
        final ViewPermissionCriteria that = (ViewPermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(i18n, that.i18n) &&
            Objects.equals(group, that.group) &&
            Objects.equals(link, that.link) &&
            Objects.equals(externalLink, that.externalLink) &&
            Objects.equals(target, that.target) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(hide, that.hide) &&
            Objects.equals(hideInBreadcrumb, that.hideInBreadcrumb) &&
            Objects.equals(shortcut, that.shortcut) &&
            Objects.equals(shortcutRoot, that.shortcutRoot) &&
            Objects.equals(reuse, that.reuse) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(order, that.order) &&
            Objects.equals(apiPermissionCodes, that.apiPermissionCodes) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenText, that.childrenText) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentText, that.parentText) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(authoritiesName, that.authoritiesName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            text,
            i18n,
            group,
            link,
            externalLink,
            target,
            icon,
            disabled,
            hide,
            hideInBreadcrumb,
            shortcut,
            shortcutRoot,
            reuse,
            code,
            description,
            type,
            order,
            apiPermissionCodes,
            childrenId,
            childrenText,
            parentId,
            parentText,
            authoritiesId,
            authoritiesName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermissionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (i18n != null ? "i18n=" + i18n + ", " : "") +
                (group != null ? "group=" + group + ", " : "") +
                (link != null ? "link=" + link + ", " : "") +
                (externalLink != null ? "externalLink=" + externalLink + ", " : "") +
                (target != null ? "target=" + target + ", " : "") +
                (icon != null ? "icon=" + icon + ", " : "") +
                (disabled != null ? "disabled=" + disabled + ", " : "") +
                (hide != null ? "hide=" + hide + ", " : "") +
                (hideInBreadcrumb != null ? "hideInBreadcrumb=" + hideInBreadcrumb + ", " : "") +
                (shortcut != null ? "shortcut=" + shortcut + ", " : "") +
                (shortcutRoot != null ? "shortcutRoot=" + shortcutRoot + ", " : "") +
                (reuse != null ? "reuse=" + reuse + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (apiPermissionCodes != null ? "apiPermissionCodes=" + apiPermissionCodes + ", " : "") +
                (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
                (childrenText != null ? "childrenText=" + childrenText + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (parentText != null ? "parentText=" + parentText + ", " : "") +
                (authoritiesId != null ? "authoritiesId=" + authoritiesId + ", " : "") +
                (authoritiesName != null ? "authoritiesName=" + authoritiesName + ", " : "") +
            "}";
    }
}
