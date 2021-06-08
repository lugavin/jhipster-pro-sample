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
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.repository.DataDictionaryRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.DataDictionaryQueryService;
import com.gavin.myapp.service.DataDictionaryService;
import com.gavin.myapp.service.criteria.DataDictionaryCriteria;
import com.gavin.myapp.service.dto.DataDictionaryDTO;
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

 * 管理实体{@link com.gavin.myapp.domain.DataDictionary}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class DataDictionaryResource {

    private final Logger log = LoggerFactory.getLogger(DataDictionaryResource.class);

    private static final String ENTITY_NAME = "settingsDataDictionary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataDictionaryService dataDictionaryService;

    private final DataDictionaryRepository dataDictionaryRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final DataDictionaryQueryService dataDictionaryQueryService;

    public DataDictionaryResource(
        DataDictionaryService dataDictionaryService,
        DataDictionaryRepository dataDictionaryRepository,
        CommonConditionQueryService commonConditionQueryService,
        DataDictionaryQueryService dataDictionaryQueryService
    ) {
        this.dataDictionaryService = dataDictionaryService;
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.dataDictionaryQueryService = dataDictionaryQueryService;
    }

    /**
     * {@code POST  /data-dictionaries} : Create a new dataDictionary.
     *
     * @param dataDictionaryDTO the dataDictionaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataDictionaryDTO, or with status {@code 400 (Bad Request)} if the dataDictionary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-dictionaries")
    public ResponseEntity<DataDictionaryDTO> createDataDictionary(@RequestBody DataDictionaryDTO dataDictionaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save DataDictionary : {}", dataDictionaryDTO);
        if (dataDictionaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataDictionary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataDictionaryDTO result = dataDictionaryService.save(dataDictionaryDTO);
        return ResponseEntity
            .created(new URI("/api/data-dictionaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-dictionaries/:id} : Updates an existing dataDictionary.
     *
     * @param id the id of the dataDictionaryDTO to save.
     * @param dataDictionaryDTO the dataDictionaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dataDictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataDictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-dictionaries/{id}")
    public ResponseEntity<DataDictionaryDTO> updateDataDictionary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataDictionaryDTO dataDictionaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DataDictionary : {}, {}", id, dataDictionaryDTO);
        if (dataDictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataDictionaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataDictionaryService.exists(DataDictionary::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataDictionaryDTO result = dataDictionaryService.save(dataDictionaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataDictionaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-dictionaries/:id} : Partial updates given fields of an existing dataDictionary, field will ignore if it is null.
     *
     * @param id the id of the dataDictionaryDTO to save.
     * @param dataDictionaryDTO the dataDictionaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dataDictionaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dataDictionaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataDictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-dictionaries/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DataDictionaryDTO> partialUpdateDataDictionary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataDictionaryDTO dataDictionaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataDictionary partially : {}, {}", id, dataDictionaryDTO);
        if (dataDictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataDictionaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataDictionaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataDictionaryDTO> result = dataDictionaryService.partialUpdate(dataDictionaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataDictionaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /data-dictionaries} : get all the dataDictionaries.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataDictionaries in body.
     */
    @GetMapping("/data-dictionaries")
    public ResponseEntity<List<DataDictionaryDTO>> getAllDataDictionaries(
        DataDictionaryCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get DataDictionaries by criteria: {}", criteria);
        IPage<DataDictionaryDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    dataDictionaryQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = dataDictionaryQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = dataDictionaryQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = dataDictionaryQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /data-dictionaries/count} : count all the dataDictionaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/data-dictionaries/count")
    public ResponseEntity<Long> countDataDictionaries(DataDictionaryCriteria criteria) {
        log.debug("REST request to count DataDictionaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataDictionaryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /data-dictionaries/tree : get all the dataDictionaries for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataDictionaries in body
     */
    @GetMapping("/data-dictionaries/tree")
    public ResponseEntity<List<DataDictionaryDTO>> getAllDataDictionariesofTree(Pageable pageable) {
        log.debug("REST request to get a page of DataDictionaries");
        IPage<DataDictionaryDTO> page = dataDictionaryService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /data-dictionaries/{parentId}/tree : get all the dataDictionaries for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of dataDictionaries in body
     */
    @GetMapping("/data-dictionaries/{parentId}/tree")
    public ResponseEntity<List<DataDictionaryDTO>> getAllDataDictionariesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all DataDictionaries of parentId");
        List<DataDictionaryDTO> list = dataDictionaryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /data-dictionaries/:id} : get the "id" dataDictionary.
     *
     * @param id the id of the dataDictionaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataDictionaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-dictionaries/{id}")
    public ResponseEntity<DataDictionaryDTO> getDataDictionary(@PathVariable Long id) {
        log.debug("REST request to get DataDictionary : {}", id);
        Optional<DataDictionaryDTO> dataDictionaryDTO = dataDictionaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataDictionaryDTO);
    }

    /**
     * GET  /data-dictionaries/export : export the dataDictionaries.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the dataDictionaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/data-dictionaries/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<DataDictionaryDTO> page = dataDictionaryService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            DataDictionaryDTO.class,
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
     * POST  /data-dictionaries/import : import the dataDictionaries from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the dataDictionaryDTO, or with status 404 (Not Found)
     */
    @PostMapping("/data-dictionaries/import")
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
        List<DataDictionaryDTO> list = ExcelImportUtil.importExcel(savedFile, DataDictionaryDTO.class, params);
        list.forEach(dataDictionaryService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /data-dictionaries/:id} : delete the "id" dataDictionary.
     *
     * @param id the id of the dataDictionaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-dictionaries/{id}")
    public ResponseEntity<Void> deleteDataDictionary(@PathVariable Long id) {
        log.debug("REST request to delete DataDictionary : {}", id);
        dataDictionaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /data-dictionaries} : delete all the "ids" DataDictionaries.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-dictionaries")
    public ResponseEntity<Void> deleteDataDictionariesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete DataDictionaries : {}", ids);
        if (ids != null) {
            ids.forEach(dataDictionaryService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /data-dictionaries/specified-fields} : Updates an existing dataDictionary by specified fields.
     *
     * @param dataDictionaryDTOAndSpecifiedFields the dataDictionaryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dataDictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataDictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-dictionaries/specified-fields")
    public ResponseEntity<DataDictionaryDTO> updateDataDictionaryBySpecifiedFields(
        @RequestBody DataDictionaryDTOAndSpecifiedFields dataDictionaryDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update DataDictionary : {}", dataDictionaryDTOAndSpecifiedFields);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryDTOAndSpecifiedFields.getDataDictionary();
        Set<String> specifiedFields = dataDictionaryDTOAndSpecifiedFields.getSpecifiedFields();
        if (dataDictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataDictionaryDTO result = dataDictionaryService.updateBySpecifiedFields(dataDictionaryDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataDictionaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-dictionaries/specified-field} : Updates an existing dataDictionary by specified field.
     *
     * @param dataDictionaryDTOAndSpecifiedFields the dataDictionaryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dataDictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataDictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-dictionaries/specified-field")
    public ResponseEntity<DataDictionaryDTO> updateDataDictionaryBySpecifiedField(
        @RequestBody DataDictionaryDTOAndSpecifiedFields dataDictionaryDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update DataDictionary : {}", dataDictionaryDTOAndSpecifiedFields);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryDTOAndSpecifiedFields.getDataDictionary();
        String fieldName = dataDictionaryDTOAndSpecifiedFields.getSpecifiedField();
        if (dataDictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataDictionaryDTO result = dataDictionaryService.updateBySpecifiedField(dataDictionaryDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class DataDictionaryDTOAndSpecifiedFields {

        private DataDictionaryDTO dataDictionary;
        private Set<String> specifiedFields;
        private String specifiedField;

        private DataDictionaryDTO getDataDictionary() {
            return dataDictionary;
        }

        private void setDataDictionary(DataDictionaryDTO dataDictionary) {
            this.dataDictionary = dataDictionary;
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
