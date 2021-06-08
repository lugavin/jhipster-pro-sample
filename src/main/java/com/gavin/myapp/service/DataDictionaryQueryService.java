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
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.DataDictionaryRepository;
import com.gavin.myapp.service.criteria.DataDictionaryCriteria;
import com.gavin.myapp.service.dto.DataDictionaryDTO;
import com.gavin.myapp.service.mapper.DataDictionaryMapper;
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link DataDictionary}实体执行复杂查询的Service。
 * 主要输入是一个{@link DataDictionaryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DataDictionaryDTO}列表{@link List} 或 {@link DataDictionaryDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DataDictionaryQueryService extends QueryService<DataDictionary> {

    private final Logger log = LoggerFactory.getLogger(DataDictionaryQueryService.class);

    private final DataDictionaryRepository dataDictionaryRepository;

    private final CommonTableRepository commonTableRepository;

    private final DataDictionaryMapper dataDictionaryMapper;

    public DataDictionaryQueryService(
        DataDictionaryRepository dataDictionaryRepository,
        CommonTableRepository commonTableRepository,
        DataDictionaryMapper dataDictionaryMapper
    ) {
        super(DataDictionary.class, null);
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.commonTableRepository = commonTableRepository;
        this.dataDictionaryMapper = dataDictionaryMapper;
    }

    /**
     * Return a {@link List} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataDictionaryDTO> findByCriteria(DataDictionaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return dataDictionaryMapper.toDto(dataDictionaryRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataDictionaryDTO> findByQueryWrapper(QueryWrapper<DataDictionary> queryWrapper, Page<DataDictionary> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return dataDictionaryRepository.selectPage(page, queryWrapper).convert(dataDictionaryMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataDictionaryDTO> findByCriteria(DataDictionaryCriteria criteria, Page<DataDictionary> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return dataDictionaryRepository.selectPage(page, this).convert(dataDictionaryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataDictionaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return dataDictionaryRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return dataDictionaryRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link DataDictionaryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(DataDictionaryCriteria criteria) {
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
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "font_color");
                    this.or();
                    this.buildStringSpecification(
                            new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                            "background_color"
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getName() != null) {
                    this.buildStringSpecification(criteria.getName(), "name");
                }
                if (criteria.getCode() != null) {
                    this.buildStringSpecification(criteria.getCode(), "code");
                }
                if (criteria.getDescription() != null) {
                    this.buildStringSpecification(criteria.getDescription(), "description");
                }
                if (criteria.getFontColor() != null) {
                    this.buildStringSpecification(criteria.getFontColor(), "font_color");
                }
                if (criteria.getBackgroundColor() != null) {
                    this.buildStringSpecification(criteria.getBackgroundColor(), "background_color");
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getParentId() != null) {
                    this.buildRangeSpecification(criteria.getParentId(), "parent_id");
                }
                if (criteria.getParentName() != null) {
                    this.buildStringSpecification(criteria.getParentName(), "data_dictionary_left_join.name");
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
     * @return Page<DataDictionaryDTO>
     */
    @Transactional(readOnly = true)
    public Page<DataDictionaryDTO> selectByCustomEntity(
        String entityName,
        DataDictionaryCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "DataDictionary";
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
        // DynamicJoinQueryWrapper<DataDictionary, DataDictionary> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(DataDictionary.class, dynamicRelationships);
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
        List<DataDictionaryDTO> result =
            this.queryList(DataDictionary.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(dataDictionaryMapper::toDto)
                .collect(Collectors.toList());
        return new Page<DataDictionaryDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
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
