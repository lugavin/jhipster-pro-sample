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
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonTableQueryService;
import com.gavin.myapp.service.CommonTableService;
import com.gavin.myapp.service.criteria.CommonTableCriteria;
import com.gavin.myapp.service.dto.CommonTableDTO;
import com.gavin.myapp.util.web.IPageUtil;
import com.gavin.myapp.util.web.PageableUtils;
import com.gavin.myapp.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
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
 * REST controller for managing {@link com.gavin.myapp.domain.CommonTable}.
 */
@RestController
@RequestMapping("/api")
public class CommonTableResource {

    private final Logger log = LoggerFactory.getLogger(CommonTableResource.class);

    private static final String ENTITY_NAME = "modelConfigCommonTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommonTableService commonTableService;

    private final CommonTableQueryService commonTableQueryService;

    private final CommonConditionQueryService commonConditionQueryService;

    public CommonTableResource(
        CommonTableService commonTableService,
        CommonConditionQueryService commonConditionQueryService,
        CommonTableQueryService commonTableQueryService
    ) {
        this.commonTableService = commonTableService;
        this.commonConditionQueryService = commonConditionQueryService;
        this.commonTableQueryService = commonTableQueryService;
    }

    /**
     * {@code POST  /common-tables} : Create a new commonTable.
     *
     * @param commonTableDTO the commonTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonTableDTO, or with status {@code 400 (Bad Request)} if the commonTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/common-tables")
    public ResponseEntity<CommonTableDTO> createCommonTable(@Valid @RequestBody CommonTableDTO commonTableDTO) throws URISyntaxException {
        log.debug("REST request to save CommonTable : {}", commonTableDTO);
        if (commonTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new commonTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommonTableDTO result = commonTableService.save(commonTableDTO);
        return ResponseEntity
            .created(new URI("/api/common-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-tables} : Updates an existing commonTable.
     *
     * @param commonTableDTO the commonTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-tables/{id}")
    public ResponseEntity<CommonTableDTO> updateCommonTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommonTableDTO commonTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTable : {}", commonTableDTO);
        if (commonTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        CommonTableDTO result = commonTableService.save(commonTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-tables/:copyFromId} : copy from  an existing commonTableId.
     *
     * @param copyFromId the commonTableId to copy.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-tables/copy/{copyFromId}")
    public ResponseEntity<CommonTableDTO> updateCommonTable(@PathVariable Long copyFromId) throws URISyntaxException {
        log.debug("REST request to copy CommonTable from id : {}", copyFromId);
        CommonTableDTO result = commonTableService.copyFromId(copyFromId).get();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /common-tables} : get all the commonTables.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonTables in body.
     */
    @GetMapping("/common-tables")
    public ResponseEntity<List<CommonTableDTO>> getAllCommonTables(
        CommonTableCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get CommonTables by criteria: {}", criteria);
        IPage<CommonTableDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = commonTableQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonTableQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = commonTableQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonTableQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /common-tables/count} : count all the commonTables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/common-tables/count")
    public ResponseEntity<Long> countCommonTables(CommonTableCriteria criteria) {
        log.debug("REST request to count CommonTables by criteria: {}", criteria);
        return ResponseEntity.ok().body(commonTableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /common-tables/:id} : get the "id" commonTable.
     *
     * @param id the id of the commonTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-tables/{id}")
    public ResponseEntity<CommonTableDTO> getCommonTable(@PathVariable Long id) {
        log.debug("REST request to get CommonTable : {}", id);
        Optional<CommonTableDTO> commonTableDTO = commonTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonTableDTO);
    }

    /**
     * GET  /common-tables/export : export the commonTables.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonTableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-tables/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<CommonTableDTO> page = commonTableService.findAll(null);
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            CommonTableDTO.class,
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
     * POST  /common-tables/import : import the commonTables from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonTableDTO, or with status 404 (Not Found)
     */
    @PostMapping("/common-tables/import")
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
        List<CommonTableDTO> list = ExcelImportUtil.importExcel(savedFile, CommonTableDTO.class, params);
        list.forEach(commonTableService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /common-tables/:id} : delete the "id" commonTable.
     *
     * @param id the id of the commonTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-tables/{id}")
    public ResponseEntity<Void> deleteCommonTable(@PathVariable Long id) {
        log.debug("REST request to delete CommonTable : {}", id);
        commonTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /common-tables} : delete all the "ids" CommonTables.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-tables")
    public ResponseEntity<Void> deleteCommonTablesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete CommonTables : {}", ids);
        if (ids != null) {
            ids.forEach(commonTableService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/common-tables/creator/current-user")
    public ResponseEntity<List<CommonTableDTO>> findByCreatorIsCurrentUser() {
        log.debug("REST request to get CommonTable for current user. ");
        List<CommonTableDTO> result = commonTableService.findByCreatorIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /common-tables/specified-fields} : Updates an existing commonTable by specified fields.
     *
     * @param commonTableDTOAndSpecifiedFields the commonTableDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-tables/specified-fields")
    public ResponseEntity<CommonTableDTO> updateCommonTableBySpecifiedFields(
        @RequestBody CommonTableDTOAndSpecifiedFields commonTableDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTable : {}", commonTableDTOAndSpecifiedFields);
        CommonTableDTO commonTableDTO = commonTableDTOAndSpecifiedFields.getCommonTable();
        Set<String> specifiedFields = commonTableDTOAndSpecifiedFields.getSpecifiedFields();
        if (commonTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonTableDTO result = commonTableService.updateBySpecifiedFields(commonTableDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-tables/specified-field} : Updates an existing commonTable by specified field.
     *
     * @param commonTableDTOAndSpecifiedFields the commonTableDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-tables/specified-field")
    public ResponseEntity<CommonTableDTO> updateCommonTableBySpecifiedField(
        @RequestBody CommonTableDTOAndSpecifiedFields commonTableDTOAndSpecifiedFields,
        CommonTableCriteria criteria
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTable : {}", commonTableDTOAndSpecifiedFields);
        CommonTableDTO commonTableDTO = commonTableDTOAndSpecifiedFields.getCommonTable();
        String fieldName = commonTableDTOAndSpecifiedFields.getSpecifiedField();
        if (commonTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonTableDTO result = commonTableService.updateBySpecifiedField(commonTableDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /common-tables/entity-name/:entityName} : get the "entityName" commonTable.
     *
     * @param entityName the entityName of the commonTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-tables/entity-name/{entityName}")
    public ResponseEntity<CommonTableDTO> getCommonTable(@PathVariable String entityName) {
        log.debug("REST request to get CommonTable by entityName : {}", entityName);
        Optional<CommonTableDTO> commonTableDTO = commonTableService.findOneByEntityName(entityName);
        return ResponseUtil.wrapOrNotFound(commonTableDTO);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class CommonTableDTOAndSpecifiedFields {

        private CommonTableDTO commonTable;
        private Set<String> specifiedFields;
        private String specifiedField;

        private CommonTableDTO getCommonTable() {
            return commonTable;
        }

        private void setCommonTable(CommonTableDTO commonTable) {
            this.commonTable = commonTable;
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
