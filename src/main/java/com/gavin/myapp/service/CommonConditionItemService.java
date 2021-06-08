package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.CommonConditionItem;
import com.gavin.myapp.repository.CommonConditionItemRepository;
import com.gavin.myapp.service.dto.CommonConditionItemDTO;
import com.gavin.myapp.service.mapper.CommonConditionItemMapper;
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
 * Service Implementation for managing {@link CommonConditionItem}.
 */
@Service
@Transactional
public class CommonConditionItemService extends BaseServiceImpl<CommonConditionItemRepository, CommonConditionItem> {

    private final Logger log = LoggerFactory.getLogger(CommonConditionItemService.class);
    private final List<String> relationCacheNames = Arrays.asList(com.gavin.myapp.domain.CommonCondition.class.getName() + ".items");

    private final CommonConditionItemRepository commonConditionItemRepository;

    private final CacheManager cacheManager;

    private final CommonConditionItemMapper commonConditionItemMapper;

    public CommonConditionItemService(
        CommonConditionItemRepository commonConditionItemRepository,
        CacheManager cacheManager,
        CommonConditionItemMapper commonConditionItemMapper
    ) {
        this.commonConditionItemRepository = commonConditionItemRepository;
        this.cacheManager = cacheManager;
        this.commonConditionItemMapper = commonConditionItemMapper;
    }

    /**
     * Save a commonConditionItem.
     *
     * @param commonConditionItemDTO the entity to save.
     * @return the persisted entity.
     */
    public CommonConditionItemDTO save(CommonConditionItemDTO commonConditionItemDTO) {
        log.debug("Request to save CommonConditionItem : {}", commonConditionItemDTO);
        CommonConditionItem commonConditionItem = commonConditionItemMapper.toEntity(commonConditionItemDTO);
        this.saveOrUpdate(commonConditionItem);
        return commonConditionItemMapper.toDto(this.getById(commonConditionItem.getId()));
    }

    /**
     * Partially update a commonConditionItem.
     *
     * @param commonConditionItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommonConditionItemDTO> partialUpdate(CommonConditionItemDTO commonConditionItemDTO) {
        log.debug("Request to partially update CommonConditionItem : {}", commonConditionItemDTO);

        return commonConditionItemRepository
            .findById(commonConditionItemDTO.getId())
            .map(
                existingCommonConditionItem -> {
                    commonConditionItemMapper.partialUpdate(existingCommonConditionItem, commonConditionItemDTO);
                    return existingCommonConditionItem;
                }
            )
            .map(commonConditionItemRepository::save)
            .map(commonConditionItemMapper::toDto);
    }

    /**
     * Get all the commonConditionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionItemDTO> findAll(Page<CommonConditionItem> pageable) {
        log.debug("Request to get all CommonConditionItems");
        return this.page(pageable).convert(commonConditionItemMapper::toDto);
    }

    /**
     * Get one commonConditionItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonConditionItemDTO> findOne(Long id) {
        log.debug("Request to get CommonConditionItem : {}", id);
        return Optional.ofNullable(commonConditionItemRepository.selectById(id)).map(commonConditionItemMapper::toDto);
    }

    /**
     * Delete the commonConditionItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonConditionItem : {}", id);
        commonConditionItemRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by commonConditionItem
     */
    public CommonConditionItemDTO updateByIgnoreSpecifiedFields(
        CommonConditionItemDTO changeCommonConditionItemDTO,
        Set<String> unchangedFields
    ) {
        CommonConditionItemDTO commonConditionItemDTO = findOne(changeCommonConditionItemDTO.getId()).get();
        BeanUtil.copyProperties(changeCommonConditionItemDTO, commonConditionItemDTO, unchangedFields.toArray(new String[0]));
        commonConditionItemDTO = save(commonConditionItemDTO);
        return commonConditionItemDTO;
    }

    /**
     * Update specified fields by commonConditionItem
     */
    public CommonConditionItemDTO updateBySpecifiedFields(CommonConditionItemDTO changeCommonConditionItemDTO, Set<String> fieldNames) {
        UpdateWrapper<CommonConditionItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonConditionItemDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonConditionItemDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeCommonConditionItemDTO.getId()).get();
    }

    /**
     * Update specified field by commonConditionItem
     */
    public CommonConditionItemDTO updateBySpecifiedField(CommonConditionItemDTO changeCommonConditionItemDTO, String fieldName) {
        CommonConditionItemDTO updateDTO = new CommonConditionItemDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeCommonConditionItemDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeCommonConditionItemDTO, fieldName));
        this.updateEntity(commonConditionItemMapper.toEntity(updateDTO));
        return findOne(changeCommonConditionItemDTO.getId()).get();
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
