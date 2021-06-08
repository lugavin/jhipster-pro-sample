package com.gavin.myapp.repository;

import com.diboot.core.mapper.BaseCrudMapper;
import com.gavin.myapp.domain.CommonTableField;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the CommonTableField entity.
 */
public interface CommonTableFieldRepository extends BaseCrudMapper<CommonTableField> {
    default List<CommonTableField> findAll() {
        return this.selectList(null);
    }

    default Optional<CommonTableField> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select(
        "select a.* from common_table_field a left join common_table b on a.common_table_id = b.id where b.entity_name = ${entityName} and a.entity_field_name = ${entityFieldName}"
    )
    CommonTableField findOneByCommonTableEntityNameAndEntityFieldName(
        @Param("entityName") String entityName,
        @Param("entityFieldName") String entityFieldName
    );

    @Select("select a.* from common_table_field a where a.common_table_id = ${commonTableId} and a.entity_field_name = ${entityFieldName}")
    CommonTableField findOneByCommonTableIdAndEntityFieldName(
        @Param("commonTableId") Long commonTableId,
        @Param("entityFieldName") String entityFieldName
    );

    @Select(
        "select a.* from common_table_field a left join common_table c on c.id = a.common_table_id where c.entity_name = #{commonTableEntityName}"
    )
    List<CommonTableField> findByCommonTableEntityName(@Param("commonTableEntityName") String commonTableEntityName);
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
