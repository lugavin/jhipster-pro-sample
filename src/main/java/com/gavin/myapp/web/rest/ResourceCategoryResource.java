package com.gavin.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.domain.ResourceCategory;
import com.gavin.myapp.repository.ResourceCategoryRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.ResourceCategoryQueryService;
import com.gavin.myapp.service.ResourceCategoryService;
import com.gavin.myapp.service.criteria.ResourceCategoryCriteria;
import com.gavin.myapp.service.dto.ResourceCategoryDTO;
import com.gavin.myapp.util.web.IPageUtil;
import com.gavin.myapp.util.web.PageableUtils;
import com.gavin.myapp.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
// import java.util.List;
// import java.util.ArrayList;
// import java.util.Objects;
// import java.util.Set;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

// import java.util.Optional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.gavin.myapp.domain.ResourceCategory}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class ResourceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryResource.class);

    private static final String ENTITY_NAME = "filesResourceCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceCategoryService resourceCategoryService;

    private final ResourceCategoryRepository resourceCategoryRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final ResourceCategoryQueryService resourceCategoryQueryService;

    public ResourceCategoryResource(
        ResourceCategoryService resourceCategoryService,
        ResourceCategoryRepository resourceCategoryRepository,
        CommonConditionQueryService commonConditionQueryService,
        ResourceCategoryQueryService resourceCategoryQueryService
    ) {
        this.resourceCategoryService = resourceCategoryService;
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.resourceCategoryQueryService = resourceCategoryQueryService;
    }

    /**
     * {@code POST  /resource-categories} : Create a new resourceCategory.
     *
     * @param resourceCategoryDTO the resourceCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceCategoryDTO, or with status {@code 400 (Bad Request)} if the resourceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-categories")
    public ResponseEntity<ResourceCategoryDTO> createResourceCategory(@Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceCategory : {}", resourceCategoryDTO);
        if (resourceCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceCategoryDTO result = resourceCategoryService.save(resourceCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/resource-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/:id} : Updates an existing resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/{id}")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceCategory : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceCategoryService.exists(ResourceCategory::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourceCategoryDTO result = resourceCategoryService.save(resourceCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resource-categories/:id} : Partial updates given fields of an existing resourceCategory, field will ignore if it is null.
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resource-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResourceCategoryDTO> partialUpdateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceCategoryDTO resourceCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceCategory partially : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceCategoryDTO> result = resourceCategoryService.partialUpdate(resourceCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-categories} : get all the resourceCategories.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceCategories in body.
     */
    @GetMapping("/resource-categories")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategories(
        ResourceCategoryCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get ResourceCategories by criteria: {}", criteria);
        IPage<ResourceCategoryDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    resourceCategoryQueryService.selectByCustomEntity(
                        listModelName,
                        criteria,
                        queryWrapper,
                        PageableUtils.toPage(pageable)
                    );
            } else {
                page = resourceCategoryQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = resourceCategoryQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = resourceCategoryQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /resource-categories/count} : count all the resourceCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/resource-categories/count")
    public ResponseEntity<Long> countResourceCategories(ResourceCategoryCriteria criteria) {
        log.debug("REST request to count ResourceCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(resourceCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /resource-categories/tree : get all the resourceCategories for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/resource-categories/tree")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategoriesofTree(Pageable pageable) {
        log.debug("REST request to get a page of ResourceCategories");
        IPage<ResourceCategoryDTO> page = resourceCategoryService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /resource-categories/{parentId}/tree : get all the resourceCategories for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/resource-categories/{parentId}/tree")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategoriesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ResourceCategories of parentId");
        List<ResourceCategoryDTO> list = resourceCategoryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /resource-categories/:id} : get the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-categories/{id}")
    public ResponseEntity<ResourceCategoryDTO> getResourceCategory(@PathVariable Long id) {
        log.debug("REST request to get ResourceCategory : {}", id);
        Optional<ResourceCategoryDTO> resourceCategoryDTO = resourceCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceCategoryDTO);
    }

    /**
     * GET  /resource-categories/export : export the resourceCategories.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the resourceCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resource-categories/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<ResourceCategoryDTO> page = resourceCategoryService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            ResourceCategoryDTO.class,
            page.getRecords()
        );
        File savefile = new File("export");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("export/personDTO_2018_09_10.xls");
        workbook.write(fos);
        fos.close();
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /resource-categories/import : import the resourceCategories from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the resourceCategoryDTO, or with status 404 (Not Found)
     */
    @PostMapping("/resource-categories/import")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //获得原始文件名;
        int pointIndex = fileRealName.lastIndexOf("."); //点号的位置
        String fileSuffix = fileRealName.substring(pointIndex); //截取文件后缀
        String fileNewName = UUID.randomUUID().toString(); //文件new名称时间戳
        String saveFileName = fileNewName.concat(fileSuffix); //文件存取名
        String filePath = "import";
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath, saveFileName);
        file.transferTo(savedFile);
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ResourceCategoryDTO> list = ExcelImportUtil.importExcel(savedFile, ResourceCategoryDTO.class, params);
        list.forEach(resourceCategoryService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /resource-categories/:id} : delete the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-categories/{id}")
    public ResponseEntity<Void> deleteResourceCategory(@PathVariable Long id) {
        log.debug("REST request to delete ResourceCategory : {}", id);
        resourceCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /resource-categories} : delete all the "ids" ResourceCategories.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-categories")
    public ResponseEntity<Void> deleteResourceCategoriesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ResourceCategories : {}", ids);
        if (ids != null) {
            ids.forEach(resourceCategoryService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /resource-categories/specified-fields} : Updates an existing resourceCategory by specified fields.
     *
     * @param resourceCategoryDTOAndSpecifiedFields the resourceCategoryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/specified-fields")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategoryBySpecifiedFields(
        @RequestBody ResourceCategoryDTOAndSpecifiedFields resourceCategoryDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceCategory : {}", resourceCategoryDTOAndSpecifiedFields);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryDTOAndSpecifiedFields.getResourceCategory();
        Set<String> specifiedFields = resourceCategoryDTOAndSpecifiedFields.getSpecifiedFields();
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceCategoryDTO result = resourceCategoryService.updateBySpecifiedFields(resourceCategoryDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/specified-field} : Updates an existing resourceCategory by specified field.
     *
     * @param resourceCategoryDTOAndSpecifiedFields the resourceCategoryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/specified-field")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategoryBySpecifiedField(
        @RequestBody ResourceCategoryDTOAndSpecifiedFields resourceCategoryDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceCategory : {}", resourceCategoryDTOAndSpecifiedFields);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryDTOAndSpecifiedFields.getResourceCategory();
        String fieldName = resourceCategoryDTOAndSpecifiedFields.getSpecifiedField();
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceCategoryDTO result = resourceCategoryService.updateBySpecifiedField(resourceCategoryDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class ResourceCategoryDTOAndSpecifiedFields {

        private ResourceCategoryDTO resourceCategory;
        private Set<String> specifiedFields;
        private String specifiedField;

        private ResourceCategoryDTO getResourceCategory() {
            return resourceCategory;
        }

        private void setResourceCategory(ResourceCategoryDTO resourceCategory) {
            this.resourceCategory = resourceCategory;
        }

        private Set<String> getSpecifiedFields() {
            return specifiedFields;
        }

        private void setSpecifiedFields(Set<String> specifiedFields) {
            this.specifiedFields = specifiedFields;
        }

        public String getSpecifiedField() {
            return specifiedField;
        }

        public void setSpecifiedField(String specifiedField) {
            this.specifiedField = specifiedField;
        }
    }
}
