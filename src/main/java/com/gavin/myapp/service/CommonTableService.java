package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.CommonTableField;
import com.gavin.myapp.domain.CommonTableRelationship;
import com.gavin.myapp.domain.User;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.repository.CommonTableFieldRepository;
import com.gavin.myapp.repository.CommonTableRelationshipRepository;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.repository.ExtDataRepository;
import com.gavin.myapp.repository.UserRepository;
import com.gavin.myapp.security.SecurityUtils;
import com.gavin.myapp.service.dto.CommonTableDTO;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import com.gavin.myapp.service.dto.CommonTableRelationshipDTO;
import com.gavin.myapp.service.mapper.CommonTableMapper;
import com.gavin.myapp.web.rest.errors.BadRequestAlertException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link CommonTable}.
 */
@Service
@Transactional
public class CommonTableService extends BaseServiceImpl<CommonTableRepository, CommonTable> {

    private final Logger log = LoggerFactory.getLogger(CommonTableService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.CommonTableField.class.getName() + ".commonTable",
        com.gavin.myapp.domain.CommonTableRelationship.class.getName() + ".commonTable",
        com.gavin.myapp.domain.User.class.getName() + ".commonTable",
        com.gavin.myapp.domain.BusinessType.class.getName() + ".commonTable"
    );

    private final CommonTableRepository commonTableRepository;

    private final CacheManager cacheManager;

    private final CommonTableMapper commonTableMapper;

    private final UserRepository userRepository;

    private final ExtDataRepository extDataRepository;

    private final CommonTableFieldRepository commonTableFieldRepository;

    private final CommonTableRelationshipRepository commonTableRelationshipRepository;

    public CommonTableService(
        CommonTableRepository commonTableRepository,
        ExtDataRepository extDataRepository,
        CacheManager cacheManager,
        CommonTableMapper commonTableMapper,
        UserRepository userRepository,
        CommonTableFieldRepository commonTableFieldRepository,
        CommonTableRelationshipRepository commonTableRelationshipRepository
    ) {
        this.commonTableRepository = commonTableRepository;
        this.cacheManager = cacheManager;
        this.commonTableMapper = commonTableMapper;
        this.userRepository = userRepository;
        this.extDataRepository = extDataRepository;
        this.commonTableFieldRepository = commonTableFieldRepository;
        this.commonTableRelationshipRepository = commonTableRelationshipRepository;
    }

    /**
     * Save a commonTable.
     *
     * @param commonTableDTO the entity to save.
     * @return the persisted entity.
     */
    public CommonTableDTO save(CommonTableDTO commonTableDTO) {
        log.debug("Request to save CommonTable : {}", commonTableDTO);
        // 获得未修改数据
        // Optional<CommonTableDTO> old = this.findOne(commonTableDTO.getId());
        // 提取扩展数据
        Map<String, Object> extData = commonTableDTO.getExtData();
        CommonTable commonTable = commonTableMapper.toEntity(commonTableDTO);
        this.saveOrUpdate(commonTable);
        // 保存拓展数据
        if (!extData.isEmpty()) {
            this.saveExtData(commonTable, extData);
        }
        // 获得更新后数据，但不含拓展数据
        CommonTable table = this.getById(commonTable.getId());
        // 绑定关联数据，但不含拓展数据
        Binder.bindRelations(table);
        CommonTableDTO result = commonTableMapper.toDto(table);
        return result;
    }

    /**
     * Get all the commonTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonTableDTO> findAll(IPage<CommonTable> pageable) {
        log.debug("Request to get all CommonTables");
        return this.page(pageable).convert(commonTableMapper::toDto);
    }

    /**
     * Get one commonTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonTableDTO> findOne(Long id) {
        log.debug("Request to get CommonTable : {}", id);
        return Optional
            .ofNullable(commonTableRepository.selectById(id))
            .map(
                commonTable -> {
                    Binder.bindRelations(commonTable);
                    return commonTable;
                }
            )
            .map(commonTableMapper::toDto);
    }

    /**
     * Delete the commonTable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonTable : {}", id);
        commonTableRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommonTableDTO> findByCreatorIsCurrentUser() {
        return commonTableMapper.toDto(commonTableRepository.findByCreatorIsCurrentUser());
    }

    /**
     * Update ignore specified fields by commonTable
     */
    public CommonTableDTO updateByIgnoreSpecifiedFields(CommonTableDTO changeCommonTableDTO, Set<String> unchangedFields) {
        CommonTableDTO commonTableDTO = findOne(changeCommonTableDTO.getId()).get();
        BeanUtil.copyProperties(changeCommonTableDTO, commonTableDTO, unchangedFields.toArray(new String[0]));
        commonTableDTO = save(commonTableDTO);
        return commonTableDTO;
    }

    /**
     * Update specified fields by commonTable
     */
    public CommonTableDTO updateBySpecifiedFields(CommonTableDTO changeCommonTableDTO, Set<String> fieldNames) {
        UpdateWrapper<CommonTable> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeCommonTableDTO.getId()).get();
    }

    public Optional<CommonTableDTO> copyFromId(Long commonTableId) {
        Optional<CommonTable> byId = commonTableRepository.findById(commonTableId);
        if (byId.isPresent()) {
            CommonTable commonTable = byId.get();
            CommonTable result = new CommonTable();
            BeanUtil.copyProperties(commonTable, result, "creator", "relationships", "commonTableFields");
            result
                .name("copy" + result.getName())
                .entityName("copy" + result.getEntityName())
                .id(null)
                .baseTableId(commonTableId)
                .system(false);
            SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(result::setCreator);
            commonTableRepository.save(result);
            CommonTable finalResult = commonTableRepository.selectById(result.getId());
            commonTable
                .getCommonTableFields()
                .forEach(
                    commonTableField -> {
                        CommonTableField newField = new CommonTableField();
                        BeanUtil.copyProperties(commonTableField, newField, "commonTable");
                        newField.setCommonTable(finalResult);
                        newField.setId(null);
                        commonTableFieldRepository.save(newField);
                    }
                );
            commonTable
                .getRelationships()
                .forEach(
                    commonTableRelationship -> {
                        CommonTableRelationship newRelationship = new CommonTableRelationship();
                        BeanUtil.copyProperties(commonTableRelationship, newRelationship, "commonTable");
                        newRelationship.setCommonTable(finalResult);
                        newRelationship.setId(null);
                        commonTableRelationshipRepository.save(newRelationship);
                    }
                );
            return findOne(result.getId());
        } else {
            throw new BadRequestAlertException("Invalid commonTableId", "CommonTable", "IdNotFound");
        }
    }

    /**
     * Update specified field by commonTable
     */
    public CommonTableDTO updateBySpecifiedField(CommonTableDTO changeCommonTableDTO, String fieldName) {
        UpdateWrapper<CommonTable> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonTableDTO.getId());
        updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonTableDTO, fieldName));
        this.update(updateWrapper);
        return findOne(changeCommonTableDTO.getId()).get();
    }

    public Optional<CommonTableDTO> findOneByEntityName(String entityName) {
        return commonTableRepository
            .findOneByEntityName(entityName)
            .map(
                commonTable -> {
                    Binder.bindRelations(commonTable);
                    return commonTable;
                }
            )
            .map(commonTableMapper::toDto);
    }

    /**
     * 保存拓展数据，包含字段和关联关系
     *
     * @param entity  指定实体
     * @param extData 扩展数据
     */
    private void saveExtData(CommonTable entity, Map<String, Object> extData) {
        String entityName = "CommonTable";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        // 先获得结构描述，然后保存到数据库
        Optional<CommonTable> commonTableOptional =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item, new String[] { "extData", "metaModel", "creator", "businessType" });
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
    private Map<String, Object> getExtData(CommonTable entity) {
        String entityName = "CommonTable";
        if (entity.getMetaModel() != null && entity.getMetaModel().getEntityName() != null) {
            entityName = entity.getMetaModel().getEntityName();
        }
        Map<String, Object> result = new HashMap<>();
        // 先获得结构描述
        Optional<CommonTable> commonTableOptional =
            this.commonTableRepository.findOneByEntityName(entityName)
                .map(
                    item -> {
                        Binder.bindRelations(item, new String[] { "extData", "metaModel", "creator", "businessType" });
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
    public CommonTable bindExtData(CommonTable entity) {
        entity.setExtData(getExtData(entity));
        return entity;
    }

    @Override
    public List<CommonTable> bindExtData(List<CommonTable> entities) {
        entities.forEach(entity -> entity.setExtData(getExtData(entity)));
        return entities;
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
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
