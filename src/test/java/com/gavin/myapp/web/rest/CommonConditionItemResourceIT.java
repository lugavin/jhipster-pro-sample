package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.CommonCondition;
import com.gavin.myapp.domain.CommonConditionItem;
import com.gavin.myapp.repository.CommonConditionItemRepository;
import com.gavin.myapp.service.CommonConditionItemQueryService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.criteria.CommonConditionItemCriteria;
import com.gavin.myapp.service.dto.CommonConditionItemDTO;
import com.gavin.myapp.service.mapper.CommonConditionItemMapper;
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
 * Integration tests for the {@link CommonConditionItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonConditionItemResourceIT {

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_SUFFIX = "AAAAAAAAAA";
    private static final String UPDATED_SUFFIX = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/common-condition-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonConditionItemRepository commonConditionItemRepository;

    @Autowired
    private CommonConditionItemMapper commonConditionItemMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restCommonConditionItemMockMvc;

    private CommonConditionItem commonConditionItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonConditionItem createEntity() {
        CommonConditionItem commonConditionItem = new CommonConditionItem()
            .prefix(DEFAULT_PREFIX)
            .fieldName(DEFAULT_FIELD_NAME)
            .fieldType(DEFAULT_FIELD_TYPE)
            .operator(DEFAULT_OPERATOR)
            .value(DEFAULT_VALUE)
            .suffix(DEFAULT_SUFFIX)
            .order(DEFAULT_ORDER);
        return commonConditionItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonConditionItem createUpdatedEntity() {
        CommonConditionItem commonConditionItem = new CommonConditionItem()
            .prefix(UPDATED_PREFIX)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .suffix(UPDATED_SUFFIX)
            .order(UPDATED_ORDER);
        return commonConditionItem;
    }

    @BeforeEach
    public void initTest() {
        commonConditionItem = createEntity();
    }

    @Test
    @Transactional
    void createCommonConditionItem() throws Exception {
        int databaseSizeBeforeCreate = commonConditionItemRepository.findAll().size();
        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);
        restCommonConditionItemMockMvc
            .perform(
                post(ENTITY_API_URL_ID, commonConditionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeCreate + 1);
        CommonConditionItem testCommonConditionItem = commonConditionItemList.get(commonConditionItemList.size() - 1);
        assertThat(testCommonConditionItem.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testCommonConditionItem.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testCommonConditionItem.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testCommonConditionItem.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testCommonConditionItem.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCommonConditionItem.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testCommonConditionItem.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createCommonConditionItemWithExistingId() throws Exception {
        // Create the CommonConditionItem with an existing ID
        commonConditionItem.setId(1L);
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        int databaseSizeBeforeCreate = commonConditionItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonConditionItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommonConditionItems() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonConditionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE)))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    public void getCommonConditionItem() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get the commonConditionItem
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL_ID, commonConditionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonConditionItem.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    public void getCommonConditionItemsByIdFiltering() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        Long id = commonConditionItem.getId();

        defaultCommonConditionItemShouldBeFound("id.equals=" + id);
        defaultCommonConditionItemShouldNotBeFound("id.notEquals=" + id);

        defaultCommonConditionItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonConditionItemShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonConditionItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonConditionItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix equals to DEFAULT_PREFIX
        defaultCommonConditionItemShouldBeFound("prefix.equals=" + DEFAULT_PREFIX);

        // Get all the commonConditionItemList where prefix equals to UPDATED_PREFIX
        defaultCommonConditionItemShouldNotBeFound("prefix.equals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix not equals to DEFAULT_PREFIX
        defaultCommonConditionItemShouldNotBeFound("prefix.notEquals=" + DEFAULT_PREFIX);

        // Get all the commonConditionItemList where prefix not equals to UPDATED_PREFIX
        defaultCommonConditionItemShouldBeFound("prefix.notEquals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix in DEFAULT_PREFIX or UPDATED_PREFIX
        defaultCommonConditionItemShouldBeFound("prefix.in=" + DEFAULT_PREFIX + "," + UPDATED_PREFIX);

        // Get all the commonConditionItemList where prefix equals to UPDATED_PREFIX
        defaultCommonConditionItemShouldNotBeFound("prefix.in=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix is not null
        defaultCommonConditionItemShouldBeFound("prefix.specified=true");

        // Get all the commonConditionItemList where prefix is null
        defaultCommonConditionItemShouldNotBeFound("prefix.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix contains DEFAULT_PREFIX
        defaultCommonConditionItemShouldBeFound("prefix.contains=" + DEFAULT_PREFIX);

        // Get all the commonConditionItemList where prefix contains UPDATED_PREFIX
        defaultCommonConditionItemShouldNotBeFound("prefix.contains=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByPrefixNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where prefix does not contain DEFAULT_PREFIX
        defaultCommonConditionItemShouldNotBeFound("prefix.doesNotContain=" + DEFAULT_PREFIX);

        // Get all the commonConditionItemList where prefix does not contain UPDATED_PREFIX
        defaultCommonConditionItemShouldBeFound("prefix.doesNotContain=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName equals to DEFAULT_FIELD_NAME
        defaultCommonConditionItemShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the commonConditionItemList where fieldName equals to UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName not equals to DEFAULT_FIELD_NAME
        defaultCommonConditionItemShouldNotBeFound("fieldName.notEquals=" + DEFAULT_FIELD_NAME);

        // Get all the commonConditionItemList where fieldName not equals to UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldBeFound("fieldName.notEquals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName in DEFAULT_FIELD_NAME or UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the commonConditionItemList where fieldName equals to UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName is not null
        defaultCommonConditionItemShouldBeFound("fieldName.specified=true");

        // Get all the commonConditionItemList where fieldName is null
        defaultCommonConditionItemShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName contains DEFAULT_FIELD_NAME
        defaultCommonConditionItemShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the commonConditionItemList where fieldName contains UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldName does not contain DEFAULT_FIELD_NAME
        defaultCommonConditionItemShouldNotBeFound("fieldName.doesNotContain=" + DEFAULT_FIELD_NAME);

        // Get all the commonConditionItemList where fieldName does not contain UPDATED_FIELD_NAME
        defaultCommonConditionItemShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType equals to DEFAULT_FIELD_TYPE
        defaultCommonConditionItemShouldBeFound("fieldType.equals=" + DEFAULT_FIELD_TYPE);

        // Get all the commonConditionItemList where fieldType equals to UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldNotBeFound("fieldType.equals=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType not equals to DEFAULT_FIELD_TYPE
        defaultCommonConditionItemShouldNotBeFound("fieldType.notEquals=" + DEFAULT_FIELD_TYPE);

        // Get all the commonConditionItemList where fieldType not equals to UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldBeFound("fieldType.notEquals=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType in DEFAULT_FIELD_TYPE or UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldBeFound("fieldType.in=" + DEFAULT_FIELD_TYPE + "," + UPDATED_FIELD_TYPE);

        // Get all the commonConditionItemList where fieldType equals to UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldNotBeFound("fieldType.in=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType is not null
        defaultCommonConditionItemShouldBeFound("fieldType.specified=true");

        // Get all the commonConditionItemList where fieldType is null
        defaultCommonConditionItemShouldNotBeFound("fieldType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType contains DEFAULT_FIELD_TYPE
        defaultCommonConditionItemShouldBeFound("fieldType.contains=" + DEFAULT_FIELD_TYPE);

        // Get all the commonConditionItemList where fieldType contains UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldNotBeFound("fieldType.contains=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByFieldTypeNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where fieldType does not contain DEFAULT_FIELD_TYPE
        defaultCommonConditionItemShouldNotBeFound("fieldType.doesNotContain=" + DEFAULT_FIELD_TYPE);

        // Get all the commonConditionItemList where fieldType does not contain UPDATED_FIELD_TYPE
        defaultCommonConditionItemShouldBeFound("fieldType.doesNotContain=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator equals to DEFAULT_OPERATOR
        defaultCommonConditionItemShouldBeFound("operator.equals=" + DEFAULT_OPERATOR);

        // Get all the commonConditionItemList where operator equals to UPDATED_OPERATOR
        defaultCommonConditionItemShouldNotBeFound("operator.equals=" + UPDATED_OPERATOR);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator not equals to DEFAULT_OPERATOR
        defaultCommonConditionItemShouldNotBeFound("operator.notEquals=" + DEFAULT_OPERATOR);

        // Get all the commonConditionItemList where operator not equals to UPDATED_OPERATOR
        defaultCommonConditionItemShouldBeFound("operator.notEquals=" + UPDATED_OPERATOR);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator in DEFAULT_OPERATOR or UPDATED_OPERATOR
        defaultCommonConditionItemShouldBeFound("operator.in=" + DEFAULT_OPERATOR + "," + UPDATED_OPERATOR);

        // Get all the commonConditionItemList where operator equals to UPDATED_OPERATOR
        defaultCommonConditionItemShouldNotBeFound("operator.in=" + UPDATED_OPERATOR);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator is not null
        defaultCommonConditionItemShouldBeFound("operator.specified=true");

        // Get all the commonConditionItemList where operator is null
        defaultCommonConditionItemShouldNotBeFound("operator.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator contains DEFAULT_OPERATOR
        defaultCommonConditionItemShouldBeFound("operator.contains=" + DEFAULT_OPERATOR);

        // Get all the commonConditionItemList where operator contains UPDATED_OPERATOR
        defaultCommonConditionItemShouldNotBeFound("operator.contains=" + UPDATED_OPERATOR);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOperatorNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where operator does not contain DEFAULT_OPERATOR
        defaultCommonConditionItemShouldNotBeFound("operator.doesNotContain=" + DEFAULT_OPERATOR);

        // Get all the commonConditionItemList where operator does not contain UPDATED_OPERATOR
        defaultCommonConditionItemShouldBeFound("operator.doesNotContain=" + UPDATED_OPERATOR);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value equals to DEFAULT_VALUE
        defaultCommonConditionItemShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the commonConditionItemList where value equals to UPDATED_VALUE
        defaultCommonConditionItemShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value not equals to DEFAULT_VALUE
        defaultCommonConditionItemShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the commonConditionItemList where value not equals to UPDATED_VALUE
        defaultCommonConditionItemShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultCommonConditionItemShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the commonConditionItemList where value equals to UPDATED_VALUE
        defaultCommonConditionItemShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value is not null
        defaultCommonConditionItemShouldBeFound("value.specified=true");

        // Get all the commonConditionItemList where value is null
        defaultCommonConditionItemShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value contains DEFAULT_VALUE
        defaultCommonConditionItemShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the commonConditionItemList where value contains UPDATED_VALUE
        defaultCommonConditionItemShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where value does not contain DEFAULT_VALUE
        defaultCommonConditionItemShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the commonConditionItemList where value does not contain UPDATED_VALUE
        defaultCommonConditionItemShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix equals to DEFAULT_SUFFIX
        defaultCommonConditionItemShouldBeFound("suffix.equals=" + DEFAULT_SUFFIX);

        // Get all the commonConditionItemList where suffix equals to UPDATED_SUFFIX
        defaultCommonConditionItemShouldNotBeFound("suffix.equals=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix not equals to DEFAULT_SUFFIX
        defaultCommonConditionItemShouldNotBeFound("suffix.notEquals=" + DEFAULT_SUFFIX);

        // Get all the commonConditionItemList where suffix not equals to UPDATED_SUFFIX
        defaultCommonConditionItemShouldBeFound("suffix.notEquals=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix in DEFAULT_SUFFIX or UPDATED_SUFFIX
        defaultCommonConditionItemShouldBeFound("suffix.in=" + DEFAULT_SUFFIX + "," + UPDATED_SUFFIX);

        // Get all the commonConditionItemList where suffix equals to UPDATED_SUFFIX
        defaultCommonConditionItemShouldNotBeFound("suffix.in=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix is not null
        defaultCommonConditionItemShouldBeFound("suffix.specified=true");

        // Get all the commonConditionItemList where suffix is null
        defaultCommonConditionItemShouldNotBeFound("suffix.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix contains DEFAULT_SUFFIX
        defaultCommonConditionItemShouldBeFound("suffix.contains=" + DEFAULT_SUFFIX);

        // Get all the commonConditionItemList where suffix contains UPDATED_SUFFIX
        defaultCommonConditionItemShouldNotBeFound("suffix.contains=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsBySuffixNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where suffix does not contain DEFAULT_SUFFIX
        defaultCommonConditionItemShouldNotBeFound("suffix.doesNotContain=" + DEFAULT_SUFFIX);

        // Get all the commonConditionItemList where suffix does not contain UPDATED_SUFFIX
        defaultCommonConditionItemShouldBeFound("suffix.doesNotContain=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order equals to DEFAULT_ORDER
        defaultCommonConditionItemShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order equals to UPDATED_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order not equals to DEFAULT_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order not equals to UPDATED_ORDER
        defaultCommonConditionItemShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultCommonConditionItemShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the commonConditionItemList where order equals to UPDATED_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order is not null
        defaultCommonConditionItemShouldBeFound("order.specified=true");

        // Get all the commonConditionItemList where order is null
        defaultCommonConditionItemShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order is greater than or equal to DEFAULT_ORDER
        defaultCommonConditionItemShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order is greater than or equal to UPDATED_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order is less than or equal to DEFAULT_ORDER
        defaultCommonConditionItemShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order is less than or equal to SMALLER_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order is less than DEFAULT_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order is less than UPDATED_ORDER
        defaultCommonConditionItemShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonConditionItemsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        // Get all the commonConditionItemList where order is greater than DEFAULT_ORDER
        defaultCommonConditionItemShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the commonConditionItemList where order is greater than SMALLER_ORDER
        defaultCommonConditionItemShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllCommonConditionItemsByCommonConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);
        CommonCondition commonCondition = CommonConditionResourceIT.createEntity();
        commonConditionItem.setCommonCondition(commonCondition);
        commonConditionItemRepository.insert(commonConditionItem);
        Long commonConditionId = commonCondition.getId();

        // Get all the commonConditionItemList where commonCondition equals to commonConditionId
        defaultCommonConditionItemShouldBeFound("commonConditionId.equals=" + commonConditionId);

        // Get all the commonConditionItemList where commonCondition equals to (commonConditionId + 1)
        defaultCommonConditionItemShouldNotBeFound("commonConditionId.equals=" + (commonConditionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonConditionItemShouldBeFound(String filter) throws Exception {
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonConditionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE)))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));

        // Check, that the count call also returns 1
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonConditionItemShouldNotBeFound(String filter) throws Exception {
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonConditionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonConditionItem() throws Exception {
        // Get the commonConditionItem
        restCommonConditionItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonConditionItem() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();

        // Update the commonConditionItem
        CommonConditionItem updatedCommonConditionItem = commonConditionItemRepository.findById(commonConditionItem.getId()).get();

        // Disconnect from session so that the updates on updatedCommonConditionItem are not directly saved in db

        updatedCommonConditionItem
            .prefix(UPDATED_PREFIX)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .suffix(UPDATED_SUFFIX)
            .order(UPDATED_ORDER);
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(updatedCommonConditionItem);

        restCommonConditionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonConditionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
        CommonConditionItem testCommonConditionItem = commonConditionItemList.get(commonConditionItemList.size() - 1);
        assertThat(testCommonConditionItem.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testCommonConditionItem.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testCommonConditionItem.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testCommonConditionItem.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testCommonConditionItem.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCommonConditionItem.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testCommonConditionItem.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingngCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        restCommonConditionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonConditionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonConditionItemWithPatch() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();

        // Update the commonConditionItem using partial update
        CommonConditionItem partialUpdatedCommonConditionItem = new CommonConditionItem();
        partialUpdatedCommonConditionItem.setId(commonConditionItem.getId());

        restCommonConditionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonConditionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonConditionItem))
            )
            .andExpect(status().isOk());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
        CommonConditionItem testCommonConditionItem = commonConditionItemList.get(commonConditionItemList.size() - 1);
        assertThat(testCommonConditionItem.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testCommonConditionItem.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testCommonConditionItem.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testCommonConditionItem.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testCommonConditionItem.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCommonConditionItem.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testCommonConditionItem.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateCommonConditionItemWithPatch() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();

        // Update the commonConditionItem using partial update
        CommonConditionItem partialUpdatedCommonConditionItem = new CommonConditionItem();
        partialUpdatedCommonConditionItem.setId(commonConditionItem.getId());

        partialUpdatedCommonConditionItem
            .prefix(UPDATED_PREFIX)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .operator(UPDATED_OPERATOR)
            .value(UPDATED_VALUE)
            .suffix(UPDATED_SUFFIX)
            .order(UPDATED_ORDER);

        restCommonConditionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonConditionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonConditionItem))
            )
            .andExpect(status().isOk());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
        CommonConditionItem testCommonConditionItem = commonConditionItemList.get(commonConditionItemList.size() - 1);
        assertThat(testCommonConditionItem.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testCommonConditionItem.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testCommonConditionItem.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testCommonConditionItem.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testCommonConditionItem.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCommonConditionItem.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testCommonConditionItem.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonConditionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonConditionItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonConditionItem() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionItemRepository.findAll().size();
        commonConditionItem.setId(count.incrementAndGet());

        // Create the CommonConditionItem
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemMapper.toDto(commonConditionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonConditionItem in the database
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonConditionItem() throws Exception {
        // Initialize the database
        commonConditionItemRepository.insert(commonConditionItem);

        int databaseSizeBeforeDelete = commonConditionItemRepository.findAll().size();

        // Delete the commonConditionItem
        restCommonConditionItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonConditionItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonConditionItem> commonConditionItemList = commonConditionItemRepository.findAll();
        assertThat(commonConditionItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
