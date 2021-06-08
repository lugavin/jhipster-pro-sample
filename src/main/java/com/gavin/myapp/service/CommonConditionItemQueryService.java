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
import com.gavin.myapp.domain.CommonConditionItem;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonConditionItemRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.service.criteria.CommonConditionItemCriteria;
import com.gavin.myapp.service.dto.CommonConditionItemDTO;
import com.gavin.myapp.service.mapper.CommonConditionItemMapper;
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

/**
 * 用于对数据库中的{@link CommonConditionItem}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonConditionItemCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonConditionItemDTO}列表{@link List} 或 {@link CommonConditionItemDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonConditionItemQueryService extends QueryService<CommonConditionItem> {

    private final Logger log = LoggerFactory.getLogger(CommonConditionItemQueryService.class);

    private final CommonConditionItemRepository commonConditionItemRepository;

    private final CommonTableRepository commonTableRepository;

    private final CommonConditionItemMapper commonConditionItemMapper;

    public CommonConditionItemQueryService(
        CommonConditionItemRepository commonConditionItemRepository,
        CommonTableRepository commonTableRepository,
        CommonConditionItemMapper commonConditionItemMapper
    ) {
        super(CommonConditionItem.class, null);
        this.commonConditionItemRepository = commonConditionItemRepository;
        this.commonTableRepository = commonTableRepository;
        this.commonConditionItemMapper = commonConditionItemMapper;
    }

    /**
     * Return a {@link List} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonConditionItemDTO> findByCriteria(CommonConditionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonConditionItemMapper.toDto(commonConditionItemRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionItemDTO> findByQueryWrapper(
        QueryWrapper<CommonConditionItem> queryWrapper,
        Page<CommonConditionItem> page
    ) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonConditionItemRepository.selectPage(page, queryWrapper).convert(commonConditionItemMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonConditionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionItemDTO> findByCriteria(CommonConditionItemCriteria criteria, Page<CommonConditionItem> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return commonConditionItemRepository.selectPage(page, this).convert(commonConditionItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonConditionItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonConditionItemRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonConditionItemRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link CommonConditionItemCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(CommonConditionItemCriteria criteria) {
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
                            (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "order"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "prefix");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_type");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "operator");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "value");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "suffix");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getPrefix() != null) {
                    this.buildStringSpecification(criteria.getPrefix(), "prefix");
                }
                if (criteria.getFieldName() != null) {
                    this.buildStringSpecification(criteria.getFieldName(), "field_name");
                }
                if (criteria.getFieldType() != null) {
                    this.buildStringSpecification(criteria.getFieldType(), "field_type");
                }
                if (criteria.getOperator() != null) {
                    this.buildStringSpecification(criteria.getOperator(), "operator");
                }
                if (criteria.getValue() != null) {
                    this.buildStringSpecification(criteria.getValue(), "value");
                }
                if (criteria.getSuffix() != null) {
                    this.buildStringSpecification(criteria.getSuffix(), "suffix");
                }
                if (criteria.getOrder() != null) {
                    this.buildRangeSpecification(criteria.getOrder(), "order");
                }
                if (criteria.getCommonConditionId() != null) {
                    this.buildRangeSpecification(criteria.getCommonConditionId(), "common_condition_id");
                }
                if (criteria.getCommonConditionName() != null) {
                    this.buildStringSpecification(criteria.getCommonConditionName(), "common_condition_left_join.name");
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
     * @return Page<CommonConditionItemDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonConditionItemDTO> selectByCustomEntity(
        String entityName,
        CommonConditionItemCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonConditionItem";
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
        // DynamicJoinQueryWrapper<CommonConditionItem, CommonConditionItem> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonConditionItem.class, dynamicRelationships);
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
        List<CommonConditionItemDTO> result =
            this.queryList(CommonConditionItem.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(commonConditionItemMapper::toDto)
                .collect(Collectors.toList());
        return new Page<CommonConditionItemDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
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
