package com.gavin.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.service.ApiPermissionQueryService;
import com.gavin.myapp.service.ApiPermissionService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.criteria.ApiPermissionCriteria;
import com.gavin.myapp.service.dto.ApiPermissionDTO;
import com.gavin.myapp.util.web.IPageUtil;
import com.gavin.myapp.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
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

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * REST controller for managing {@link com.gavin.myapp.domain.ApiPermission}.
 */
@RestController
@RequestMapping("/api")
public class ApiPermissionResource {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionResource.class);

    private static final String ENTITY_NAME = "systemApiPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiPermissionService apiPermissionService;

    private final ApiPermissionQueryService apiPermissionQueryService;

    private final CommonConditionQueryService commonConditionQueryService;

    public ApiPermissionResource(
        ApiPermissionService apiPermissionService,
        CommonConditionQueryService commonConditionQueryService,
        ApiPermissionQueryService apiPermissionQueryService
    ) {
        this.apiPermissionService = apiPermissionService;
        this.apiPermissionQueryService = apiPermissionQueryService;
        this.commonConditionQueryService = commonConditionQueryService;
    }

    /**
     * {@code POST  /api-permissions} : Create a new apiPermission.
     *
     * @param apiPermissionDTO the apiPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiPermissionDTO, or with status {@code 400 (Bad Request)} if the apiPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api-permissions")
    public ResponseEntity<ApiPermissionDTO> createApiPermission(@RequestBody ApiPermissionDTO apiPermissionDTO) throws URISyntaxException {
        log.debug("REST request to save ApiPermission : {}", apiPermissionDTO);
        if (apiPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiPermissionDTO result = apiPermissionService.save(apiPermissionDTO);
        return ResponseEntity
            .created(new URI("/api/api-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions} : Updates an existing apiPermission.
     *
     * @param apiPermissionDTO the apiPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/{id}")
    public ResponseEntity<ApiPermissionDTO> updateApiPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiPermissionDTO apiPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApiPermission : {}", apiPermissionDTO);
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        ApiPermissionDTO result = apiPermissionService.save(apiPermissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /api-permissions} : get all the apiPermissions.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiPermissions in body.
     */
    @GetMapping("/api-permissions")
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissions(
        ApiPermissionCriteria criteria,
        Page pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName
    ) {
        log.debug("REST request to get ApiPermissions by criteria: {}", criteria);
        IPage<ApiPermissionDTO> page;
        if (listModelName != null) {
            page = apiPermissionQueryService.selectByCustomEntity(listModelName, criteria, null, pageable);
        } else {
            page = apiPermissionQueryService.findByCriteria(criteria, pageable);
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /api-permissions/count} : count all the apiPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/api-permissions/count")
    public ResponseEntity<Long> countApiPermissions(ApiPermissionCriteria criteria) {
        log.debug("REST request to count ApiPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(apiPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /api-permissions/tree : get all the apiPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/api-permissions/tree")
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissionsofTree(Page pageable) {
        log.debug("REST request to get a page of ApiPermissions");
        IPage<ApiPermissionDTO> page = apiPermissionService.findAllTop(pageable);
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /api-permissions/generate : regenerate all api permissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/api-permissions/generate")
    public ResponseEntity<Void> generate() {
        log.debug("REST request to get a page of ApiPermissions");
        apiPermissionService.regenerateApiPermissions();
        return ResponseEntity.ok().build();
    }

    /**
     * {@code GET  /api-permissions/:id} : get the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api-permissions/{id}")
    public ResponseEntity<ApiPermissionDTO> getApiPermission(@PathVariable Long id) {
        log.debug("REST request to get ApiPermission : {}", id);
        Optional<ApiPermissionDTO> apiPermissionDTO = apiPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiPermissionDTO);
    }

    /**
     * GET  /api-permissions/export : export the apiPermissions.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the apiPermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/api-permissions/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<ApiPermissionDTO> page = apiPermissionService.findAll(null);
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            ApiPermissionDTO.class,
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
     * POST  /api-permissions/import : import the apiPermissions from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the apiPermissionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/api-permissions/import")
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
        List<ApiPermissionDTO> list = ExcelImportUtil.importExcel(savedFile, ApiPermissionDTO.class, params);
        list.forEach(apiPermissionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /api-permissions/:id} : delete the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-permissions/{id}")
    public ResponseEntity<Void> deleteApiPermission(@PathVariable Long id) {
        log.debug("REST request to delete ApiPermission : {}", id);
        apiPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /api-permissions} : delete all the "ids" ApiPermissions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-permissions")
    public ResponseEntity<Void> deleteApiPermissionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ApiPermissions : {}", ids);
        if (ids != null) {
            ids.forEach(apiPermissionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /api-permissions/specified-fields} : Updates an existing apiPermission by specified fields.
     *
     * @param apiPermissionDTOAndSpecifiedFields the apiPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/specified-fields")
    public ResponseEntity<ApiPermissionDTO> updateApiPermissionBySpecifiedFields(
        @RequestBody ApiPermissionDTOAndSpecifiedFields apiPermissionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update ApiPermission : {}", apiPermissionDTOAndSpecifiedFields);
        ApiPermissionDTO apiPermissionDTO = apiPermissionDTOAndSpecifiedFields.getApiPermission();
        Set<String> specifiedFields = apiPermissionDTOAndSpecifiedFields.getSpecifiedFields();
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiPermissionDTO result = apiPermissionService.updateBySpecifiedFields(apiPermissionDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions/specified-field} : Updates an existing apiPermission by specified field.
     *
     * @param apiPermissionDTOAndSpecifiedFields the apiPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/specified-field")
    public ResponseEntity<ApiPermissionDTO> updateApiPermissionBySpecifiedField(
        @RequestBody ApiPermissionDTOAndSpecifiedFields apiPermissionDTOAndSpecifiedFields,
        ApiPermissionCriteria criteria
    ) throws URISyntaxException {
        log.debug("REST request to update ApiPermission : {}", apiPermissionDTOAndSpecifiedFields);
        ApiPermissionDTO apiPermissionDTO = apiPermissionDTOAndSpecifiedFields.getApiPermission();
        String fieldName = apiPermissionDTOAndSpecifiedFields.getSpecifiedField();
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiPermissionDTO result = apiPermissionService.updateBySpecifiedField(apiPermissionDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class ApiPermissionDTOAndSpecifiedFields {

        private ApiPermissionDTO apiPermission;
        private Set<String> specifiedFields;
        private String specifiedField;

        private ApiPermissionDTO getApiPermission() {
            return apiPermission;
        }

        private void setApiPermission(ApiPermissionDTO apiPermission) {
            this.apiPermission = apiPermission;
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
