package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.AdministrativeDivision;
import com.gavin.myapp.domain.AdministrativeDivision;
import com.gavin.myapp.repository.AdministrativeDivisionRepository;
import com.gavin.myapp.service.AdministrativeDivisionQueryService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.criteria.AdministrativeDivisionCriteria;
import com.gavin.myapp.service.dto.AdministrativeDivisionDTO;
import com.gavin.myapp.service.mapper.AdministrativeDivisionMapper;
import com.gavin.myapp.web.rest.errors.ExceptionTranslator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link AdministrativeDivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class AdministrativeDivisionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MERGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MERGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;
    private static final Double SMALLER_LNG = 1D - 1D;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/administrative-divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministrativeDivisionRepository administrativeDivisionRepository;

    @Autowired
    private AdministrativeDivisionMapper administrativeDivisionMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restAdministrativeDivisionMockMvc;

    private AdministrativeDivision administrativeDivision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdministrativeDivision createEntity() {
        AdministrativeDivision administrativeDivision = new AdministrativeDivision()
            .name(DEFAULT_NAME)
            .areaCode(DEFAULT_AREA_CODE)
            .cityCode(DEFAULT_CITY_CODE)
            .mergerName(DEFAULT_MERGER_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .zipCode(DEFAULT_ZIP_CODE)
            .level(DEFAULT_LEVEL)
            .lng(DEFAULT_LNG)
            .lat(DEFAULT_LAT);
        return administrativeDivision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdministrativeDivision createUpdatedEntity() {
        AdministrativeDivision administrativeDivision = new AdministrativeDivision()
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);
        return administrativeDivision;
    }

    @BeforeEach
    public void initTest() {
        administrativeDivision = createEntity();
    }

    @Test
    @Transactional
    void createAdministrativeDivision() throws Exception {
        int databaseSizeBeforeCreate = administrativeDivisionRepository.findAll().size();
        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);
        restAdministrativeDivisionMockMvc
            .perform(
                post(ENTITY_API_URL_ID, administrativeDivisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeCreate + 1);
        AdministrativeDivision testAdministrativeDivision = administrativeDivisionList.get(administrativeDivisionList.size() - 1);
        assertThat(testAdministrativeDivision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdministrativeDivision.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testAdministrativeDivision.getCityCode()).isEqualTo(DEFAULT_CITY_CODE);
        assertThat(testAdministrativeDivision.getMergerName()).isEqualTo(DEFAULT_MERGER_NAME);
        assertThat(testAdministrativeDivision.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testAdministrativeDivision.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAdministrativeDivision.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testAdministrativeDivision.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testAdministrativeDivision.getLat()).isEqualTo(DEFAULT_LAT);
    }

    @Test
    @Transactional
    void createAdministrativeDivisionWithExistingId() throws Exception {
        // Create the AdministrativeDivision with an existing ID
        administrativeDivision.setId(1L);
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        int databaseSizeBeforeCreate = administrativeDivisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrativeDivisionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdministrativeDivisions() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrativeDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].mergerName").value(hasItem(DEFAULT_MERGER_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())));
    }

    @Test
    @Transactional
    public void getAdministrativeDivision() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get the administrativeDivision
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, administrativeDivision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrativeDivision.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE))
            .andExpect(jsonPath("$.cityCode").value(DEFAULT_CITY_CODE))
            .andExpect(jsonPath("$.mergerName").value(DEFAULT_MERGER_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAdministrativeDivisionsByIdFiltering() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        Long id = administrativeDivision.getId();

        defaultAdministrativeDivisionShouldBeFound("id.equals=" + id);
        defaultAdministrativeDivisionShouldNotBeFound("id.notEquals=" + id);

        defaultAdministrativeDivisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdministrativeDivisionShouldNotBeFound("id.greaterThan=" + id);

        defaultAdministrativeDivisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdministrativeDivisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name equals to DEFAULT_NAME
        defaultAdministrativeDivisionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the administrativeDivisionList where name equals to UPDATED_NAME
        defaultAdministrativeDivisionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name not equals to DEFAULT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the administrativeDivisionList where name not equals to UPDATED_NAME
        defaultAdministrativeDivisionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAdministrativeDivisionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the administrativeDivisionList where name equals to UPDATED_NAME
        defaultAdministrativeDivisionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name is not null
        defaultAdministrativeDivisionShouldBeFound("name.specified=true");

        // Get all the administrativeDivisionList where name is null
        defaultAdministrativeDivisionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name contains DEFAULT_NAME
        defaultAdministrativeDivisionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the administrativeDivisionList where name contains UPDATED_NAME
        defaultAdministrativeDivisionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where name does not contain DEFAULT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the administrativeDivisionList where name does not contain UPDATED_NAME
        defaultAdministrativeDivisionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode equals to DEFAULT_AREA_CODE
        defaultAdministrativeDivisionShouldBeFound("areaCode.equals=" + DEFAULT_AREA_CODE);

        // Get all the administrativeDivisionList where areaCode equals to UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.equals=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode not equals to DEFAULT_AREA_CODE
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.notEquals=" + DEFAULT_AREA_CODE);

        // Get all the administrativeDivisionList where areaCode not equals to UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldBeFound("areaCode.notEquals=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode in DEFAULT_AREA_CODE or UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldBeFound("areaCode.in=" + DEFAULT_AREA_CODE + "," + UPDATED_AREA_CODE);

        // Get all the administrativeDivisionList where areaCode equals to UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.in=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode is not null
        defaultAdministrativeDivisionShouldBeFound("areaCode.specified=true");

        // Get all the administrativeDivisionList where areaCode is null
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode contains DEFAULT_AREA_CODE
        defaultAdministrativeDivisionShouldBeFound("areaCode.contains=" + DEFAULT_AREA_CODE);

        // Get all the administrativeDivisionList where areaCode contains UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.contains=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByAreaCodeNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where areaCode does not contain DEFAULT_AREA_CODE
        defaultAdministrativeDivisionShouldNotBeFound("areaCode.doesNotContain=" + DEFAULT_AREA_CODE);

        // Get all the administrativeDivisionList where areaCode does not contain UPDATED_AREA_CODE
        defaultAdministrativeDivisionShouldBeFound("areaCode.doesNotContain=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode equals to DEFAULT_CITY_CODE
        defaultAdministrativeDivisionShouldBeFound("cityCode.equals=" + DEFAULT_CITY_CODE);

        // Get all the administrativeDivisionList where cityCode equals to UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.equals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode not equals to DEFAULT_CITY_CODE
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.notEquals=" + DEFAULT_CITY_CODE);

        // Get all the administrativeDivisionList where cityCode not equals to UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldBeFound("cityCode.notEquals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode in DEFAULT_CITY_CODE or UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldBeFound("cityCode.in=" + DEFAULT_CITY_CODE + "," + UPDATED_CITY_CODE);

        // Get all the administrativeDivisionList where cityCode equals to UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.in=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode is not null
        defaultAdministrativeDivisionShouldBeFound("cityCode.specified=true");

        // Get all the administrativeDivisionList where cityCode is null
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode contains DEFAULT_CITY_CODE
        defaultAdministrativeDivisionShouldBeFound("cityCode.contains=" + DEFAULT_CITY_CODE);

        // Get all the administrativeDivisionList where cityCode contains UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.contains=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where cityCode does not contain DEFAULT_CITY_CODE
        defaultAdministrativeDivisionShouldNotBeFound("cityCode.doesNotContain=" + DEFAULT_CITY_CODE);

        // Get all the administrativeDivisionList where cityCode does not contain UPDATED_CITY_CODE
        defaultAdministrativeDivisionShouldBeFound("cityCode.doesNotContain=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName equals to DEFAULT_MERGER_NAME
        defaultAdministrativeDivisionShouldBeFound("mergerName.equals=" + DEFAULT_MERGER_NAME);

        // Get all the administrativeDivisionList where mergerName equals to UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.equals=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName not equals to DEFAULT_MERGER_NAME
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.notEquals=" + DEFAULT_MERGER_NAME);

        // Get all the administrativeDivisionList where mergerName not equals to UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldBeFound("mergerName.notEquals=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName in DEFAULT_MERGER_NAME or UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldBeFound("mergerName.in=" + DEFAULT_MERGER_NAME + "," + UPDATED_MERGER_NAME);

        // Get all the administrativeDivisionList where mergerName equals to UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.in=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName is not null
        defaultAdministrativeDivisionShouldBeFound("mergerName.specified=true");

        // Get all the administrativeDivisionList where mergerName is null
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName contains DEFAULT_MERGER_NAME
        defaultAdministrativeDivisionShouldBeFound("mergerName.contains=" + DEFAULT_MERGER_NAME);

        // Get all the administrativeDivisionList where mergerName contains UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.contains=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByMergerNameNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where mergerName does not contain DEFAULT_MERGER_NAME
        defaultAdministrativeDivisionShouldNotBeFound("mergerName.doesNotContain=" + DEFAULT_MERGER_NAME);

        // Get all the administrativeDivisionList where mergerName does not contain UPDATED_MERGER_NAME
        defaultAdministrativeDivisionShouldBeFound("mergerName.doesNotContain=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName equals to DEFAULT_SHORT_NAME
        defaultAdministrativeDivisionShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the administrativeDivisionList where shortName equals to UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName not equals to DEFAULT_SHORT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the administrativeDivisionList where shortName not equals to UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the administrativeDivisionList where shortName equals to UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName is not null
        defaultAdministrativeDivisionShouldBeFound("shortName.specified=true");

        // Get all the administrativeDivisionList where shortName is null
        defaultAdministrativeDivisionShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName contains DEFAULT_SHORT_NAME
        defaultAdministrativeDivisionShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the administrativeDivisionList where shortName contains UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where shortName does not contain DEFAULT_SHORT_NAME
        defaultAdministrativeDivisionShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the administrativeDivisionList where shortName does not contain UPDATED_SHORT_NAME
        defaultAdministrativeDivisionShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode equals to DEFAULT_ZIP_CODE
        defaultAdministrativeDivisionShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the administrativeDivisionList where zipCode equals to UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode not equals to DEFAULT_ZIP_CODE
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.notEquals=" + DEFAULT_ZIP_CODE);

        // Get all the administrativeDivisionList where zipCode not equals to UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldBeFound("zipCode.notEquals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the administrativeDivisionList where zipCode equals to UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode is not null
        defaultAdministrativeDivisionShouldBeFound("zipCode.specified=true");

        // Get all the administrativeDivisionList where zipCode is null
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode contains DEFAULT_ZIP_CODE
        defaultAdministrativeDivisionShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the administrativeDivisionList where zipCode contains UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultAdministrativeDivisionShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the administrativeDivisionList where zipCode does not contain UPDATED_ZIP_CODE
        defaultAdministrativeDivisionShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level equals to DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level equals to UPDATED_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level not equals to DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.notEquals=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level not equals to UPDATED_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.notEquals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the administrativeDivisionList where level equals to UPDATED_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level is not null
        defaultAdministrativeDivisionShouldBeFound("level.specified=true");

        // Get all the administrativeDivisionList where level is null
        defaultAdministrativeDivisionShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level is greater than or equal to DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.greaterThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level is greater than or equal to UPDATED_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level is less than or equal to DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.lessThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level is less than or equal to SMALLER_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level is less than DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level is less than UPDATED_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where level is greater than DEFAULT_LEVEL
        defaultAdministrativeDivisionShouldNotBeFound("level.greaterThan=" + DEFAULT_LEVEL);

        // Get all the administrativeDivisionList where level is greater than SMALLER_LEVEL
        defaultAdministrativeDivisionShouldBeFound("level.greaterThan=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng equals to DEFAULT_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.equals=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng equals to UPDATED_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.equals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng not equals to DEFAULT_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.notEquals=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng not equals to UPDATED_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.notEquals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng in DEFAULT_LNG or UPDATED_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.in=" + DEFAULT_LNG + "," + UPDATED_LNG);

        // Get all the administrativeDivisionList where lng equals to UPDATED_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.in=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng is not null
        defaultAdministrativeDivisionShouldBeFound("lng.specified=true");

        // Get all the administrativeDivisionList where lng is null
        defaultAdministrativeDivisionShouldNotBeFound("lng.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng is greater than or equal to DEFAULT_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.greaterThanOrEqual=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng is greater than or equal to UPDATED_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.greaterThanOrEqual=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng is less than or equal to DEFAULT_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.lessThanOrEqual=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng is less than or equal to SMALLER_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.lessThanOrEqual=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsLessThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng is less than DEFAULT_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.lessThan=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng is less than UPDATED_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.lessThan=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lng is greater than DEFAULT_LNG
        defaultAdministrativeDivisionShouldNotBeFound("lng.greaterThan=" + DEFAULT_LNG);

        // Get all the administrativeDivisionList where lng is greater than SMALLER_LNG
        defaultAdministrativeDivisionShouldBeFound("lng.greaterThan=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat equals to DEFAULT_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat equals to UPDATED_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat not equals to DEFAULT_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.notEquals=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat not equals to UPDATED_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.notEquals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsInShouldWork() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the administrativeDivisionList where lat equals to UPDATED_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat is not null
        defaultAdministrativeDivisionShouldBeFound("lat.specified=true");

        // Get all the administrativeDivisionList where lat is null
        defaultAdministrativeDivisionShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat is greater than or equal to DEFAULT_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat is greater than or equal to UPDATED_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat is less than or equal to DEFAULT_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat is less than or equal to SMALLER_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat is less than DEFAULT_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat is less than UPDATED_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAdministrativeDivisionsByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        // Get all the administrativeDivisionList where lat is greater than DEFAULT_LAT
        defaultAdministrativeDivisionShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the administrativeDivisionList where lat is greater than SMALLER_LAT
        defaultAdministrativeDivisionShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    public void getAllAdministrativeDivisionsByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);
        AdministrativeDivision children = AdministrativeDivisionResourceIT.createEntity();
        administrativeDivision.addChildren(children);
        administrativeDivisionRepository.insert(administrativeDivision);
        Long childrenId = children.getId();

        // Get all the administrativeDivisionList where children equals to childrenId
        defaultAdministrativeDivisionShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the administrativeDivisionList where children equals to (childrenId + 1)
        defaultAdministrativeDivisionShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    public void getAllAdministrativeDivisionsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);
        AdministrativeDivision parent = AdministrativeDivisionResourceIT.createEntity();
        administrativeDivision.setParent(parent);
        administrativeDivisionRepository.insert(administrativeDivision);
        Long parentId = parent.getId();

        // Get all the administrativeDivisionList where parent equals to parentId
        defaultAdministrativeDivisionShouldBeFound("parentId.equals=" + parentId);

        // Get all the administrativeDivisionList where parent equals to (parentId + 1)
        defaultAdministrativeDivisionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdministrativeDivisionShouldBeFound(String filter) throws Exception {
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrativeDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].mergerName").value(hasItem(DEFAULT_MERGER_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())));

        // Check, that the count call also returns 1
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdministrativeDivisionShouldNotBeFound(String filter) throws Exception {
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdministrativeDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdministrativeDivision() throws Exception {
        // Get the administrativeDivision
        restAdministrativeDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrativeDivision() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();

        // Update the administrativeDivision
        AdministrativeDivision updatedAdministrativeDivision = administrativeDivisionRepository
            .findById(administrativeDivision.getId())
            .get();

        // Disconnect from session so that the updates on updatedAdministrativeDivision are not directly saved in db

        updatedAdministrativeDivision
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(updatedAdministrativeDivision);

        restAdministrativeDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrativeDivisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
        AdministrativeDivision testAdministrativeDivision = administrativeDivisionList.get(administrativeDivisionList.size() - 1);
        assertThat(testAdministrativeDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdministrativeDivision.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testAdministrativeDivision.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testAdministrativeDivision.getMergerName()).isEqualTo(UPDATED_MERGER_NAME);
        assertThat(testAdministrativeDivision.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testAdministrativeDivision.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAdministrativeDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testAdministrativeDivision.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testAdministrativeDivision.getLat()).isEqualTo(UPDATED_LAT);
    }

    @Test
    @Transactional
    void putNonExistingngAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        restAdministrativeDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrativeDivisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrativeDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrativeDivisionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrativeDivisionWithPatch() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();

        // Update the administrativeDivision using partial update
        AdministrativeDivision partialUpdatedAdministrativeDivision = new AdministrativeDivision();
        partialUpdatedAdministrativeDivision.setId(administrativeDivision.getId());

        partialUpdatedAdministrativeDivision
            .name(UPDATED_NAME)
            .mergerName(UPDATED_MERGER_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL);

        restAdministrativeDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrativeDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrativeDivision))
            )
            .andExpect(status().isOk());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
        AdministrativeDivision testAdministrativeDivision = administrativeDivisionList.get(administrativeDivisionList.size() - 1);
        assertThat(testAdministrativeDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdministrativeDivision.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testAdministrativeDivision.getCityCode()).isEqualTo(DEFAULT_CITY_CODE);
        assertThat(testAdministrativeDivision.getMergerName()).isEqualTo(UPDATED_MERGER_NAME);
        assertThat(testAdministrativeDivision.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testAdministrativeDivision.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAdministrativeDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testAdministrativeDivision.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testAdministrativeDivision.getLat()).isEqualTo(DEFAULT_LAT);
    }

    @Test
    @Transactional
    void fullUpdateAdministrativeDivisionWithPatch() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();

        // Update the administrativeDivision using partial update
        AdministrativeDivision partialUpdatedAdministrativeDivision = new AdministrativeDivision();
        partialUpdatedAdministrativeDivision.setId(administrativeDivision.getId());

        partialUpdatedAdministrativeDivision
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);

        restAdministrativeDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrativeDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrativeDivision))
            )
            .andExpect(status().isOk());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
        AdministrativeDivision testAdministrativeDivision = administrativeDivisionList.get(administrativeDivisionList.size() - 1);
        assertThat(testAdministrativeDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdministrativeDivision.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testAdministrativeDivision.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testAdministrativeDivision.getMergerName()).isEqualTo(UPDATED_MERGER_NAME);
        assertThat(testAdministrativeDivision.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testAdministrativeDivision.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAdministrativeDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testAdministrativeDivision.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testAdministrativeDivision.getLat()).isEqualTo(UPDATED_LAT);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrativeDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrativeDivisionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrativeDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrativeDivision() throws Exception {
        int databaseSizeBeforeUpdate = administrativeDivisionRepository.findAll().size();
        administrativeDivision.setId(count.incrementAndGet());

        // Create the AdministrativeDivision
        AdministrativeDivisionDTO administrativeDivisionDTO = administrativeDivisionMapper.toDto(administrativeDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrativeDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrativeDivisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdministrativeDivision in the database
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrativeDivision() throws Exception {
        // Initialize the database
        administrativeDivisionRepository.insert(administrativeDivision);

        int databaseSizeBeforeDelete = administrativeDivisionRepository.findAll().size();

        // Delete the administrativeDivision
        restAdministrativeDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrativeDivision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdministrativeDivision> administrativeDivisionList = administrativeDivisionRepository.findAll();
        assertThat(administrativeDivisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
