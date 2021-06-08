package com.gavin.myapp.service;

import cn.hutool.core.bean.DynaBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.gavin.myapp.anltr.CriteriaLogicExprListener;
import com.gavin.myapp.anltr.LogicExprLexer;
import com.gavin.myapp.anltr.LogicExprParser;
import com.gavin.myapp.domain.*; // for static metamodels
import com.gavin.myapp.domain.CommonCondition;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonConditionRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.service.criteria.CommonConditionCriteria;
import com.gavin.myapp.service.dto.CommonConditionDTO;
import com.gavin.myapp.service.mapper.CommonConditionMapper;
import com.gavin.myapp.util.mybatis.filter.QueryService;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.*;

/**
 * Service for executing complex queries for {@link CommonCondition} entities in the database.
 * The main input is a {@link CommonConditionCriteria} which gets converted to {@link QueryWrapper},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommonConditionDTO} or a {@link IPage} of {@link CommonConditionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommonConditionQueryService extends QueryService<CommonCondition> {

    private final Logger log = LoggerFactory.getLogger(CommonConditionQueryService.class);

    public static Map<String, Object> specificationMap = new HashMap<>();

    private final CommonConditionRepository commonConditionRepository;

    private final CommonTableRepository commonTableRepository;

    private final CommonConditionMapper commonConditionMapper;

    public CommonConditionQueryService(
        CommonConditionRepository commonConditionRepository,
        CommonTableRepository commonTableRepository,
        CommonConditionMapper commonConditionMapper
    ) {
        super(CommonCondition.class, null);
        this.commonConditionRepository = commonConditionRepository;
        this.commonTableRepository = commonTableRepository;
        this.commonConditionMapper = commonConditionMapper;
    }

    /**
     * Return a {@link List} of {@link CommonConditionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommonConditionDTO> findByCriteria(CommonConditionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<CommonCondition> queryWrapper = createQueryWrapper(criteria);
        return commonConditionMapper.toDto(commonConditionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link Page} of {@link CommonConditionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionDTO> findByCriteria(CommonConditionCriteria criteria, Page<CommonCondition> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<CommonCondition> queryWrapper = createQueryWrapper(criteria);
        return commonConditionRepository.selectPage(page, queryWrapper).convert(commonConditionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link CommonConditionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionDTO> findByQueryWrapper(QueryWrapper<CommonCondition> queryWrapper, Page<CommonCondition> page) {
        log.debug("find by specification : {}, page: {}", queryWrapper, page);
        return commonConditionRepository.selectPage(page, queryWrapper).convert(commonConditionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommonConditionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<CommonCondition> queryWrapper = createQueryWrapper(criteria);
        return commonConditionRepository.selectCount(queryWrapper);
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return commonConditionRepository.selectCount(queryWrapper);
    }

    /**
     * Function to convert {@link CommonConditionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<CommonCondition> createQueryWrapper(CommonConditionCriteria criteria) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id")
                        );
                } else {
                    queryWrapper =
                        queryWrapper.or(
                            i -> buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        queryWrapper.or(
                            i ->
                                buildStringSpecification(
                                    new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                    "description"
                                )
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getDescription() != null) {
                    queryWrapper = queryWrapper.and(i -> buildStringSpecification(criteria.getDescription(), "description"));
                }
                if (criteria.getLastModifiedTime() != null) {
                    queryWrapper = queryWrapper.and(i -> buildRangeSpecification(criteria.getLastModifiedTime(), "last_modified_time"));
                }
                /*if (criteria.getItemsId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getItemsId(),
                        root -> root.join(CommonCondition_.items, JoinType.LEFT).get(CommonConditionItem_.id)));
                }*/
                /*if (criteria.getModifierId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getModifierId(),
                        root -> root.join(CommonCondition_.modifier, JoinType.LEFT).get(User_.id)));
                }*/
                /*if (criteria.getCommonTableId() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getCommonTableId(),
                        root -> root.join(CommonCondition_.commonTable, JoinType.LEFT).get(CommonTable_.id)));
                }*/
                /*if (criteria.getCommonTableClazzName() != null) {
                    queryWrapper = queryWrapper.and(i -> buildSpecification(criteria.getCommonTableClazzName(),
                        root -> root.join(CommonCondition_.commonTable, JoinType.LEFT).get(CommonTable_.clazzName)));
                }*/
            }
        }
        return queryWrapper;
    }

    /**
     * 直接转换为dto。maytoone的，直接查询结果。one-to-many和many-to-many后续加载
     * @param entityName 模型名称
     * @param criteria 条件表达式
     * @param pageable 分页
     * @return Page<CommonConditionDTO>
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionDTO> selectByCustomEntity(
        String entityName,
        CommonConditionCriteria criteria,
        QueryWrapper queryWrapper,
        Page pageable
    ) {
        if (StringUtils.isEmpty(entityName)) {
            entityName = "CommonCondition";
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
        DynamicJoinQueryWrapper<CommonCondition, CommonCondition> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
            CommonCondition.class,
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
        List<CommonConditionDTO> result = dynamicJoinQueryWrapper
            .queryList(CommonCondition.class, pagination)
            .stream()
            .peek(Binder::bindRelations)
            .map(commonConditionMapper::toDto)
            .collect(Collectors.toList());
        return new Page<CommonConditionDTO>(pagination.getPageIndex(), pagination.getPageSize(), pagination.getTotalCount())
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

    // 把commonQuery转为DynamicJoinQueryWrapper
    public DynamicJoinQueryWrapper createQueryWrapper(Long commonQueryId) throws ClassNotFoundException {
        CommonCondition commonCondition = this.commonConditionRepository.selectById(commonQueryId);
        if (commonCondition != null) {
            String packageName = ClassUtils.getPackageName(CommonCondition.class);
            String servicePackageName = ClassUtils.getPackageName(this.getClass());
            Class targetClass = Class.forName(packageName + "." + commonCondition.getCommonTable().getClazzName() + "_");
            Class targetCriteriaClass = Class.forName(
                servicePackageName + ".dto." + commonCondition.getCommonTable().getClazzName() + "Criteria"
            );
            List<CommonConditionItem> conditionItems = commonCondition.getItems();
            StringBuffer s = new StringBuffer();
            conditionItems.forEach(
                queryItem -> {
                    String prefix = queryItem.getPrefix();
                    String suffix = queryItem.getSuffix();
                    if (StringUtils.isNotEmpty(prefix)) {
                        switch (prefix) {
                            case ")":
                                s.append(" )");
                                break;
                            case "(":
                                s.append(" (");
                                break;
                            case "AND":
                                s.append(" AND");
                                break;
                            case "OR":
                                s.append(" AND");
                                break;
                            default:
                        }
                    }
                    try {
                        Integer specId = specificationMap.size() + 1;
                        specificationMap.put(
                            queryItem.getId() + "_" + specId,
                            createMergeSegmentsByQueryItem(queryItem, targetClass, targetCriteriaClass)
                        );
                        s.append(" " + queryItem.getId() + "_" + specId);
                    } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (StringUtils.isNotEmpty(suffix)) {
                        switch (suffix) {
                            case ")":
                                s.append(" )");
                                break;
                            case "(":
                                s.append(" (");
                                break;
                            case "AND":
                                s.append(" AND");
                                break;
                            case "OR":
                                s.append(" AND");
                                break;
                            default:
                        }
                    }
                }
            );

            /*String packageName = ClassUtils.getPackageName(CommonCondition.class);
        String servicePackageName = ClassUtils.getPackageName(this.getClass());
        Specification specification;
        if (commonCondition != null) {
            Class targetClass = Class.forName(packageName + "." + commonCondition.getCommonTable().getClazzName() + "_");
            Class targetCriteriaClass = Class.forName(servicePackageName + ".dto." + commonCondition.getCommonTable().getClazzName() + "Criteria");
            StringBuffer s = new StringBuffer();
            Set<CommonConditionItem> conditionItems = commonCondition.getItems();
            conditionItems.forEach( queryItem -> {
                String prefix = queryItem.getPrefix();
                String suffix = queryItem.getSuffix();
                if (StringUtils.isNotEmpty(prefix)) {
                    switch (prefix) {
                        case ")":
                            s.append(" )");
                            break;
                        case "(":
                            s.append(" (");
                            break;
                        case "AND":
                            s.append(" AND");
                            break;
                        case "OR":
                            s.append(" AND");
                            break;
                        default:

                    }
                }
                try {
                    Integer specId = specificationMap.size()+1;
                    specificationMap.put(queryItem.getId() + "_" + specId, createMergeSegmentsByQueryItem(queryItem,targetClass,targetCriteriaClass));
                    s.append(" " +queryItem.getId() + "_" + specId);
                } catch (NoSuchFieldException | InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotEmpty(suffix)) {
                    switch (suffix) {
                        case ")":
                            s.append(" )");
                            break;
                        case "(":
                            s.append(" (");
                            break;
                        case "AND":
                            s.append(" AND");
                            break;
                        case "OR":
                            s.append(" AND");
                            break;
                        default:
                    }
                }
            });*/
            ANTLRInputStream input = new ANTLRInputStream(s.toString());
            LogicExprLexer lexer = new LogicExprLexer(input);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            LogicExprParser parser = new LogicExprParser(tokenStream);
            LogicExprParser.StatContext parseTree = parser.stat();
            CriteriaLogicExprListener visitor = new CriteriaLogicExprListener();
            System.out.println(parseTree.toStringTree(parser)); //打印规则数
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(visitor, parseTree);
            return (DynamicJoinQueryWrapper) (new DynamicJoinQueryWrapper(targetClass, null)).apply(
                    visitor.specifications.get(parseTree).getSqlSegment()
                );
        } else {
            return null;
        }
    }

    private MergeSegments createMergeSegmentsByQueryItem(
        CommonConditionItem commonConditionItem,
        Class jPAMetaModelClass,
        Class targetCriteriaClass
    ) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        if (
            StringUtils.isNotEmpty(commonConditionItem.getFieldName()) &&
            StringUtils.isNotEmpty(commonConditionItem.getOperator()) &&
            StringUtils.isNotEmpty(commonConditionItem.getValue())
        ) {
            DynaBean criteria = DynaBean.create(targetCriteriaClass.newInstance());
            DynaBean filter;
            switch (commonConditionItem.getFieldType()) {
                case "LONG":
                    filter = DynaBean.create(LongFilter.class);
                    filter.set(commonConditionItem.getOperator(), Long.parseLong(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildRangeMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                case "INTEGER":
                    filter = DynaBean.create(IntegerFilter.class);
                    filter.set(commonConditionItem.getOperator(), Integer.parseInt(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildRangeMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                case "STRING":
                    filter = DynaBean.create(new StringFilter());
                    filter.set(commonConditionItem.getOperator(), commonConditionItem.getValue());
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildStringMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                case "FLOAT":
                    filter = DynaBean.create(FloatFilter.class);
                    filter.set(commonConditionItem.getOperator(), Float.parseFloat(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildRangeMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                case "DOUBLE":
                    filter = DynaBean.create(DoubleFilter.class);
                    filter.set(commonConditionItem.getOperator(), Double.parseDouble(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildRangeMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                case "BOOLEAN":
                    filter = DynaBean.create(BooleanFilter.class);
                    filter.set(commonConditionItem.getOperator(), Boolean.parseBoolean(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildMergeSegments(
                        (BooleanFilter) criteria.get(commonConditionItem.getFieldName()),
                        commonConditionItem.getFieldName()
                    );
                case "ZONED_DATE_TIME":
                    filter = DynaBean.create(ZonedDateTimeFilter.class);
                    filter.set(commonConditionItem.getOperator(), ZonedDateTime.parse(commonConditionItem.getValue()));
                    criteria.set(commonConditionItem.getFieldName(), filter.getBean());
                    return buildRangeMergeSegments(criteria.get(commonConditionItem.getFieldName()), commonConditionItem.getFieldName());
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
}
