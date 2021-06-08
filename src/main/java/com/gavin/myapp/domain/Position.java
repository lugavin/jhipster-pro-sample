package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * 岗位
 * @author jhipster.pro
 */

@TableName(value = "position")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 岗位代码
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "code")
    private String code;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "name")
    private String name;

    /**
     * 排序
     */
    @TableField(value = "sort_no")
    private Integer sortNo;

    /**
     * 描述
     */
    @Size(max = 200)
    @TableField(value = "description")
    private String description;

    /**
     * 员工列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "id=position_id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Position code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Position name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNo() {
        return this.sortNo;
    }

    public Position sortNo(Integer sortNo) {
        this.sortNo = sortNo;
        return this;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getDescription() {
        return this.description;
    }

    public Position description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public Position users(List<User> users) {
        this.setUsers(users);
        return this;
    }

    public Position addUsers(User user) {
        this.users.add(user);
        user.setPosition(this);
        return this;
    }

    public Position removeUsers(User user) {
        this.users.remove(user);
        user.setPosition(null);
        return this;
    }

    public void setUsers(List<User> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.setPosition(null));
        }
        if (users != null) {
            users.forEach(i -> i.setPosition(this));
        }
        this.users = users;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return id != null && id.equals(((Position) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Position{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortNo=" + getSortNo() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
