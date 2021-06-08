package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.security.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the CommonTable entity.
 */
public interface CommonTableRepository extends BaseCrudMapper<CommonTable> {
    default List<CommonTable> findAll() {
        return this.selectList(null);
    }

    default Optional<CommonTable> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default List<CommonTable> findByCreatorIsCurrentUser() {
        List<CommonTable> result = new ArrayList<>();
        SecurityUtils
            .getCurrentUserId()
            .ifPresent(userid -> result.addAll(this.selectList(new QueryWrapper<CommonTable>().eq("creator_id", userid))));
        return result;
    }

    default Optional<CommonTable> findOneByEntityName(String entityName) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<CommonTable>().eq(CommonTable::getEntityName, entityName)));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
