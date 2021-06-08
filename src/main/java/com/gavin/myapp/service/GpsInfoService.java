package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.GpsInfo;
import com.gavin.myapp.repository.GpsInfoRepository;
import com.gavin.myapp.service.dto.GpsInfoDTO;
import com.gavin.myapp.service.mapper.GpsInfoMapper;
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
 * Service Implementation for managing {@link GpsInfo}.
 */
@Service
@Transactional
public class GpsInfoService extends BaseServiceImpl<GpsInfoRepository, GpsInfo> {

    private final Logger log = LoggerFactory.getLogger(GpsInfoService.class);

    private final GpsInfoRepository gpsInfoRepository;

    private final CacheManager cacheManager;

    private final GpsInfoMapper gpsInfoMapper;

    public GpsInfoService(GpsInfoRepository gpsInfoRepository, CacheManager cacheManager, GpsInfoMapper gpsInfoMapper) {
        this.gpsInfoRepository = gpsInfoRepository;
        this.cacheManager = cacheManager;
        this.gpsInfoMapper = gpsInfoMapper;
    }

    /**
     * Save a gpsInfo.
     *
     * @param gpsInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public GpsInfoDTO save(GpsInfoDTO gpsInfoDTO) {
        log.debug("Request to save GpsInfo : {}", gpsInfoDTO);
        GpsInfo gpsInfo = gpsInfoMapper.toEntity(gpsInfoDTO);
        this.saveOrUpdate(gpsInfo);
        return gpsInfoMapper.toDto(this.getById(gpsInfo.getId()));
    }

    /**
     * Partially update a gpsInfo.
     *
     * @param gpsInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GpsInfoDTO> partialUpdate(GpsInfoDTO gpsInfoDTO) {
        log.debug("Request to partially update GpsInfo : {}", gpsInfoDTO);

        return gpsInfoRepository
            .findById(gpsInfoDTO.getId())
            .map(
                existingGpsInfo -> {
                    gpsInfoMapper.partialUpdate(existingGpsInfo, gpsInfoDTO);
                    return existingGpsInfo;
                }
            )
            .map(gpsInfoRepository::save)
            .map(gpsInfoMapper::toDto);
    }

    /**
     * Get all the gpsInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<GpsInfoDTO> findAll(Page<GpsInfo> pageable) {
        log.debug("Request to get all GpsInfos");
        return this.page(pageable).convert(gpsInfoMapper::toDto);
    }

    /**
     * Get one gpsInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GpsInfoDTO> findOne(Long id) {
        log.debug("Request to get GpsInfo : {}", id);
        return Optional.ofNullable(gpsInfoRepository.selectById(id)).map(gpsInfoMapper::toDto);
    }

    /**
     * Delete the gpsInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GpsInfo : {}", id);
        gpsInfoRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by gpsInfo
     */
    public GpsInfoDTO updateByIgnoreSpecifiedFields(GpsInfoDTO changeGpsInfoDTO, Set<String> unchangedFields) {
        GpsInfoDTO gpsInfoDTO = findOne(changeGpsInfoDTO.getId()).get();
        BeanUtil.copyProperties(changeGpsInfoDTO, gpsInfoDTO, unchangedFields.toArray(new String[0]));
        gpsInfoDTO = save(gpsInfoDTO);
        return gpsInfoDTO;
    }

    /**
     * Update specified fields by gpsInfo
     */
    public GpsInfoDTO updateBySpecifiedFields(GpsInfoDTO changeGpsInfoDTO, Set<String> fieldNames) {
        UpdateWrapper<GpsInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeGpsInfoDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeGpsInfoDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeGpsInfoDTO.getId()).get();
    }

    /**
     * Update specified field by gpsInfo
     */
    public GpsInfoDTO updateBySpecifiedField(GpsInfoDTO changeGpsInfoDTO, String fieldName) {
        GpsInfoDTO updateDTO = new GpsInfoDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeGpsInfoDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeGpsInfoDTO, fieldName));
        this.updateEntity(gpsInfoMapper.toEntity(updateDTO));
        return findOne(changeGpsInfoDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
