package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.ApiPermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.ApiPermission}的DTO。
 */
@ApiModel(description = "API权限\n菜单或按钮下有相关的api权限")
public class ApiPermissionDTO implements Serializable {

    private Long id;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String name;

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
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private ApiPermissionType type;

    /**
     * 请求类型
     */
    @ApiModelProperty(value = "请求类型")
    private String method;

    /**
     * url 地址
     */
    @ApiModelProperty(value = "url 地址")
    private String url;

    private List<ApiPermissionDTO> children = new ArrayList<>();

    private ApiPermissionSimpleDTO parent;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiPermissionType getType() {
        return type;
    }

    public void setType(ApiPermissionType type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ApiPermissionDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ApiPermissionDTO> children) {
        this.children = children;
    }

    public ApiPermissionSimpleDTO getParent() {
        return parent;
    }

    public void setParent(ApiPermissionSimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiPermissionDTO)) {
            return false;
        }

        ApiPermissionDTO apiPermissionDTO = (ApiPermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, apiPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiPermissionDTO{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", method='" + getMethod() + "'" +
            ", url='" + getUrl() + "'" +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
