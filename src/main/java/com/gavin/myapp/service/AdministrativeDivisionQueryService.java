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
import com.gavin.myapp.domain.AdministrativeDivision;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.AdministrativeDivisionRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.service.criteria.AdministrativeDivisionCriteria;
import com.gavin.myapp.service.dto.AdministrativeDivisionDTO;
import com.gavin.myapp.service.mapper.AdministrativeDivisionMapper;
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
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link AdministrativeDivision}实体执行复杂查询的Service。
 * 主要输入是一个{@link AdministrativeDivisionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AdministrativeDivisionDTO}列表{@link List} 或 {@link AdministrativeDivisionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class AdministrativeDivisionQueryService extends QueryService<AdministrativeDivision> {

    private final Logger log = LoggerFactory.getLogger(AdministrativeDivisionQueryService.class);

    private final AdministrativeDivisionRepository administrativeDivisionRepository;

    private final CommonTableRepository commonTableRepository;

    private final AdministrativeDivisionMapper administrativeDivisionMapper;

    public AdministrativeDivisionQueryService(
        AdministrativeDivisionRepository administrativeDivisionRepository,
        CommonTableRepository commonTableRepository,
        AdministrativeDivisionMapper administrativeDivisionMapper
    ) {
        super(AdministrativeDivision.class, null);
        this.administrativeDivisionRepository = administrativeDivisionRepository;
        this.commonTableRepository = commonTableRepository;
        this.administrativeDivisionMapper = administrativeDivisionMapper;
    }

    /**
     * Return a {@link List} of {@link AdministrativeDivisionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdministrativeDivisionDTO> findByCriteria(AdministrativeDivisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return administrativeDivisionMapper.toDto(administrativeDivisionRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link AdministrativeDivisionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AdministrativeDivisionDTO> findByQueryWrapper(
        QueryWrapper<AdministrativeDivision> queryWrapper,
        Page<AdministrativeDivision> page
    ) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return administrativeDivisionRepository.selectPage(page, queryWrapper).convert(administrativeDivisionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AdministrativeDivisionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AdministrativeDivisionDTO> findByCriteria(AdministrativeDivisionCriteria criteria, Page<AdministrativeDivision> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return administrativeDivisionRepository.selectPage(page, this).convert(administrativeDivisionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdministrativeDivisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return administrativeDivisionRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return administrativeDivisionRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link AdministrativeDivisionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(AdministrativeDivisionCriteria criteria) {
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
                            "level"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "lng"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "lat"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "area_code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "city_code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "merger_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "short_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "zip_code");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getName() != null) {
                    this.buildStringSpecification(criteria.getName(), "name");
                }
                if (criteria.getAreaCode() != null) {
                    this.buildStringSpecification(criteria.getAreaCode(), "area_code");
                }
                if (criteria.getCityCode() != null) {
                    this.buildStringSpecification(criteria.getCityCode(), "city_code");
                }
                if (criteria.getMergerName() != null) {
                    this.buildStringSpecification(criteria.getMergerName(), "merger_name");
                }
                if (criteria.getShortName() != null) {
                    this.buildStringSpecification(criteria.getShortName(), "short_name");
                }
                if (criteria.getZipCode() != null) {
                    this.buildStringSpecification(criteria.getZipCode(), "zip_code");
                }
                if (criteria.getLevel() != null) {
                    this.buildRangeSpecification(criteria.getLevel(), "level");
                }
                if (criteria.getLng() != null) {
                    this.buildRangeSpecification(criteria.getLng(), "lng");
                }
                if (criteria.getLat() != null) {
                    this.buildRangeSpecification(criteria.getLat(), "lat");
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
                    this.buildStringSpecification(criteria.getParentName(), "administrative_division_left_join.name");
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
     * @return Page<AdministrativeDivisionDTO>
     */
    @Transactional(readOnly = true)
    public Page<AdministrativeDivisionDTO> selectByCustomEntity(
        String entityName,
        AdministrativeDivisionCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "AdministrativeDivision";
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
        // DynamicJoinQueryWrapper<AdministrativeDivision, AdministrativeDivision> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(AdministrativeDivision.class, dynamicRelationships);
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
        List<AdministrativeDivisionDTO> result =
            this.queryList(AdministrativeDivision.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(administrativeDivisionMapper::toDto)
                .collect(Collectors.toList());
        return new Page<AdministrativeDivisionDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
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
