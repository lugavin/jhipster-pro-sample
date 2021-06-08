package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.DataDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the DataDictionary entity.
 */
@SuppressWarnings("unused")
public interface DataDictionaryRepository extends BaseCrudMapper<DataDictionary> {
    default List<DataDictionary> findAll() {
        return this.selectList(null);
    }

    default Optional<DataDictionary> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<DataDictionary> findAllByParentIsNull(IPage<DataDictionary> pageable) {
        return this.selectPage(pageable, new QueryWrapper<DataDictionary>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
