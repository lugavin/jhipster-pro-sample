package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 上传文件
 */

@TableName(value = "upload_file")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 完整文件名\n不含路径
     */
    @TableField(value = "full_name")
    private String fullName;

    /**
     * 文件名\n不含扩展名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 扩展名
     */
    @TableField(value = "ext")
    private String ext;

    /**
     * 文件类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * Url地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 本地路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 存储目录
     */
    @TableField(value = "folder")
    private String folder;

    /**
     * 实体名称
     */
    @TableField(value = "entity_name")
    private String entityName;

    /**
     * 创建时间
     */
    @TableField(value = "create_at")
    private ZonedDateTime createAt;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 被引次数
     */
    @TableField(value = "reference_count")
    private Long referenceCount;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 上传者
     */
    @TableField(exist = false)
    @BindEntity(entity = User.class, condition = "this.user_id=id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private User user;

    /**
     * 所属分类
     */
    @TableField(exist = false)
    @BindEntity(entity = ResourceCategory.class, condition = "this.category_id=id")
    @JsonIgnoreProperties(value = { "files", "children", "images", "parent" }, allowSetters = true)
    private ResourceCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UploadFile id(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return this.fullName;
    }

    public UploadFile fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return this.name;
    }

    public UploadFile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return this.ext;
    }

    public UploadFile ext(String ext) {
        this.ext = ext;
        return this;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getType() {
        return this.type;
    }

    public UploadFile type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public UploadFile url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public UploadFile path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder() {
        return this.folder;
    }

    public UploadFile folder(String folder) {
        this.folder = folder;
        return this;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public UploadFile entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public UploadFile createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public UploadFile fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getReferenceCount() {
        return this.referenceCount;
    }

    public UploadFile referenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
        return this;
    }

    public void setReferenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
    }

    public User getUser() {
        return this.user;
    }

    public UploadFile user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ResourceCategory getCategory() {
        return this.category;
    }

    public UploadFile category(ResourceCategory resourceCategory) {
        this.setCategory(resourceCategory);
        return this;
    }

    public void setCategory(ResourceCategory resourceCategory) {
        this.category = resourceCategory;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadFile)) {
            return false;
        }
        return id != null && id.equals(((UploadFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadFile{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", name='" + getName() + "'" +
            ", ext='" + getExt() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", path='" + getPath() + "'" +
            ", folder='" + getFolder() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", fileSize=" + getFileSize() +
            ", referenceCount=" + getReferenceCount() +
            "}";
    }
}
