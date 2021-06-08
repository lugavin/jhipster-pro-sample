package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static com.gavin.myapp.web.rest.TestUtil.sameInstant;
import static com.gavin.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.StatisticsApi;
import com.gavin.myapp.domain.User;
import com.gavin.myapp.domain.enumeration.StatSourceType;
import com.gavin.myapp.repository.StatisticsApiRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.StatisticsApiQueryService;
import com.gavin.myapp.service.criteria.StatisticsApiCriteria;
import com.gavin.myapp.service.dto.StatisticsApiDTO;
import com.gavin.myapp.service.mapper.StatisticsApiMapper;
import com.gavin.myapp.web.rest.errors.ExceptionTranslator;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link StatisticsApiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class StatisticsApiResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final StatSourceType DEFAULT_SOURCE_TYPE = StatSourceType.HTTP;
    private static final StatSourceType UPDATED_SOURCE_TYPE = StatSourceType.SQL;

    private static final String DEFAULT_API_BODY = "AAAAAAAAAA";
    private static final String UPDATED_API_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATE_INTERVAL = 1;
    private static final Integer UPDATED_UPDATE_INTERVAL = 2;
    private static final Integer SMALLER_UPDATE_INTERVAL = 1 - 1;

    private static final ZonedDateTime DEFAULT_LAST_SQL_RUN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_SQL_RUN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_SQL_RUN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String ENTITY_API_URL = "/api/statistics-apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatisticsApiRepository statisticsApiRepository;

    @Autowired
    private StatisticsApiMapper statisticsApiMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restStatisticsApiMockMvc;

    private StatisticsApi statisticsApi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatisticsApi createEntity() {
        StatisticsApi statisticsApi = new StatisticsApi()
            .title(DEFAULT_TITLE)
            .apiKey(DEFAULT_API_KEY)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .sourceType(DEFAULT_SOURCE_TYPE)
            .apiBody(DEFAULT_API_BODY)
            .result(DEFAULT_RESULT)
            .updateInterval(DEFAULT_UPDATE_INTERVAL)
            .lastSQLRunTime(DEFAULT_LAST_SQL_RUN_TIME)
            .enable(DEFAULT_ENABLE);
        return statisticsApi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatisticsApi createUpdatedEntity() {
        StatisticsApi statisticsApi = new StatisticsApi()
            .title(UPDATED_TITLE)
            .apiKey(UPDATED_API_KEY)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .sourceType(UPDATED_SOURCE_TYPE)
            .apiBody(UPDATED_API_BODY)
            .result(UPDATED_RESULT)
            .updateInterval(UPDATED_UPDATE_INTERVAL)
            .lastSQLRunTime(UPDATED_LAST_SQL_RUN_TIME)
            .enable(UPDATED_ENABLE);
        return statisticsApi;
    }

    @BeforeEach
    public void initTest() {
        statisticsApi = createEntity();
    }

    @Test
    @Transactional
    void createStatisticsApi() throws Exception {
        int databaseSizeBeforeCreate = statisticsApiRepository.findAll().size();
        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);
        restStatisticsApiMockMvc
            .perform(
                post(ENTITY_API_URL_ID, statisticsApiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeCreate + 1);
        StatisticsApi testStatisticsApi = statisticsApiList.get(statisticsApiList.size() - 1);
        assertThat(testStatisticsApi.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStatisticsApi.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testStatisticsApi.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testStatisticsApi.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testStatisticsApi.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testStatisticsApi.getApiBody()).isEqualTo(DEFAULT_API_BODY);
        assertThat(testStatisticsApi.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testStatisticsApi.getUpdateInterval()).isEqualTo(DEFAULT_UPDATE_INTERVAL);
        assertThat(testStatisticsApi.getLastSQLRunTime()).isEqualTo(DEFAULT_LAST_SQL_RUN_TIME);
        assertThat(testStatisticsApi.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void createStatisticsApiWithExistingId() throws Exception {
        // Create the StatisticsApi with an existing ID
        statisticsApi.setId(1L);
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        int databaseSizeBeforeCreate = statisticsApiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatisticsApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStatisticsApis() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statisticsApi.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].apiBody").value(hasItem(DEFAULT_API_BODY.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].updateInterval").value(hasItem(DEFAULT_UPDATE_INTERVAL)))
            .andExpect(jsonPath("$.[*].lastSQLRunTime").value(hasItem(sameInstant(DEFAULT_LAST_SQL_RUN_TIME))))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void getStatisticsApi() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get the statisticsApi
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL_ID, statisticsApi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statisticsApi.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.apiKey").value(DEFAULT_API_KEY))
            .andExpect(jsonPath("$.createAt").value(sameInstant(DEFAULT_CREATE_AT)))
            .andExpect(jsonPath("$.updateAt").value(sameInstant(DEFAULT_UPDATE_AT)))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.apiBody").value(DEFAULT_API_BODY.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.updateInterval").value(DEFAULT_UPDATE_INTERVAL))
            .andExpect(jsonPath("$.lastSQLRunTime").value(sameInstant(DEFAULT_LAST_SQL_RUN_TIME)))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getStatisticsApisByIdFiltering() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        Long id = statisticsApi.getId();

        defaultStatisticsApiShouldBeFound("id.equals=" + id);
        defaultStatisticsApiShouldNotBeFound("id.notEquals=" + id);

        defaultStatisticsApiShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStatisticsApiShouldNotBeFound("id.greaterThan=" + id);

        defaultStatisticsApiShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStatisticsApiShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title equals to DEFAULT_TITLE
        defaultStatisticsApiShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the statisticsApiList where title equals to UPDATED_TITLE
        defaultStatisticsApiShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title not equals to DEFAULT_TITLE
        defaultStatisticsApiShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the statisticsApiList where title not equals to UPDATED_TITLE
        defaultStatisticsApiShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultStatisticsApiShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the statisticsApiList where title equals to UPDATED_TITLE
        defaultStatisticsApiShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title is not null
        defaultStatisticsApiShouldBeFound("title.specified=true");

        // Get all the statisticsApiList where title is null
        defaultStatisticsApiShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleContainsSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title contains DEFAULT_TITLE
        defaultStatisticsApiShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the statisticsApiList where title contains UPDATED_TITLE
        defaultStatisticsApiShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where title does not contain DEFAULT_TITLE
        defaultStatisticsApiShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the statisticsApiList where title does not contain UPDATED_TITLE
        defaultStatisticsApiShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey equals to DEFAULT_API_KEY
        defaultStatisticsApiShouldBeFound("apiKey.equals=" + DEFAULT_API_KEY);

        // Get all the statisticsApiList where apiKey equals to UPDATED_API_KEY
        defaultStatisticsApiShouldNotBeFound("apiKey.equals=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey not equals to DEFAULT_API_KEY
        defaultStatisticsApiShouldNotBeFound("apiKey.notEquals=" + DEFAULT_API_KEY);

        // Get all the statisticsApiList where apiKey not equals to UPDATED_API_KEY
        defaultStatisticsApiShouldBeFound("apiKey.notEquals=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey in DEFAULT_API_KEY or UPDATED_API_KEY
        defaultStatisticsApiShouldBeFound("apiKey.in=" + DEFAULT_API_KEY + "," + UPDATED_API_KEY);

        // Get all the statisticsApiList where apiKey equals to UPDATED_API_KEY
        defaultStatisticsApiShouldNotBeFound("apiKey.in=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey is not null
        defaultStatisticsApiShouldBeFound("apiKey.specified=true");

        // Get all the statisticsApiList where apiKey is null
        defaultStatisticsApiShouldNotBeFound("apiKey.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyContainsSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey contains DEFAULT_API_KEY
        defaultStatisticsApiShouldBeFound("apiKey.contains=" + DEFAULT_API_KEY);

        // Get all the statisticsApiList where apiKey contains UPDATED_API_KEY
        defaultStatisticsApiShouldNotBeFound("apiKey.contains=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByApiKeyNotContainsSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where apiKey does not contain DEFAULT_API_KEY
        defaultStatisticsApiShouldNotBeFound("apiKey.doesNotContain=" + DEFAULT_API_KEY);

        // Get all the statisticsApiList where apiKey does not contain UPDATED_API_KEY
        defaultStatisticsApiShouldBeFound("apiKey.doesNotContain=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt equals to DEFAULT_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt equals to UPDATED_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt not equals to DEFAULT_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.notEquals=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt not equals to UPDATED_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.notEquals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the statisticsApiList where createAt equals to UPDATED_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt is not null
        defaultStatisticsApiShouldBeFound("createAt.specified=true");

        // Get all the statisticsApiList where createAt is null
        defaultStatisticsApiShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt is less than DEFAULT_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt is less than UPDATED_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where createAt is greater than DEFAULT_CREATE_AT
        defaultStatisticsApiShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the statisticsApiList where createAt is greater than SMALLER_CREATE_AT
        defaultStatisticsApiShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt equals to DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt equals to UPDATED_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt not equals to DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.notEquals=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt not equals to UPDATED_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.notEquals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the statisticsApiList where updateAt equals to UPDATED_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt is not null
        defaultStatisticsApiShouldBeFound("updateAt.specified=true");

        // Get all the statisticsApiList where updateAt is null
        defaultStatisticsApiShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt is greater than or equal to DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.greaterThanOrEqual=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt is greater than or equal to UPDATED_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.greaterThanOrEqual=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt is less than or equal to DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.lessThanOrEqual=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt is less than or equal to SMALLER_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.lessThanOrEqual=" + SMALLER_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt is less than DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt is less than UPDATED_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateAt is greater than DEFAULT_UPDATE_AT
        defaultStatisticsApiShouldNotBeFound("updateAt.greaterThan=" + DEFAULT_UPDATE_AT);

        // Get all the statisticsApiList where updateAt is greater than SMALLER_UPDATE_AT
        defaultStatisticsApiShouldBeFound("updateAt.greaterThan=" + SMALLER_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllStatisticsApisBySourceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where sourceType equals to DEFAULT_SOURCE_TYPE
        defaultStatisticsApiShouldBeFound("sourceType.equals=" + DEFAULT_SOURCE_TYPE);

        // Get all the statisticsApiList where sourceType equals to UPDATED_SOURCE_TYPE
        defaultStatisticsApiShouldNotBeFound("sourceType.equals=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisBySourceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where sourceType not equals to DEFAULT_SOURCE_TYPE
        defaultStatisticsApiShouldNotBeFound("sourceType.notEquals=" + DEFAULT_SOURCE_TYPE);

        // Get all the statisticsApiList where sourceType not equals to UPDATED_SOURCE_TYPE
        defaultStatisticsApiShouldBeFound("sourceType.notEquals=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisBySourceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where sourceType in DEFAULT_SOURCE_TYPE or UPDATED_SOURCE_TYPE
        defaultStatisticsApiShouldBeFound("sourceType.in=" + DEFAULT_SOURCE_TYPE + "," + UPDATED_SOURCE_TYPE);

        // Get all the statisticsApiList where sourceType equals to UPDATED_SOURCE_TYPE
        defaultStatisticsApiShouldNotBeFound("sourceType.in=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisBySourceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where sourceType is not null
        defaultStatisticsApiShouldBeFound("sourceType.specified=true");

        // Get all the statisticsApiList where sourceType is null
        defaultStatisticsApiShouldNotBeFound("sourceType.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval equals to DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.equals=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval equals to UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.equals=" + UPDATED_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval not equals to DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.notEquals=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval not equals to UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.notEquals=" + UPDATED_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval in DEFAULT_UPDATE_INTERVAL or UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.in=" + DEFAULT_UPDATE_INTERVAL + "," + UPDATED_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval equals to UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.in=" + UPDATED_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval is not null
        defaultStatisticsApiShouldBeFound("updateInterval.specified=true");

        // Get all the statisticsApiList where updateInterval is null
        defaultStatisticsApiShouldNotBeFound("updateInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval is greater than or equal to DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.greaterThanOrEqual=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval is greater than or equal to UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.greaterThanOrEqual=" + UPDATED_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval is less than or equal to DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.lessThanOrEqual=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval is less than or equal to SMALLER_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.lessThanOrEqual=" + SMALLER_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval is less than DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.lessThan=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval is less than UPDATED_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.lessThan=" + UPDATED_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByUpdateIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where updateInterval is greater than DEFAULT_UPDATE_INTERVAL
        defaultStatisticsApiShouldNotBeFound("updateInterval.greaterThan=" + DEFAULT_UPDATE_INTERVAL);

        // Get all the statisticsApiList where updateInterval is greater than SMALLER_UPDATE_INTERVAL
        defaultStatisticsApiShouldBeFound("updateInterval.greaterThan=" + SMALLER_UPDATE_INTERVAL);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime equals to DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.equals=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime equals to UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.equals=" + UPDATED_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime not equals to DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.notEquals=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime not equals to UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.notEquals=" + UPDATED_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime in DEFAULT_LAST_SQL_RUN_TIME or UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.in=" + DEFAULT_LAST_SQL_RUN_TIME + "," + UPDATED_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime equals to UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.in=" + UPDATED_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime is not null
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.specified=true");

        // Get all the statisticsApiList where lastSQLRunTime is null
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.specified=false");
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime is greater than or equal to DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.greaterThanOrEqual=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime is greater than or equal to UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.greaterThanOrEqual=" + UPDATED_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime is less than or equal to DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.lessThanOrEqual=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime is less than or equal to SMALLER_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.lessThanOrEqual=" + SMALLER_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime is less than DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.lessThan=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime is less than UPDATED_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.lessThan=" + UPDATED_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByLastSQLRunTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where lastSQLRunTime is greater than DEFAULT_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldNotBeFound("lastSQLRunTime.greaterThan=" + DEFAULT_LAST_SQL_RUN_TIME);

        // Get all the statisticsApiList where lastSQLRunTime is greater than SMALLER_LAST_SQL_RUN_TIME
        defaultStatisticsApiShouldBeFound("lastSQLRunTime.greaterThan=" + SMALLER_LAST_SQL_RUN_TIME);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where enable equals to DEFAULT_ENABLE
        defaultStatisticsApiShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the statisticsApiList where enable equals to UPDATED_ENABLE
        defaultStatisticsApiShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where enable not equals to DEFAULT_ENABLE
        defaultStatisticsApiShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the statisticsApiList where enable not equals to UPDATED_ENABLE
        defaultStatisticsApiShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultStatisticsApiShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the statisticsApiList where enable equals to UPDATED_ENABLE
        defaultStatisticsApiShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllStatisticsApisByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        // Get all the statisticsApiList where enable is not null
        defaultStatisticsApiShouldBeFound("enable.specified=true");

        // Get all the statisticsApiList where enable is null
        defaultStatisticsApiShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    public void getAllStatisticsApisByCommonTableIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);
        CommonTable commonTable = CommonTableResourceIT.createEntity();
        statisticsApi.setCommonTable(commonTable);
        statisticsApiRepository.insert(statisticsApi);
        Long commonTableId = commonTable.getId();

        // Get all the statisticsApiList where commonTable equals to commonTableId
        defaultStatisticsApiShouldBeFound("commonTableId.equals=" + commonTableId);

        // Get all the statisticsApiList where commonTable equals to (commonTableId + 1)
        defaultStatisticsApiShouldNotBeFound("commonTableId.equals=" + (commonTableId + 1));
    }

    @Test
    @Transactional
    public void getAllStatisticsApisByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);
        User creator = UserResourceIT.createEntity();
        statisticsApi.setCreator(creator);
        statisticsApiRepository.insert(statisticsApi);
        Long creatorId = creator.getId();

        // Get all the statisticsApiList where creator equals to creatorId
        defaultStatisticsApiShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the statisticsApiList where creator equals to (creatorId + 1)
        defaultStatisticsApiShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    @Test
    @Transactional
    public void getAllStatisticsApisByModifierIsEqualToSomething() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);
        User modifier = UserResourceIT.createEntity();
        statisticsApi.setModifier(modifier);
        statisticsApiRepository.insert(statisticsApi);
        Long modifierId = modifier.getId();

        // Get all the statisticsApiList where modifier equals to modifierId
        defaultStatisticsApiShouldBeFound("modifierId.equals=" + modifierId);

        // Get all the statisticsApiList where modifier equals to (modifierId + 1)
        defaultStatisticsApiShouldNotBeFound("modifierId.equals=" + (modifierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStatisticsApiShouldBeFound(String filter) throws Exception {
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statisticsApi.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].apiBody").value(hasItem(DEFAULT_API_BODY.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].updateInterval").value(hasItem(DEFAULT_UPDATE_INTERVAL)))
            .andExpect(jsonPath("$.[*].lastSQLRunTime").value(hasItem(sameInstant(DEFAULT_LAST_SQL_RUN_TIME))))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));

        // Check, that the count call also returns 1
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStatisticsApiShouldNotBeFound(String filter) throws Exception {
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStatisticsApiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStatisticsApi() throws Exception {
        // Get the statisticsApi
        restStatisticsApiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStatisticsApi() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();

        // Update the statisticsApi
        StatisticsApi updatedStatisticsApi = statisticsApiRepository.findById(statisticsApi.getId()).get();

        // Disconnect from session so that the updates on updatedStatisticsApi are not directly saved in db

        updatedStatisticsApi
            .title(UPDATED_TITLE)
            .apiKey(UPDATED_API_KEY)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .sourceType(UPDATED_SOURCE_TYPE)
            .apiBody(UPDATED_API_BODY)
            .result(UPDATED_RESULT)
            .updateInterval(UPDATED_UPDATE_INTERVAL)
            .lastSQLRunTime(UPDATED_LAST_SQL_RUN_TIME)
            .enable(UPDATED_ENABLE);
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(updatedStatisticsApi);

        restStatisticsApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statisticsApiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isOk());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
        StatisticsApi testStatisticsApi = statisticsApiList.get(statisticsApiList.size() - 1);
        assertThat(testStatisticsApi.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStatisticsApi.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testStatisticsApi.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testStatisticsApi.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testStatisticsApi.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testStatisticsApi.getApiBody()).isEqualTo(UPDATED_API_BODY);
        assertThat(testStatisticsApi.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testStatisticsApi.getUpdateInterval()).isEqualTo(UPDATED_UPDATE_INTERVAL);
        assertThat(testStatisticsApi.getLastSQLRunTime()).isEqualTo(UPDATED_LAST_SQL_RUN_TIME);
        assertThat(testStatisticsApi.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void putNonExistingngStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        restStatisticsApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statisticsApiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticsApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticsApiMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatisticsApiWithPatch() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();

        // Update the statisticsApi using partial update
        StatisticsApi partialUpdatedStatisticsApi = new StatisticsApi();
        partialUpdatedStatisticsApi.setId(statisticsApi.getId());

        partialUpdatedStatisticsApi.apiBody(UPDATED_API_BODY);

        restStatisticsApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatisticsApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatisticsApi))
            )
            .andExpect(status().isOk());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
        StatisticsApi testStatisticsApi = statisticsApiList.get(statisticsApiList.size() - 1);
        assertThat(testStatisticsApi.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStatisticsApi.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testStatisticsApi.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testStatisticsApi.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testStatisticsApi.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testStatisticsApi.getApiBody()).isEqualTo(UPDATED_API_BODY);
        assertThat(testStatisticsApi.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testStatisticsApi.getUpdateInterval()).isEqualTo(DEFAULT_UPDATE_INTERVAL);
        assertThat(testStatisticsApi.getLastSQLRunTime()).isEqualTo(DEFAULT_LAST_SQL_RUN_TIME);
        assertThat(testStatisticsApi.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void fullUpdateStatisticsApiWithPatch() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();

        // Update the statisticsApi using partial update
        StatisticsApi partialUpdatedStatisticsApi = new StatisticsApi();
        partialUpdatedStatisticsApi.setId(statisticsApi.getId());

        partialUpdatedStatisticsApi
            .title(UPDATED_TITLE)
            .apiKey(UPDATED_API_KEY)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .sourceType(UPDATED_SOURCE_TYPE)
            .apiBody(UPDATED_API_BODY)
            .result(UPDATED_RESULT)
            .updateInterval(UPDATED_UPDATE_INTERVAL)
            .lastSQLRunTime(UPDATED_LAST_SQL_RUN_TIME)
            .enable(UPDATED_ENABLE);

        restStatisticsApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatisticsApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatisticsApi))
            )
            .andExpect(status().isOk());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
        StatisticsApi testStatisticsApi = statisticsApiList.get(statisticsApiList.size() - 1);
        assertThat(testStatisticsApi.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStatisticsApi.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testStatisticsApi.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testStatisticsApi.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testStatisticsApi.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testStatisticsApi.getApiBody()).isEqualTo(UPDATED_API_BODY);
        assertThat(testStatisticsApi.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testStatisticsApi.getUpdateInterval()).isEqualTo(UPDATED_UPDATE_INTERVAL);
        assertThat(testStatisticsApi.getLastSQLRunTime()).isEqualTo(UPDATED_LAST_SQL_RUN_TIME);
        assertThat(testStatisticsApi.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void patchNonExistingStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatisticsApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statisticsApiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticsApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatisticsApi() throws Exception {
        int databaseSizeBeforeUpdate = statisticsApiRepository.findAll().size();
        statisticsApi.setId(count.incrementAndGet());

        // Create the StatisticsApi
        StatisticsApiDTO statisticsApiDTO = statisticsApiMapper.toDto(statisticsApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticsApiMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticsApiDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatisticsApi in the database
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatisticsApi() throws Exception {
        // Initialize the database
        statisticsApiRepository.insert(statisticsApi);

        int databaseSizeBeforeDelete = statisticsApiRepository.findAll().size();

        // Delete the statisticsApi
        restStatisticsApiMockMvc
            .perform(delete(ENTITY_API_URL_ID, statisticsApi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatisticsApi> statisticsApiList = statisticsApiRepository.findAll();
        assertThat(statisticsApiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
