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
import com.gavin.myapp.domain.CommonTableField;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableFieldRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.ExtDataRepository;
import com.gavin.myapp.service.criteria.CommonTableFieldCriteria;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import com.gavin.myapp.service.mapper.CommonTableFieldMapper;
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
 * 用于对数据库中的{@link CommonTableField}实体执行复杂查询的Service。
 * 主要输入是一个{@link CommonTableFieldCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link CommonTableFieldDTO}列表{@link List} 或 {@link CommonTableFieldDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class CommonTableFieldQueryService extends QueryService<CommonTableField> {

    private final Logger log = LoggerFactory.getLogger(CommonTableFieldQueryService.class);

    private final CommonTableFieldRepository commonTableFieldRepository;

    private final CommonTableRepository commonTableRepository;

    private final ExtDataRepository extDataRepository;

    private final CommonTableFieldMapper commonTableFieldMapper;

    public CommonTableFieldQueryService(
        CommonTableFieldRepository commonTableFieldRepository,
        CommonTableRepository commonTableRepository,
        ExtDataRepository extDataRepository,
        CommonTableFieldMapper commonTableFieldMapper
    ) {
        super(CommonTableField.class, null);
        this.commonTableFieldRepository = commonTableFieldRepository;
        this.commonTableRepository = commonTableRepository;
        this.extDataRepository = extDataRepository;
        this.commonTableFieldMapper = commonTableFieldMapper;
    }

    /**
     * Return a {@link List} of {@link CommonTableFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableFieldDTO> findByCriteria(CommonTableFieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonTableFieldMapper.toDto(commonTableFieldRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link CommonTableFieldDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableFieldDTO> findByQueryWrapper(QueryWrapper<CommonTableField> queryWrapper, Page<CommonTableField> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return commonTableFieldRepository.selectPage(page, queryWrapper).convert(commonTableFieldMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonTableFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableFieldDTO> findByCriteria(CommonTableFieldCriteria criteria, Page<CommonTableField> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return commonTableFieldRepository.selectPage(page, this).convert(commonTableFieldMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonTableFieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return commonTableFieldRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonTableFieldRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link CommonTableFieldCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(CommonTableFieldCriteria criteria) {
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
                            "column_width"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "order"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title");
                    this.or();
                    this.buildStringSpecification(
                            new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                            "entity_field_name"
                        );
                    this.or();
                    this.buildStringSpecification(
                            new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                            "table_column_name"
                        );
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "validate_rules");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_values");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "help");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "font_color");
                    this.or();
                    this.buildStringSpecification(
                            new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                            "background_color"
                        );
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "options");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getTitle() != null) {
                    this.buildStringSpecification(criteria.getTitle(), "title");
                }
                if (criteria.getEntityFieldName() != null) {
                    this.buildStringSpecification(criteria.getEntityFieldName(), "entity_field_name");
                }
                if (criteria.getType() != null) {
                    this.buildSpecification(criteria.getType(), "type");
                }
                if (criteria.getTableColumnName() != null) {
                    this.buildStringSpecification(criteria.getTableColumnName(), "table_column_name");
                }
                if (criteria.getColumnWidth() != null) {
                    this.buildRangeSpecification(criteria.getColumnWidth(), "column_width");
                }
                if (criteria.getOrder() != null) {
                    this.buildRangeSpecification(criteria.getOrder(), "order");
                }
                if (criteria.getEditInList() != null) {
                    this.buildSpecification(criteria.getEditInList(), "edit_in_list");
                }
                if (criteria.getHideInList() != null) {
                    this.buildSpecification(criteria.getHideInList(), "hide_in_list");
                }
                if (criteria.getHideInForm() != null) {
                    this.buildSpecification(criteria.getHideInForm(), "hide_in_form");
                }
                if (criteria.getEnableFilter() != null) {
                    this.buildSpecification(criteria.getEnableFilter(), "enable_filter");
                }
                if (criteria.getValidateRules() != null) {
                    this.buildStringSpecification(criteria.getValidateRules(), "validate_rules");
                }
                if (criteria.getShowInFilterTree() != null) {
                    this.buildSpecification(criteria.getShowInFilterTree(), "show_in_filter_tree");
                }
                if (criteria.getFixed() != null) {
                    this.buildSpecification(criteria.getFixed(), "fixed");
                }
                if (criteria.getSortable() != null) {
                    this.buildSpecification(criteria.getSortable(), "sortable");
                }
                if (criteria.getTreeIndicator() != null) {
                    this.buildSpecification(criteria.getTreeIndicator(), "tree_indicator");
                }
                if (criteria.getClientReadOnly() != null) {
                    this.buildSpecification(criteria.getClientReadOnly(), "client_read_only");
                }
                if (criteria.getFieldValues() != null) {
                    this.buildStringSpecification(criteria.getFieldValues(), "field_values");
                }
                if (criteria.getNotNull() != null) {
                    this.buildSpecification(criteria.getNotNull(), "not_null");
                }
                if (criteria.getSystem() != null) {
                    this.buildSpecification(criteria.getSystem(), "system");
                }
                if (criteria.getHelp() != null) {
                    this.buildStringSpecification(criteria.getHelp(), "help");
                }
                if (criteria.getFontColor() != null) {
                    this.buildStringSpecification(criteria.getFontColor(), "font_color");
                }
                if (criteria.getBackgroundColor() != null) {
                    this.buildStringSpecification(criteria.getBackgroundColor(), "background_color");
                }
                if (criteria.getNullHideInForm() != null) {
                    this.buildSpecification(criteria.getNullHideInForm(), "null_hide_in_form");
                }
                if (criteria.getEndUsed() != null) {
                    this.buildSpecification(criteria.getEndUsed(), "end_used");
                }
                if (criteria.getOptions() != null) {
                    this.buildStringSpecification(criteria.getOptions(), "options");
                }
                if (criteria.getMetaModelId() != null) {
                    this.buildRangeSpecification(criteria.getMetaModelId(), "meta_model_id");
                }
                if (criteria.getMetaModelName() != null) {
                    this.buildStringSpecification(criteria.getMetaModelName(), "common_table_left_join.name");
                }
                if (criteria.getCommonTableId() != null) {
                    this.buildRangeSpecification(criteria.getCommonTableId(), "common_table_id");
                }
                if (criteria.getCommonTableName() != null) {
                    this.buildStringSpecification(criteria.getCommonTableName(), "common_table_left_join.name");
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
     * @return Page<CommonTableFieldDTO>
     */
    @Transactional(readOnly = true)
    public Page<CommonTableFieldDTO> selectByCustomEntity(
        String entityName,
        CommonTableFieldCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonTableField";
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
        // DynamicJoinQueryWrapper<CommonTableField, CommonTableField> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(CommonTableField.class, dynamicRelationships);
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
        List<CommonTableFieldDTO> result =
            this.queryList(CommonTableField.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(commonTableFieldMapper::toDto)
                .collect(Collectors.toList());
        return new Page<CommonTableFieldDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
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
