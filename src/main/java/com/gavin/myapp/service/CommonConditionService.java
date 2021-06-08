package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.CommonCondition;
import com.gavin.myapp.repository.CommonConditionRepository;
import com.gavin.myapp.service.dto.CommonConditionDTO;
import com.gavin.myapp.service.mapper.CommonConditionMapper;
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
 * Service Implementation for managing {@link CommonCondition}.
 */
@Service
@Transactional
public class CommonConditionService extends BaseServiceImpl<CommonConditionRepository, CommonCondition> {

    private final Logger log = LoggerFactory.getLogger(CommonConditionService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.CommonConditionItem.class.getName() + ".commonCondition",
        com.gavin.myapp.domain.CommonTable.class.getName() + ".commonCondition"
    );

    private final CommonConditionRepository commonConditionRepository;

    private final CacheManager cacheManager;

    private final CommonConditionMapper commonConditionMapper;

    public CommonConditionService(
        CommonConditionRepository commonConditionRepository,
        CacheManager cacheManager,
        CommonConditionMapper commonConditionMapper
    ) {
        this.commonConditionRepository = commonConditionRepository;
        this.cacheManager = cacheManager;
        this.commonConditionMapper = commonConditionMapper;
    }

    /**
     * Save a commonCondition.
     *
     * @param commonConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public CommonConditionDTO save(CommonConditionDTO commonConditionDTO) {
        log.debug("Request to save CommonCondition : {}", commonConditionDTO);
        CommonCondition commonCondition = commonConditionMapper.toEntity(commonConditionDTO);
        this.saveOrUpdate(commonCondition);
        return commonConditionMapper.toDto(this.getById(commonCondition.getId()));
    }

    /**
     * Partially update a commonCondition.
     *
     * @param commonConditionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommonConditionDTO> partialUpdate(CommonConditionDTO commonConditionDTO) {
        log.debug("Request to partially update CommonCondition : {}", commonConditionDTO);

        return commonConditionRepository
            .findById(commonConditionDTO.getId())
            .map(
                existingCommonCondition -> {
                    commonConditionMapper.partialUpdate(existingCommonCondition, commonConditionDTO);
                    return existingCommonCondition;
                }
            )
            .map(commonConditionRepository::save)
            .map(commonConditionMapper::toDto);
    }

    /**
     * Get all the commonConditions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<CommonConditionDTO> findAll(Page<CommonCondition> pageable) {
        log.debug("Request to get all CommonConditions");
        return this.page(pageable).convert(commonConditionMapper::toDto);
    }

    /**
     * Get one commonCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommonConditionDTO> findOne(Long id) {
        log.debug("Request to get CommonCondition : {}", id);
        return Optional.ofNullable(commonConditionRepository.selectById(id)).map(commonConditionMapper::toDto);
    }

    /**
     * Delete the commonCondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonCondition : {}", id);
        commonConditionRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by commonCondition
     */
    public CommonConditionDTO updateByIgnoreSpecifiedFields(CommonConditionDTO changeCommonConditionDTO, Set<String> unchangedFields) {
        CommonConditionDTO commonConditionDTO = findOne(changeCommonConditionDTO.getId()).get();
        BeanUtil.copyProperties(changeCommonConditionDTO, commonConditionDTO, unchangedFields.toArray(new String[0]));
        commonConditionDTO = save(commonConditionDTO);
        return commonConditionDTO;
    }

    /**
     * Update specified fields by commonCondition
     */
    public CommonConditionDTO updateBySpecifiedFields(CommonConditionDTO changeCommonConditionDTO, Set<String> fieldNames) {
        UpdateWrapper<CommonCondition> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeCommonConditionDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeCommonConditionDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeCommonConditionDTO.getId()).get();
    }

    /**
     * Update specified field by commonCondition
     */
    public CommonConditionDTO updateBySpecifiedField(CommonConditionDTO changeCommonConditionDTO, String fieldName) {
        CommonConditionDTO updateDTO = new CommonConditionDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeCommonConditionDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeCommonConditionDTO, fieldName));
        this.updateEntity(commonConditionMapper.toEntity(updateDTO));
        return findOne(changeCommonConditionDTO.getId()).get();
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
