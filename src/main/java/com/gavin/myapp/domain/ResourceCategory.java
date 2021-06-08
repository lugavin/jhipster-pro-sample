package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * 资源分类
 */

@TableName(value = "resource_category")
public class ResourceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @Size(max = 40)
    @TableField(value = "title")
    private String title;

    /**
     * 代码
     */
    @Size(max = 20)
    @TableField(value = "code")
    private String code;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 文件列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = UploadFile.class, condition = "id=category_id")
    @JsonIgnoreProperties(value = { "user", "category" }, allowSetters = true)
    private List<UploadFile> files = new ArrayList<>();

    /**
     * 下级列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = ResourceCategory.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "files", "children", "images", "parent" }, allowSetters = true)
    private List<ResourceCategory> children = new ArrayList<>();

    /**
     * 图片列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = UploadImage.class, condition = "id=category_id")
    @JsonIgnoreProperties(value = { "user", "category" }, allowSetters = true)
    private List<UploadImage> images = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ResourceCategory.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "files", "children", "images", "parent" }, allowSetters = true)
    private ResourceCategory parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResourceCategory id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ResourceCategory title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }

    public ResourceCategory code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return this.sort;
    }

    public ResourceCategory sort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<UploadFile> getFiles() {
        return this.files;
    }

    public ResourceCategory files(List<UploadFile> uploadFiles) {
        this.setFiles(uploadFiles);
        return this;
    }

    public ResourceCategory addFiles(UploadFile uploadFile) {
        this.files.add(uploadFile);
        uploadFile.setCategory(this);
        return this;
    }

    public ResourceCategory removeFiles(UploadFile uploadFile) {
        this.files.remove(uploadFile);
        uploadFile.setCategory(null);
        return this;
    }

    public void setFiles(List<UploadFile> uploadFiles) {
        if (this.files != null) {
            this.files.forEach(i -> i.setCategory(null));
        }
        if (uploadFiles != null) {
            uploadFiles.forEach(i -> i.setCategory(this));
        }
        this.files = uploadFiles;
    }

    public List<ResourceCategory> getChildren() {
        return this.children;
    }

    public ResourceCategory children(List<ResourceCategory> resourceCategories) {
        this.setChildren(resourceCategories);
        return this;
    }

    public ResourceCategory addChildren(ResourceCategory resourceCategory) {
        this.children.add(resourceCategory);
        resourceCategory.setParent(this);
        return this;
    }

    public ResourceCategory removeChildren(ResourceCategory resourceCategory) {
        this.children.remove(resourceCategory);
        resourceCategory.setParent(null);
        return this;
    }

    public void setChildren(List<ResourceCategory> resourceCategories) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (resourceCategories != null) {
            resourceCategories.forEach(i -> i.setParent(this));
        }
        this.children = resourceCategories;
    }

    public List<UploadImage> getImages() {
        return this.images;
    }

    public ResourceCategory images(List<UploadImage> uploadImages) {
        this.setImages(uploadImages);
        return this;
    }

    public ResourceCategory addImages(UploadImage uploadImage) {
        this.images.add(uploadImage);
        uploadImage.setCategory(this);
        return this;
    }

    public ResourceCategory removeImages(UploadImage uploadImage) {
        this.images.remove(uploadImage);
        uploadImage.setCategory(null);
        return this;
    }

    public void setImages(List<UploadImage> uploadImages) {
        if (this.images != null) {
            this.images.forEach(i -> i.setCategory(null));
        }
        if (uploadImages != null) {
            uploadImages.forEach(i -> i.setCategory(this));
        }
        this.images = uploadImages;
    }

    public ResourceCategory getParent() {
        return this.parent;
    }

    public ResourceCategory parent(ResourceCategory resourceCategory) {
        this.setParent(resourceCategory);
        return this;
    }

    public void setParent(ResourceCategory resourceCategory) {
        this.parent = resourceCategory;
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
        if (!(o instanceof ResourceCategory)) {
            return false;
        }
        return id != null && id.equals(((ResourceCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCategory{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", sort=" + getSort() +
            "}";
    }
}
