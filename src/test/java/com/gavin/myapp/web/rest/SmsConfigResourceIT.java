package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.SmsConfig;
import com.gavin.myapp.domain.enumeration.SmsProvider;
import com.gavin.myapp.repository.SmsConfigRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.SmsConfigQueryService;
import com.gavin.myapp.service.criteria.SmsConfigCriteria;
import com.gavin.myapp.service.dto.SmsConfigDTO;
import com.gavin.myapp.service.mapper.SmsConfigMapper;
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
 * Integration tests for the {@link SmsConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class SmsConfigResourceIT {

    private static final SmsProvider DEFAULT_PROVIDER = SmsProvider.YUNPIAN;
    private static final SmsProvider UPDATED_PROVIDER = SmsProvider.QINIU;

    private static final String DEFAULT_SMS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SMS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SECRET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECRET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_REGION_ID = "AAAAAAAAAA";
    private static final String UPDATED_REGION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SIGN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/sms-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SmsConfigRepository smsConfigRepository;

    @Autowired
    private SmsConfigMapper smsConfigMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restSmsConfigMockMvc;

    private SmsConfig smsConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsConfig createEntity() {
        SmsConfig smsConfig = new SmsConfig()
            .provider(DEFAULT_PROVIDER)
            .smsCode(DEFAULT_SMS_CODE)
            .templateId(DEFAULT_TEMPLATE_ID)
            .accessKey(DEFAULT_ACCESS_KEY)
            .secretKey(DEFAULT_SECRET_KEY)
            .regionId(DEFAULT_REGION_ID)
            .signName(DEFAULT_SIGN_NAME)
            .remark(DEFAULT_REMARK)
            .enabled(DEFAULT_ENABLED);
        return smsConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsConfig createUpdatedEntity() {
        SmsConfig smsConfig = new SmsConfig()
            .provider(UPDATED_PROVIDER)
            .smsCode(UPDATED_SMS_CODE)
            .templateId(UPDATED_TEMPLATE_ID)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .regionId(UPDATED_REGION_ID)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        return smsConfig;
    }

    @BeforeEach
    public void initTest() {
        smsConfig = createEntity();
    }

    @Test
    @Transactional
    void createSmsConfig() throws Exception {
        int databaseSizeBeforeCreate = smsConfigRepository.findAll().size();
        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);
        restSmsConfigMockMvc
            .perform(
                post(ENTITY_API_URL_ID, smsConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SmsConfig testSmsConfig = smsConfigList.get(smsConfigList.size() - 1);
        assertThat(testSmsConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testSmsConfig.getSmsCode()).isEqualTo(DEFAULT_SMS_CODE);
        assertThat(testSmsConfig.getTemplateId()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testSmsConfig.getAccessKey()).isEqualTo(DEFAULT_ACCESS_KEY);
        assertThat(testSmsConfig.getSecretKey()).isEqualTo(DEFAULT_SECRET_KEY);
        assertThat(testSmsConfig.getRegionId()).isEqualTo(DEFAULT_REGION_ID);
        assertThat(testSmsConfig.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testSmsConfig.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testSmsConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createSmsConfigWithExistingId() throws Exception {
        // Create the SmsConfig with an existing ID
        smsConfig.setId(1L);
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        int databaseSizeBeforeCreate = smsConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmsConfigs() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].smsCode").value(hasItem(DEFAULT_SMS_CODE)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].regionId").value(hasItem(DEFAULT_REGION_ID)))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSmsConfig() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get the smsConfig
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, smsConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsConfig.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.smsCode").value(DEFAULT_SMS_CODE))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.accessKey").value(DEFAULT_ACCESS_KEY))
            .andExpect(jsonPath("$.secretKey").value(DEFAULT_SECRET_KEY))
            .andExpect(jsonPath("$.regionId").value(DEFAULT_REGION_ID))
            .andExpect(jsonPath("$.signName").value(DEFAULT_SIGN_NAME))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getSmsConfigsByIdFiltering() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        Long id = smsConfig.getId();

        defaultSmsConfigShouldBeFound("id.equals=" + id);
        defaultSmsConfigShouldNotBeFound("id.notEquals=" + id);

        defaultSmsConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSmsConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultSmsConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSmsConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where provider equals to DEFAULT_PROVIDER
        defaultSmsConfigShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the smsConfigList where provider equals to UPDATED_PROVIDER
        defaultSmsConfigShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByProviderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where provider not equals to DEFAULT_PROVIDER
        defaultSmsConfigShouldNotBeFound("provider.notEquals=" + DEFAULT_PROVIDER);

        // Get all the smsConfigList where provider not equals to UPDATED_PROVIDER
        defaultSmsConfigShouldBeFound("provider.notEquals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultSmsConfigShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the smsConfigList where provider equals to UPDATED_PROVIDER
        defaultSmsConfigShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where provider is not null
        defaultSmsConfigShouldBeFound("provider.specified=true");

        // Get all the smsConfigList where provider is null
        defaultSmsConfigShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode equals to DEFAULT_SMS_CODE
        defaultSmsConfigShouldBeFound("smsCode.equals=" + DEFAULT_SMS_CODE);

        // Get all the smsConfigList where smsCode equals to UPDATED_SMS_CODE
        defaultSmsConfigShouldNotBeFound("smsCode.equals=" + UPDATED_SMS_CODE);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode not equals to DEFAULT_SMS_CODE
        defaultSmsConfigShouldNotBeFound("smsCode.notEquals=" + DEFAULT_SMS_CODE);

        // Get all the smsConfigList where smsCode not equals to UPDATED_SMS_CODE
        defaultSmsConfigShouldBeFound("smsCode.notEquals=" + UPDATED_SMS_CODE);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode in DEFAULT_SMS_CODE or UPDATED_SMS_CODE
        defaultSmsConfigShouldBeFound("smsCode.in=" + DEFAULT_SMS_CODE + "," + UPDATED_SMS_CODE);

        // Get all the smsConfigList where smsCode equals to UPDATED_SMS_CODE
        defaultSmsConfigShouldNotBeFound("smsCode.in=" + UPDATED_SMS_CODE);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode is not null
        defaultSmsConfigShouldBeFound("smsCode.specified=true");

        // Get all the smsConfigList where smsCode is null
        defaultSmsConfigShouldNotBeFound("smsCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode contains DEFAULT_SMS_CODE
        defaultSmsConfigShouldBeFound("smsCode.contains=" + DEFAULT_SMS_CODE);

        // Get all the smsConfigList where smsCode contains UPDATED_SMS_CODE
        defaultSmsConfigShouldNotBeFound("smsCode.contains=" + UPDATED_SMS_CODE);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySmsCodeNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where smsCode does not contain DEFAULT_SMS_CODE
        defaultSmsConfigShouldNotBeFound("smsCode.doesNotContain=" + DEFAULT_SMS_CODE);

        // Get all the smsConfigList where smsCode does not contain UPDATED_SMS_CODE
        defaultSmsConfigShouldBeFound("smsCode.doesNotContain=" + UPDATED_SMS_CODE);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId equals to DEFAULT_TEMPLATE_ID
        defaultSmsConfigShouldBeFound("templateId.equals=" + DEFAULT_TEMPLATE_ID);

        // Get all the smsConfigList where templateId equals to UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldNotBeFound("templateId.equals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId not equals to DEFAULT_TEMPLATE_ID
        defaultSmsConfigShouldNotBeFound("templateId.notEquals=" + DEFAULT_TEMPLATE_ID);

        // Get all the smsConfigList where templateId not equals to UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldBeFound("templateId.notEquals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId in DEFAULT_TEMPLATE_ID or UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldBeFound("templateId.in=" + DEFAULT_TEMPLATE_ID + "," + UPDATED_TEMPLATE_ID);

        // Get all the smsConfigList where templateId equals to UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldNotBeFound("templateId.in=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId is not null
        defaultSmsConfigShouldBeFound("templateId.specified=true");

        // Get all the smsConfigList where templateId is null
        defaultSmsConfigShouldNotBeFound("templateId.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId contains DEFAULT_TEMPLATE_ID
        defaultSmsConfigShouldBeFound("templateId.contains=" + DEFAULT_TEMPLATE_ID);

        // Get all the smsConfigList where templateId contains UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldNotBeFound("templateId.contains=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByTemplateIdNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where templateId does not contain DEFAULT_TEMPLATE_ID
        defaultSmsConfigShouldNotBeFound("templateId.doesNotContain=" + DEFAULT_TEMPLATE_ID);

        // Get all the smsConfigList where templateId does not contain UPDATED_TEMPLATE_ID
        defaultSmsConfigShouldBeFound("templateId.doesNotContain=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey equals to DEFAULT_ACCESS_KEY
        defaultSmsConfigShouldBeFound("accessKey.equals=" + DEFAULT_ACCESS_KEY);

        // Get all the smsConfigList where accessKey equals to UPDATED_ACCESS_KEY
        defaultSmsConfigShouldNotBeFound("accessKey.equals=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey not equals to DEFAULT_ACCESS_KEY
        defaultSmsConfigShouldNotBeFound("accessKey.notEquals=" + DEFAULT_ACCESS_KEY);

        // Get all the smsConfigList where accessKey not equals to UPDATED_ACCESS_KEY
        defaultSmsConfigShouldBeFound("accessKey.notEquals=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey in DEFAULT_ACCESS_KEY or UPDATED_ACCESS_KEY
        defaultSmsConfigShouldBeFound("accessKey.in=" + DEFAULT_ACCESS_KEY + "," + UPDATED_ACCESS_KEY);

        // Get all the smsConfigList where accessKey equals to UPDATED_ACCESS_KEY
        defaultSmsConfigShouldNotBeFound("accessKey.in=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey is not null
        defaultSmsConfigShouldBeFound("accessKey.specified=true");

        // Get all the smsConfigList where accessKey is null
        defaultSmsConfigShouldNotBeFound("accessKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey contains DEFAULT_ACCESS_KEY
        defaultSmsConfigShouldBeFound("accessKey.contains=" + DEFAULT_ACCESS_KEY);

        // Get all the smsConfigList where accessKey contains UPDATED_ACCESS_KEY
        defaultSmsConfigShouldNotBeFound("accessKey.contains=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByAccessKeyNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where accessKey does not contain DEFAULT_ACCESS_KEY
        defaultSmsConfigShouldNotBeFound("accessKey.doesNotContain=" + DEFAULT_ACCESS_KEY);

        // Get all the smsConfigList where accessKey does not contain UPDATED_ACCESS_KEY
        defaultSmsConfigShouldBeFound("accessKey.doesNotContain=" + UPDATED_ACCESS_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey equals to DEFAULT_SECRET_KEY
        defaultSmsConfigShouldBeFound("secretKey.equals=" + DEFAULT_SECRET_KEY);

        // Get all the smsConfigList where secretKey equals to UPDATED_SECRET_KEY
        defaultSmsConfigShouldNotBeFound("secretKey.equals=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey not equals to DEFAULT_SECRET_KEY
        defaultSmsConfigShouldNotBeFound("secretKey.notEquals=" + DEFAULT_SECRET_KEY);

        // Get all the smsConfigList where secretKey not equals to UPDATED_SECRET_KEY
        defaultSmsConfigShouldBeFound("secretKey.notEquals=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey in DEFAULT_SECRET_KEY or UPDATED_SECRET_KEY
        defaultSmsConfigShouldBeFound("secretKey.in=" + DEFAULT_SECRET_KEY + "," + UPDATED_SECRET_KEY);

        // Get all the smsConfigList where secretKey equals to UPDATED_SECRET_KEY
        defaultSmsConfigShouldNotBeFound("secretKey.in=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey is not null
        defaultSmsConfigShouldBeFound("secretKey.specified=true");

        // Get all the smsConfigList where secretKey is null
        defaultSmsConfigShouldNotBeFound("secretKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey contains DEFAULT_SECRET_KEY
        defaultSmsConfigShouldBeFound("secretKey.contains=" + DEFAULT_SECRET_KEY);

        // Get all the smsConfigList where secretKey contains UPDATED_SECRET_KEY
        defaultSmsConfigShouldNotBeFound("secretKey.contains=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySecretKeyNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where secretKey does not contain DEFAULT_SECRET_KEY
        defaultSmsConfigShouldNotBeFound("secretKey.doesNotContain=" + DEFAULT_SECRET_KEY);

        // Get all the smsConfigList where secretKey does not contain UPDATED_SECRET_KEY
        defaultSmsConfigShouldBeFound("secretKey.doesNotContain=" + UPDATED_SECRET_KEY);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId equals to DEFAULT_REGION_ID
        defaultSmsConfigShouldBeFound("regionId.equals=" + DEFAULT_REGION_ID);

        // Get all the smsConfigList where regionId equals to UPDATED_REGION_ID
        defaultSmsConfigShouldNotBeFound("regionId.equals=" + UPDATED_REGION_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId not equals to DEFAULT_REGION_ID
        defaultSmsConfigShouldNotBeFound("regionId.notEquals=" + DEFAULT_REGION_ID);

        // Get all the smsConfigList where regionId not equals to UPDATED_REGION_ID
        defaultSmsConfigShouldBeFound("regionId.notEquals=" + UPDATED_REGION_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId in DEFAULT_REGION_ID or UPDATED_REGION_ID
        defaultSmsConfigShouldBeFound("regionId.in=" + DEFAULT_REGION_ID + "," + UPDATED_REGION_ID);

        // Get all the smsConfigList where regionId equals to UPDATED_REGION_ID
        defaultSmsConfigShouldNotBeFound("regionId.in=" + UPDATED_REGION_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId is not null
        defaultSmsConfigShouldBeFound("regionId.specified=true");

        // Get all the smsConfigList where regionId is null
        defaultSmsConfigShouldNotBeFound("regionId.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId contains DEFAULT_REGION_ID
        defaultSmsConfigShouldBeFound("regionId.contains=" + DEFAULT_REGION_ID);

        // Get all the smsConfigList where regionId contains UPDATED_REGION_ID
        defaultSmsConfigShouldNotBeFound("regionId.contains=" + UPDATED_REGION_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRegionIdNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where regionId does not contain DEFAULT_REGION_ID
        defaultSmsConfigShouldNotBeFound("regionId.doesNotContain=" + DEFAULT_REGION_ID);

        // Get all the smsConfigList where regionId does not contain UPDATED_REGION_ID
        defaultSmsConfigShouldBeFound("regionId.doesNotContain=" + UPDATED_REGION_ID);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName equals to DEFAULT_SIGN_NAME
        defaultSmsConfigShouldBeFound("signName.equals=" + DEFAULT_SIGN_NAME);

        // Get all the smsConfigList where signName equals to UPDATED_SIGN_NAME
        defaultSmsConfigShouldNotBeFound("signName.equals=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName not equals to DEFAULT_SIGN_NAME
        defaultSmsConfigShouldNotBeFound("signName.notEquals=" + DEFAULT_SIGN_NAME);

        // Get all the smsConfigList where signName not equals to UPDATED_SIGN_NAME
        defaultSmsConfigShouldBeFound("signName.notEquals=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName in DEFAULT_SIGN_NAME or UPDATED_SIGN_NAME
        defaultSmsConfigShouldBeFound("signName.in=" + DEFAULT_SIGN_NAME + "," + UPDATED_SIGN_NAME);

        // Get all the smsConfigList where signName equals to UPDATED_SIGN_NAME
        defaultSmsConfigShouldNotBeFound("signName.in=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName is not null
        defaultSmsConfigShouldBeFound("signName.specified=true");

        // Get all the smsConfigList where signName is null
        defaultSmsConfigShouldNotBeFound("signName.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName contains DEFAULT_SIGN_NAME
        defaultSmsConfigShouldBeFound("signName.contains=" + DEFAULT_SIGN_NAME);

        // Get all the smsConfigList where signName contains UPDATED_SIGN_NAME
        defaultSmsConfigShouldNotBeFound("signName.contains=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsConfigsBySignNameNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where signName does not contain DEFAULT_SIGN_NAME
        defaultSmsConfigShouldNotBeFound("signName.doesNotContain=" + DEFAULT_SIGN_NAME);

        // Get all the smsConfigList where signName does not contain UPDATED_SIGN_NAME
        defaultSmsConfigShouldBeFound("signName.doesNotContain=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark equals to DEFAULT_REMARK
        defaultSmsConfigShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the smsConfigList where remark equals to UPDATED_REMARK
        defaultSmsConfigShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark not equals to DEFAULT_REMARK
        defaultSmsConfigShouldNotBeFound("remark.notEquals=" + DEFAULT_REMARK);

        // Get all the smsConfigList where remark not equals to UPDATED_REMARK
        defaultSmsConfigShouldBeFound("remark.notEquals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultSmsConfigShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the smsConfigList where remark equals to UPDATED_REMARK
        defaultSmsConfigShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark is not null
        defaultSmsConfigShouldBeFound("remark.specified=true");

        // Get all the smsConfigList where remark is null
        defaultSmsConfigShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark contains DEFAULT_REMARK
        defaultSmsConfigShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the smsConfigList where remark contains UPDATED_REMARK
        defaultSmsConfigShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where remark does not contain DEFAULT_REMARK
        defaultSmsConfigShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the smsConfigList where remark does not contain UPDATED_REMARK
        defaultSmsConfigShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where enabled equals to DEFAULT_ENABLED
        defaultSmsConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the smsConfigList where enabled equals to UPDATED_ENABLED
        defaultSmsConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where enabled not equals to DEFAULT_ENABLED
        defaultSmsConfigShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the smsConfigList where enabled not equals to UPDATED_ENABLED
        defaultSmsConfigShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultSmsConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the smsConfigList where enabled equals to UPDATED_ENABLED
        defaultSmsConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        // Get all the smsConfigList where enabled is not null
        defaultSmsConfigShouldBeFound("enabled.specified=true");

        // Get all the smsConfigList where enabled is null
        defaultSmsConfigShouldNotBeFound("enabled.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsConfigShouldBeFound(String filter) throws Exception {
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].smsCode").value(hasItem(DEFAULT_SMS_CODE)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].secretKey").value(hasItem(DEFAULT_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].regionId").value(hasItem(DEFAULT_REGION_ID)))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsConfigShouldNotBeFound(String filter) throws Exception {
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmsConfig() throws Exception {
        // Get the smsConfig
        restSmsConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSmsConfig() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();

        // Update the smsConfig
        SmsConfig updatedSmsConfig = smsConfigRepository.findById(smsConfig.getId()).get();

        // Disconnect from session so that the updates on updatedSmsConfig are not directly saved in db

        updatedSmsConfig
            .provider(UPDATED_PROVIDER)
            .smsCode(UPDATED_SMS_CODE)
            .templateId(UPDATED_TEMPLATE_ID)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .regionId(UPDATED_REGION_ID)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(updatedSmsConfig);

        restSmsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
        SmsConfig testSmsConfig = smsConfigList.get(smsConfigList.size() - 1);
        assertThat(testSmsConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testSmsConfig.getSmsCode()).isEqualTo(UPDATED_SMS_CODE);
        assertThat(testSmsConfig.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testSmsConfig.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testSmsConfig.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testSmsConfig.getRegionId()).isEqualTo(UPDATED_REGION_ID);
        assertThat(testSmsConfig.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testSmsConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingngSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        restSmsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsConfigWithPatch() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();

        // Update the smsConfig using partial update
        SmsConfig partialUpdatedSmsConfig = new SmsConfig();
        partialUpdatedSmsConfig.setId(smsConfig.getId());

        partialUpdatedSmsConfig.templateId(UPDATED_TEMPLATE_ID).remark(UPDATED_REMARK);

        restSmsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsConfig))
            )
            .andExpect(status().isOk());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
        SmsConfig testSmsConfig = smsConfigList.get(smsConfigList.size() - 1);
        assertThat(testSmsConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testSmsConfig.getSmsCode()).isEqualTo(DEFAULT_SMS_CODE);
        assertThat(testSmsConfig.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testSmsConfig.getAccessKey()).isEqualTo(DEFAULT_ACCESS_KEY);
        assertThat(testSmsConfig.getSecretKey()).isEqualTo(DEFAULT_SECRET_KEY);
        assertThat(testSmsConfig.getRegionId()).isEqualTo(DEFAULT_REGION_ID);
        assertThat(testSmsConfig.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testSmsConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateSmsConfigWithPatch() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();

        // Update the smsConfig using partial update
        SmsConfig partialUpdatedSmsConfig = new SmsConfig();
        partialUpdatedSmsConfig.setId(smsConfig.getId());

        partialUpdatedSmsConfig
            .provider(UPDATED_PROVIDER)
            .smsCode(UPDATED_SMS_CODE)
            .templateId(UPDATED_TEMPLATE_ID)
            .accessKey(UPDATED_ACCESS_KEY)
            .secretKey(UPDATED_SECRET_KEY)
            .regionId(UPDATED_REGION_ID)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);

        restSmsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsConfig))
            )
            .andExpect(status().isOk());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
        SmsConfig testSmsConfig = smsConfigList.get(smsConfigList.size() - 1);
        assertThat(testSmsConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testSmsConfig.getSmsCode()).isEqualTo(UPDATED_SMS_CODE);
        assertThat(testSmsConfig.getTemplateId()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testSmsConfig.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testSmsConfig.getSecretKey()).isEqualTo(UPDATED_SECRET_KEY);
        assertThat(testSmsConfig.getRegionId()).isEqualTo(UPDATED_REGION_ID);
        assertThat(testSmsConfig.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testSmsConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsConfig() throws Exception {
        int databaseSizeBeforeUpdate = smsConfigRepository.findAll().size();
        smsConfig.setId(count.incrementAndGet());

        // Create the SmsConfig
        SmsConfigDTO smsConfigDTO = smsConfigMapper.toDto(smsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(smsConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsConfig in the database
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsConfig() throws Exception {
        // Initialize the database
        smsConfigRepository.insert(smsConfig);

        int databaseSizeBeforeDelete = smsConfigRepository.findAll().size();

        // Delete the smsConfig
        restSmsConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmsConfig> smsConfigList = smsConfigRepository.findAll();
        assertThat(smsConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
