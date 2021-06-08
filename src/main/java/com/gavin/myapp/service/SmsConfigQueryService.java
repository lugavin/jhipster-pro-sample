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
import com.gavin.myapp.domain.SmsConfig;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.SmsConfigRepository;
import com.gavin.myapp.service.criteria.SmsConfigCriteria;
import com.gavin.myapp.service.dto.SmsConfigDTO;
import com.gavin.myapp.service.mapper.SmsConfigMapper;
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
 * 用于对数据库中的{@link SmsConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsConfigDTO}列表{@link List} 或 {@link SmsConfigDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SmsConfigQueryService extends QueryService<SmsConfig> {

    private final Logger log = LoggerFactory.getLogger(SmsConfigQueryService.class);

    private final SmsConfigRepository smsConfigRepository;

    private final CommonTableRepository commonTableRepository;

    private final SmsConfigMapper smsConfigMapper;

    public SmsConfigQueryService(
        SmsConfigRepository smsConfigRepository,
        CommonTableRepository commonTableRepository,
        SmsConfigMapper smsConfigMapper
    ) {
        super(SmsConfig.class, null);
        this.smsConfigRepository = smsConfigRepository;
        this.commonTableRepository = commonTableRepository;
        this.smsConfigMapper = smsConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return smsConfigMapper.toDto(smsConfigRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsConfigDTO> findByQueryWrapper(QueryWrapper<SmsConfig> queryWrapper, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsConfigRepository.selectPage(page, queryWrapper).convert(smsConfigMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return smsConfigRepository.selectPage(page, this).convert(smsConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return smsConfigRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return smsConfigRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link SmsConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(SmsConfigCriteria criteria) {
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
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sms_code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "template_id");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "access_key");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "secret_key");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "region_id");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sign_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "remark");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getProvider() != null) {
                    this.buildSpecification(criteria.getProvider(), "provider");
                }
                if (criteria.getSmsCode() != null) {
                    this.buildStringSpecification(criteria.getSmsCode(), "sms_code");
                }
                if (criteria.getTemplateId() != null) {
                    this.buildStringSpecification(criteria.getTemplateId(), "template_id");
                }
                if (criteria.getAccessKey() != null) {
                    this.buildStringSpecification(criteria.getAccessKey(), "access_key");
                }
                if (criteria.getSecretKey() != null) {
                    this.buildStringSpecification(criteria.getSecretKey(), "secret_key");
                }
                if (criteria.getRegionId() != null) {
                    this.buildStringSpecification(criteria.getRegionId(), "region_id");
                }
                if (criteria.getSignName() != null) {
                    this.buildStringSpecification(criteria.getSignName(), "sign_name");
                }
                if (criteria.getRemark() != null) {
                    this.buildStringSpecification(criteria.getRemark(), "remark");
                }
                if (criteria.getEnabled() != null) {
                    this.buildSpecification(criteria.getEnabled(), "enabled");
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
     * @return Page<SmsConfigDTO>
     */
    @Transactional(readOnly = true)
    public Page<SmsConfigDTO> selectByCustomEntity(
        String entityName,
        SmsConfigCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "SmsConfig";
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
        // DynamicJoinQueryWrapper<SmsConfig, SmsConfig> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(SmsConfig.class, dynamicRelationships);
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
        List<SmsConfigDTO> result =
            this.queryList(SmsConfig.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(smsConfigMapper::toDto)
                .collect(Collectors.toList());
        return new Page<SmsConfigDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
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
