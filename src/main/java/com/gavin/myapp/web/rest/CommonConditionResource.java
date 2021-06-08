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
import com.gavin.myapp.domain.CommonCondition;
import com.gavin.myapp.repository.CommonConditionRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonConditionService;
import com.gavin.myapp.service.criteria.CommonConditionCriteria;
import com.gavin.myapp.service.dto.CommonConditionDTO;
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

 * 管理实体{@link com.gavin.myapp.domain.CommonCondition}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class CommonConditionResource {

    private final Logger log = LoggerFactory.getLogger(CommonConditionResource.class);

    private static final String ENTITY_NAME = "reportCommonCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommonConditionService commonConditionService;

    private final CommonConditionRepository commonConditionRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    public CommonConditionResource(
        CommonConditionService commonConditionService,
        CommonConditionRepository commonConditionRepository,
        CommonConditionQueryService commonConditionQueryService
    ) {
        this.commonConditionService = commonConditionService;
        this.commonConditionRepository = commonConditionRepository;
        this.commonConditionQueryService = commonConditionQueryService;
    }

    /**
     * {@code POST  /common-conditions} : Create a new commonCondition.
     *
     * @param commonConditionDTO the commonConditionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonConditionDTO, or with status {@code 400 (Bad Request)} if the commonCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/common-conditions")
    public ResponseEntity<CommonConditionDTO> createCommonCondition(@Valid @RequestBody CommonConditionDTO commonConditionDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommonCondition : {}", commonConditionDTO);
        if (commonConditionDTO.getId() != null) {
            throw new BadRequestAlertException("A new commonCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommonConditionDTO result = commonConditionService.save(commonConditionDTO);
        return ResponseEntity
            .created(new URI("/api/common-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-conditions/:id} : Updates an existing commonCondition.
     *
     * @param id the id of the commonConditionDTO to save.
     * @param commonConditionDTO the commonConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-conditions/{id}")
    public ResponseEntity<CommonConditionDTO> updateCommonCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommonConditionDTO commonConditionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommonCondition : {}, {}", id, commonConditionDTO);
        if (commonConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonConditionService.exists(CommonCondition::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommonConditionDTO result = commonConditionService.save(commonConditionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonConditionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /common-conditions/:id} : Partial updates given fields of an existing commonCondition, field will ignore if it is null.
     *
     * @param id the id of the commonConditionDTO to save.
     * @param commonConditionDTO the commonConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commonConditionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/common-conditions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommonConditionDTO> partialUpdateCommonCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommonConditionDTO commonConditionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommonCondition partially : {}, {}", id, commonConditionDTO);
        if (commonConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommonConditionDTO> result = commonConditionService.partialUpdate(commonConditionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonConditionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /common-conditions} : get all the commonConditions.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonConditions in body.
     */
    @GetMapping("/common-conditions")
    public ResponseEntity<List<CommonConditionDTO>> getAllCommonConditions(
        CommonConditionCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get CommonConditions by criteria: {}", criteria);
        IPage<CommonConditionDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    commonConditionQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonConditionQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = commonConditionQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonConditionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /common-conditions/count} : count all the commonConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/common-conditions/count")
    public ResponseEntity<Long> countCommonConditions(CommonConditionCriteria criteria) {
        log.debug("REST request to count CommonConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(commonConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /common-conditions/:id} : get the "id" commonCondition.
     *
     * @param id the id of the commonConditionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonConditionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-conditions/{id}")
    public ResponseEntity<CommonConditionDTO> getCommonCondition(@PathVariable Long id) {
        log.debug("REST request to get CommonCondition : {}", id);
        Optional<CommonConditionDTO> commonConditionDTO = commonConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonConditionDTO);
    }

    /**
     * GET  /common-conditions/export : export the commonConditions.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonConditionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-conditions/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<CommonConditionDTO> page = commonConditionService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            CommonConditionDTO.class,
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
     * POST  /common-conditions/import : import the commonConditions from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonConditionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/common-conditions/import")
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
        List<CommonConditionDTO> list = ExcelImportUtil.importExcel(savedFile, CommonConditionDTO.class, params);
        list.forEach(commonConditionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /common-conditions/:id} : delete the "id" commonCondition.
     *
     * @param id the id of the commonConditionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-conditions/{id}")
    public ResponseEntity<Void> deleteCommonCondition(@PathVariable Long id) {
        log.debug("REST request to delete CommonCondition : {}", id);
        commonConditionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /common-conditions} : delete all the "ids" CommonConditions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-conditions")
    public ResponseEntity<Void> deleteCommonConditionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete CommonConditions : {}", ids);
        if (ids != null) {
            ids.forEach(commonConditionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /common-conditions/specified-fields} : Updates an existing commonCondition by specified fields.
     *
     * @param commonConditionDTOAndSpecifiedFields the commonConditionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-conditions/specified-fields")
    public ResponseEntity<CommonConditionDTO> updateCommonConditionBySpecifiedFields(
        @RequestBody CommonConditionDTOAndSpecifiedFields commonConditionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonCondition : {}", commonConditionDTOAndSpecifiedFields);
        CommonConditionDTO commonConditionDTO = commonConditionDTOAndSpecifiedFields.getCommonCondition();
        Set<String> specifiedFields = commonConditionDTOAndSpecifiedFields.getSpecifiedFields();
        if (commonConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonConditionDTO result = commonConditionService.updateBySpecifiedFields(commonConditionDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonConditionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-conditions/specified-field} : Updates an existing commonCondition by specified field.
     *
     * @param commonConditionDTOAndSpecifiedFields the commonConditionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-conditions/specified-field")
    public ResponseEntity<CommonConditionDTO> updateCommonConditionBySpecifiedField(
        @RequestBody CommonConditionDTOAndSpecifiedFields commonConditionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonCondition : {}", commonConditionDTOAndSpecifiedFields);
        CommonConditionDTO commonConditionDTO = commonConditionDTOAndSpecifiedFields.getCommonCondition();
        String fieldName = commonConditionDTOAndSpecifiedFields.getSpecifiedField();
        if (commonConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonConditionDTO result = commonConditionService.updateBySpecifiedField(commonConditionDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class CommonConditionDTOAndSpecifiedFields {

        private CommonConditionDTO commonCondition;
        private Set<String> specifiedFields;
        private String specifiedField;

        private CommonConditionDTO getCommonCondition() {
            return commonCondition;
        }

        private void setCommonCondition(CommonConditionDTO commonCondition) {
            this.commonCondition = commonCondition;
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
