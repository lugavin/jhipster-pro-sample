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
import com.gavin.myapp.domain.Authority;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.AuthorityRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.service.dto.AuthorityCriteria;
import com.gavin.myapp.service.dto.AuthorityDTO;
import com.gavin.myapp.service.mapper.AuthorityMapper;
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

/**
 * Service for executing complex queries for {@link Authority} entities in the database.
 * The main input is a {@link AuthorityCriteria} which gets converted to {@link QueryWrapper},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuthorityDTO} or a {@link Page} of {@link AuthorityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorityQueryService extends QueryService<Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityQueryService.class);

    private final AuthorityRepository authorityRepository;

    private final CommonTableRepository commonTableRepository;

    private final AuthorityMapper authorityMapper;

    public AuthorityQueryService(
        AuthorityRepository authorityRepository,
        CommonTableRepository commonTableRepository,
        AuthorityMapper authorityMapper
    ) {
        super(Authority.class, null);
        this.authorityRepository = authorityRepository;
        this.commonTableRepository = commonTableRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Return a {@link List} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorityDTO> findByCriteria(AuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityMapper.toDto(authorityRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AuthorityDTO> findByQueryWrapper(QueryWrapper<Authority> queryWrapper, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AuthorityDTO> findByCriteria(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuthorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectCount(queryWrapper);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return authorityRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link AuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<Authority> createQueryWrapper(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
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
                                    "order"
                                )
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "info")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getInfo() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getInfo(), "info"));
                }
                if (criteria.getOrder() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getDisplay() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getDisplay(), "display"));
                }
                /* if (criteria.getChildrenId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getChildrenId(),
                        root -> root.join(Authority_.children, JoinType.LEFT).get(Authority_.id)));
                }
                if (criteria.getUsersId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getUsersId(),
                        root -> root.join(Authority_.users, JoinType.LEFT).get(User_.id)));
                }
                if (criteria.getUsersLogin() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getUsersLogin(),
                        root -> root.join(Authority_.users, JoinType.LEFT).get(User_.login)));
                }
                if (criteria.getViewPermissionId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getViewPermissionId(),
                        root -> root.join(Authority_.viewPermissions, JoinType.LEFT).get(ViewPermission_.id)));
                }
                if (criteria.getViewPermissionText() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getViewPermissionText(),
                        root -> root.join(Authority_.viewPermissions, JoinType.LEFT).get(ViewPermission_.text)));
                }
                if (criteria.getParentId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getParentId(),
                        root -> root.join(Authority_.parent, JoinType.LEFT).get(Authority_.id)));
                }
                if (criteria.getParentName() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getParentName(),
                        root -> root.join(Authority_.parent, JoinType.LEFT).get(Authority_.name)));
                } */
            }
        }
        return queryWrapper;
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param pageable 分页
     * @return IPage<AuthorityDTO>
     */
    @Transactional(readOnly = true)
    public Page<AuthorityDTO> selectByCustomEntity(
        String entityName,
        AuthorityCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "Authority";
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
        DynamicJoinQueryWrapper<Authority, Authority> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
            Authority.class,
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
        List<AuthorityDTO> result = dynamicJoinQueryWrapper
            .queryList(Authority.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(authorityMapper::toDto)
            .collect(Collectors.toList());
        return new Page<AuthorityDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
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
