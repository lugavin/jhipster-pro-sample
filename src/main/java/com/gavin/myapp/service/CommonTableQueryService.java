package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.gavin.myapp.domain.*; // for static metamodels
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.ExtDataRepository;
import com.gavin.myapp.service.criteria.CommonTableCriteria;
import com.gavin.myapp.service.dto.CommonTableDTO;
import com.gavin.myapp.service.mapper.CommonTableMapper;
import com.gavin.myapp.util.mybatis.filter.QueryService;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link CommonTable}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonTableCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonTableDTO}列表{@link List} 或 {@link CommonTableDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonTableQueryService extends QueryService<CommonTable> {

    private final Logger log = LoggerFactory.getLogger(CommonTableQueryService.class);

    private final CommonTableRepository commonTableRepository;

    private final ExtDataRepository extDataRepository;

    private final CommonTableMapper commonTableMapper;

    public CommonTableQueryService(
        CommonTableRepository commonTableRepository,
        ExtDataRepository extDataRepository,
        CommonTableMapper commonTableMapper
    ) {
        super(CommonTable.class, null);
        this.commonTableRepository = commonTableRepository;
        this.extDataRepository = extDataRepository;
        this.commonTableMapper = commonTableMapper;
    }

    /**
     * Return a {@link List} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableDTO> findByCriteria(CommonTableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonTableMapper.toDto(commonTableRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableDTO> findByQueryWrapper(QueryWrapper<CommonTable> queryWrapper, Page<CommonTable> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonTableRepository.selectPage(page, queryWrapper).convert(commonTableMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableDTO> findByCriteria(CommonTableCriteria criteria, Page<CommonTable> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return commonTableRepository.selectPage(page, this).convert(commonTableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonTableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonTableRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonTableRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link CommonTableCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(CommonTableCriteria criteria) {
        this.clear();
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    this.or();
                    this.buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id");
                    this.or();
                    this.buildRangeSpecification(
                            (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "id"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "base_table_id"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "record_action_width"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "entity_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "table_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "clazz_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getName() != null) {
                    this.buildStringSpecification(criteria.getName(), "name");
                }
                if (criteria.getEntityName() != null) {
                    this.buildStringSpecification(criteria.getEntityName(), "entity_name");
                }
                if (criteria.getTableName() != null) {
                    this.buildStringSpecification(criteria.getTableName(), "table_name");
                }
                if (criteria.getSystem() != null) {
                    this.buildSpecification(criteria.getSystem(), "system");
                }
                if (criteria.getClazzName() != null) {
                    this.buildStringSpecification(criteria.getClazzName(), "clazz_name");
                }
                if (criteria.getGenerated() != null) {
                    this.buildSpecification(criteria.getGenerated(), "generated");
                }
                if (criteria.getCreatAt() != null) {
                    this.buildRangeSpecification(criteria.getCreatAt(), "creat_at");
                }
                if (criteria.getGenerateAt() != null) {
                    this.buildRangeSpecification(criteria.getGenerateAt(), "generate_at");
                }
                if (criteria.getGenerateClassAt() != null) {
                    this.buildRangeSpecification(criteria.getGenerateClassAt(), "generate_class_at");
                }
                if (criteria.getDescription() != null) {
                    this.buildStringSpecification(criteria.getDescription(), "description");
                }
                if (criteria.getTreeTable() != null) {
                    this.buildSpecification(criteria.getTreeTable(), "tree_table");
                }
                if (criteria.getBaseTableId() != null) {
                    this.buildRangeSpecification(criteria.getBaseTableId(), "base_table_id");
                }
                if (criteria.getRecordActionWidth() != null) {
                    this.buildRangeSpecification(criteria.getRecordActionWidth(), "record_action_width");
                }
                if (criteria.getEditInModal() != null) {
                    this.buildSpecification(criteria.getEditInModal(), "edit_in_modal");
                }
                if (criteria.getCommonTableFieldsId() != null) {
                    // todo 未实现
                }
                if (criteria.getCommonTableFieldsTitle() != null) {
                    // todo 未实现 one-to-many;[object Object];title
                }
                if (criteria.getRelationshipsId() != null) {
                    // todo 未实现
                }
                if (criteria.getRelationshipsName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getMetaModelId() != null) {
                    this.buildRangeSpecification(criteria.getMetaModelId(), "meta_model_id");
                }
                if (criteria.getMetaModelName() != null) {
                    this.buildStringSpecification(criteria.getMetaModelName(), "common_table_left_join.name");
                }
                if (criteria.getCreatorId() != null) {
                    this.buildRangeSpecification(criteria.getCreatorId(), "creator_id");
                }
                if (criteria.getCreatorLogin() != null) {
                    this.buildStringSpecification(criteria.getCreatorLogin(), "jhi_user_left_join.login");
                }
                if (criteria.getBusinessTypeId() != null) {
                    this.buildRangeSpecification(criteria.getBusinessTypeId(), "business_type_id");
                }
                if (criteria.getBusinessTypeName() != null) {
                    this.buildStringSpecification(criteria.getBusinessTypeName(), "business_type_left_join.name");
                }
            }
        }
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param predicate 条件
     * @param pageable 分页
     * @return Page<CommonTableDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonTableDTO> selectByCustomEntity(
        String entityName,
        CommonTableCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonTable";
        }
        createQueryWrapper(criteria);
        Optional<CommonTable> oneByEntityName = commonTableRepository.findOneByEntityName(entityName);
        List<String> dynamicFields = new ArrayList<>();
        List<String> dynamicRelationships = new ArrayList<>();
        oneByEntityName.ifPresent(
            commonTable -> {
                commonTable
                    .getCommonTableFields()
                    .stream()
                    .filter(commonTableField -> !commonTableField.getHideInList())
                    .forEach(commonTableField -> dynamicFields.add(commonTableField.getEntityFieldName()));
                commonTable
                    .getRelationships()
                    .stream()
                    .filter(commonTableRelationship -> !commonTableRelationship.getHideInList())
                    .forEach(commonTableRelationship -> dynamicRelationships.add(commonTableRelationship.getRelationshipName()));
            }
        );
        // DynamicJoinQueryWrapper<CommonTable, CommonTable> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonTable.class, dynamicRelationships);
        String[] fields = new String[dynamicFields.size()];
        dynamicFields.toArray(fields);
        this.select(fields);
        List<String> orders = (List<String>) pageable
            .orders()
            .stream()
            .map(orderItem -> ((OrderItem) orderItem).getColumn() + ':' + (((OrderItem) orderItem).isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.toList());
        Pagination pagination = new Pagination().setOrderBy(String.join(",", orders));
        pagination.setPageSize((int) pageable.getSize());
        pagination.setPageIndex((int) pageable.getCurrent());
        List<CommonTableDTO> result =
            this.queryList(CommonTable.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(commonTableMapper::toDto)
                .collect(Collectors.toList());
        return new Page<CommonTableDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
    }

    public String toUpperFirstChar2(String string) {
        char[] chars = string.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
            return String.valueOf(chars);
        }
        return string;
    }
}
