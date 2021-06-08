package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.BusinessType;
import com.gavin.myapp.repository.BusinessTypeRepository;
import com.gavin.myapp.service.dto.BusinessTypeDTO;
import com.gavin.myapp.service.mapper.BusinessTypeMapper;
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
 * Service Implementation for managing {@link BusinessType}.
 */
@Service
@Transactional
public class BusinessTypeService extends BaseServiceImpl<BusinessTypeRepository, BusinessType> {

    private final Logger log = LoggerFactory.getLogger(BusinessTypeService.class);

    private final BusinessTypeRepository businessTypeRepository;

    private final CacheManager cacheManager;

    private final BusinessTypeMapper businessTypeMapper;

    public BusinessTypeService(
        BusinessTypeRepository businessTypeRepository,
        CacheManager cacheManager,
        BusinessTypeMapper businessTypeMapper
    ) {
        this.businessTypeRepository = businessTypeRepository;
        this.cacheManager = cacheManager;
        this.businessTypeMapper = businessTypeMapper;
    }

    /**
     * Save a businessType.
     *
     * @param businessTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessTypeDTO save(BusinessTypeDTO businessTypeDTO) {
        log.debug("Request to save BusinessType : {}", businessTypeDTO);
        BusinessType businessType = businessTypeMapper.toEntity(businessTypeDTO);
        this.saveOrUpdate(businessType);
        return businessTypeMapper.toDto(this.getById(businessType.getId()));
    }

    /**
     * Partially update a businessType.
     *
     * @param businessTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessTypeDTO> partialUpdate(BusinessTypeDTO businessTypeDTO) {
        log.debug("Request to partially update BusinessType : {}", businessTypeDTO);

        return businessTypeRepository
            .findById(businessTypeDTO.getId())
            .map(
                existingBusinessType -> {
                    businessTypeMapper.partialUpdate(existingBusinessType, businessTypeDTO);
                    return existingBusinessType;
                }
            )
            .map(businessTypeRepository::save)
            .map(businessTypeMapper::toDto);
    }

    /**
     * Get all the businessTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<BusinessTypeDTO> findAll(Page<BusinessType> pageable) {
        log.debug("Request to get all BusinessTypes");
        return this.page(pageable).convert(businessTypeMapper::toDto);
    }

    /**
     * Get one businessType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessTypeDTO> findOne(Long id) {
        log.debug("Request to get BusinessType : {}", id);
        return Optional.ofNullable(businessTypeRepository.selectById(id)).map(businessTypeMapper::toDto);
    }

    /**
     * Delete the businessType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessType : {}", id);
        businessTypeRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by businessType
     */
    public BusinessTypeDTO updateByIgnoreSpecifiedFields(BusinessTypeDTO changeBusinessTypeDTO, Set<String> unchangedFields) {
        BusinessTypeDTO businessTypeDTO = findOne(changeBusinessTypeDTO.getId()).get();
        BeanUtil.copyProperties(changeBusinessTypeDTO, businessTypeDTO, unchangedFields.toArray(new String[0]));
        businessTypeDTO = save(businessTypeDTO);
        return businessTypeDTO;
    }

    /**
     * Update specified fields by businessType
     */
    public BusinessTypeDTO updateBySpecifiedFields(BusinessTypeDTO changeBusinessTypeDTO, Set<String> fieldNames) {
        UpdateWrapper<BusinessType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeBusinessTypeDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeBusinessTypeDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeBusinessTypeDTO.getId()).get();
    }

    /**
     * Update specified field by businessType
     */
    public BusinessTypeDTO updateBySpecifiedField(BusinessTypeDTO changeBusinessTypeDTO, String fieldName) {
        BusinessTypeDTO updateDTO = new BusinessTypeDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeBusinessTypeDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeBusinessTypeDTO, fieldName));
        this.updateEntity(businessTypeMapper.toEntity(updateDTO));
        return findOne(changeBusinessTypeDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
