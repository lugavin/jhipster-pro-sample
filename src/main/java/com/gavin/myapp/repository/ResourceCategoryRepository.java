package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.ResourceCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the ResourceCategory entity.
 */
@SuppressWarnings("unused")
public interface ResourceCategoryRepository extends BaseCrudMapper<ResourceCategory> {
    default List<ResourceCategory> findAll() {
        return this.selectList(null);
    }

    default Optional<ResourceCategory> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<ResourceCategory> findAllByParentIsNull(IPage<ResourceCategory> pageable) {
        return this.selectPage(pageable, new QueryWrapper<ResourceCategory>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
