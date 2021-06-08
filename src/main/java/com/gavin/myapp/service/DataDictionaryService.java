package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.repository.DataDictionaryRepository;
import com.gavin.myapp.service.dto.DataDictionaryDTO;
import com.gavin.myapp.service.mapper.DataDictionaryMapper;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link DataDictionary}.
 */
@Service
@Transactional
public class DataDictionaryService extends BaseServiceImpl<DataDictionaryRepository, DataDictionary> {

    private final Logger log = LoggerFactory.getLogger(DataDictionaryService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.DataDictionary.class.getName() + ".parent",
        com.gavin.myapp.domain.DataDictionary.class.getName() + ".children"
    );

    private final DataDictionaryRepository dataDictionaryRepository;

    private final CacheManager cacheManager;

    private final DataDictionaryMapper dataDictionaryMapper;

    public DataDictionaryService(
        DataDictionaryRepository dataDictionaryRepository,
        CacheManager cacheManager,
        DataDictionaryMapper dataDictionaryMapper
    ) {
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.cacheManager = cacheManager;
        this.dataDictionaryMapper = dataDictionaryMapper;
    }

    /**
     * Save a dataDictionary.
     *
     * @param dataDictionaryDTO the entity to save.
     * @return the persisted entity.
     */
    public DataDictionaryDTO save(DataDictionaryDTO dataDictionaryDTO) {
        log.debug("Request to save DataDictionary : {}", dataDictionaryDTO);
        DataDictionary dataDictionary = dataDictionaryMapper.toEntity(dataDictionaryDTO);
        clearChildrenCache();
        this.saveOrUpdate(dataDictionary);
        // 更新缓存
        if (dataDictionary.getParent() != null) {
            dataDictionary.getParent().addChildren(dataDictionary);
        }
        return dataDictionaryMapper.toDto(this.getById(dataDictionary.getId()));
    }

    /**
     * Partially update a dataDictionary.
     *
     * @param dataDictionaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DataDictionaryDTO> partialUpdate(DataDictionaryDTO dataDictionaryDTO) {
        log.debug("Request to partially update DataDictionary : {}", dataDictionaryDTO);

        return dataDictionaryRepository
            .findById(dataDictionaryDTO.getId())
            .map(
                existingDataDictionary -> {
                    dataDictionaryMapper.partialUpdate(existingDataDictionary, dataDictionaryDTO);
                    return existingDataDictionary;
                }
            )
            .map(dataDictionaryRepository::save)
            .map(dataDictionaryMapper::toDto);
    }

    /**
     * Get all the dataDictionaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataDictionaryDTO> findAll(Page<DataDictionary> pageable) {
        log.debug("Request to get all DataDictionaries");
        return this.page(pageable).convert(dataDictionaryMapper::toDto);
    }

    /**
     * Get all the dataDictionaries for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<DataDictionaryDTO> findAllTop(Page<DataDictionary> pageable) {
        log.debug("Request to get all DataDictionaries for parent is null");
        return dataDictionaryRepository
            .findAllByParentIsNull(pageable)
            .convert(
                dataDictionary -> {
                    Binder.bindRelations(dataDictionary, new String[] { "children", "parent" });
                    return dataDictionaryMapper.toDto(dataDictionary);
                }
            );
    }

    /**
     * Get all the dataDictionaries for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<DataDictionaryDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all DataDictionaries for parent is parentId");
        return dataDictionaryRepository
            .selectList(new LambdaUpdateWrapper<DataDictionary>().eq(DataDictionary::getParentId, parentId))
            .stream()
            .map(
                dataDictionary -> {
                    Binder.bindRelations(dataDictionary, new String[] { "children", "parent" });
                    return dataDictionaryMapper.toDto(dataDictionary);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one dataDictionary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataDictionaryDTO> findOne(Long id) {
        log.debug("Request to get DataDictionary : {}", id);
        return Optional.ofNullable(dataDictionaryRepository.selectById(id)).map(dataDictionaryMapper::toDto);
    }

    /**
     * Delete the dataDictionary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataDictionary : {}", id);
        DataDictionary dataDictionary = dataDictionaryRepository.selectById(id);
        if (dataDictionary.getParent() != null) {
            dataDictionary.getParent().removeChildren(dataDictionary);
        }
        if (dataDictionary.getChildren() != null) {
            dataDictionary
                .getChildren()
                .forEach(
                    subDataDictionary -> {
                        subDataDictionary.setParent(null);
                    }
                );
        }
        dataDictionaryRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by dataDictionary
     */
    public DataDictionaryDTO updateByIgnoreSpecifiedFields(DataDictionaryDTO changeDataDictionaryDTO, Set<String> unchangedFields) {
        DataDictionaryDTO dataDictionaryDTO = findOne(changeDataDictionaryDTO.getId()).get();
        BeanUtil.copyProperties(changeDataDictionaryDTO, dataDictionaryDTO, unchangedFields.toArray(new String[0]));
        dataDictionaryDTO = save(dataDictionaryDTO);
        return dataDictionaryDTO;
    }

    /**
     * Update specified fields by dataDictionary
     */
    public DataDictionaryDTO updateBySpecifiedFields(DataDictionaryDTO changeDataDictionaryDTO, Set<String> fieldNames) {
        UpdateWrapper<DataDictionary> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeDataDictionaryDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeDataDictionaryDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeDataDictionaryDTO.getId()).get();
    }

    /**
     * Update specified field by dataDictionary
     */
    public DataDictionaryDTO updateBySpecifiedField(DataDictionaryDTO changeDataDictionaryDTO, String fieldName) {
        DataDictionaryDTO updateDTO = new DataDictionaryDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeDataDictionaryDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeDataDictionaryDTO, fieldName));
        this.updateEntity(dataDictionaryMapper.toEntity(updateDTO));
        return findOne(changeDataDictionaryDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.gavin.myapp.domain.DataDictionary.class.getName() + ".children")).clear();
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
