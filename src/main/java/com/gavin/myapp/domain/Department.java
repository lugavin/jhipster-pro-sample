package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门
 */

@TableName(value = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 联系电话
     */
    @TableField(value = "phone_num")
    private String phoneNum;

    /**
     * logo地址
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 联系人
     */
    @TableField(value = "contact")
    private String contact;

    /**
     * 创建用户 Id
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private ZonedDateTime createTime;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 下级部门
     */
    @TableField(exist = false)
    @BindEntityList(entity = Department.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users" }, allowSetters = true)
    private List<Department> children = new ArrayList<>();

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        condition = "this.id=rel_department__authorities.department_id AND rel_department__authorities.authorities_id=id"
    )
    @JsonIgnoreProperties(value = { "departments", "apiPermissions", "viewPermissions" }, allowSetters = true)
    private List<Authority> authorities = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = Department.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users" }, allowSetters = true)
    private Department parent;

    /**
     * 员工列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "id=department_id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Department code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return this.address;
    }

    public Department address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public Department phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLogo() {
        return this.logo;
    }

    public Department logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getContact() {
        return this.contact;
    }

    public Department contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public Department createUserId(Long createUserId) {
        this.createUserId = createUserId;
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public ZonedDateTime getCreateTime() {
        return this.createTime;
    }

    public Department createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public List<Department> getChildren() {
        return this.children;
    }

    public Department children(List<Department> departments) {
        this.setChildren(departments);
        return this;
    }

    public Department addChildren(Department department) {
        this.children.add(department);
        department.setParent(this);
        return this;
    }

    public Department removeChildren(Department department) {
        this.children.remove(department);
        department.setParent(null);
        return this;
    }

    public void setChildren(List<Department> departments) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setParent(this));
        }
        this.children = departments;
    }

    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public Department authorities(List<Authority> authorities) {
        this.setAuthorities(authorities);
        return this;
    }

    public Department addAuthorities(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public Department removeAuthorities(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Department getParent() {
        return this.parent;
    }

    public Department parent(Department department) {
        this.setParent(department);
        return this;
    }

    public void setParent(Department department) {
        this.parent = department;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public Department users(List<User> users) {
        this.setUsers(users);
        return this;
    }

    public Department addUsers(User user) {
        this.users.add(user);
        user.setDepartment(this);
        return this;
    }

    public Department removeUsers(User user) {
        this.users.remove(user);
        user.setDepartment(null);
        return this;
    }

    public void setUsers(List<User> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.setDepartment(null));
        }
        if (users != null) {
            users.forEach(i -> i.setDepartment(this));
        }
        this.users = users;
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
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", logo='" + getLogo() + "'" +
            ", contact='" + getContact() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", createTime='" + getCreateTime() + "'" +
            "}";
    }
}
