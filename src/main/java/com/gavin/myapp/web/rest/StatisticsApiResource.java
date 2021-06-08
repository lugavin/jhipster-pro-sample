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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.StatisticsApiQueryService;
import com.gavin.myapp.service.StatisticsApiService;
import com.gavin.myapp.service.criteria.StatisticsApiCriteria;
import com.gavin.myapp.service.dto.StatisticsApiDTO;
import com.gavin.myapp.util.web.IPageUtil;
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
 * REST controller for managing {@link com.gavin.myapp.domain.StatisticsApi}.
 */
@RestController
@RequestMapping("/api")
public class StatisticsApiResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsApiResource.class);
    public static ObjectMapper mapper = new ObjectMapper();

    private static final String ENTITY_NAME = "statisticsStatisticsApi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatisticsApiService statisticsApiService;

    private final CommonConditionQueryService commonConditionQueryService;

    private final StatisticsApiQueryService statisticsApiQueryService;

    public StatisticsApiResource(
        StatisticsApiService statisticsApiService,
        CommonConditionQueryService commonConditionQueryService,
        StatisticsApiQueryService statisticsApiQueryService
    ) {
        this.statisticsApiService = statisticsApiService;
        this.commonConditionQueryService = commonConditionQueryService;
        this.statisticsApiQueryService = statisticsApiQueryService;
    }

    /**
     * {@code POST  /statistics-apis} : Create a new statisticsApi.
     *
     * @param statisticsApiDTO the statisticsApiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statisticsApiDTO, or with status {@code 400 (Bad Request)} if the statisticsApi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/statistics-apis")
    public ResponseEntity<StatisticsApiDTO> createStatisticsApi(@Valid @RequestBody StatisticsApiDTO statisticsApiDTO)
        throws URISyntaxException {
        log.debug("REST request to save StatisticsApi : {}", statisticsApiDTO);
        if (statisticsApiDTO.getId() != null) {
            throw new BadRequestAlertException("A new statisticsApi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatisticsApiDTO result = statisticsApiService.save(statisticsApiDTO);
        return ResponseEntity
            .created(new URI("/api/statistics-apis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /statistics-apis} : Updates an existing statisticsApi.
     *
     * @param statisticsApiDTO the statisticsApiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statisticsApiDTO,
     * or with status {@code 400 (Bad Request)} if the statisticsApiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statisticsApiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/statistics-apis/{id}")
    public ResponseEntity<StatisticsApiDTO> updateStatisticsApi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StatisticsApiDTO statisticsApiDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StatisticsApi : {}", statisticsApiDTO);
        if (statisticsApiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statisticsApiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        StatisticsApiDTO result = statisticsApiService.save(statisticsApiDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statisticsApiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /statistics-apis} : get all the statisticsApis.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statisticsApis in body.
     */
    @GetMapping("/statistics-apis")
    public ResponseEntity<List<StatisticsApiDTO>> getAllStatisticsApis(
        StatisticsApiCriteria criteria,
        Page pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get StatisticsApis by criteria: {}", criteria);
        IPage<StatisticsApiDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = statisticsApiQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, pageable);
            } else {
                page = statisticsApiQueryService.selectByCustomEntity(listModelName, criteria, null, pageable);
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = statisticsApiQueryService.findByQueryWrapper(queryWrapper, pageable);
            } else {
                page = statisticsApiQueryService.findByCriteria(criteria, pageable);
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /statistics-apis/count} : count all the statisticsApis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/statistics-apis/count")
    public ResponseEntity<Long> countStatisticsApis(StatisticsApiCriteria criteria) {
        log.debug("REST request to count StatisticsApis by criteria: {}", criteria);
        return ResponseEntity.ok().body(statisticsApiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /statistics-apis/count} : count all the statisticsApis.
     *
     * @param id the statisticsApis which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/statistics-apis/result/{id}")
    public ResponseEntity<Object> runStatisticsApis(@PathVariable Long id) throws JsonProcessingException {
        Optional<StatisticsApiDTO> one = statisticsApiService.findOne(id);
        if (one.isPresent()) {
            List result = statisticsApiQueryService.runSql(one.get().getApiBody());
            one.get().setResult(mapper.writeValueAsString(result));
            statisticsApiService.save(one.get());
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * {@code GET  /statistics-apis/:id} : get the "id" statisticsApi.
     *
     * @param id the id of the statisticsApiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statisticsApiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/statistics-apis/{id}")
    public ResponseEntity<StatisticsApiDTO> getStatisticsApi(@PathVariable Long id) {
        log.debug("REST request to get StatisticsApi : {}", id);
        Optional<StatisticsApiDTO> statisticsApiDTO = statisticsApiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statisticsApiDTO);
    }

    /**
     * GET  /statistics-apis/export : export the statisticsApis.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the statisticsApiDTO, or with status 404 (Not Found)
     */
    @GetMapping("/statistics-apis/export")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<StatisticsApiDTO> page = statisticsApiService.findAll(null);
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            StatisticsApiDTO.class,
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
     * POST  /statistics-apis/import : import the statisticsApis from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the statisticsApiDTO, or with status 404 (Not Found)
     */
    @PostMapping("/statistics-apis/import")
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
        List<StatisticsApiDTO> list = ExcelImportUtil.importExcel(savedFile, StatisticsApiDTO.class, params);
        list.forEach(statisticsApiService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /statistics-apis/:id} : delete the "id" statisticsApi.
     *
     * @param id the id of the statisticsApiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/statistics-apis/{id}")
    public ResponseEntity<Void> deleteStatisticsApi(@PathVariable Long id) {
        log.debug("REST request to delete StatisticsApi : {}", id);
        statisticsApiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /statistics-apis} : delete all the "ids" StatisticsApis.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/statistics-apis")
    public ResponseEntity<Void> deleteStatisticsApisByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete StatisticsApis : {}", ids);
        if (ids != null) {
            ids.forEach(statisticsApiService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/statistics-apis/creator/current-user")
    public ResponseEntity<List<StatisticsApiDTO>> findByCreatorIsCurrentUser() {
        log.debug("REST request to get StatisticsApi for current user. ");
        List<StatisticsApiDTO> result = statisticsApiService.findByCreatorIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics-apis/modifier/current-user")
    public ResponseEntity<List<StatisticsApiDTO>> findByModifierIsCurrentUser() {
        log.debug("REST request to get StatisticsApi for current user. ");
        List<StatisticsApiDTO> result = statisticsApiService.findByModifierIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /statistics-apis/specified-fields} : Updates an existing statisticsApi by specified fields.
     *
     * @param statisticsApiDTOAndSpecifiedFields the statisticsApiDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statisticsApiDTO,
     * or with status {@code 400 (Bad Request)} if the statisticsApiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statisticsApiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/statistics-apis/specified-fields")
    public ResponseEntity<StatisticsApiDTO> updateStatisticsApiBySpecifiedFields(
        @RequestBody StatisticsApiDTOAndSpecifiedFields statisticsApiDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update StatisticsApi : {}", statisticsApiDTOAndSpecifiedFields);
        StatisticsApiDTO statisticsApiDTO = statisticsApiDTOAndSpecifiedFields.getStatisticsApi();
        Set<String> specifiedFields = statisticsApiDTOAndSpecifiedFields.getSpecifiedFields();
        if (statisticsApiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatisticsApiDTO result = statisticsApiService.updateBySpecifiedFields(statisticsApiDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statisticsApiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /statistics-apis/specified-field} : Updates an existing statisticsApi by specified field.
     *
     * @param statisticsApiDTOAndSpecifiedFields the statisticsApiDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statisticsApiDTO,
     * or with status {@code 400 (Bad Request)} if the statisticsApiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statisticsApiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/statistics-apis/specified-field")
    public ResponseEntity<StatisticsApiDTO> updateStatisticsApiBySpecifiedField(
        @RequestBody StatisticsApiDTOAndSpecifiedFields statisticsApiDTOAndSpecifiedFields,
        StatisticsApiCriteria criteria
    ) throws URISyntaxException {
        log.debug("REST request to update StatisticsApi : {}", statisticsApiDTOAndSpecifiedFields);
        StatisticsApiDTO statisticsApiDTO = statisticsApiDTOAndSpecifiedFields.getStatisticsApi();
        String fieldName = statisticsApiDTOAndSpecifiedFields.getSpecifiedField();
        if (statisticsApiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatisticsApiDTO result = statisticsApiService.updateBySpecifiedField(statisticsApiDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class StatisticsApiDTOAndSpecifiedFields {

        private StatisticsApiDTO statisticsApi;
        private Set<String> specifiedFields;
        private String specifiedField;

        private StatisticsApiDTO getStatisticsApi() {
            return statisticsApi;
        }

        private void setStatisticsApi(StatisticsApiDTO statisticsApi) {
            this.statisticsApi = statisticsApi;
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
