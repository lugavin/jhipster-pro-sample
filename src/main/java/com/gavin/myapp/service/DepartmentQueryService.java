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
import com.gavin.myapp.domain.Department;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.DepartmentRepository;
import com.gavin.myapp.service.criteria.DepartmentCriteria;
import com.gavin.myapp.service.dto.DepartmentDTO;
import com.gavin.myapp.service.mapper.DepartmentMapper;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link Department}实体执行复杂查询的Service。
 * 主要输入是一个{@link DepartmentCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DepartmentDTO}列表{@link List} 或 {@link DepartmentDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService extends QueryService<Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DepartmentRepository departmentRepository;

    private final CommonTableRepository commonTableRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentQueryService(
        DepartmentRepository departmentRepository,
        CommonTableRepository commonTableRepository,
        DepartmentMapper departmentMapper
    ) {
        super(Department.class, null);
        this.departmentRepository = departmentRepository;
        this.commonTableRepository = commonTableRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return departmentMapper.toDto(departmentRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentDTO> findByQueryWrapper(QueryWrapper<Department> queryWrapper, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return departmentRepository.selectPage(page, queryWrapper).convert(departmentMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentDTO> findByCriteria(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return departmentRepository.selectPage(page, this).convert(departmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return departmentRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return departmentRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(DepartmentCriteria criteria) {
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
                            "create_user_id"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "address");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "phone_num");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "logo");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "contact");
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
                if (criteria.getAddress() != null) {
                    this.buildStringSpecification(criteria.getAddress(), "address");
                }
                if (criteria.getPhoneNum() != null) {
                    this.buildStringSpecification(criteria.getPhoneNum(), "phone_num");
                }
                if (criteria.getLogo() != null) {
                    this.buildStringSpecification(criteria.getLogo(), "logo");
                }
                if (criteria.getContact() != null) {
                    this.buildStringSpecification(criteria.getContact(), "contact");
                }
                if (criteria.getCreateUserId() != null) {
                    this.buildRangeSpecification(criteria.getCreateUserId(), "create_user_id");
                }
                if (criteria.getCreateTime() != null) {
                    this.buildRangeSpecification(criteria.getCreateTime(), "create_time");
                }
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getAuthoritiesId() != null) {
                    this.buildRangeSpecification(criteria.getAuthoritiesId(), "jhi_authority_left_join.id");
                }
                if (criteria.getAuthoritiesName() != null) {
                    this.buildStringSpecification(criteria.getAuthoritiesName(), "jhi_authority_left_join.name");
                }
                if (criteria.getParentId() != null) {
                    this.buildRangeSpecification(criteria.getParentId(), "parent_id");
                }
                if (criteria.getParentName() != null) {
                    this.buildStringSpecification(criteria.getParentName(), "department_left_join.name");
                }
                if (criteria.getUsersId() != null) {
                    // todo 未实现
                }
                if (criteria.getUsersFirstName() != null) {
                    // todo 未实现 one-to-many;[object Object];firstName
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
     * @return Page<DepartmentDTO>
     */
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> selectByCustomEntity(
        String entityName,
        DepartmentCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "Department";
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
        // DynamicJoinQueryWrapper<Department, Department> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(Department.class, dynamicRelationships);
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
        List<DepartmentDTO> result =
            this.queryList(Department.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
        return new Page<DepartmentDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
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
