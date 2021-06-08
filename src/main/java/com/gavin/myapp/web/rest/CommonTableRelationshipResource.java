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
import com.gavin.myapp.domain.CommonTableRelationship;
import com.gavin.myapp.repository.CommonTableRelationshipRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonTableRelationshipQueryService;
import com.gavin.myapp.service.CommonTableRelationshipService;
import com.gavin.myapp.service.criteria.CommonTableRelationshipCriteria;
import com.gavin.myapp.service.dto.CommonTableRelationshipDTO;
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

 * 管理实体{@link com.gavin.myapp.domain.CommonTableRelationship}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class CommonTableRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(CommonTableRelationshipResource.class);

    private static final String ENTITY_NAME = "modelConfigCommonTableRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommonTableRelationshipService commonTableRelationshipService;

    private final CommonTableRelationshipRepository commonTableRelationshipRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final CommonTableRelationshipQueryService commonTableRelationshipQueryService;

    public CommonTableRelationshipResource(
        CommonTableRelationshipService commonTableRelationshipService,
        CommonTableRelationshipRepository commonTableRelationshipRepository,
        CommonConditionQueryService commonConditionQueryService,
        CommonTableRelationshipQueryService commonTableRelationshipQueryService
    ) {
        this.commonTableRelationshipService = commonTableRelationshipService;
        this.commonTableRelationshipRepository = commonTableRelationshipRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.commonTableRelationshipQueryService = commonTableRelationshipQueryService;
    }

    /**
     * {@code POST  /common-table-relationships} : Create a new commonTableRelationship.
     *
     * @param commonTableRelationshipDTO the commonTableRelationshipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonTableRelationshipDTO, or with status {@code 400 (Bad Request)} if the commonTableRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/common-table-relationships")
    public ResponseEntity<CommonTableRelationshipDTO> createCommonTableRelationship(
        @Valid @RequestBody CommonTableRelationshipDTO commonTableRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CommonTableRelationship : {}", commonTableRelationshipDTO);
        if (commonTableRelationshipDTO.getId() != null) {
            throw new BadRequestAlertException("A new commonTableRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommonTableRelationshipDTO result = commonTableRelationshipService.save(commonTableRelationshipDTO);
        return ResponseEntity
            .created(new URI("/api/common-table-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-table-relationships/:id} : Updates an existing commonTableRelationship.
     *
     * @param id the id of the commonTableRelationshipDTO to save.
     * @param commonTableRelationshipDTO the commonTableRelationshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableRelationshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-table-relationships/{id}")
    public ResponseEntity<CommonTableRelationshipDTO> updateCommonTableRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommonTableRelationshipDTO commonTableRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTableRelationship : {}, {}", id, commonTableRelationshipDTO);
        if (commonTableRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonTableRelationshipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonTableRelationshipService.exists(CommonTableRelationship::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommonTableRelationshipDTO result = commonTableRelationshipService.save(commonTableRelationshipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonTableRelationshipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /common-table-relationships/:id} : Partial updates given fields of an existing commonTableRelationship, field will ignore if it is null.
     *
     * @param id the id of the commonTableRelationshipDTO to save.
     * @param commonTableRelationshipDTO the commonTableRelationshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableRelationshipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commonTableRelationshipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commonTableRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/common-table-relationships/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommonTableRelationshipDTO> partialUpdateCommonTableRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommonTableRelationshipDTO commonTableRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommonTableRelationship partially : {}, {}", id, commonTableRelationshipDTO);
        if (commonTableRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonTableRelationshipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonTableRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommonTableRelationshipDTO> result = commonTableRelationshipService.partialUpdate(commonTableRelationshipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonTableRelationshipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /common-table-relationships} : get all the commonTableRelationships.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonTableRelationships in body.
     */
    @GetMapping("/common-table-relationships")
    public ResponseEntity<List<CommonTableRelationshipDTO>> getAllCommonTableRelationships(
        CommonTableRelationshipCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get CommonTableRelationships by criteria: {}", criteria);
        IPage<CommonTableRelationshipDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    commonTableRelationshipQueryService.selectByCustomEntity(
                        listModelName,
                        criteria,
                        queryWrapper,
                        PageableUtils.toPage(pageable)
                    );
            } else {
                page =
                    commonTableRelationshipQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = commonTableRelationshipQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonTableRelationshipQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /common-table-relationships/count} : count all the commonTableRelationships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/common-table-relationships/count")
    public ResponseEntity<Long> countCommonTableRelationships(CommonTableRelationshipCriteria criteria) {
        log.debug("REST request to count CommonTableRelationships by criteria: {}", criteria);
        return ResponseEntity.ok().body(commonTableRelationshipQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /common-table-relationships/:id} : get the "id" commonTableRelationship.
     *
     * @param id the id of the commonTableRelationshipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonTableRelationshipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-table-relationships/{id}")
    public ResponseEntity<CommonTableRelationshipDTO> getCommonTableRelationship(@PathVariable Long id) {
        log.debug("REST request to get CommonTableRelationship : {}", id);
        Optional<CommonTableRelationshipDTO> commonTableRelationshipDTO = commonTableRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonTableRelationshipDTO);
    }

    /**
     * GET  /common-table-relationships/export : export the commonTableRelationships.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonTableRelationshipDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-table-relationships/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<CommonTableRelationshipDTO> page = commonTableRelationshipService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            CommonTableRelationshipDTO.class,
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
     * POST  /common-table-relationships/import : import the commonTableRelationships from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonTableRelationshipDTO, or with status 404 (Not Found)
     */
    @PostMapping("/common-table-relationships/import")
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
        List<CommonTableRelationshipDTO> list = ExcelImportUtil.importExcel(savedFile, CommonTableRelationshipDTO.class, params);
        list.forEach(commonTableRelationshipService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /common-table-relationships/:id} : delete the "id" commonTableRelationship.
     *
     * @param id the id of the commonTableRelationshipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-table-relationships/{id}")
    public ResponseEntity<Void> deleteCommonTableRelationship(@PathVariable Long id) {
        log.debug("REST request to delete CommonTableRelationship : {}", id);
        commonTableRelationshipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /common-table-relationships} : delete all the "ids" CommonTableRelationships.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-table-relationships")
    public ResponseEntity<Void> deleteCommonTableRelationshipsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete CommonTableRelationships : {}", ids);
        if (ids != null) {
            ids.forEach(commonTableRelationshipService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /common-table-relationships/specified-fields} : Updates an existing commonTableRelationship by specified fields.
     *
     * @param commonTableRelationshipDTOAndSpecifiedFields the commonTableRelationshipDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableRelationshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-table-relationships/specified-fields")
    public ResponseEntity<CommonTableRelationshipDTO> updateCommonTableRelationshipBySpecifiedFields(
        @RequestBody CommonTableRelationshipDTOAndSpecifiedFields commonTableRelationshipDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTableRelationship : {}", commonTableRelationshipDTOAndSpecifiedFields);
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipDTOAndSpecifiedFields.getCommonTableRelationship();
        Set<String> specifiedFields = commonTableRelationshipDTOAndSpecifiedFields.getSpecifiedFields();
        if (commonTableRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonTableRelationshipDTO result = commonTableRelationshipService.updateBySpecifiedFields(
            commonTableRelationshipDTO,
            specifiedFields
        );
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonTableRelationshipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-table-relationships/specified-field} : Updates an existing commonTableRelationship by specified field.
     *
     * @param commonTableRelationshipDTOAndSpecifiedFields the commonTableRelationshipDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonTableRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the commonTableRelationshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonTableRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-table-relationships/specified-field")
    public ResponseEntity<CommonTableRelationshipDTO> updateCommonTableRelationshipBySpecifiedField(
        @RequestBody CommonTableRelationshipDTOAndSpecifiedFields commonTableRelationshipDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonTableRelationship : {}", commonTableRelationshipDTOAndSpecifiedFields);
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipDTOAndSpecifiedFields.getCommonTableRelationship();
        String fieldName = commonTableRelationshipDTOAndSpecifiedFields.getSpecifiedField();
        if (commonTableRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonTableRelationshipDTO result = commonTableRelationshipService.updateBySpecifiedField(commonTableRelationshipDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class CommonTableRelationshipDTOAndSpecifiedFields {

        private CommonTableRelationshipDTO commonTableRelationship;
        private Set<String> specifiedFields;
        private String specifiedField;

        private CommonTableRelationshipDTO getCommonTableRelationship() {
            return commonTableRelationship;
        }

        private void setCommonTableRelationship(CommonTableRelationshipDTO commonTableRelationship) {
            this.commonTableRelationship = commonTableRelationship;
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
