package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.ApiPermission;
import com.gavin.myapp.domain.ApiPermission;
import com.gavin.myapp.domain.Authority;
import com.gavin.myapp.domain.enumeration.ApiPermissionType;
import com.gavin.myapp.repository.ApiPermissionRepository;
import com.gavin.myapp.service.ApiPermissionQueryService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.criteria.ApiPermissionCriteria;
import com.gavin.myapp.service.dto.ApiPermissionDTO;
import com.gavin.myapp.service.mapper.ApiPermissionMapper;
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
 * Integration tests for the {@link ApiPermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class ApiPermissionResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ApiPermissionType DEFAULT_TYPE = ApiPermissionType.BUSINESS;
    private static final ApiPermissionType UPDATED_TYPE = ApiPermissionType.API;

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/api-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApiPermissionRepository apiPermissionRepository;

    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restApiPermissionMockMvc;

    private ApiPermission apiPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiPermission createEntity() {
        ApiPermission apiPermission = new ApiPermission()
            .serviceName(DEFAULT_SERVICE_NAME)
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .method(DEFAULT_METHOD)
            .url(DEFAULT_URL);
        return apiPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiPermission createUpdatedEntity() {
        ApiPermission apiPermission = new ApiPermission()
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL);
        return apiPermission;
    }

    @BeforeEach
    public void initTest() {
        apiPermission = createEntity();
    }

    @Test
    @Transactional
    void createApiPermission() throws Exception {
        int databaseSizeBeforeCreate = apiPermissionRepository.findAll().size();
        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);
        restApiPermissionMockMvc
            .perform(
                post(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        ApiPermission testApiPermission = apiPermissionList.get(apiPermissionList.size() - 1);
        assertThat(testApiPermission.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testApiPermission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApiPermission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApiPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApiPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApiPermission.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testApiPermission.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createApiPermissionWithExistingId() throws Exception {
        // Create the ApiPermission with an existing ID
        apiPermission.setId(1L);
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        int databaseSizeBeforeCreate = apiPermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApiPermissions() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void getApiPermission() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get the apiPermission
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, apiPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiPermission.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    public void getApiPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        Long id = apiPermission.getId();

        defaultApiPermissionShouldBeFound("id.equals=" + id);
        defaultApiPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultApiPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApiPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultApiPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApiPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultApiPermissionShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the apiPermissionList where serviceName equals to UPDATED_SERVICE_NAME
        defaultApiPermissionShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName not equals to DEFAULT_SERVICE_NAME
        defaultApiPermissionShouldNotBeFound("serviceName.notEquals=" + DEFAULT_SERVICE_NAME);

        // Get all the apiPermissionList where serviceName not equals to UPDATED_SERVICE_NAME
        defaultApiPermissionShouldBeFound("serviceName.notEquals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultApiPermissionShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the apiPermissionList where serviceName equals to UPDATED_SERVICE_NAME
        defaultApiPermissionShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName is not null
        defaultApiPermissionShouldBeFound("serviceName.specified=true");

        // Get all the apiPermissionList where serviceName is null
        defaultApiPermissionShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName contains DEFAULT_SERVICE_NAME
        defaultApiPermissionShouldBeFound("serviceName.contains=" + DEFAULT_SERVICE_NAME);

        // Get all the apiPermissionList where serviceName contains UPDATED_SERVICE_NAME
        defaultApiPermissionShouldNotBeFound("serviceName.contains=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where serviceName does not contain DEFAULT_SERVICE_NAME
        defaultApiPermissionShouldNotBeFound("serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME);

        // Get all the apiPermissionList where serviceName does not contain UPDATED_SERVICE_NAME
        defaultApiPermissionShouldBeFound("serviceName.doesNotContain=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name equals to DEFAULT_NAME
        defaultApiPermissionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the apiPermissionList where name equals to UPDATED_NAME
        defaultApiPermissionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name not equals to DEFAULT_NAME
        defaultApiPermissionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the apiPermissionList where name not equals to UPDATED_NAME
        defaultApiPermissionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultApiPermissionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the apiPermissionList where name equals to UPDATED_NAME
        defaultApiPermissionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name is not null
        defaultApiPermissionShouldBeFound("name.specified=true");

        // Get all the apiPermissionList where name is null
        defaultApiPermissionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name contains DEFAULT_NAME
        defaultApiPermissionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the apiPermissionList where name contains UPDATED_NAME
        defaultApiPermissionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where name does not contain DEFAULT_NAME
        defaultApiPermissionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the apiPermissionList where name does not contain UPDATED_NAME
        defaultApiPermissionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code equals to DEFAULT_CODE
        defaultApiPermissionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the apiPermissionList where code equals to UPDATED_CODE
        defaultApiPermissionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code not equals to DEFAULT_CODE
        defaultApiPermissionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the apiPermissionList where code not equals to UPDATED_CODE
        defaultApiPermissionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultApiPermissionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the apiPermissionList where code equals to UPDATED_CODE
        defaultApiPermissionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code is not null
        defaultApiPermissionShouldBeFound("code.specified=true");

        // Get all the apiPermissionList where code is null
        defaultApiPermissionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code contains DEFAULT_CODE
        defaultApiPermissionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the apiPermissionList where code contains UPDATED_CODE
        defaultApiPermissionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where code does not contain DEFAULT_CODE
        defaultApiPermissionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the apiPermissionList where code does not contain UPDATED_CODE
        defaultApiPermissionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description equals to DEFAULT_DESCRIPTION
        defaultApiPermissionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the apiPermissionList where description equals to UPDATED_DESCRIPTION
        defaultApiPermissionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description not equals to DEFAULT_DESCRIPTION
        defaultApiPermissionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the apiPermissionList where description not equals to UPDATED_DESCRIPTION
        defaultApiPermissionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultApiPermissionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the apiPermissionList where description equals to UPDATED_DESCRIPTION
        defaultApiPermissionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description is not null
        defaultApiPermissionShouldBeFound("description.specified=true");

        // Get all the apiPermissionList where description is null
        defaultApiPermissionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description contains DEFAULT_DESCRIPTION
        defaultApiPermissionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the apiPermissionList where description contains UPDATED_DESCRIPTION
        defaultApiPermissionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where description does not contain DEFAULT_DESCRIPTION
        defaultApiPermissionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the apiPermissionList where description does not contain UPDATED_DESCRIPTION
        defaultApiPermissionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where type equals to DEFAULT_TYPE
        defaultApiPermissionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the apiPermissionList where type equals to UPDATED_TYPE
        defaultApiPermissionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where type not equals to DEFAULT_TYPE
        defaultApiPermissionShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the apiPermissionList where type not equals to UPDATED_TYPE
        defaultApiPermissionShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultApiPermissionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the apiPermissionList where type equals to UPDATED_TYPE
        defaultApiPermissionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where type is not null
        defaultApiPermissionShouldBeFound("type.specified=true");

        // Get all the apiPermissionList where type is null
        defaultApiPermissionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method equals to DEFAULT_METHOD
        defaultApiPermissionShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the apiPermissionList where method equals to UPDATED_METHOD
        defaultApiPermissionShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method not equals to DEFAULT_METHOD
        defaultApiPermissionShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the apiPermissionList where method not equals to UPDATED_METHOD
        defaultApiPermissionShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultApiPermissionShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the apiPermissionList where method equals to UPDATED_METHOD
        defaultApiPermissionShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method is not null
        defaultApiPermissionShouldBeFound("method.specified=true");

        // Get all the apiPermissionList where method is null
        defaultApiPermissionShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method contains DEFAULT_METHOD
        defaultApiPermissionShouldBeFound("method.contains=" + DEFAULT_METHOD);

        // Get all the apiPermissionList where method contains UPDATED_METHOD
        defaultApiPermissionShouldNotBeFound("method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where method does not contain DEFAULT_METHOD
        defaultApiPermissionShouldNotBeFound("method.doesNotContain=" + DEFAULT_METHOD);

        // Get all the apiPermissionList where method does not contain UPDATED_METHOD
        defaultApiPermissionShouldBeFound("method.doesNotContain=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url equals to DEFAULT_URL
        defaultApiPermissionShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the apiPermissionList where url equals to UPDATED_URL
        defaultApiPermissionShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url not equals to DEFAULT_URL
        defaultApiPermissionShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the apiPermissionList where url not equals to UPDATED_URL
        defaultApiPermissionShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url in DEFAULT_URL or UPDATED_URL
        defaultApiPermissionShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the apiPermissionList where url equals to UPDATED_URL
        defaultApiPermissionShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url is not null
        defaultApiPermissionShouldBeFound("url.specified=true");

        // Get all the apiPermissionList where url is null
        defaultApiPermissionShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url contains DEFAULT_URL
        defaultApiPermissionShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the apiPermissionList where url contains UPDATED_URL
        defaultApiPermissionShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        // Get all the apiPermissionList where url does not contain DEFAULT_URL
        defaultApiPermissionShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the apiPermissionList where url does not contain UPDATED_URL
        defaultApiPermissionShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllApiPermissionsByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);
        ApiPermission children = ApiPermissionResourceIT.createEntity();
        apiPermission.addChildren(children);
        apiPermissionRepository.insert(apiPermission);
        Long childrenId = children.getId();

        // Get all the apiPermissionList where children equals to childrenId
        defaultApiPermissionShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the apiPermissionList where children equals to (childrenId + 1)
        defaultApiPermissionShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    public void getAllApiPermissionsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);
        ApiPermission parent = ApiPermissionResourceIT.createEntity();
        apiPermission.setParent(parent);
        apiPermissionRepository.insert(apiPermission);
        Long parentId = parent.getId();

        // Get all the apiPermissionList where parent equals to parentId
        defaultApiPermissionShouldBeFound("parentId.equals=" + parentId);

        // Get all the apiPermissionList where parent equals to (parentId + 1)
        defaultApiPermissionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    public void getAllApiPermissionsByAuthoritiesIsEqualToSomething() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);
        Authority authorities = AuthorityResourceIT.createEntity();
        apiPermission.addAuthorities(authorities);
        apiPermissionRepository.insert(apiPermission);
        Long authoritiesId = authorities.getId();

        // Get all the apiPermissionList where authorities equals to authoritiesId
        defaultApiPermissionShouldBeFound("authoritiesId.equals=" + authoritiesId);

        // Get all the apiPermissionList where authorities equals to (authoritiesId + 1)
        defaultApiPermissionShouldNotBeFound("authoritiesId.equals=" + (authoritiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApiPermissionShouldBeFound(String filter) throws Exception {
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApiPermissionShouldNotBeFound(String filter) throws Exception {
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApiPermission() throws Exception {
        // Get the apiPermission
        restApiPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApiPermission() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();

        // Update the apiPermission
        ApiPermission updatedApiPermission = apiPermissionRepository.findById(apiPermission.getId()).get();

        // Disconnect from session so that the updates on updatedApiPermission are not directly saved in db

        updatedApiPermission
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL);
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(updatedApiPermission);

        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
        ApiPermission testApiPermission = apiPermissionList.get(apiPermissionList.size() - 1);
        assertThat(testApiPermission.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testApiPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApiPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApiPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApiPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApiPermission.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testApiPermission.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingngApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiPermissionWithPatch() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();

        // Update the apiPermission using partial update
        ApiPermission partialUpdatedApiPermission = new ApiPermission();
        partialUpdatedApiPermission.setId(apiPermission.getId());

        partialUpdatedApiPermission
            .serviceName(UPDATED_SERVICE_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL);

        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApiPermission))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
        ApiPermission testApiPermission = apiPermissionList.get(apiPermissionList.size() - 1);
        assertThat(testApiPermission.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testApiPermission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApiPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApiPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApiPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApiPermission.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testApiPermission.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateApiPermissionWithPatch() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();

        // Update the apiPermission using partial update
        ApiPermission partialUpdatedApiPermission = new ApiPermission();
        partialUpdatedApiPermission.setId(apiPermission.getId());

        partialUpdatedApiPermission
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL);

        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApiPermission))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
        ApiPermission testApiPermission = apiPermissionList.get(apiPermissionList.size() - 1);
        assertThat(testApiPermission.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testApiPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApiPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApiPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApiPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApiPermission.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testApiPermission.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiPermission() throws Exception {
        int databaseSizeBeforeUpdate = apiPermissionRepository.findAll().size();
        apiPermission.setId(count.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apiPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiPermission in the database
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiPermission() throws Exception {
        // Initialize the database
        apiPermissionRepository.insert(apiPermission);

        int databaseSizeBeforeDelete = apiPermissionRepository.findAll().size();

        // Delete the apiPermission
        restApiPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApiPermission> apiPermissionList = apiPermissionRepository.findAll();
        assertThat(apiPermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
