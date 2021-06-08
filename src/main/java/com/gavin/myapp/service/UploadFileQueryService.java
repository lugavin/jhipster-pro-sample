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
import com.gavin.myapp.domain.UploadFile;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.UploadFileRepository;
import com.gavin.myapp.service.criteria.UploadFileCriteria;
import com.gavin.myapp.service.dto.UploadFileDTO;
import com.gavin.myapp.service.mapper.UploadFileMapper;
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
 * 用于对数据库中的{@link UploadFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UploadFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UploadFileDTO}列表{@link List} 或 {@link UploadFileDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class UploadFileQueryService extends QueryService<UploadFile> {

    private final Logger log = LoggerFactory.getLogger(UploadFileQueryService.class);

    private final UploadFileRepository uploadFileRepository;

    private final CommonTableRepository commonTableRepository;

    private final UploadFileMapper uploadFileMapper;

    public UploadFileQueryService(
        UploadFileRepository uploadFileRepository,
        CommonTableRepository commonTableRepository,
        UploadFileMapper uploadFileMapper
    ) {
        super(UploadFile.class, null);
        this.uploadFileRepository = uploadFileRepository;
        this.commonTableRepository = commonTableRepository;
        this.uploadFileMapper = uploadFileMapper;
    }

    /**
     * Return a {@link List} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UploadFileDTO> findByCriteria(UploadFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return uploadFileMapper.toDto(uploadFileRepository.selectList(this));
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UploadFileDTO> findByQueryWrapper(QueryWrapper<UploadFile> queryWrapper, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uploadFileRepository.selectPage(page, queryWrapper).convert(uploadFileMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UploadFileDTO> findByCriteria(UploadFileCriteria criteria, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        createQueryWrapper(criteria);
        return uploadFileRepository.selectPage(page, this).convert(uploadFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UploadFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        createQueryWrapper(criteria);
        return uploadFileRepository.selectCount(this);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return uploadFileRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link UploadFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected void createQueryWrapper(UploadFileCriteria criteria) {
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
                            "file_size"
                        );
                    this.or();
                    this.buildRangeSpecification(
                            (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "reference_count"
                        );
                } else {
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "full_name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "ext");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "type");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "url");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "path");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "folder");
                    this.or();
                    this.buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "entity_name");
                }
            } else {
                if (criteria.getId() != null) {
                    this.buildRangeSpecification(criteria.getId(), "id");
                }
                if (criteria.getFullName() != null) {
                    this.buildStringSpecification(criteria.getFullName(), "full_name");
                }
                if (criteria.getName() != null) {
                    this.buildStringSpecification(criteria.getName(), "name");
                }
                if (criteria.getExt() != null) {
                    this.buildStringSpecification(criteria.getExt(), "ext");
                }
                if (criteria.getType() != null) {
                    this.buildStringSpecification(criteria.getType(), "type");
                }
                if (criteria.getUrl() != null) {
                    this.buildStringSpecification(criteria.getUrl(), "url");
                }
                if (criteria.getPath() != null) {
                    this.buildStringSpecification(criteria.getPath(), "path");
                }
                if (criteria.getFolder() != null) {
                    this.buildStringSpecification(criteria.getFolder(), "folder");
                }
                if (criteria.getEntityName() != null) {
                    this.buildStringSpecification(criteria.getEntityName(), "entity_name");
                }
                if (criteria.getCreateAt() != null) {
                    this.buildRangeSpecification(criteria.getCreateAt(), "create_at");
                }
                if (criteria.getFileSize() != null) {
                    this.buildRangeSpecification(criteria.getFileSize(), "file_size");
                }
                if (criteria.getReferenceCount() != null) {
                    this.buildRangeSpecification(criteria.getReferenceCount(), "reference_count");
                }
                if (criteria.getUserId() != null) {
                    this.buildRangeSpecification(criteria.getUserId(), "user_id");
                }
                if (criteria.getUserLogin() != null) {
                    this.buildStringSpecification(criteria.getUserLogin(), "jhi_user_left_join.login");
                }
                if (criteria.getCategoryId() != null) {
                    this.buildRangeSpecification(criteria.getCategoryId(), "category_id");
                }
                if (criteria.getCategoryTitle() != null) {
                    this.buildStringSpecification(criteria.getCategoryTitle(), "resource_category_left_join.title");
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
     * @return Page<UploadFileDTO>
     */
    @Transactional(readOnly = true)
    public Page<UploadFileDTO> selectByCustomEntity(
        String entityName,
        UploadFileCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "UploadFile";
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
        // DynamicJoinQueryWrapper<UploadFile, UploadFile> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(UploadFile.class, dynamicRelationships);
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
        List<UploadFileDTO> result =
            this.queryList(UploadFile.class, pagination)
                .stream()
                .peek(Binder::bindRelations)
                .map(uploadFileMapper::toDto)
                .collect(Collectors.toList());
        return new Page<UploadFileDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount()).setRecords(result);
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
