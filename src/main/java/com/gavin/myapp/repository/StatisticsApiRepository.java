package com.gavin.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.StatisticsApi;
import com.gavin.myapp.security.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the StatisticsApi entity.
 */
@SuppressWarnings("unused")
public interface StatisticsApiRepository extends BaseCrudMapper<StatisticsApi> {
    default List<StatisticsApi> findAll() {
        return this.selectList(null);
    }

    default Optional<StatisticsApi> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default List<StatisticsApi> findByCreatorIsCurrentUser() {
        List<StatisticsApi> result = new ArrayList<>();
        SecurityUtils
            .getCurrentUserId()
            .ifPresent(userid -> result.addAll(this.selectList(new QueryWrapper<StatisticsApi>().eq("creator_id", userid))));
        return result;
    }

    default List<StatisticsApi> findByModifierIsCurrentUser() {
        List<StatisticsApi> result = new ArrayList<>();
        SecurityUtils
            .getCurrentUserId()
            .ifPresent(userid -> result.addAll(this.selectList(new QueryWrapper<StatisticsApi>().eq("modifier_id", userid))));
        return result;
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
