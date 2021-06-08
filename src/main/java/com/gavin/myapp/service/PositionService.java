package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.Position;
import com.gavin.myapp.repository.PositionRepository;
import com.gavin.myapp.service.dto.PositionDTO;
import com.gavin.myapp.service.mapper.PositionMapper;
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
 * Service Implementation for managing {@link Position}.
 */
@Service
@Transactional
public class PositionService extends BaseServiceImpl<PositionRepository, Position> {

    private final Logger log = LoggerFactory.getLogger(PositionService.class);

    private final PositionRepository positionRepository;

    private final CacheManager cacheManager;

    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, CacheManager cacheManager, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.cacheManager = cacheManager;
        this.positionMapper = positionMapper;
    }

    /**
     * Save a position.
     *
     * @param positionDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionDTO save(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);
        this.saveOrUpdate(position);
        return positionMapper.toDto(this.getById(position.getId()));
    }

    /**
     * Partially update a position.
     *
     * @param positionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionDTO> partialUpdate(PositionDTO positionDTO) {
        log.debug("Request to partially update Position : {}", positionDTO);

        return positionRepository
            .findById(positionDTO.getId())
            .map(
                existingPosition -> {
                    positionMapper.partialUpdate(existingPosition, positionDTO);
                    return existingPosition;
                }
            )
            .map(positionRepository::save)
            .map(positionMapper::toDto);
    }

    /**
     * Get all the positions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<PositionDTO> findAll(Page<Position> pageable) {
        log.debug("Request to get all Positions");
        return this.page(pageable).convert(positionMapper::toDto);
    }

    /**
     * Get one position by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionDTO> findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        return Optional.ofNullable(positionRepository.selectById(id)).map(positionMapper::toDto);
    }

    /**
     * Delete the position by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by position
     */
    public PositionDTO updateByIgnoreSpecifiedFields(PositionDTO changePositionDTO, Set<String> unchangedFields) {
        PositionDTO positionDTO = findOne(changePositionDTO.getId()).get();
        BeanUtil.copyProperties(changePositionDTO, positionDTO, unchangedFields.toArray(new String[0]));
        positionDTO = save(positionDTO);
        return positionDTO;
    }

    /**
     * Update specified fields by position
     */
    public PositionDTO updateBySpecifiedFields(PositionDTO changePositionDTO, Set<String> fieldNames) {
        UpdateWrapper<Position> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changePositionDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changePositionDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changePositionDTO.getId()).get();
    }

    /**
     * Update specified field by position
     */
    public PositionDTO updateBySpecifiedField(PositionDTO changePositionDTO, String fieldName) {
        PositionDTO updateDTO = new PositionDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changePositionDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changePositionDTO, fieldName));
        this.updateEntity(positionMapper.toEntity(updateDTO));
        return findOne(changePositionDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
