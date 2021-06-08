package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.CommonTableField;
import com.gavin.myapp.domain.CommonTableRelationship;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableFieldRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.ExtDataRepository;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import com.gavin.myapp.service.mapper.CommonTableFieldMapper;
import com.gavin.myapp.util.mybatis.handler.HandlerTableChange;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link CommonTableField}.
 */
@Service
@Transactional
public class CommonTableFieldService extends BaseServiceImpl<CommonTableFieldRepository, CommonTableField> {

    private final Logger log = LoggerFactory.getLogger(CommonTableFieldService.class);

    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.CommonTable.class.getName() + ".commonTableFields"
    );

    private final CommonTableFieldRepository commonTableFieldRepository;

    private final CacheManager cacheManager;

    private final ExtDataRepository extDataRepository;

    private final CommonTableRepository commonTableRepository;

    private final CommonTableFieldMapper commonTableFieldMapper;

    public CommonTableFieldService(
        CommonTableFieldRepository commonTableFieldRepository,
        CacheManager cacheManager,
        ExtDataRepository extDataRepository,
        CommonTableRepository commonTableRepository,
        CommonTableFieldMapper commonTableFieldMapper
    ) {
        this.commonTableFieldRepository = commonTableFieldRepository;
        this.cacheManager = cacheManager;
        this.extDataRepository = extDataRepository;
        this.commonTableRepository = commonTableRepository;
        this.commonTableFieldMapper = commonTableFieldMapper;
    }

    /**
     * Save a commonTableField.
     *
     * @param commonTableFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public CommonTableFieldDTO save(CommonTableFieldDTO commonTableFieldDTO) {
        log.debug("Request to save CommonTableField : {}", commonTableFieldDTO);
        // 获得未修改数据
        Optional<CommonTableFieldDTO> old = this.findOne(commonTableFieldDTO.getId());
        // 提取扩展数据
        Map<String, Object> extData = commonTableFieldDTO.getExtData();
        CommonTableField commonTableField = commonTableFieldMapper.toEntity(commonTableFieldDTO);
        this.saveOrUpdate(commonTableField);
        // 保存拓展数据
        if (!extData.isEmpty()) {
            this.saveExtData(commonTableField, extData);
        }
        // 获得更新后数据，但不含拓展数据
        CommonTableField tableField = this.getById(commonTableField.getId());
        // 绑定关联数据，但不含拓展数据
        Binder.bindRelations(tableField, new String[] { "extData" });
        CommonTableFieldDTO result = commonTableFieldMapper.toDto(tableField);
        // 涉及修改表结构，导致无法直接加载新结构时的extData数据。
        // result.setExtData(this.getExtData(result.getId()));
        if (!commonTableFieldDTO.getSystem()) {
            if (commonTableFieldDTO.getId() == null) {
                HandlerTableChange.compareCommonTableField(result, null);
            } else {
                HandlerTableChange.compareCommonTableField(result, old.get());
            }
        }
        return result;
    }

    /**
     * Partially update a commonTableField.
     *
     * @param commonTableFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommonTableFieldDTO> partialUpdate(CommonTableFieldDTO commonTableFieldDTO) {
        log.debug("Request to partially update CommonTableField : {}", commonTableFieldDTO);

        return commonTableFieldRepository
            .findById(commonTableFieldDTO.getId())
            .map(
                existingAdministrativeDivision -> {
                    commonTableFieldMapper.partialUpdate(existingAdministrativeDivision, commonTableFieldDTO);
                    return existingAdministrativeDivision;
                }
            )
            .map(commonTableFieldRepository::save)
            .map(commonTableFieldMapper::toDto);
    }

    /**
     * Get all the commonTableFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableFieldDTO> findAll(IPage<CommonTableField> pageable) {
        log.debug("Request to get all CommonTableFields");
        return this.page(pageable).convert(commonTableFieldMapper::toDto);
    }

    /**
     * Get all the commonTableFields where ExtData is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommonTableFieldDTO> findAllWhereExtDataIsNull() {
        log.debug("Request to get all commonTableFields where ExtData is null");
        return StreamSupport
            .stream(commonTableFieldRepository.findAll().spliterator(), false)
            .filter(commonTableField -> commonTableField.getExtData() == null)
            .map(commonTableFieldMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one commonTableField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonTableFieldDTO> findOne(Long id) {
        log.debug("Request to get CommonTableField : {}", id);
        return Optional
            .ofNullable(commonTableFieldRepository.selectById(id))
            .map(
                commonTableField -> {
                    Binder.bindRelations(commonTableField);
                    commonTableField.extData(this.getExtData(commonTableField));
                    return commonTableField;
                }
            )
            .map(commonTableFieldMapper::toDto);
    }

    /**
     * Delete the commonTableField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonTableField : {}", id);
        commonTableFieldRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by commonTableField
     */
    public CommonTableFieldDTO updateByIgnoreSpecifiedFields(CommonTableFieldDTO changeCommonTableFieldDTO, Set<String> unchangedFields) {
        CommonTableFieldDTO commonTableFieldDTO = findOne(changeCommonTableFieldDTO.getId()).get();
        BeanUtil.copyProperties(changeCommonTableFieldDTO, commonTableFieldDTO, unchangedFields.toArray(new String[0]));
        commonTableFieldDTO = save(commonTableFieldDTO);
        return commonTableFieldDTO;
    }

    /**
     * Update specified fields by commonTableField
     */
    public CommonTableFieldDTO updateBySpecifiedFields(CommonTableFieldDTO changeCommonTableFieldDTO, Set<String> fieldNames) {
        UpdateWrapper<CommonTableField> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableFieldDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableFieldDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeCommonTableFieldDTO.getId()).get();
    }

    /**
     * Update specified field by commonTableField
     */
    public CommonTableFieldDTO updateBySpecifiedField(CommonTableFieldDTO changeCommonTableFieldDTO, String fieldName) {
        UpdateWrapper<CommonTableField> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableFieldDTO.getId());
        updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableFieldDTO, fieldName));
        this.update(updateWrapper);
        return findOne(changeCommonTableFieldDTO.getId()).get();
    }

    private void clearRelationsCache() {
        this.relationCacheNames.forEach(
                cacheName -> {
                    if (cacheManager.getCache(cacheName) != null) {
                        cacheManager.getCache(cacheName).clear();
                    }
                }
            );
    }

    /**
     * 保存拓展数据，包含字段和关联关系
     *
     * @param entity  指定实体
     * @param extData 扩展数据
     */
    private void saveExtData(CommonTableField entity, Map<String, Object> extData) {
        String entityName = "CommonTableField";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        // 先获得结构描述，然后保存到数据库
        Optional<CommonTable> commonTableOptional =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item);
                        return item;
                    }
                );
        commonTableOptional.ifPresent(
            commonTable -> {
                String extTableName = commonTable.getTableName() + "_" + commonTable.getId() + "_ext";
                if (this.extDataRepository.existsByTableNameAndId(extTableName, entity.getId())) {
                    // 更新拓展字段
                    Map<String, Object> fieldsMap = new HashMap<>();
                    for (CommonTableField commonTableField : commonTable.getCommonTableFields()) {
                        if (!commonTableField.getSystem()) {
                            fieldsMap.put(commonTableField.getTableColumnName(), extData.get(commonTableField.getEntityFieldName()));
                        }
                    }
                    // 更新拓展关联关系
                    for (CommonTableRelationship commonTableRelationship : commonTable.getRelationships()) {
                        if (!commonTableRelationship.getSystem()) {
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                            ) {
                                String fieldName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                fieldsMap.put(fieldName, extData.get(commonTableRelationship.getRelationshipName() + "Id"));
                            } else {
                                // 有中间表
                                String tableName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityName()
                                );
                                String relationshipFieldName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + "Id"
                                );
                                String otherEntityIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName() + "Id");
                                Map<String, Object> relationFieldsMap = new HashMap<>();
                                relationFieldsMap.put(relationshipFieldName, entity.getId());
                                relationFieldsMap.put(otherEntityIdName, extData.get(otherEntityIdName));
                                this.extDataRepository.updateToManyRelationById(tableName, entity.getId(), relationFieldsMap);
                            }
                        }
                    }
                    if (!fieldsMap.isEmpty()) {
                        this.extDataRepository.updateToManyRelationById(extTableName, entity.getId(), fieldsMap);
                    }
                } else {
                    // insert拓展的字段数据
                    Map<String, Object> insertMap = new HashMap<>();
                    commonTable
                        .getCommonTableFields()
                        .stream()
                        .filter(commonTableField -> !commonTableField.getSystem())
                        .forEach(
                            field -> {
                                insertMap.put(field.getTableColumnName(), extData.get(field.getEntityFieldName()));
                            }
                        );
                    // insert拓展的关联数据
                    for (CommonTableRelationship commonTableRelationship : commonTable.getRelationships()) {
                        if (!commonTableRelationship.getSystem()) {
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                            ) {
                                String fieldName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                insertMap.put(fieldName, extData.get(commonTableRelationship.getRelationshipName() + "Id"));
                            } else {
                                // 有中间表
                                String jointTableName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityName()
                                );
                                String relationshipFieldName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + "Id"
                                );
                                String otherEntityIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName() + "Id");
                                Map<String, Object> fieldsMap = new HashMap<>();
                                fieldsMap.put(relationshipFieldName, entity.getId());
                                fieldsMap.put(otherEntityIdName, extData.get(otherEntityIdName));
                                this.extDataRepository.insertToManyRelation(jointTableName, fieldsMap);
                            }
                        }
                    }
                    if (!insertMap.isEmpty()) {
                        insertMap.put("id", entity.getId());
                        this.extDataRepository.insertByMap(extTableName, insertMap);
                    }
                }
            }
        );
    }

    /**
     * 返回指定实体的拓展数据
     *
     * @param entity 实体
     * @return Map<String, Object>
     */
    private Map<String, Object> getExtData(CommonTableField entity) {
        String entityName = "CommonTableField";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        Map<String, Object> result = new HashMap<>();
        // 先获得结构描述
        Optional<CommonTable> commonTableOptional =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item);
                        return item;
                    }
                );
        Map<String, Object> fieldsMap = new HashMap<>();
        commonTableOptional.ifPresent(
            commonTable -> {
                String extTableName = commonTable.getTableName() + "_" + commonTable.getId() + "_ext";
                // 返回拓展字段内容
                commonTable
                    .getCommonTableFields()
                    .stream()
                    .filter(commonTableField -> !commonTableField.getSystem())
                    .forEach(
                        field -> {
                            fieldsMap.put(field.getTableColumnName(), field.getEntityFieldName());
                        }
                    );
                if (!fieldsMap.isEmpty()) {
                    Map<String, Object> objectMap = extDataRepository.selectMapByIdAndColumns(extTableName, entity.getId(), fieldsMap);
                    if (objectMap != null && !objectMap.isEmpty()) {
                        result.putAll(objectMap);
                    }
                }
                // 返回拓展关联内容
                commonTable
                    .getRelationships()
                    .stream()
                    .filter(commonTableRelationship -> !commonTableRelationship.getSystem())
                    .forEach(
                        commonTableRelationship -> {
                            Binder.bindRelations(commonTableRelationship, new String[] { "extData", "dataDictionaryNode" });
                            if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE)
                            ) {
                                String relationIdName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                String relationTitleName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityField());
                                String tableName =
                                    commonTableRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    commonTableRelationship.getCommonTable().getId() +
                                    "_ext";
                                Optional<CommonTable> oneByEntityName = commonTableRepository.findOneByEntityName(
                                    commonTableRelationship.getOtherEntityName()
                                );
                                String joinTableName = oneByEntityName.get().getTableName();
                                fieldsMap.clear();
                                fieldsMap.put("a." + relationIdName, commonTableRelationship.getRelationshipName() + "Id");
                                fieldsMap.put(
                                    "b." + relationTitleName,
                                    commonTableRelationship.getRelationshipName() +
                                    StrUtil.upperFirst(commonTableRelationship.getOtherEntityField())
                                );
                                Map<String, Object> one =
                                    this.extDataRepository.selectMapByTableAndIdAndColumns(
                                            tableName,
                                            entity.getId(),
                                            fieldsMap,
                                            joinTableName,
                                            relationIdName
                                        );
                                if (one != null) {
                                    result.putAll(one);
                                }
                            } else if (
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY) ||
                                commonTableRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)
                            ) {
                                String relationIdName = StrUtil.toUnderlineCase(commonTableRelationship.getRelationshipName() + "Id");
                                String relationTitleName = StrUtil.toUnderlineCase(
                                    commonTableRelationship.getRelationshipName() + commonTableRelationship.getOtherEntityField()
                                );
                                String tableName = "RelationshipJhiExt";
                                String otherIdName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName()) + "_id";
                                String joinTableName = commonTableRelationship.getCommonTable().getTableName();
                                String otherTableName = StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityName());
                                fieldsMap.clear();
                                fieldsMap.put("b.id", "id");
                                fieldsMap.put(
                                    "b." + StrUtil.toUnderlineCase(commonTableRelationship.getOtherEntityField()),
                                    commonTableRelationship.getOtherEntityField()
                                );
                                List<Map<String, Object>> list =
                                    this.extDataRepository.selectMapsByTableAndIdAndColumns(
                                            entity.getId(),
                                            fieldsMap,
                                            joinTableName,
                                            otherTableName,
                                            relationIdName,
                                            otherIdName
                                        );
                                if (list != null && !list.isEmpty()) {
                                    result.put(commonTableRelationship.getRelationshipName(), list);
                                }
                            }
                        }
                    );
            }
        );
        return result;
    }

    @Override
    public CommonTableField bindExtData(CommonTableField commonTableField) {
        commonTableField.setExtData(getExtData(commonTableField));
        return commonTableField;
    }

    @Override
    public List<CommonTableField> bindExtData(List<CommonTableField> commonTableFields) {
        commonTableFields.forEach(commonTableField -> commonTableField.setExtData(getExtData(commonTableField)));
        return commonTableFields;
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
