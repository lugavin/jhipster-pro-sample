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
import com.gavin.myapp.domain.OssConfig;
import com.gavin.myapp.repository.OssConfigRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.OssConfigQueryService;
import com.gavin.myapp.service.OssConfigService;
import com.gavin.myapp.service.criteria.OssConfigCriteria;
import com.gavin.myapp.service.dto.OssConfigDTO;
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

 * 管理实体{@link com.gavin.myapp.domain.OssConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api")
public class OssConfigResource {

    private final Logger log = LoggerFactory.getLogger(OssConfigResource.class);

    private static final String ENTITY_NAME = "filesOssConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OssConfigService ossConfigService;

    private final OssConfigRepository ossConfigRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final OssConfigQueryService ossConfigQueryService;

    public OssConfigResource(
        OssConfigService ossConfigService,
        OssConfigRepository ossConfigRepository,
        CommonConditionQueryService commonConditionQueryService,
        OssConfigQueryService ossConfigQueryService
    ) {
        this.ossConfigService = ossConfigService;
        this.ossConfigRepository = ossConfigRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.ossConfigQueryService = ossConfigQueryService;
    }

    /**
     * {@code POST  /oss-configs} : Create a new ossConfig.
     *
     * @param ossConfigDTO the ossConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ossConfigDTO, or with status {@code 400 (Bad Request)} if the ossConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oss-configs")
    public ResponseEntity<OssConfigDTO> createOssConfig(@RequestBody OssConfigDTO ossConfigDTO) throws URISyntaxException {
        log.debug("REST request to save OssConfig : {}", ossConfigDTO);
        if (ossConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new ossConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OssConfigDTO result = ossConfigService.save(ossConfigDTO);
        return ResponseEntity
            .created(new URI("/api/oss-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oss-configs/:id} : Updates an existing ossConfig.
     *
     * @param id the id of the ossConfigDTO to save.
     * @param ossConfigDTO the ossConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/{id}")
    public ResponseEntity<OssConfigDTO> updateOssConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OssConfigDTO ossConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OssConfig : {}, {}", id, ossConfigDTO);
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ossConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ossConfigService.exists(OssConfig::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OssConfigDTO result = ossConfigService.save(ossConfigDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /oss-configs/:id} : Partial updates given fields of an existing ossConfig, field will ignore if it is null.
     *
     * @param id the id of the ossConfigDTO to save.
     * @param ossConfigDTO the ossConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ossConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/oss-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OssConfigDTO> partialUpdateOssConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OssConfigDTO ossConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OssConfig partially : {}, {}", id, ossConfigDTO);
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ossConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ossConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OssConfigDTO> result = ossConfigService.partialUpdate(ossConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /oss-configs} : get all the ossConfigs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ossConfigs in body.
     */
    @GetMapping("/oss-configs")
    public ResponseEntity<List<OssConfigDTO>> getAllOssConfigs(
        OssConfigCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get OssConfigs by criteria: {}", criteria);
        IPage<OssConfigDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = ossConfigQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = ossConfigQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = ossConfigQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = ossConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /oss-configs/count} : count all the ossConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/oss-configs/count")
    public ResponseEntity<Long> countOssConfigs(OssConfigCriteria criteria) {
        log.debug("REST request to count OssConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ossConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /oss-configs/:id} : get the "id" ossConfig.
     *
     * @param id the id of the ossConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ossConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oss-configs/{id}")
    public ResponseEntity<OssConfigDTO> getOssConfig(@PathVariable Long id) {
        log.debug("REST request to get OssConfig : {}", id);
        Optional<OssConfigDTO> ossConfigDTO = ossConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ossConfigDTO);
    }

    /**
     * GET  /oss-configs/export : export the ossConfigs.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the ossConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/oss-configs/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<OssConfigDTO> page = ossConfigService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"), OssConfigDTO.class, page.getRecords());
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
     * POST  /oss-configs/import : import the ossConfigs from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the ossConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/oss-configs/import")
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
        List<OssConfigDTO> list = ExcelImportUtil.importExcel(savedFile, OssConfigDTO.class, params);
        list.forEach(ossConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /oss-configs/:id} : delete the "id" ossConfig.
     *
     * @param id the id of the ossConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oss-configs/{id}")
    public ResponseEntity<Void> deleteOssConfig(@PathVariable Long id) {
        log.debug("REST request to delete OssConfig : {}", id);
        ossConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /oss-configs} : delete all the "ids" OssConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oss-configs")
    public ResponseEntity<Void> deleteOssConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete OssConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(ossConfigService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /oss-configs/specified-fields} : Updates an existing ossConfig by specified fields.
     *
     * @param ossConfigDTOAndSpecifiedFields the ossConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/specified-fields")
    public ResponseEntity<OssConfigDTO> updateOssConfigBySpecifiedFields(
        @RequestBody OssConfigDTOAndSpecifiedFields ossConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update OssConfig : {}", ossConfigDTOAndSpecifiedFields);
        OssConfigDTO ossConfigDTO = ossConfigDTOAndSpecifiedFields.getOssConfig();
        Set<String> specifiedFields = ossConfigDTOAndSpecifiedFields.getSpecifiedFields();
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OssConfigDTO result = ossConfigService.updateBySpecifiedFields(ossConfigDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oss-configs/specified-field} : Updates an existing ossConfig by specified field.
     *
     * @param ossConfigDTOAndSpecifiedFields the ossConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/specified-field")
    public ResponseEntity<OssConfigDTO> updateOssConfigBySpecifiedField(
        @RequestBody OssConfigDTOAndSpecifiedFields ossConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update OssConfig : {}", ossConfigDTOAndSpecifiedFields);
        OssConfigDTO ossConfigDTO = ossConfigDTOAndSpecifiedFields.getOssConfig();
        String fieldName = ossConfigDTOAndSpecifiedFields.getSpecifiedField();
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OssConfigDTO result = ossConfigService.updateBySpecifiedField(ossConfigDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class OssConfigDTOAndSpecifiedFields {

        private OssConfigDTO ossConfig;
        private Set<String> specifiedFields;
        private String specifiedField;

        private OssConfigDTO getOssConfig() {
            return ossConfig;
        }

        private void setOssConfig(OssConfigDTO ossConfig) {
            this.ossConfig = ossConfig;
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
