package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.ApiPermission;
import com.gavin.myapp.domain.enumeration.ApiPermissionType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the ApiPermission entity.
 */
public interface ApiPermissionRepository extends BaseCrudMapper<ApiPermission> {
    default List<ApiPermission> findAll() {
        return this.selectList(null);
    }

    default Optional<ApiPermission> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<ApiPermission> findAllByParentIsNull(IPage<ApiPermission> pageable) {
        return this.selectPage(pageable, new QueryWrapper<ApiPermission>().isNull("parent_id"));
    }

    default Optional<ApiPermission> findOneByCode(String groupCode) {
        return Optional.ofNullable(this.selectOne(new QueryWrapper<ApiPermission>().eq("code", groupCode)));
    }

    default List<ApiPermission> findAllByType(ApiPermissionType type) {
        return this.selectList(new LambdaQueryWrapper<ApiPermission>().eq(ApiPermission::getType, type));
    }

    @Select(
        "select self.* from api_permission self left join rel_jhi_authority__api_permissions aap on self.id = aap.api_permissions_id " +
        "left join jhi_user_authority jua on on jua.authority_id = aap.authority_id " +
        "where jua.user_id = #{userId}"
    )
    List<ApiPermission> findAllApiPermissionsByCurrentUser(@Param("userId") Long userId);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
