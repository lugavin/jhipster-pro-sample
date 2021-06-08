package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.UReportFile;
import java.util.List;
import java.util.Optional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the UReportFile entity.
 */
@SuppressWarnings("unused")
public interface UReportFileRepository extends BaseCrudMapper<UReportFile> {
    default List<UReportFile> findAll() {
        return this.selectList(null);
    }

    default Optional<UReportFile> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default Boolean existsByName(String name) {
        return this.selectCount(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)) > 0;
    }

    default Optional<UReportFile> getByName(String name) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)));
    }

    default void deleteByName(String name) {
        this.delete(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
