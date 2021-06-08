package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.OssConfig;
import com.gavin.myapp.domain.enumeration.OssProvider;
import com.gavin.myapp.repository.OssConfigRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.OssConfigQueryService;
import com.gavin.myapp.service.criteria.OssConfigCriteria;
import com.gavin.myapp.service.dto.OssConfigDTO;
import com.gavin.myapp.service.mapper.OssConfigMapper;
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
 * Integration tests for the {@link OssConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class OssConfigResourceIT {

    private static final OssProvider DEFAULT_PROVIDER = OssProvider.MINIO;
    private static final OssProvider UPDATED_PROVIDER = OssProvider.QINIU;

    private static final String DEFAULT_OSS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OSS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SECRET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECRET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_BUCKET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUCKET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APP_ID = "AAAAAAAAAA";
    private static final String UPDATED_APP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/oss-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OssConfigRepository ossConfigRepository;

    @Autowired
    private OssConfigMapper ossConfigMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restOssConfigMockMvc;

    private OssConfig ossConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OssConfig createEntity() {
        OssConfig ossConfig = new OssConfig()
            .provider(DEFAULT_PROVIDER)
            .ossCode(DEFAULT_OSS_CODE)
            .endpoint(DEFAULT_ENDPOINT)
            .accessKey(DEFAULT_ACCESS_KEY)
            .secretKey(DEFAULT_SECRET_KEY)
            .bucketName(DEFAULT_BUCKET_NAME)
            .appId(DEFAULT_APP_ID)
            .region(DEFAULT_REGION)
            .remark(DEFAULT_REMARK)
            .enabled(DEFAULT_ENABLED);
        return ossConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OssConfig createUpdatedEntity() {
        OssConfig ossConfig = new OssConfig()
            .provider(UPDATED_PROVIDER)
            .ossCode(UPDATED_OSS_CODE)
            .endpoint(UPDATED_ENDPOINT)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .bucketName(UPDATED_BUCKET_NAME)
            .appId(UPDATED_APP_ID)
            .region(UPDATED_REGION)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        return ossConfig;
    }

    @BeforeEach
    public void initTest() {
        ossConfig = createEntity();
    }

    @Test
    @Transactional
    void createOssConfig() throws Exception {
        int databaseSizeBeforeCreate = ossConfigRepository.findAll().size();
        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);
        restOssConfigMockMvc
            .perform(
                post(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeCreate + 1);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testOssConfig.getOssCode()).isEqualTo(DEFAULT_OSS_CODE);
        assertThat(testOssConfig.getEndpoint()).isEqualTo(DEFAULT_ENDPOINT);
        assertThat(testOssConfig.getAccessKey()).isEqualTo(DEFAULT_ACCESS_KEY);
        assertThat(testOssConfig.getSecretKey()).isEqualTo(DEFAULT_SECRET_KEY);
        assertThat(testOssConfig.getBucketName()).isEqualTo(DEFAULT_BUCKET_NAME);
        assertThat(testOssConfig.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testOssConfig.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testOssConfig.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testOssConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createOssConfigWithExistingId() throws Exception {
        // Create the OssConfig with an existing ID
        ossConfig.setId(1L);
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        int databaseSizeBeforeCreate = ossConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOssConfigs() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ossConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].ossCode").value(hasItem(DEFAULT_OSS_CODE)))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].bucketName").value(hasItem(DEFAULT_BUCKET_NAME)))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getOssConfig() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get the ossConfig
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, ossConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ossConfig.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.ossCode").value(DEFAULT_OSS_CODE))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT))
            .andExpect(jsonPath("$.accessKey").value(DEFAULT_ACCESS_KEY))
            .andExpect(jsonPath("$.secretKey").value(DEFAULT_SECRET_KEY))
            .andExpect(jsonPath("$.bucketName").value(DEFAULT_BUCKET_NAME))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getOssConfigsByIdFiltering() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        Long id = ossConfig.getId();

        defaultOssConfigShouldBeFound("id.equals=" + id);
        defaultOssConfigShouldNotBeFound("id.notEquals=" + id);

        defaultOssConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOssConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultOssConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOssConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where provider equals to DEFAULT_PROVIDER
        defaultOssConfigShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the ossConfigList where provider equals to UPDATED_PROVIDER
        defaultOssConfigShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where provider not equals to DEFAULT_PROVIDER
        defaultOssConfigShouldNotBeFound("provider.notEquals=" + DEFAULT_PROVIDER);

        // Get all the ossConfigList where provider not equals to UPDATED_PROVIDER
        defaultOssConfigShouldBeFound("provider.notEquals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultOssConfigShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the ossConfigList where provider equals to UPDATED_PROVIDER
        defaultOssConfigShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where provider is not null
        defaultOssConfigShouldBeFound("provider.specified=true");

        // Get all the ossConfigList where provider is null
        defaultOssConfigShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode equals to DEFAULT_OSS_CODE
        defaultOssConfigShouldBeFound("ossCode.equals=" + DEFAULT_OSS_CODE);

        // Get all the ossConfigList where ossCode equals to UPDATED_OSS_CODE
        defaultOssConfigShouldNotBeFound("ossCode.equals=" + UPDATED_OSS_CODE);
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode not equals to DEFAULT_OSS_CODE
        defaultOssConfigShouldNotBeFound("ossCode.notEquals=" + DEFAULT_OSS_CODE);

        // Get all the ossConfigList where ossCode not equals to UPDATED_OSS_CODE
        defaultOssConfigShouldBeFound("ossCode.notEquals=" + UPDATED_OSS_CODE);
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode in DEFAULT_OSS_CODE or UPDATED_OSS_CODE
        defaultOssConfigShouldBeFound("ossCode.in=" + DEFAULT_OSS_CODE + "," + UPDATED_OSS_CODE);

        // Get all the ossConfigList where ossCode equals to UPDATED_OSS_CODE
        defaultOssConfigShouldNotBeFound("ossCode.in=" + UPDATED_OSS_CODE);
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode is not null
        defaultOssConfigShouldBeFound("ossCode.specified=true");

        // Get all the ossConfigList where ossCode is null
        defaultOssConfigShouldNotBeFound("ossCode.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode contains DEFAULT_OSS_CODE
        defaultOssConfigShouldBeFound("ossCode.contains=" + DEFAULT_OSS_CODE);

        // Get all the ossConfigList where ossCode contains UPDATED_OSS_CODE
        defaultOssConfigShouldNotBeFound("ossCode.contains=" + UPDATED_OSS_CODE);
    }

    @Test
    @Transactional
    void getAllOssConfigsByOssCodeNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where ossCode does not contain DEFAULT_OSS_CODE
        defaultOssConfigShouldNotBeFound("ossCode.doesNotContain=" + DEFAULT_OSS_CODE);

        // Get all the ossConfigList where ossCode does not contain UPDATED_OSS_CODE
        defaultOssConfigShouldBeFound("ossCode.doesNotContain=" + UPDATED_OSS_CODE);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint equals to DEFAULT_ENDPOINT
        defaultOssConfigShouldBeFound("endpoint.equals=" + DEFAULT_ENDPOINT);

        // Get all the ossConfigList where endpoint equals to UPDATED_ENDPOINT
        defaultOssConfigShouldNotBeFound("endpoint.equals=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint not equals to DEFAULT_ENDPOINT
        defaultOssConfigShouldNotBeFound("endpoint.notEquals=" + DEFAULT_ENDPOINT);

        // Get all the ossConfigList where endpoint not equals to UPDATED_ENDPOINT
        defaultOssConfigShouldBeFound("endpoint.notEquals=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint in DEFAULT_ENDPOINT or UPDATED_ENDPOINT
        defaultOssConfigShouldBeFound("endpoint.in=" + DEFAULT_ENDPOINT + "," + UPDATED_ENDPOINT);

        // Get all the ossConfigList where endpoint equals to UPDATED_ENDPOINT
        defaultOssConfigShouldNotBeFound("endpoint.in=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint is not null
        defaultOssConfigShouldBeFound("endpoint.specified=true");

        // Get all the ossConfigList where endpoint is null
        defaultOssConfigShouldNotBeFound("endpoint.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint contains DEFAULT_ENDPOINT
        defaultOssConfigShouldBeFound("endpoint.contains=" + DEFAULT_ENDPOINT);

        // Get all the ossConfigList where endpoint contains UPDATED_ENDPOINT
        defaultOssConfigShouldNotBeFound("endpoint.contains=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEndpointNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where endpoint does not contain DEFAULT_ENDPOINT
        defaultOssConfigShouldNotBeFound("endpoint.doesNotContain=" + DEFAULT_ENDPOINT);

        // Get all the ossConfigList where endpoint does not contain UPDATED_ENDPOINT
        defaultOssConfigShouldBeFound("endpoint.doesNotContain=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey equals to DEFAULT_ACCESS_KEY
        defaultOssConfigShouldBeFound("accessKey.equals=" + DEFAULT_ACCESS_KEY);

        // Get all the ossConfigList where accessKey equals to UPDATED_ACCESS_KEY
        defaultOssConfigShouldNotBeFound("accessKey.equals=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey not equals to DEFAULT_ACCESS_KEY
        defaultOssConfigShouldNotBeFound("accessKey.notEquals=" + DEFAULT_ACCESS_KEY);

        // Get all the ossConfigList where accessKey not equals to UPDATED_ACCESS_KEY
        defaultOssConfigShouldBeFound("accessKey.notEquals=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey in DEFAULT_ACCESS_KEY or UPDATED_ACCESS_KEY
        defaultOssConfigShouldBeFound("accessKey.in=" + DEFAULT_ACCESS_KEY + "," + UPDATED_ACCESS_KEY);

        // Get all the ossConfigList where accessKey equals to UPDATED_ACCESS_KEY
        defaultOssConfigShouldNotBeFound("accessKey.in=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey is not null
        defaultOssConfigShouldBeFound("accessKey.specified=true");

        // Get all the ossConfigList where accessKey is null
        defaultOssConfigShouldNotBeFound("accessKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey contains DEFAULT_ACCESS_KEY
        defaultOssConfigShouldBeFound("accessKey.contains=" + DEFAULT_ACCESS_KEY);

        // Get all the ossConfigList where accessKey contains UPDATED_ACCESS_KEY
        defaultOssConfigShouldNotBeFound("accessKey.contains=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAccessKeyNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where accessKey does not contain DEFAULT_ACCESS_KEY
        defaultOssConfigShouldNotBeFound("accessKey.doesNotContain=" + DEFAULT_ACCESS_KEY);

        // Get all the ossConfigList where accessKey does not contain UPDATED_ACCESS_KEY
        defaultOssConfigShouldBeFound("accessKey.doesNotContain=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey equals to DEFAULT_SECRET_KEY
        defaultOssConfigShouldBeFound("secretKey.equals=" + DEFAULT_SECRET_KEY);

        // Get all the ossConfigList where secretKey equals to UPDATED_SECRET_KEY
        defaultOssConfigShouldNotBeFound("secretKey.equals=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey not equals to DEFAULT_SECRET_KEY
        defaultOssConfigShouldNotBeFound("secretKey.notEquals=" + DEFAULT_SECRET_KEY);

        // Get all the ossConfigList where secretKey not equals to UPDATED_SECRET_KEY
        defaultOssConfigShouldBeFound("secretKey.notEquals=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey in DEFAULT_SECRET_KEY or UPDATED_SECRET_KEY
        defaultOssConfigShouldBeFound("secretKey.in=" + DEFAULT_SECRET_KEY + "," + UPDATED_SECRET_KEY);

        // Get all the ossConfigList where secretKey equals to UPDATED_SECRET_KEY
        defaultOssConfigShouldNotBeFound("secretKey.in=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey is not null
        defaultOssConfigShouldBeFound("secretKey.specified=true");

        // Get all the ossConfigList where secretKey is null
        defaultOssConfigShouldNotBeFound("secretKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey contains DEFAULT_SECRET_KEY
        defaultOssConfigShouldBeFound("secretKey.contains=" + DEFAULT_SECRET_KEY);

        // Get all the ossConfigList where secretKey contains UPDATED_SECRET_KEY
        defaultOssConfigShouldNotBeFound("secretKey.contains=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsBySecretKeyNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where secretKey does not contain DEFAULT_SECRET_KEY
        defaultOssConfigShouldNotBeFound("secretKey.doesNotContain=" + DEFAULT_SECRET_KEY);

        // Get all the ossConfigList where secretKey does not contain UPDATED_SECRET_KEY
        defaultOssConfigShouldBeFound("secretKey.doesNotContain=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName equals to DEFAULT_BUCKET_NAME
        defaultOssConfigShouldBeFound("bucketName.equals=" + DEFAULT_BUCKET_NAME);

        // Get all the ossConfigList where bucketName equals to UPDATED_BUCKET_NAME
        defaultOssConfigShouldNotBeFound("bucketName.equals=" + UPDATED_BUCKET_NAME);
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName not equals to DEFAULT_BUCKET_NAME
        defaultOssConfigShouldNotBeFound("bucketName.notEquals=" + DEFAULT_BUCKET_NAME);

        // Get all the ossConfigList where bucketName not equals to UPDATED_BUCKET_NAME
        defaultOssConfigShouldBeFound("bucketName.notEquals=" + UPDATED_BUCKET_NAME);
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName in DEFAULT_BUCKET_NAME or UPDATED_BUCKET_NAME
        defaultOssConfigShouldBeFound("bucketName.in=" + DEFAULT_BUCKET_NAME + "," + UPDATED_BUCKET_NAME);

        // Get all the ossConfigList where bucketName equals to UPDATED_BUCKET_NAME
        defaultOssConfigShouldNotBeFound("bucketName.in=" + UPDATED_BUCKET_NAME);
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName is not null
        defaultOssConfigShouldBeFound("bucketName.specified=true");

        // Get all the ossConfigList where bucketName is null
        defaultOssConfigShouldNotBeFound("bucketName.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName contains DEFAULT_BUCKET_NAME
        defaultOssConfigShouldBeFound("bucketName.contains=" + DEFAULT_BUCKET_NAME);

        // Get all the ossConfigList where bucketName contains UPDATED_BUCKET_NAME
        defaultOssConfigShouldNotBeFound("bucketName.contains=" + UPDATED_BUCKET_NAME);
    }

    @Test
    @Transactional
    void getAllOssConfigsByBucketNameNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where bucketName does not contain DEFAULT_BUCKET_NAME
        defaultOssConfigShouldNotBeFound("bucketName.doesNotContain=" + DEFAULT_BUCKET_NAME);

        // Get all the ossConfigList where bucketName does not contain UPDATED_BUCKET_NAME
        defaultOssConfigShouldBeFound("bucketName.doesNotContain=" + UPDATED_BUCKET_NAME);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId equals to DEFAULT_APP_ID
        defaultOssConfigShouldBeFound("appId.equals=" + DEFAULT_APP_ID);

        // Get all the ossConfigList where appId equals to UPDATED_APP_ID
        defaultOssConfigShouldNotBeFound("appId.equals=" + UPDATED_APP_ID);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId not equals to DEFAULT_APP_ID
        defaultOssConfigShouldNotBeFound("appId.notEquals=" + DEFAULT_APP_ID);

        // Get all the ossConfigList where appId not equals to UPDATED_APP_ID
        defaultOssConfigShouldBeFound("appId.notEquals=" + UPDATED_APP_ID);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId in DEFAULT_APP_ID or UPDATED_APP_ID
        defaultOssConfigShouldBeFound("appId.in=" + DEFAULT_APP_ID + "," + UPDATED_APP_ID);

        // Get all the ossConfigList where appId equals to UPDATED_APP_ID
        defaultOssConfigShouldNotBeFound("appId.in=" + UPDATED_APP_ID);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId is not null
        defaultOssConfigShouldBeFound("appId.specified=true");

        // Get all the ossConfigList where appId is null
        defaultOssConfigShouldNotBeFound("appId.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId contains DEFAULT_APP_ID
        defaultOssConfigShouldBeFound("appId.contains=" + DEFAULT_APP_ID);

        // Get all the ossConfigList where appId contains UPDATED_APP_ID
        defaultOssConfigShouldNotBeFound("appId.contains=" + UPDATED_APP_ID);
    }

    @Test
    @Transactional
    void getAllOssConfigsByAppIdNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where appId does not contain DEFAULT_APP_ID
        defaultOssConfigShouldNotBeFound("appId.doesNotContain=" + DEFAULT_APP_ID);

        // Get all the ossConfigList where appId does not contain UPDATED_APP_ID
        defaultOssConfigShouldBeFound("appId.doesNotContain=" + UPDATED_APP_ID);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region equals to DEFAULT_REGION
        defaultOssConfigShouldBeFound("region.equals=" + DEFAULT_REGION);

        // Get all the ossConfigList where region equals to UPDATED_REGION
        defaultOssConfigShouldNotBeFound("region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region not equals to DEFAULT_REGION
        defaultOssConfigShouldNotBeFound("region.notEquals=" + DEFAULT_REGION);

        // Get all the ossConfigList where region not equals to UPDATED_REGION
        defaultOssConfigShouldBeFound("region.notEquals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region in DEFAULT_REGION or UPDATED_REGION
        defaultOssConfigShouldBeFound("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION);

        // Get all the ossConfigList where region equals to UPDATED_REGION
        defaultOssConfigShouldNotBeFound("region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region is not null
        defaultOssConfigShouldBeFound("region.specified=true");

        // Get all the ossConfigList where region is null
        defaultOssConfigShouldNotBeFound("region.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region contains DEFAULT_REGION
        defaultOssConfigShouldBeFound("region.contains=" + DEFAULT_REGION);

        // Get all the ossConfigList where region contains UPDATED_REGION
        defaultOssConfigShouldNotBeFound("region.contains=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRegionNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where region does not contain DEFAULT_REGION
        defaultOssConfigShouldNotBeFound("region.doesNotContain=" + DEFAULT_REGION);

        // Get all the ossConfigList where region does not contain UPDATED_REGION
        defaultOssConfigShouldBeFound("region.doesNotContain=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark equals to DEFAULT_REMARK
        defaultOssConfigShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark equals to UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark not equals to DEFAULT_REMARK
        defaultOssConfigShouldNotBeFound("remark.notEquals=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark not equals to UPDATED_REMARK
        defaultOssConfigShouldBeFound("remark.notEquals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultOssConfigShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the ossConfigList where remark equals to UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark is not null
        defaultOssConfigShouldBeFound("remark.specified=true");

        // Get all the ossConfigList where remark is null
        defaultOssConfigShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark contains DEFAULT_REMARK
        defaultOssConfigShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark contains UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where remark does not contain DEFAULT_REMARK
        defaultOssConfigShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark does not contain UPDATED_REMARK
        defaultOssConfigShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where enabled equals to DEFAULT_ENABLED
        defaultOssConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the ossConfigList where enabled equals to UPDATED_ENABLED
        defaultOssConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where enabled not equals to DEFAULT_ENABLED
        defaultOssConfigShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the ossConfigList where enabled not equals to UPDATED_ENABLED
        defaultOssConfigShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultOssConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the ossConfigList where enabled equals to UPDATED_ENABLED
        defaultOssConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        // Get all the ossConfigList where enabled is not null
        defaultOssConfigShouldBeFound("enabled.specified=true");

        // Get all the ossConfigList where enabled is null
        defaultOssConfigShouldNotBeFound("enabled.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOssConfigShouldBeFound(String filter) throws Exception {
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ossConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].ossCode").value(hasItem(DEFAULT_OSS_CODE)))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].bucketName").value(hasItem(DEFAULT_BUCKET_NAME)))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOssConfigShouldNotBeFound(String filter) throws Exception {
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOssConfig() throws Exception {
        // Get the ossConfig
        restOssConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOssConfig() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

        // Update the ossConfig
        OssConfig updatedOssConfig = ossConfigRepository.findById(ossConfig.getId()).get();

        // Disconnect from session so that the updates on updatedOssConfig are not directly saved in db

        updatedOssConfig
            .provider(UPDATED_PROVIDER)
            .ossCode(UPDATED_OSS_CODE)
            .endpoint(UPDATED_ENDPOINT)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .bucketName(UPDATED_BUCKET_NAME)
            .appId(UPDATED_APP_ID)
            .region(UPDATED_REGION)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(updatedOssConfig);

        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testOssConfig.getOssCode()).isEqualTo(UPDATED_OSS_CODE);
        assertThat(testOssConfig.getEndpoint()).isEqualTo(UPDATED_ENDPOINT);
        assertThat(testOssConfig.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testOssConfig.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testOssConfig.getBucketName()).isEqualTo(UPDATED_BUCKET_NAME);
        assertThat(testOssConfig.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testOssConfig.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingngOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

        // Update the ossConfig using partial update
        OssConfig partialUpdatedOssConfig = new OssConfig();
        partialUpdatedOssConfig.setId(ossConfig.getId());

        partialUpdatedOssConfig
            .ossCode(UPDATED_OSS_CODE)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .bucketName(UPDATED_BUCKET_NAME)
            .appId(UPDATED_APP_ID)
            .remark(UPDATED_REMARK);

        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOssConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testOssConfig.getOssCode()).isEqualTo(UPDATED_OSS_CODE);
        assertThat(testOssConfig.getEndpoint()).isEqualTo(DEFAULT_ENDPOINT);
        assertThat(testOssConfig.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testOssConfig.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testOssConfig.getBucketName()).isEqualTo(UPDATED_BUCKET_NAME);
        assertThat(testOssConfig.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testOssConfig.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

        // Update the ossConfig using partial update
        OssConfig partialUpdatedOssConfig = new OssConfig();
        partialUpdatedOssConfig.setId(ossConfig.getId());

        partialUpdatedOssConfig
            .provider(UPDATED_PROVIDER)
            .ossCode(UPDATED_OSS_CODE)
            .endpoint(UPDATED_ENDPOINT)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .bucketName(UPDATED_BUCKET_NAME)
            .appId(UPDATED_APP_ID)
            .region(UPDATED_REGION)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);

        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOssConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testOssConfig.getOssCode()).isEqualTo(UPDATED_OSS_CODE);
        assertThat(testOssConfig.getEndpoint()).isEqualTo(UPDATED_ENDPOINT);
        assertThat(testOssConfig.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testOssConfig.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testOssConfig.getBucketName()).isEqualTo(UPDATED_BUCKET_NAME);
        assertThat(testOssConfig.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testOssConfig.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(count.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOssConfig() throws Exception {
        // Initialize the database
        ossConfigRepository.insert(ossConfig);

        int databaseSizeBeforeDelete = ossConfigRepository.findAll().size();

        // Delete the ossConfig
        restOssConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, ossConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
