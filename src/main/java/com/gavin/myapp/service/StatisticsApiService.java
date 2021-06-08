package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.StatisticsApi;
import com.gavin.myapp.repository.StatisticsApiRepository;
import com.gavin.myapp.service.dto.StatisticsApiDTO;
import com.gavin.myapp.service.mapper.StatisticsApiMapper;
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
 * Service Implementation for managing {@link StatisticsApi}.
 */
@Service
@Transactional
public class StatisticsApiService extends BaseServiceImpl<StatisticsApiRepository, StatisticsApi> {

    private final Logger log = LoggerFactory.getLogger(StatisticsApiService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.CommonTable.class.getName() + ".statisticsApi",
        com.gavin.myapp.domain.User.class.getName() + ".statisticsApi",
        com.gavin.myapp.domain.User.class.getName() + ".statisticsApi"
    );

    private final StatisticsApiRepository statisticsApiRepository;

    private final CacheManager cacheManager;

    private final StatisticsApiMapper statisticsApiMapper;

    public StatisticsApiService(
        StatisticsApiRepository statisticsApiRepository,
        CacheManager cacheManager,
        StatisticsApiMapper statisticsApiMapper
    ) {
        this.statisticsApiRepository = statisticsApiRepository;
        this.cacheManager = cacheManager;
        this.statisticsApiMapper = statisticsApiMapper;
    }

    /**
     * Save a statisticsApi.
     *
     * @param statisticsApiDTO the entity to save.
     * @return the persisted entity.
     */
    public StatisticsApiDTO save(StatisticsApiDTO statisticsApiDTO) {
        log.debug("Request to save StatisticsApi : {}", statisticsApiDTO);
        StatisticsApi statisticsApi = statisticsApiMapper.toEntity(statisticsApiDTO);
        this.saveOrUpdate(statisticsApi);
        return statisticsApiMapper.toDto(this.getById(statisticsApi.getId()));
    }

    /**
     * Partially update a statisticsApi.
     *
     * @param statisticsApiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StatisticsApiDTO> partialUpdate(StatisticsApiDTO statisticsApiDTO) {
        log.debug("Request to partially update StatisticsApi : {}", statisticsApiDTO);

        return statisticsApiRepository
            .findById(statisticsApiDTO.getId())
            .map(
                existingStatisticsApi -> {
                    statisticsApiMapper.partialUpdate(existingStatisticsApi, statisticsApiDTO);
                    return existingStatisticsApi;
                }
            )
            .map(statisticsApiRepository::save)
            .map(statisticsApiMapper::toDto);
    }

    /**
     * Get all the statisticsApis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<StatisticsApiDTO> findAll(Page<StatisticsApi> pageable) {
        log.debug("Request to get all StatisticsApis");
        return this.page(pageable).convert(statisticsApiMapper::toDto);
    }

    /**
     * Get one statisticsApi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StatisticsApiDTO> findOne(Long id) {
        log.debug("Request to get StatisticsApi : {}", id);
        return Optional.ofNullable(statisticsApiRepository.selectById(id)).map(statisticsApiMapper::toDto);
    }

    /**
     * Delete the statisticsApi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StatisticsApi : {}", id);
        statisticsApiRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StatisticsApiDTO> findByCreatorIsCurrentUser() {
        return statisticsApiMapper.toDto(statisticsApiRepository.findByCreatorIsCurrentUser());
    }

    @Transactional(readOnly = true)
    public List<StatisticsApiDTO> findByModifierIsCurrentUser() {
        return statisticsApiMapper.toDto(statisticsApiRepository.findByModifierIsCurrentUser());
    }

    /**
     * Update ignore specified fields by statisticsApi
     */
    public StatisticsApiDTO updateByIgnoreSpecifiedFields(StatisticsApiDTO changeStatisticsApiDTO, Set<String> unchangedFields) {
        StatisticsApiDTO statisticsApiDTO = findOne(changeStatisticsApiDTO.getId()).get();
        BeanUtil.copyProperties(changeStatisticsApiDTO, statisticsApiDTO, unchangedFields.toArray(new String[0]));
        statisticsApiDTO = save(statisticsApiDTO);
        return statisticsApiDTO;
    }

    /**
     * Update specified fields by statisticsApi
     */
    public StatisticsApiDTO updateBySpecifiedFields(StatisticsApiDTO changeStatisticsApiDTO, Set<String> fieldNames) {
        UpdateWrapper<StatisticsApi> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeStatisticsApiDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeStatisticsApiDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeStatisticsApiDTO.getId()).get();
    }

    /**
     * Update specified field by statisticsApi
     */
    public StatisticsApiDTO updateBySpecifiedField(StatisticsApiDTO changeStatisticsApiDTO, String fieldName) {
        StatisticsApiDTO updateDTO = new StatisticsApiDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeStatisticsApiDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeStatisticsApiDTO, fieldName));
        this.updateEntity(statisticsApiMapper.toEntity(updateDTO));
        return findOne(changeStatisticsApiDTO.getId()).get();
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
