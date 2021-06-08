package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.AdministrativeDivision;
import com.gavin.myapp.repository.AdministrativeDivisionRepository;
import com.gavin.myapp.service.dto.AdministrativeDivisionDTO;
import com.gavin.myapp.service.mapper.AdministrativeDivisionMapper;
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
 * Service Implementation for managing {@link AdministrativeDivision}.
 */
@Service
@Transactional
public class AdministrativeDivisionService extends BaseServiceImpl<AdministrativeDivisionRepository, AdministrativeDivision> {

    private final Logger log = LoggerFactory.getLogger(AdministrativeDivisionService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.AdministrativeDivision.class.getName() + ".parent",
        com.gavin.myapp.domain.AdministrativeDivision.class.getName() + ".children"
    );

    private final AdministrativeDivisionRepository administrativeDivisionRepository;

    private final CacheManager cacheManager;

    private final AdministrativeDivisionMapper administrativeDivisionMapper;

    public AdministrativeDivisionService(
        AdministrativeDivisionRepository administrativeDivisionRepository,
        CacheManager cacheManager,
        AdministrativeDivisionMapper administrativeDivisionMapper
    ) {
        this.administrativeDivisionRepository = administrativeDivisionRepository;
        this.cacheManager = cacheManager;
        this.administrativeDivisionMapper = administrativeDivisionMapper;
    }

    /**
     * Save a administrativeDivision.
     *
     * @param administrativeDivisionDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministrativeDivisionDTO save(AdministrativeDivisionDTO administrativeDivisionDTO) {
        log.debug("Request to save AdministrativeDivision : {}", administrativeDivisionDTO);
        AdministrativeDivision administrativeDivision = administrativeDivisionMapper.toEntity(administrativeDivisionDTO);
        clearChildrenCache();
        this.saveOrUpdate(administrativeDivision);
        // 更新缓存
        if (administrativeDivision.getParent() != null) {
            administrativeDivision.getParent().addChildren(administrativeDivision);
        }
        return administrativeDivisionMapper.toDto(this.getById(administrativeDivision.getId()));
    }

    /**
     * Partially update a administrativeDivision.
     *
     * @param administrativeDivisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministrativeDivisionDTO> partialUpdate(AdministrativeDivisionDTO administrativeDivisionDTO) {
        log.debug("Request to partially update AdministrativeDivision : {}", administrativeDivisionDTO);

        return administrativeDivisionRepository
            .findById(administrativeDivisionDTO.getId())
            .map(
                existingAdministrativeDivision -> {
                    administrativeDivisionMapper.partialUpdate(existingAdministrativeDivision, administrativeDivisionDTO);
                    return existingAdministrativeDivision;
                }
            )
            .map(administrativeDivisionRepository::save)
            .map(administrativeDivisionMapper::toDto);
    }

    /**
     * Get all the administrativeDivisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<AdministrativeDivisionDTO> findAll(Page<AdministrativeDivision> pageable) {
        log.debug("Request to get all AdministrativeDivisions");
        return this.page(pageable).convert(administrativeDivisionMapper::toDto);
    }

    /**
     * Get all the administrativeDivisions for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<AdministrativeDivisionDTO> findAllTop(Page<AdministrativeDivision> pageable) {
        log.debug("Request to get all AdministrativeDivisions for parent is null");
        return administrativeDivisionRepository
            .findAllByParentIsNull(pageable)
            .convert(
                administrativeDivision -> {
                    Binder.bindRelations(administrativeDivision, new String[] { "children", "parent" });
                    return administrativeDivisionMapper.toDto(administrativeDivision);
                }
            );
    }

    /**
     * Get all the administrativeDivisions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<AdministrativeDivisionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all AdministrativeDivisions for parent is parentId");
        return administrativeDivisionRepository
            .selectList(new LambdaUpdateWrapper<AdministrativeDivision>().eq(AdministrativeDivision::getParentId, parentId))
            .stream()
            .map(
                administrativeDivision -> {
                    Binder.bindRelations(administrativeDivision, new String[] { "children", "parent" });
                    return administrativeDivisionMapper.toDto(administrativeDivision);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one administrativeDivision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministrativeDivisionDTO> findOne(Long id) {
        log.debug("Request to get AdministrativeDivision : {}", id);
        return Optional.ofNullable(administrativeDivisionRepository.selectById(id)).map(administrativeDivisionMapper::toDto);
    }

    /**
     * Delete the administrativeDivision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdministrativeDivision : {}", id);
        AdministrativeDivision administrativeDivision = administrativeDivisionRepository.selectById(id);
        if (administrativeDivision.getParent() != null) {
            administrativeDivision.getParent().removeChildren(administrativeDivision);
        }
        if (administrativeDivision.getChildren() != null) {
            administrativeDivision
                .getChildren()
                .forEach(
                    subAdministrativeDivision -> {
                        subAdministrativeDivision.setParent(null);
                    }
                );
        }
        administrativeDivisionRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by administrativeDivision
     */
    public AdministrativeDivisionDTO updateByIgnoreSpecifiedFields(
        AdministrativeDivisionDTO changeAdministrativeDivisionDTO,
        Set<String> unchangedFields
    ) {
        AdministrativeDivisionDTO administrativeDivisionDTO = findOne(changeAdministrativeDivisionDTO.getId()).get();
        BeanUtil.copyProperties(changeAdministrativeDivisionDTO, administrativeDivisionDTO, unchangedFields.toArray(new String[0]));
        administrativeDivisionDTO = save(administrativeDivisionDTO);
        return administrativeDivisionDTO;
    }

    /**
     * Update specified fields by administrativeDivision
     */
    public AdministrativeDivisionDTO updateBySpecifiedFields(
        AdministrativeDivisionDTO changeAdministrativeDivisionDTO,
        Set<String> fieldNames
    ) {
        UpdateWrapper<AdministrativeDivision> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAdministrativeDivisionDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeAdministrativeDivisionDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeAdministrativeDivisionDTO.getId()).get();
    }

    /**
     * Update specified field by administrativeDivision
     */
    public AdministrativeDivisionDTO updateBySpecifiedField(AdministrativeDivisionDTO changeAdministrativeDivisionDTO, String fieldName) {
        AdministrativeDivisionDTO updateDTO = new AdministrativeDivisionDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeAdministrativeDivisionDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeAdministrativeDivisionDTO, fieldName));
        this.updateEntity(administrativeDivisionMapper.toEntity(updateDTO));
        return findOne(changeAdministrativeDivisionDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.gavin.myapp.domain.AdministrativeDivision.class.getName() + ".children")).clear();
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
