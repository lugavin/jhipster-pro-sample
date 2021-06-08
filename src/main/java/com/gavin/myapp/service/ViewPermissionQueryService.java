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
import com.gavin.myapp.domain.ViewPermission;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.ViewPermissionRepository;
import com.gavin.myapp.service.criteria.ViewPermissionCriteria;
import com.gavin.myapp.service.dto.ViewPermissionDTO;
import com.gavin.myapp.service.mapper.ViewPermissionMapper;
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
 * 用于对数据库中的{@link ViewPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ViewPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ViewPermissionDTO}列表{@link List} 或 {@link ViewPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ViewPermissionQueryService extends QueryService<ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionQueryService.class);

    private final ViewPermissionRepository viewPermissionRepository;

    private final CommonTableRepository commonTableRepository;

    private final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionQueryService(
        ViewPermissionRepository viewPermissionRepository,
        CommonTableRepository commonTableRepository,
        ViewPermissionMapper viewPermissionMapper
    ) {
        super(ViewPermission.class, null);
        this.viewPermissionRepository = viewPermissionRepository;
        this.commonTableRepository = commonTableRepository;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return viewPermissionMapper.toDto(viewPermissionRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByQueryWrapper(QueryWrapper<ViewPermission> queryWrapper, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return viewPermissionRepository.selectPage(page, this).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViewPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return viewPermissionRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return viewPermissionRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(ViewPermissionCriteria criteria) {
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
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "text");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "i18n");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "link");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "external_link");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "icon");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description");
                    this.or();
                    this.buildStringSpecification(
                            new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                            "api_permission_codes"
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getText() != null) {
                    this.buildStringSpecification(criteria.getText(), "text");
                }
                if (criteria.geti18n() != null) {
                    this.buildStringSpecification(criteria.geti18n(), "i18n");
                }
                if (criteria.getGroup() != null) {
                    this.buildSpecification(criteria.getGroup(), "group");
                }
                if (criteria.getLink() != null) {
                    this.buildStringSpecification(criteria.getLink(), "link");
                }
                if (criteria.getExternalLink() != null) {
                    this.buildStringSpecification(criteria.getExternalLink(), "external_link");
                }
                if (criteria.getTarget() != null) {
                    this.buildSpecification(criteria.getTarget(), "target");
                }
                if (criteria.getIcon() != null) {
                    this.buildStringSpecification(criteria.getIcon(), "icon");
                }
                if (criteria.getDisabled() != null) {
                    this.buildSpecification(criteria.getDisabled(), "disabled");
                }
                if (criteria.getHide() != null) {
                    this.buildSpecification(criteria.getHide(), "hide");
                }
                if (criteria.getHideInBreadcrumb() != null) {
                    this.buildSpecification(criteria.getHideInBreadcrumb(), "hide_in_breadcrumb");
                }
                if (criteria.getShortcut() != null) {
                    this.buildSpecification(criteria.getShortcut(), "shortcut");
                }
                if (criteria.getShortcutRoot() != null) {
                    this.buildSpecification(criteria.getShortcutRoot(), "shortcut_root");
                }
                if (criteria.getReuse() != null) {
                    this.buildSpecification(criteria.getReuse(), "reuse");
                }
                if (criteria.getCode() != null) {
                    this.buildStringSpecification(criteria.getCode(), "code");
                }
                if (criteria.getDescription() != null) {
                    this.buildStringSpecification(criteria.getDescription(), "description");
                }
                if (criteria.getType() != null) {
                    this.buildSpecification(criteria.getType(), "type");
                }
                if (criteria.getOrder() != null) {
                    this.buildRangeSpecification(criteria.getOrder(), "order");
                }
                if (criteria.getApiPermissionCodes() != null) {
                    this.buildStringSpecification(criteria.getApiPermissionCodes(), "api_permission_codes");
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenText() != null) {
                    // todo 未实现 one-to-many;[object Object];text
                }
                if (criteria.getParentId() != null) {
                    this.buildRangeSpecification(criteria.getParentId(), "parent_id");
                }
                if (criteria.getParentText() != null) {
                    this.buildStringSpecification(criteria.getParentText(), "view_permission_left_join.text");
                }
                if (criteria.getAuthoritiesId() != null) {
                    this.buildRangeSpecification(criteria.getAuthoritiesId(), "jhi_authority_left_join.id");
                }
                if (criteria.getAuthoritiesName() != null) {
                    this.buildStringSpecification(criteria.getAuthoritiesName(), "jhi_authority_left_join.name");
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
     * @return Page<ViewPermissionDTO>
     */
    @Transactional(readOnly = true)
    public Page<ViewPermissionDTO> selectByCustomEntity(
        String entityName,
        ViewPermissionCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "ViewPermission";
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
        // DynamicJoinQueryWrapper<ViewPermission, ViewPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(ViewPermission.class, dynamicRelationships);
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
        List<ViewPermissionDTO> result =
            this.queryList(ViewPermission.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(viewPermissionMapper::toDto)
                .collect(Collectors.toList());
        return new Page<ViewPermissionDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
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
