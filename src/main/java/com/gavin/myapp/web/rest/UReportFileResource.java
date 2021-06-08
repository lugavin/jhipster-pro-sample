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
import com.gavin.myapp.domain.UReportFile;
import com.gavin.myapp.repository.UReportFileRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.UReportFileQueryService;
import com.gavin.myapp.service.UReportFileService;
import com.gavin.myapp.service.criteria.UReportFileCriteria;
import com.gavin.myapp.service.dto.UReportFileDTO;
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

 * 管理实体{@link com.gavin.myapp.domain.UReportFile}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class UReportFileResource {

    private final Logger log = LoggerFactory.getLogger(UReportFileResource.class);

    private static final String ENTITY_NAME = "reportUReportFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UReportFileService uReportFileService;

    private final UReportFileRepository uReportFileRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final UReportFileQueryService uReportFileQueryService;

    public UReportFileResource(
        UReportFileService uReportFileService,
        UReportFileRepository uReportFileRepository,
        CommonConditionQueryService commonConditionQueryService,
        UReportFileQueryService uReportFileQueryService
    ) {
        this.uReportFileService = uReportFileService;
        this.uReportFileRepository = uReportFileRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.uReportFileQueryService = uReportFileQueryService;
    }

    /**
     * {@code POST  /u-report-files} : Create a new uReportFile.
     *
     * @param uReportFileDTO the uReportFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uReportFileDTO, or with status {@code 400 (Bad Request)} if the uReportFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/u-report-files")
    public ResponseEntity<UReportFileDTO> createUReportFile(@RequestBody UReportFileDTO uReportFileDTO) throws URISyntaxException {
        log.debug("REST request to save UReportFile : {}", uReportFileDTO);
        if (uReportFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new uReportFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UReportFileDTO result = uReportFileService.save(uReportFileDTO);
        return ResponseEntity
            .created(new URI("/api/u-report-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /u-report-files/:id} : Updates an existing uReportFile.
     *
     * @param id the id of the uReportFileDTO to save.
     * @param uReportFileDTO the uReportFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/u-report-files/{id}")
    public ResponseEntity<UReportFileDTO> updateUReportFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UReportFileDTO uReportFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UReportFile : {}, {}", id, uReportFileDTO);
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uReportFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uReportFileService.exists(UReportFile::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UReportFileDTO result = uReportFileService.save(uReportFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uReportFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /u-report-files/:id} : Partial updates given fields of an existing uReportFile, field will ignore if it is null.
     *
     * @param id the id of the uReportFileDTO to save.
     * @param uReportFileDTO the uReportFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uReportFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/u-report-files/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UReportFileDTO> partialUpdateUReportFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UReportFileDTO uReportFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UReportFile partially : {}, {}", id, uReportFileDTO);
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uReportFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uReportFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UReportFileDTO> result = uReportFileService.partialUpdate(uReportFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uReportFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /u-report-files} : get all the uReportFiles.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uReportFiles in body.
     */
    @GetMapping("/u-report-files")
    public ResponseEntity<List<UReportFileDTO>> getAllUReportFiles(
        UReportFileCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get UReportFiles by criteria: {}", criteria);
        IPage<UReportFileDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = uReportFileQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = uReportFileQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = uReportFileQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = uReportFileQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /u-report-files/count} : count all the uReportFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/u-report-files/count")
    public ResponseEntity<Long> countUReportFiles(UReportFileCriteria criteria) {
        log.debug("REST request to count UReportFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(uReportFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /u-report-files/:id} : get the "id" uReportFile.
     *
     * @param id the id of the uReportFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uReportFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/u-report-files/{id}")
    public ResponseEntity<UReportFileDTO> getUReportFile(@PathVariable Long id) {
        log.debug("REST request to get UReportFile : {}", id);
        Optional<UReportFileDTO> uReportFileDTO = uReportFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uReportFileDTO);
    }

    /**
     * GET  /u-report-files/export : export the uReportFiles.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uReportFileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/u-report-files/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<UReportFileDTO> page = uReportFileService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            UReportFileDTO.class,
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
     * POST  /u-report-files/import : import the uReportFiles from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uReportFileDTO, or with status 404 (Not Found)
     */
    @PostMapping("/u-report-files/import")
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
        List<UReportFileDTO> list = ExcelImportUtil.importExcel(savedFile, UReportFileDTO.class, params);
        list.forEach(uReportFileService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /u-report-files/:id} : delete the "id" uReportFile.
     *
     * @param id the id of the uReportFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/u-report-files/{id}")
    public ResponseEntity<Void> deleteUReportFile(@PathVariable Long id) {
        log.debug("REST request to delete UReportFile : {}", id);
        uReportFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /u-report-files} : delete all the "ids" UReportFiles.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/u-report-files")
    public ResponseEntity<Void> deleteUReportFilesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UReportFiles : {}", ids);
        if (ids != null) {
            ids.forEach(uReportFileService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /u-report-files/specified-fields} : Updates an existing uReportFile by specified fields.
     *
     * @param uReportFileDTOAndSpecifiedFields the uReportFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/u-report-files/specified-fields")
    public ResponseEntity<UReportFileDTO> updateUReportFileBySpecifiedFields(
        @RequestBody UReportFileDTOAndSpecifiedFields uReportFileDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update UReportFile : {}", uReportFileDTOAndSpecifiedFields);
        UReportFileDTO uReportFileDTO = uReportFileDTOAndSpecifiedFields.getUReportFile();
        Set<String> specifiedFields = uReportFileDTOAndSpecifiedFields.getSpecifiedFields();
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UReportFileDTO result = uReportFileService.updateBySpecifiedFields(uReportFileDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uReportFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /u-report-files/specified-field} : Updates an existing uReportFile by specified field.
     *
     * @param uReportFileDTOAndSpecifiedFields the uReportFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/u-report-files/specified-field")
    public ResponseEntity<UReportFileDTO> updateUReportFileBySpecifiedField(
        @RequestBody UReportFileDTOAndSpecifiedFields uReportFileDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update UReportFile : {}", uReportFileDTOAndSpecifiedFields);
        UReportFileDTO uReportFileDTO = uReportFileDTOAndSpecifiedFields.getUReportFile();
        String fieldName = uReportFileDTOAndSpecifiedFields.getSpecifiedField();
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UReportFileDTO result = uReportFileService.updateBySpecifiedField(uReportFileDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class UReportFileDTOAndSpecifiedFields {

        private UReportFileDTO uReportFile;
        private Set<String> specifiedFields;
        private String specifiedField;

        private UReportFileDTO getUReportFile() {
            return uReportFile;
        }

        private void setUReportFile(UReportFileDTO uReportFile) {
            this.uReportFile = uReportFile;
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
