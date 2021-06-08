package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.ResourceCategory}的DTO。
 */
@ApiModel(description = "资源分类")
public class ResourceCategoryDTO implements Serializable {

    private Long id;

    /**
     * 标题
     */
    @Size(max = 40)
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 代码
     */
    @Size(max = 20)
    @ApiModelProperty(value = "代码")
    private String code;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    private List<ResourceCategoryDTO> children = new ArrayList<>();

    private ResourceCategorySimpleDTO parent;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<ResourceCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceCategoryDTO> children) {
        this.children = children;
    }

    public ResourceCategorySimpleDTO getParent() {
        return parent;
    }

    public void setParent(ResourceCategorySimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceCategoryDTO)) {
            return false;
        }

        ResourceCategoryDTO resourceCategoryDTO = (ResourceCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", sort=" + getSort() +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
