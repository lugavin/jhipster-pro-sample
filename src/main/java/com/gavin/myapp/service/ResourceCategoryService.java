package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.ResourceCategory;
import com.gavin.myapp.repository.ResourceCategoryRepository;
import com.gavin.myapp.service.dto.ResourceCategoryDTO;
import com.gavin.myapp.service.mapper.ResourceCategoryMapper;
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
 * Service Implementation for managing {@link ResourceCategory}.
 */
@Service
@Transactional
public class ResourceCategoryService extends BaseServiceImpl<ResourceCategoryRepository, ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.UploadFile.class.getName() + ".category",
        com.gavin.myapp.domain.ResourceCategory.class.getName() + ".parent",
        com.gavin.myapp.domain.UploadImage.class.getName() + ".category",
        com.gavin.myapp.domain.ResourceCategory.class.getName() + ".children"
    );

    private final ResourceCategoryRepository resourceCategoryRepository;

    private final CacheManager cacheManager;

    private final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryService(
        ResourceCategoryRepository resourceCategoryRepository,
        CacheManager cacheManager,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.cacheManager = cacheManager;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Save a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceCategoryDTO save(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to save ResourceCategory : {}", resourceCategoryDTO);
        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);
        clearChildrenCache();
        this.saveOrUpdate(resourceCategory);
        // 更新缓存
        if (resourceCategory.getParent() != null) {
            resourceCategory.getParent().addChildren(resourceCategory);
        }
        return resourceCategoryMapper.toDto(this.getById(resourceCategory.getId()));
    }

    /**
     * Partially update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourceCategoryDTO> partialUpdate(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to partially update ResourceCategory : {}", resourceCategoryDTO);

        return resourceCategoryRepository
            .findById(resourceCategoryDTO.getId())
            .map(
                existingResourceCategory -> {
                    resourceCategoryMapper.partialUpdate(existingResourceCategory, resourceCategoryDTO);
                    return existingResourceCategory;
                }
            )
            .map(resourceCategoryRepository::save)
            .map(resourceCategoryMapper::toDto);
    }

    /**
     * Get all the resourceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<ResourceCategoryDTO> findAll(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories");
        return this.page(pageable).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Get all the resourceCategories for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<ResourceCategoryDTO> findAllTop(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories for parent is null");
        return resourceCategoryRepository
            .findAllByParentIsNull(pageable)
            .convert(
                resourceCategory -> {
                    Binder.bindRelations(resourceCategory, new String[] { "files", "children", "images", "parent" });
                    return resourceCategoryMapper.toDto(resourceCategory);
                }
            );
    }

    /**
     * Get all the resourceCategories for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ResourceCategoryDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ResourceCategories for parent is parentId");
        return resourceCategoryRepository
            .selectList(new LambdaUpdateWrapper<ResourceCategory>().eq(ResourceCategory::getParentId, parentId))
            .stream()
            .map(
                resourceCategory -> {
                    Binder.bindRelations(resourceCategory, new String[] { "files", "children", "images", "parent" });
                    return resourceCategoryMapper.toDto(resourceCategory);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one resourceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceCategoryDTO> findOne(Long id) {
        log.debug("Request to get ResourceCategory : {}", id);
        return Optional.ofNullable(resourceCategoryRepository.selectById(id)).map(resourceCategoryMapper::toDto);
    }

    /**
     * Delete the resourceCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceCategory : {}", id);
        ResourceCategory resourceCategory = resourceCategoryRepository.selectById(id);
        if (resourceCategory.getParent() != null) {
            resourceCategory.getParent().removeChildren(resourceCategory);
        }
        if (resourceCategory.getChildren() != null) {
            resourceCategory
                .getChildren()
                .forEach(
                    subResourceCategory -> {
                        subResourceCategory.setParent(null);
                    }
                );
        }
        resourceCategoryRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by resourceCategory
     */
    public ResourceCategoryDTO updateByIgnoreSpecifiedFields(ResourceCategoryDTO changeResourceCategoryDTO, Set<String> unchangedFields) {
        ResourceCategoryDTO resourceCategoryDTO = findOne(changeResourceCategoryDTO.getId()).get();
        BeanUtil.copyProperties(changeResourceCategoryDTO, resourceCategoryDTO, unchangedFields.toArray(new String[0]));
        resourceCategoryDTO = save(resourceCategoryDTO);
        return resourceCategoryDTO;
    }

    /**
     * Update specified fields by resourceCategory
     */
    public ResourceCategoryDTO updateBySpecifiedFields(ResourceCategoryDTO changeResourceCategoryDTO, Set<String> fieldNames) {
        UpdateWrapper<ResourceCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeResourceCategoryDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeResourceCategoryDTO.getId()).get();
    }

    /**
     * Update specified field by resourceCategory
     */
    public ResourceCategoryDTO updateBySpecifiedField(ResourceCategoryDTO changeResourceCategoryDTO, String fieldName) {
        ResourceCategoryDTO updateDTO = new ResourceCategoryDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeResourceCategoryDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName));
        this.updateEntity(resourceCategoryMapper.toEntity(updateDTO));
        return findOne(changeResourceCategoryDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.gavin.myapp.domain.ResourceCategory.class.getName() + ".children")).clear();
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
