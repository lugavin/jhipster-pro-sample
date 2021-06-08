package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.AdministrativeDivision;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the AdministrativeDivision entity.
 */
@SuppressWarnings("unused")
public interface AdministrativeDivisionRepository extends BaseCrudMapper<AdministrativeDivision> {
    default List<AdministrativeDivision> findAll() {
        return this.selectList(null);
    }

    default Optional<AdministrativeDivision> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<AdministrativeDivision> findAllByParentIsNull(IPage<AdministrativeDivision> pageable) {
        return this.selectPage(pageable, new QueryWrapper<AdministrativeDivision>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
