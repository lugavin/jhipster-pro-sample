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
import com.gavin.myapp.domain.StatisticsApi;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.StatisticsApiRepository;
import com.gavin.myapp.service.criteria.StatisticsApiCriteria;
import com.gavin.myapp.service.dto.StatisticsApiDTO;
import com.gavin.myapp.service.mapper.StatisticsApiMapper;
import com.gavin.myapp.util.mybatis.filter.QueryService;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service for executing complex queries for {@link StatisticsApi} entities in the database.
 * The main input is a {@link StatisticsApiCriteria} which gets converted to {@link QueryWrapper},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StatisticsApiDTO} or a {@link IPage} of {@link StatisticsApiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatisticsApiQueryService extends QueryService<StatisticsApi> {

    private final Logger log = LoggerFactory.getLogger(StatisticsApiQueryService.class);

    private final StatisticsApiRepository statisticsApiRepository;

    private final CommonTableRepository commonTableRepository;

    private final StatisticsApiMapper statisticsApiMapper;

    public StatisticsApiQueryService(
        StatisticsApiRepository statisticsApiRepository,
        CommonTableRepository commonTableRepository,
        StatisticsApiMapper statisticsApiMapper
    ) {
        super(StatisticsApi.class, null);
        this.statisticsApiRepository = statisticsApiRepository;
        this.commonTableRepository = commonTableRepository;
        this.statisticsApiMapper = statisticsApiMapper;
    }

    /**
     * Return a {@link List} of {@link StatisticsApiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StatisticsApiDTO> findByCriteria(StatisticsApiCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<StatisticsApi> queryWrapper = createQueryWrapper(criteria);
        return statisticsApiMapper.toDto(statisticsApiRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link StatisticsApiDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<StatisticsApiDTO> findByQueryWrapper(QueryWrapper<StatisticsApi> queryWrapper, IPage<StatisticsApi> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return statisticsApiRepository.selectPage(page, queryWrapper).convert(statisticsApiMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link StatisticsApiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<StatisticsApiDTO> findByCriteria(StatisticsApiCriteria criteria, IPage<StatisticsApi> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<StatisticsApi> queryWrapper = createQueryWrapper(criteria);
        return statisticsApiRepository.selectPage(page, queryWrapper).convert(statisticsApiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatisticsApiCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<StatisticsApi> queryWrapper = createQueryWrapper(criteria);
        return statisticsApiRepository.selectCount(queryWrapper);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countBySpecification(QueryWrapper queryWrapper) {
        log.debug("count by specification : {}", queryWrapper);
        return statisticsApiRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link StatisticsApiCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<StatisticsApi> createQueryWrapper(StatisticsApiCriteria criteria) {
        QueryWrapper<StatisticsApi> queryWrapper = new QueryWrapper<>();
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            i ->
                                buildRangeSpecification(
                                    (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                                    "update_interval"
                                )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "api_key")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getTitle() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getTitle(), "title"));
                }
                if (criteria.getApiKey() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getApiKey(), "api_key"));
                }
                if (criteria.getCreateAt() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getCreateAt(), "create_at"));
                }
                if (criteria.getUpdateAt() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getUpdateAt(), "update_at"));
                }
                if (criteria.getSourceType() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getSourceType(), "source_type"));
                }
                if (criteria.getUpdateInterval() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getUpdateInterval(), "update_interval"));
                }
                if (criteria.getLastSQLRunTime() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getLastSQLRunTime(), "last_sql_run_time"));
                }
                if (criteria.getCommonTableId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getCommonTableId(), "common_table_id"));
                }
                if (criteria.getCreatorId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getCreatorId(), "creator_id"));
                }
                if (criteria.getModifierId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getModifierId(), "modifier_id"));
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param pageable 分页
     * @return Page<StatisticsApiDTO>
     */
    @Transactional(readOnly = true)
    public Page<StatisticsApiDTO> selectByCustomEntity(
        String entityName,
        StatisticsApiCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "StatisticsApi";
        }
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
        DynamicJoinQueryWrapper<StatisticsApi, StatisticsApi> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
            StatisticsApi.class,
            dynamicRelationships
        );
        dynamicJoinQueryWrapper.select(new String[dynamicFields.size()]);
        List<String> orders = (List<String>) pageable
            .orders()
            .stream()
            .map(orderItem -> ((OrderItem) orderItem).getColumn() + ':' + (((OrderItem) orderItem).isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.toList());
        Pagination pagination = new Pagination().setOrderBy(String.join(",", orders));
        pagination.setPageSize((int) pageable.getSize());
        pagination.setPageIndex((int) pageable.getCurrent());
        List<StatisticsApiDTO> result = dynamicJoinQueryWrapper
            .queryList(StatisticsApi.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(statisticsApiMapper::toDto)
            .collect(Collectors.toList());
        return new Page<StatisticsApiDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
            .setRecords(result);
    }

    public List runSql(String sql) {
        return new ArrayList();
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
