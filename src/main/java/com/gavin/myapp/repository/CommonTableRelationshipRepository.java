package com.gavin.myapp.repository;

import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.CommonTableRelationship;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the CommonTableRelationship entity.
 */
public interface CommonTableRelationshipRepository extends BaseCrudMapper<CommonTableRelationship> {
    default List<CommonTableRelationship> findAll() {
        return this.selectList(null);
    }

    default Optional<CommonTableRelationship> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select(
        "select a.* from common_table_relationship a left join common_table c on c.id = a.common_table_id where c.entity_name = #{commonTableEntityName}"
    )
    List<CommonTableRelationship> findByCommonTableEntityName(@Param("commonTableEntityName") String commonTableEntityName);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
