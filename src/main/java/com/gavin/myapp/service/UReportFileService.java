package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.UReportFile;
import com.gavin.myapp.repository.UReportFileRepository;
import com.gavin.myapp.service.dto.UReportFileDTO;
import com.gavin.myapp.service.mapper.UReportFileMapper;
import java.beans.PropertyDescriptor;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link UReportFile}.
 */
@Service
@Transactional
public class UReportFileService extends BaseServiceImpl<UReportFileRepository, UReportFile> {

    private final Logger log = LoggerFactory.getLogger(UReportFileService.class);
    private final List<String> relationCacheNames = Arrays.asList(com.gavin.myapp.domain.CommonTable.class.getName() + ".uReportFile");

    private final UReportFileRepository uReportFileRepository;

    private final CacheManager cacheManager;

    private final UReportFileMapper uReportFileMapper;

    public UReportFileService(UReportFileRepository uReportFileRepository, CacheManager cacheManager, UReportFileMapper uReportFileMapper) {
        this.uReportFileRepository = uReportFileRepository;
        this.cacheManager = cacheManager;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Save a uReportFile.
     *
     * @param uReportFileDTO the entity to save.
     * @return the persisted entity.
     */
    public UReportFileDTO save(UReportFileDTO uReportFileDTO) {
        log.debug("Request to save UReportFile : {}", uReportFileDTO);
        UReportFile uReportFile = uReportFileMapper.toEntity(uReportFileDTO);
        this.saveOrUpdate(uReportFile);
        return uReportFileMapper.toDto(this.getById(uReportFile.getId()));
    }

    /**
     * Partially update a uReportFile.
     *
     * @param uReportFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UReportFileDTO> partialUpdate(UReportFileDTO uReportFileDTO) {
        log.debug("Request to partially update UReportFile : {}", uReportFileDTO);

        return uReportFileRepository
            .findById(uReportFileDTO.getId())
            .map(
                existingAdministrativeDivision -> {
                    uReportFileMapper.partialUpdate(existingAdministrativeDivision, uReportFileDTO);
                    return existingAdministrativeDivision;
                }
            )
            .map(uReportFileRepository::save)
            .map(uReportFileMapper::toDto);
    }

    /**
     * Get all the uReportFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<UReportFileDTO> findAll(IPage<UReportFile> pageable) {
        log.debug("Request to get all UReportFiles");
        return this.page(pageable).convert(uReportFileMapper::toDto);
    }

    /**
     * Get one uReportFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UReportFileDTO> findOne(Long id) {
        log.debug("Request to get UReportFile : {}", id);
        return Optional.ofNullable(uReportFileRepository.selectById(id)).map(uReportFileMapper::toDto);
    }

    /**
     * Delete the uReportFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UReportFile : {}", id);
        uReportFileRepository.deleteById(id);
    }

    /**
     * Delete the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public void deleteByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        uReportFileRepository.deleteByName(name);
    }

    /**
     * Get the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public Optional<UReportFileDTO> getByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        return uReportFileRepository.getByName(name).map(uReportFileMapper::toDto);
    }

    /**
     * Update ignore specified fields by uReportFile
     */
    public UReportFileDTO updateByIgnoreSpecifiedFields(UReportFileDTO changeUReportFileDTO, Set<String> unchangedFields) {
        UReportFileDTO uReportFileDTO = findOne(changeUReportFileDTO.getId()).get();
        BeanUtil.copyProperties(changeUReportFileDTO, uReportFileDTO, unchangedFields.toArray(new String[0]));
        uReportFileDTO = save(uReportFileDTO);
        return uReportFileDTO;
    }

    /**
     * Update specified fields by uReportFile
     */
    public UReportFileDTO updateBySpecifiedFields(UReportFileDTO changeUReportFileDTO, Set<String> fieldNames) {
        UReportFileDTO uReportFileDTO = findOne(changeUReportFileDTO.getId()).get();
        UReportFileDTO finalUReportFileDTO = uReportFileDTO;
        fieldNames.forEach(
            fieldName -> {
                BeanUtil.setFieldValue(finalUReportFileDTO, fieldName, BeanUtil.getFieldValue(changeUReportFileDTO, fieldName));
            }
        );
        uReportFileDTO = save(finalUReportFileDTO);
        return uReportFileDTO;
    }

    /**
     * Update specified field by uReportFile
     */
    public UReportFileDTO updateBySpecifiedField(UReportFileDTO changeUReportFileDTO, String fieldName) {
        UReportFileDTO uReportFileDTO = findOne(changeUReportFileDTO.getId()).get();
        BeanUtil.setFieldValue(uReportFileDTO, fieldName, BeanUtil.getFieldValue(changeUReportFileDTO, fieldName));
        uReportFileDTO = save(uReportFileDTO);
        return uReportFileDTO;
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
