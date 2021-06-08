package com.gavin.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.domain.Department;
import com.gavin.myapp.repository.DepartmentRepository;
import com.gavin.myapp.service.dto.DepartmentDTO;
import com.gavin.myapp.service.mapper.DepartmentMapper;
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
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentService extends BaseServiceImpl<DepartmentRepository, Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.gavin.myapp.domain.Department.class.getName() + ".parent",
        com.gavin.myapp.domain.Authority.class.getName() + ".departments",
        com.gavin.myapp.domain.Department.class.getName() + ".children",
        com.gavin.myapp.domain.User.class.getName() + ".department"
    );

    private final DepartmentRepository departmentRepository;

    private final CacheManager cacheManager;

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, CacheManager cacheManager, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.cacheManager = cacheManager;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Save a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        log.debug("Request to save Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        clearChildrenCache();
        this.saveOrUpdate(department);
        // 更新缓存
        if (department.getParent() != null) {
            department.getParent().addChildren(department);
        }
        return departmentMapper.toDto(this.getById(department.getId()));
    }

    /**
     * Partially update a department.
     *
     * @param departmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartmentDTO> partialUpdate(DepartmentDTO departmentDTO) {
        log.debug("Request to partially update Department : {}", departmentDTO);

        return departmentRepository
            .findById(departmentDTO.getId())
            .map(
                existingDepartment -> {
                    departmentMapper.partialUpdate(existingDepartment, departmentDTO);
                    return existingDepartment;
                }
            )
            .map(departmentRepository::save)
            .map(departmentMapper::toDto);
    }

    /**
     * Get all the departments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentDTO> findAll(Page<Department> pageable) {
        log.debug("Request to get all Departments");
        return this.page(pageable).convert(departmentMapper::toDto);
    }

    /**
     * Get all the departments for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<DepartmentDTO> findAllTop(Page<Department> pageable) {
        log.debug("Request to get all Departments for parent is null");
        return departmentRepository
            .findAllByParentIsNull(pageable)
            .convert(
                department -> {
                    Binder.bindRelations(department, new String[] { "children", "authorities", "parent", "users" });
                    return departmentMapper.toDto(department);
                }
            );
    }

    /**
     * Get all the departments for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<DepartmentDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Departments for parent is parentId");
        return departmentRepository
            .selectList(new LambdaUpdateWrapper<Department>().eq(Department::getParentId, parentId))
            .stream()
            .map(
                department -> {
                    Binder.bindRelations(department, new String[] { "children", "authorities", "parent", "users" });
                    return departmentMapper.toDto(department);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get all the departments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public IPage<DepartmentDTO> findAllWithEagerRelationships(Page<Department> pageable) {
        IPage<Department> result = departmentRepository.selectPage(pageable, null);
        Binder.bindRelations(result.getRecords());
        return result.convert(departmentMapper::toDto);
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return Optional
            .ofNullable(departmentRepository.selectById(id))
            .map(
                department -> {
                    Binder.bindRelations(department);
                    return department;
                }
            )
            .map(departmentMapper::toDto);
    }

    /**
     * Delete the department by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        Department department = departmentRepository.selectById(id);
        if (department.getParent() != null) {
            department.getParent().removeChildren(department);
        }
        if (department.getChildren() != null) {
            department
                .getChildren()
                .forEach(
                    subDepartment -> {
                        subDepartment.setParent(null);
                    }
                );
        }
        departmentRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by department
     */
    public DepartmentDTO updateByIgnoreSpecifiedFields(DepartmentDTO changeDepartmentDTO, Set<String> unchangedFields) {
        DepartmentDTO departmentDTO = findOne(changeDepartmentDTO.getId()).get();
        BeanUtil.copyProperties(changeDepartmentDTO, departmentDTO, unchangedFields.toArray(new String[0]));
        departmentDTO = save(departmentDTO);
        return departmentDTO;
    }

    /**
     * Update specified fields by department
     */
    public DepartmentDTO updateBySpecifiedFields(DepartmentDTO changeDepartmentDTO, Set<String> fieldNames) {
        UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeDepartmentDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(fieldName, BeanUtil.getFieldValue(changeDepartmentDTO, fieldName));
            }
        );
        this.update(updateWrapper);
        return findOne(changeDepartmentDTO.getId()).get();
    }

    /**
     * Update specified field by department
     */
    public DepartmentDTO updateBySpecifiedField(DepartmentDTO changeDepartmentDTO, String fieldName) {
        DepartmentDTO updateDTO = new DepartmentDTO();
        BeanUtil.setFieldValue(updateDTO, "id", changeDepartmentDTO.getId());
        BeanUtil.setFieldValue(updateDTO, fieldName, BeanUtil.getFieldValue(changeDepartmentDTO, fieldName));
        this.updateEntity(departmentMapper.toEntity(updateDTO));
        return findOne(changeDepartmentDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.gavin.myapp.domain.Department.class.getName() + ".children")).clear();
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
