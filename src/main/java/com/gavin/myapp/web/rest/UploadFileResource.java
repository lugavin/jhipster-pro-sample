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
import com.gavin.myapp.service.UploadFileQueryService;
import com.gavin.myapp.service.UploadFileService;
import com.gavin.myapp.service.criteria.UploadFileCriteria;
import com.gavin.myapp.service.dto.UploadFileDTO;
import com.gavin.myapp.util.web.IPageUtil;
import com.gavin.myapp.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link com.gavin.myapp.domain.UploadFile}.
 */
@RestController
@RequestMapping("/api")
public class UploadFileResource {

    private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

    private static final String ENTITY_NAME = "filesUploadFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadFileService uploadFileService;

    private final UploadFileQueryService uploadFileQueryService;

    private final CommonConditionQueryService commonConditionQueryService;

    public UploadFileResource(
        UploadFileService uploadFileService,
        CommonConditionQueryService commonConditionQueryService,
        UploadFileQueryService uploadFileQueryService
    ) {
        this.uploadFileService = uploadFileService;
        this.commonConditionQueryService = commonConditionQueryService;
        this.uploadFileQueryService = uploadFileQueryService;
    }

    /**
     * {@code POST  /upload-files} : Create a new uploadFile.
     *
     * @param file the uploadFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadFileDTO, or with status {@code 400 (Bad Request)} if the uploadFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upload-files")
    public ResponseEntity<UploadFileDTO> createUploadFile(@RequestParam(value = "file", required = false) final MultipartFile file)
        throws Exception {
        log.debug("REST request to save UploadFile : {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new BadRequestAlertException("A new uploadImage cannot null", ENTITY_NAME, "imageisnull");
        }
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setFile(file);
        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity
            .created(new URI("/api/upload-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upload-files} : Updates an existing uploadFile.
     *
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files")
    public ResponseEntity<UploadFileDTO> updateUploadFile(@RequestBody UploadFileDTO uploadFileDTO) throws Exception {
        log.debug("REST request to update UploadFile : {}", uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /upload-files} : get all the uploadFiles.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadFiles in body.
     */
    @GetMapping("/upload-files")
    public ResponseEntity<List<UploadFileDTO>> getAllUploadFiles(
        UploadFileCriteria criteria,
        Page pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName
    ) {
        log.debug("REST request to get UploadFiles by criteria: {}", criteria);
        IPage<UploadFileDTO> page;
        if (listModelName != null) {
            page = uploadFileQueryService.selectByCustomEntity(listModelName, criteria, null, pageable);
        } else {
            page = uploadFileQueryService.findByCriteria(criteria, pageable);
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /upload-files/count} : count all the uploadFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/upload-files/count")
    public ResponseEntity<Long> countUploadFiles(UploadFileCriteria criteria) {
        log.debug("REST request to count UploadFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(uploadFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upload-files/:id} : get the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upload-files/{id}")
    public ResponseEntity<UploadFileDTO> getUploadFile(@PathVariable Long id) {
        log.debug("REST request to get UploadFile : {}", id);
        Optional<UploadFileDTO> uploadFileDTO = uploadFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadFileDTO);
    }

    /**
     * GET  /upload-files/export : export the uploadFiles.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadFileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/upload-files/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<UploadFileDTO> page = uploadFileService.findAll(null);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"), UploadFileDTO.class, page.getRecords());
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
     * POST  /upload-files/import : import the uploadFiles from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadFileDTO, or with status 404 (Not Found)
     */
    @PostMapping("/upload-files/import")
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
        List<UploadFileDTO> list = ExcelImportUtil.importExcel(savedFile, UploadFileDTO.class, params);
        list.forEach(
            uploadFileDTO -> {
                try {
                    uploadFileService.save(uploadFileDTO);
                } catch (Exception e) {
                    throw new BadRequestAlertException("UploadImageError", ENTITY_NAME, "fileError");
                }
            }
        );
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /upload-files/:id} : delete the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-files/{id}")
    public ResponseEntity<Void> deleteUploadFile(@PathVariable Long id) {
        log.debug("REST request to delete UploadFile : {}", id);
        uploadFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /upload-files} : delete all the "ids" UploadFiles.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-files")
    public ResponseEntity<Void> deleteUploadFilesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UploadFiles : {}", ids);
        if (ids != null) {
            ids.forEach(uploadFileService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/upload-files/user/current-user")
    public ResponseEntity<List<UploadFileDTO>> findByUserIsCurrentUser() {
        log.debug("REST request to get UploadFile for current user. ");
        List<UploadFileDTO> result = uploadFileService.findByUserIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /upload-files/specified-fields} : Updates an existing uploadFile by specified fields.
     *
     * @param uploadFileDTOAndSpecifiedFields the uploadFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files/specified-fields")
    public ResponseEntity<UploadFileDTO> updateUploadFileBySpecifiedFields(
        @RequestBody UploadFileDTOAndSpecifiedFields uploadFileDTOAndSpecifiedFields
    ) throws Exception {
        log.debug("REST request to update UploadFile : {}", uploadFileDTOAndSpecifiedFields);
        UploadFileDTO uploadFileDTO = uploadFileDTOAndSpecifiedFields.getUploadFile();
        Set<String> specifiedFields = uploadFileDTOAndSpecifiedFields.getSpecifiedFields();
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.updateBySpecifiedFields(uploadFileDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upload-files/specified-field} : Updates an existing uploadFile by specified field.
     *
     * @param uploadFileDTOAndSpecifiedFields the uploadFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files/specified-field")
    public ResponseEntity<UploadFileDTO> updateUploadFileBySpecifiedField(
        @RequestBody UploadFileDTOAndSpecifiedFields uploadFileDTOAndSpecifiedFields,
        UploadFileCriteria criteria
    ) throws Exception {
        log.debug("REST request to update UploadFile : {}", uploadFileDTOAndSpecifiedFields);
        UploadFileDTO uploadFileDTO = uploadFileDTOAndSpecifiedFields.getUploadFile();
        String fieldName = uploadFileDTOAndSpecifiedFields.getSpecifiedField();
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.updateBySpecifiedField(uploadFileDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class UploadFileDTOAndSpecifiedFields {

        private UploadFileDTO uploadFile;
        private Set<String> specifiedFields;
        private String specifiedField;

        private UploadFileDTO getUploadFile() {
            return uploadFile;
        }

        private void setUploadFile(UploadFileDTO uploadFile) {
            this.uploadFile = uploadFile;
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
