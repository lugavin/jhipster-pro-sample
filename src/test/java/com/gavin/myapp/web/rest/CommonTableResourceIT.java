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
import com.gavin.myapp.domain.BusinessType;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.CommonTableField;
import com.gavin.myapp.domain.CommonTableRelationship;
import com.gavin.myapp.domain.User;
import com.gavin.myapp.repository.CommonTableRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonTableQueryService;
import com.gavin.myapp.service.criteria.CommonTableCriteria;
import com.gavin.myapp.service.dto.CommonTableDTO;
import com.gavin.myapp.service.mapper.CommonTableMapper;
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
 * Integration tests for the {@link CommonTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonTableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SYSTEM = false;
    private static final Boolean UPDATED_SYSTEM = true;

    private static final String DEFAULT_CLAZZ_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLAZZ_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GENERATED = false;
    private static final Boolean UPDATED_GENERATED = true;

    private static final ZonedDateTime DEFAULT_CREAT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREAT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREAT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_GENERATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_GENERATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_GENERATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_GENERATE_CLASS_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_GENERATE_CLASS_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_GENERATE_CLASS_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TREE_TABLE = false;
    private static final Boolean UPDATED_TREE_TABLE = true;

    private static final Long DEFAULT_BASE_TABLE_ID = 1L;
    private static final Long UPDATED_BASE_TABLE_ID = 2L;
    private static final Long SMALLER_BASE_TABLE_ID = 1L - 1L;

    private static final Integer DEFAULT_RECORD_ACTION_WIDTH = 1;
    private static final Integer UPDATED_RECORD_ACTION_WIDTH = 2;
    private static final Integer SMALLER_RECORD_ACTION_WIDTH = 1 - 1;

    private static final String DEFAULT_LIST_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_LIST_CONFIG = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_FORM_CONFIG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EDIT_IN_MODAL = false;
    private static final Boolean UPDATED_EDIT_IN_MODAL = true;

    private static final String ENTITY_API_URL = "/api/common-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonTableRepository commonTableRepository;

    @Autowired
    private CommonTableMapper commonTableMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restCommonTableMockMvc;

    private CommonTable commonTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTable createEntity() {
        CommonTable commonTable = new CommonTable()
            .name(DEFAULT_NAME)
            .entityName(DEFAULT_ENTITY_NAME)
            .tableName(DEFAULT_TABLE_NAME)
            .system(DEFAULT_SYSTEM)
            .clazzName(DEFAULT_CLAZZ_NAME)
            .generated(DEFAULT_GENERATED)
            .creatAt(DEFAULT_CREAT_AT)
            .generateAt(DEFAULT_GENERATE_AT)
            .generateClassAt(DEFAULT_GENERATE_CLASS_AT)
            .description(DEFAULT_DESCRIPTION)
            .treeTable(DEFAULT_TREE_TABLE)
            .baseTableId(DEFAULT_BASE_TABLE_ID)
            .recordActionWidth(DEFAULT_RECORD_ACTION_WIDTH)
            .listConfig(DEFAULT_LIST_CONFIG)
            .formConfig(DEFAULT_FORM_CONFIG)
            .editInModal(DEFAULT_EDIT_IN_MODAL);
        return commonTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTable createUpdatedEntity() {
        CommonTable commonTable = new CommonTable()
            .name(UPDATED_NAME)
            .entityName(UPDATED_ENTITY_NAME)
            .tableName(UPDATED_TABLE_NAME)
            .system(UPDATED_SYSTEM)
            .clazzName(UPDATED_CLAZZ_NAME)
            .generated(UPDATED_GENERATED)
            .creatAt(UPDATED_CREAT_AT)
            .generateAt(UPDATED_GENERATE_AT)
            .generateClassAt(UPDATED_GENERATE_CLASS_AT)
            .description(UPDATED_DESCRIPTION)
            .treeTable(UPDATED_TREE_TABLE)
            .baseTableId(UPDATED_BASE_TABLE_ID)
            .recordActionWidth(UPDATED_RECORD_ACTION_WIDTH)
            .listConfig(UPDATED_LIST_CONFIG)
            .formConfig(UPDATED_FORM_CONFIG)
            .editInModal(UPDATED_EDIT_IN_MODAL);
        return commonTable;
    }

    @BeforeEach
    public void initTest() {
        commonTable = createEntity();
    }

    @Test
    @Transactional
    void createCommonTable() throws Exception {
        int databaseSizeBeforeCreate = commonTableRepository.findAll().size();
        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);
        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL_ID, commonTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeCreate + 1);
        CommonTable testCommonTable = commonTableList.get(commonTableList.size() - 1);
        assertThat(testCommonTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonTable.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testCommonTable.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testCommonTable.getSystem()).isEqualTo(DEFAULT_SYSTEM);
        assertThat(testCommonTable.getClazzName()).isEqualTo(DEFAULT_CLAZZ_NAME);
        assertThat(testCommonTable.getGenerated()).isEqualTo(DEFAULT_GENERATED);
        assertThat(testCommonTable.getCreatAt()).isEqualTo(DEFAULT_CREAT_AT);
        assertThat(testCommonTable.getGenerateAt()).isEqualTo(DEFAULT_GENERATE_AT);
        assertThat(testCommonTable.getGenerateClassAt()).isEqualTo(DEFAULT_GENERATE_CLASS_AT);
        assertThat(testCommonTable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommonTable.getTreeTable()).isEqualTo(DEFAULT_TREE_TABLE);
        assertThat(testCommonTable.getBaseTableId()).isEqualTo(DEFAULT_BASE_TABLE_ID);
        assertThat(testCommonTable.getRecordActionWidth()).isEqualTo(DEFAULT_RECORD_ACTION_WIDTH);
        assertThat(testCommonTable.getListConfig()).isEqualTo(DEFAULT_LIST_CONFIG);
        assertThat(testCommonTable.getFormConfig()).isEqualTo(DEFAULT_FORM_CONFIG);
        assertThat(testCommonTable.getEditInModal()).isEqualTo(DEFAULT_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void createCommonTableWithExistingId() throws Exception {
        // Create the CommonTable with an existing ID
        commonTable.setId(1L);
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        int databaseSizeBeforeCreate = commonTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRepository.findAll().size();
        // set the field null
        commonTable.setName(null);

        // Create the CommonTable, which fails.
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRepository.findAll().size();
        // set the field null
        commonTable.setEntityName(null);

        // Create the CommonTable, which fails.
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRepository.findAll().size();
        // set the field null
        commonTable.setTableName(null);

        // Create the CommonTable, which fails.
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClazzNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRepository.findAll().size();
        // set the field null
        commonTable.setClazzName(null);

        // Create the CommonTable, which fails.
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        restCommonTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonTables() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].clazzName").value(hasItem(DEFAULT_CLAZZ_NAME)))
            .andExpect(jsonPath("$.[*].generated").value(hasItem(DEFAULT_GENERATED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatAt").value(hasItem(sameInstant(DEFAULT_CREAT_AT))))
            .andExpect(jsonPath("$.[*].generateAt").value(hasItem(sameInstant(DEFAULT_GENERATE_AT))))
            .andExpect(jsonPath("$.[*].generateClassAt").value(hasItem(sameInstant(DEFAULT_GENERATE_CLASS_AT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeTable").value(hasItem(DEFAULT_TREE_TABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].baseTableId").value(hasItem(DEFAULT_BASE_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].recordActionWidth").value(hasItem(DEFAULT_RECORD_ACTION_WIDTH)))
            .andExpect(jsonPath("$.[*].listConfig").value(hasItem(DEFAULT_LIST_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].formConfig").value(hasItem(DEFAULT_FORM_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].editInModal").value(hasItem(DEFAULT_EDIT_IN_MODAL.booleanValue())));
    }

    @Test
    @Transactional
    public void getCommonTable() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get the commonTable
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL_ID, commonTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonTable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.clazzName").value(DEFAULT_CLAZZ_NAME))
            .andExpect(jsonPath("$.generated").value(DEFAULT_GENERATED.booleanValue()))
            .andExpect(jsonPath("$.creatAt").value(sameInstant(DEFAULT_CREAT_AT)))
            .andExpect(jsonPath("$.generateAt").value(sameInstant(DEFAULT_GENERATE_AT)))
            .andExpect(jsonPath("$.generateClassAt").value(sameInstant(DEFAULT_GENERATE_CLASS_AT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.treeTable").value(DEFAULT_TREE_TABLE.booleanValue()))
            .andExpect(jsonPath("$.baseTableId").value(DEFAULT_BASE_TABLE_ID.intValue()))
            .andExpect(jsonPath("$.recordActionWidth").value(DEFAULT_RECORD_ACTION_WIDTH))
            .andExpect(jsonPath("$.listConfig").value(DEFAULT_LIST_CONFIG.toString()))
            .andExpect(jsonPath("$.formConfig").value(DEFAULT_FORM_CONFIG.toString()))
            .andExpect(jsonPath("$.editInModal").value(DEFAULT_EDIT_IN_MODAL.booleanValue()));
    }

    @Test
    @Transactional
    public void getCommonTablesByIdFiltering() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        Long id = commonTable.getId();

        defaultCommonTableShouldBeFound("id.equals=" + id);
        defaultCommonTableShouldNotBeFound("id.notEquals=" + id);

        defaultCommonTableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonTableShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonTableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonTableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name equals to DEFAULT_NAME
        defaultCommonTableShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the commonTableList where name equals to UPDATED_NAME
        defaultCommonTableShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name not equals to DEFAULT_NAME
        defaultCommonTableShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the commonTableList where name not equals to UPDATED_NAME
        defaultCommonTableShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCommonTableShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the commonTableList where name equals to UPDATED_NAME
        defaultCommonTableShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name is not null
        defaultCommonTableShouldBeFound("name.specified=true");

        // Get all the commonTableList where name is null
        defaultCommonTableShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name contains DEFAULT_NAME
        defaultCommonTableShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the commonTableList where name contains UPDATED_NAME
        defaultCommonTableShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where name does not contain DEFAULT_NAME
        defaultCommonTableShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the commonTableList where name does not contain UPDATED_NAME
        defaultCommonTableShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName equals to DEFAULT_ENTITY_NAME
        defaultCommonTableShouldBeFound("entityName.equals=" + DEFAULT_ENTITY_NAME);

        // Get all the commonTableList where entityName equals to UPDATED_ENTITY_NAME
        defaultCommonTableShouldNotBeFound("entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName not equals to DEFAULT_ENTITY_NAME
        defaultCommonTableShouldNotBeFound("entityName.notEquals=" + DEFAULT_ENTITY_NAME);

        // Get all the commonTableList where entityName not equals to UPDATED_ENTITY_NAME
        defaultCommonTableShouldBeFound("entityName.notEquals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName in DEFAULT_ENTITY_NAME or UPDATED_ENTITY_NAME
        defaultCommonTableShouldBeFound("entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME);

        // Get all the commonTableList where entityName equals to UPDATED_ENTITY_NAME
        defaultCommonTableShouldNotBeFound("entityName.in=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName is not null
        defaultCommonTableShouldBeFound("entityName.specified=true");

        // Get all the commonTableList where entityName is null
        defaultCommonTableShouldNotBeFound("entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName contains DEFAULT_ENTITY_NAME
        defaultCommonTableShouldBeFound("entityName.contains=" + DEFAULT_ENTITY_NAME);

        // Get all the commonTableList where entityName contains UPDATED_ENTITY_NAME
        defaultCommonTableShouldNotBeFound("entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where entityName does not contain DEFAULT_ENTITY_NAME
        defaultCommonTableShouldNotBeFound("entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);

        // Get all the commonTableList where entityName does not contain UPDATED_ENTITY_NAME
        defaultCommonTableShouldBeFound("entityName.doesNotContain=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName equals to DEFAULT_TABLE_NAME
        defaultCommonTableShouldBeFound("tableName.equals=" + DEFAULT_TABLE_NAME);

        // Get all the commonTableList where tableName equals to UPDATED_TABLE_NAME
        defaultCommonTableShouldNotBeFound("tableName.equals=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName not equals to DEFAULT_TABLE_NAME
        defaultCommonTableShouldNotBeFound("tableName.notEquals=" + DEFAULT_TABLE_NAME);

        // Get all the commonTableList where tableName not equals to UPDATED_TABLE_NAME
        defaultCommonTableShouldBeFound("tableName.notEquals=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName in DEFAULT_TABLE_NAME or UPDATED_TABLE_NAME
        defaultCommonTableShouldBeFound("tableName.in=" + DEFAULT_TABLE_NAME + "," + UPDATED_TABLE_NAME);

        // Get all the commonTableList where tableName equals to UPDATED_TABLE_NAME
        defaultCommonTableShouldNotBeFound("tableName.in=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName is not null
        defaultCommonTableShouldBeFound("tableName.specified=true");

        // Get all the commonTableList where tableName is null
        defaultCommonTableShouldNotBeFound("tableName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName contains DEFAULT_TABLE_NAME
        defaultCommonTableShouldBeFound("tableName.contains=" + DEFAULT_TABLE_NAME);

        // Get all the commonTableList where tableName contains UPDATED_TABLE_NAME
        defaultCommonTableShouldNotBeFound("tableName.contains=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTableNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where tableName does not contain DEFAULT_TABLE_NAME
        defaultCommonTableShouldNotBeFound("tableName.doesNotContain=" + DEFAULT_TABLE_NAME);

        // Get all the commonTableList where tableName does not contain UPDATED_TABLE_NAME
        defaultCommonTableShouldBeFound("tableName.doesNotContain=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesBySystemIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where system equals to DEFAULT_SYSTEM
        defaultCommonTableShouldBeFound("system.equals=" + DEFAULT_SYSTEM);

        // Get all the commonTableList where system equals to UPDATED_SYSTEM
        defaultCommonTableShouldNotBeFound("system.equals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTablesBySystemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where system not equals to DEFAULT_SYSTEM
        defaultCommonTableShouldNotBeFound("system.notEquals=" + DEFAULT_SYSTEM);

        // Get all the commonTableList where system not equals to UPDATED_SYSTEM
        defaultCommonTableShouldBeFound("system.notEquals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTablesBySystemIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where system in DEFAULT_SYSTEM or UPDATED_SYSTEM
        defaultCommonTableShouldBeFound("system.in=" + DEFAULT_SYSTEM + "," + UPDATED_SYSTEM);

        // Get all the commonTableList where system equals to UPDATED_SYSTEM
        defaultCommonTableShouldNotBeFound("system.in=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTablesBySystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where system is not null
        defaultCommonTableShouldBeFound("system.specified=true");

        // Get all the commonTableList where system is null
        defaultCommonTableShouldNotBeFound("system.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName equals to DEFAULT_CLAZZ_NAME
        defaultCommonTableShouldBeFound("clazzName.equals=" + DEFAULT_CLAZZ_NAME);

        // Get all the commonTableList where clazzName equals to UPDATED_CLAZZ_NAME
        defaultCommonTableShouldNotBeFound("clazzName.equals=" + UPDATED_CLAZZ_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName not equals to DEFAULT_CLAZZ_NAME
        defaultCommonTableShouldNotBeFound("clazzName.notEquals=" + DEFAULT_CLAZZ_NAME);

        // Get all the commonTableList where clazzName not equals to UPDATED_CLAZZ_NAME
        defaultCommonTableShouldBeFound("clazzName.notEquals=" + UPDATED_CLAZZ_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName in DEFAULT_CLAZZ_NAME or UPDATED_CLAZZ_NAME
        defaultCommonTableShouldBeFound("clazzName.in=" + DEFAULT_CLAZZ_NAME + "," + UPDATED_CLAZZ_NAME);

        // Get all the commonTableList where clazzName equals to UPDATED_CLAZZ_NAME
        defaultCommonTableShouldNotBeFound("clazzName.in=" + UPDATED_CLAZZ_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName is not null
        defaultCommonTableShouldBeFound("clazzName.specified=true");

        // Get all the commonTableList where clazzName is null
        defaultCommonTableShouldNotBeFound("clazzName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName contains DEFAULT_CLAZZ_NAME
        defaultCommonTableShouldBeFound("clazzName.contains=" + DEFAULT_CLAZZ_NAME);

        // Get all the commonTableList where clazzName contains UPDATED_CLAZZ_NAME
        defaultCommonTableShouldNotBeFound("clazzName.contains=" + UPDATED_CLAZZ_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByClazzNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where clazzName does not contain DEFAULT_CLAZZ_NAME
        defaultCommonTableShouldNotBeFound("clazzName.doesNotContain=" + DEFAULT_CLAZZ_NAME);

        // Get all the commonTableList where clazzName does not contain UPDATED_CLAZZ_NAME
        defaultCommonTableShouldBeFound("clazzName.doesNotContain=" + UPDATED_CLAZZ_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGeneratedIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generated equals to DEFAULT_GENERATED
        defaultCommonTableShouldBeFound("generated.equals=" + DEFAULT_GENERATED);

        // Get all the commonTableList where generated equals to UPDATED_GENERATED
        defaultCommonTableShouldNotBeFound("generated.equals=" + UPDATED_GENERATED);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGeneratedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generated not equals to DEFAULT_GENERATED
        defaultCommonTableShouldNotBeFound("generated.notEquals=" + DEFAULT_GENERATED);

        // Get all the commonTableList where generated not equals to UPDATED_GENERATED
        defaultCommonTableShouldBeFound("generated.notEquals=" + UPDATED_GENERATED);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGeneratedIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generated in DEFAULT_GENERATED or UPDATED_GENERATED
        defaultCommonTableShouldBeFound("generated.in=" + DEFAULT_GENERATED + "," + UPDATED_GENERATED);

        // Get all the commonTableList where generated equals to UPDATED_GENERATED
        defaultCommonTableShouldNotBeFound("generated.in=" + UPDATED_GENERATED);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGeneratedIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generated is not null
        defaultCommonTableShouldBeFound("generated.specified=true");

        // Get all the commonTableList where generated is null
        defaultCommonTableShouldNotBeFound("generated.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt equals to DEFAULT_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.equals=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt equals to UPDATED_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.equals=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt not equals to DEFAULT_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.notEquals=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt not equals to UPDATED_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.notEquals=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt in DEFAULT_CREAT_AT or UPDATED_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.in=" + DEFAULT_CREAT_AT + "," + UPDATED_CREAT_AT);

        // Get all the commonTableList where creatAt equals to UPDATED_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.in=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt is not null
        defaultCommonTableShouldBeFound("creatAt.specified=true");

        // Get all the commonTableList where creatAt is null
        defaultCommonTableShouldNotBeFound("creatAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt is greater than or equal to DEFAULT_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.greaterThanOrEqual=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt is greater than or equal to UPDATED_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.greaterThanOrEqual=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt is less than or equal to DEFAULT_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.lessThanOrEqual=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt is less than or equal to SMALLER_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.lessThanOrEqual=" + SMALLER_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt is less than DEFAULT_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.lessThan=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt is less than UPDATED_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.lessThan=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByCreatAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where creatAt is greater than DEFAULT_CREAT_AT
        defaultCommonTableShouldNotBeFound("creatAt.greaterThan=" + DEFAULT_CREAT_AT);

        // Get all the commonTableList where creatAt is greater than SMALLER_CREAT_AT
        defaultCommonTableShouldBeFound("creatAt.greaterThan=" + SMALLER_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt equals to DEFAULT_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.equals=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt equals to UPDATED_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.equals=" + UPDATED_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt not equals to DEFAULT_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.notEquals=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt not equals to UPDATED_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.notEquals=" + UPDATED_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt in DEFAULT_GENERATE_AT or UPDATED_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.in=" + DEFAULT_GENERATE_AT + "," + UPDATED_GENERATE_AT);

        // Get all the commonTableList where generateAt equals to UPDATED_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.in=" + UPDATED_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt is not null
        defaultCommonTableShouldBeFound("generateAt.specified=true");

        // Get all the commonTableList where generateAt is null
        defaultCommonTableShouldNotBeFound("generateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt is greater than or equal to DEFAULT_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.greaterThanOrEqual=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt is greater than or equal to UPDATED_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.greaterThanOrEqual=" + UPDATED_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt is less than or equal to DEFAULT_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.lessThanOrEqual=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt is less than or equal to SMALLER_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.lessThanOrEqual=" + SMALLER_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt is less than DEFAULT_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.lessThan=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt is less than UPDATED_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.lessThan=" + UPDATED_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateAt is greater than DEFAULT_GENERATE_AT
        defaultCommonTableShouldNotBeFound("generateAt.greaterThan=" + DEFAULT_GENERATE_AT);

        // Get all the commonTableList where generateAt is greater than SMALLER_GENERATE_AT
        defaultCommonTableShouldBeFound("generateAt.greaterThan=" + SMALLER_GENERATE_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt equals to DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.equals=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt equals to UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.equals=" + UPDATED_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt not equals to DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.notEquals=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt not equals to UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.notEquals=" + UPDATED_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt in DEFAULT_GENERATE_CLASS_AT or UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.in=" + DEFAULT_GENERATE_CLASS_AT + "," + UPDATED_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt equals to UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.in=" + UPDATED_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt is not null
        defaultCommonTableShouldBeFound("generateClassAt.specified=true");

        // Get all the commonTableList where generateClassAt is null
        defaultCommonTableShouldNotBeFound("generateClassAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt is greater than or equal to DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.greaterThanOrEqual=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt is greater than or equal to UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.greaterThanOrEqual=" + UPDATED_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt is less than or equal to DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.lessThanOrEqual=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt is less than or equal to SMALLER_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.lessThanOrEqual=" + SMALLER_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt is less than DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.lessThan=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt is less than UPDATED_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.lessThan=" + UPDATED_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByGenerateClassAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where generateClassAt is greater than DEFAULT_GENERATE_CLASS_AT
        defaultCommonTableShouldNotBeFound("generateClassAt.greaterThan=" + DEFAULT_GENERATE_CLASS_AT);

        // Get all the commonTableList where generateClassAt is greater than SMALLER_GENERATE_CLASS_AT
        defaultCommonTableShouldBeFound("generateClassAt.greaterThan=" + SMALLER_GENERATE_CLASS_AT);
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description equals to DEFAULT_DESCRIPTION
        defaultCommonTableShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the commonTableList where description equals to UPDATED_DESCRIPTION
        defaultCommonTableShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description not equals to DEFAULT_DESCRIPTION
        defaultCommonTableShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the commonTableList where description not equals to UPDATED_DESCRIPTION
        defaultCommonTableShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCommonTableShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the commonTableList where description equals to UPDATED_DESCRIPTION
        defaultCommonTableShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description is not null
        defaultCommonTableShouldBeFound("description.specified=true");

        // Get all the commonTableList where description is null
        defaultCommonTableShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description contains DEFAULT_DESCRIPTION
        defaultCommonTableShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the commonTableList where description contains UPDATED_DESCRIPTION
        defaultCommonTableShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonTablesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where description does not contain DEFAULT_DESCRIPTION
        defaultCommonTableShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the commonTableList where description does not contain UPDATED_DESCRIPTION
        defaultCommonTableShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTreeTableIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where treeTable equals to DEFAULT_TREE_TABLE
        defaultCommonTableShouldBeFound("treeTable.equals=" + DEFAULT_TREE_TABLE);

        // Get all the commonTableList where treeTable equals to UPDATED_TREE_TABLE
        defaultCommonTableShouldNotBeFound("treeTable.equals=" + UPDATED_TREE_TABLE);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTreeTableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where treeTable not equals to DEFAULT_TREE_TABLE
        defaultCommonTableShouldNotBeFound("treeTable.notEquals=" + DEFAULT_TREE_TABLE);

        // Get all the commonTableList where treeTable not equals to UPDATED_TREE_TABLE
        defaultCommonTableShouldBeFound("treeTable.notEquals=" + UPDATED_TREE_TABLE);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTreeTableIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where treeTable in DEFAULT_TREE_TABLE or UPDATED_TREE_TABLE
        defaultCommonTableShouldBeFound("treeTable.in=" + DEFAULT_TREE_TABLE + "," + UPDATED_TREE_TABLE);

        // Get all the commonTableList where treeTable equals to UPDATED_TREE_TABLE
        defaultCommonTableShouldNotBeFound("treeTable.in=" + UPDATED_TREE_TABLE);
    }

    @Test
    @Transactional
    void getAllCommonTablesByTreeTableIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where treeTable is not null
        defaultCommonTableShouldBeFound("treeTable.specified=true");

        // Get all the commonTableList where treeTable is null
        defaultCommonTableShouldNotBeFound("treeTable.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId equals to DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.equals=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId equals to UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.equals=" + UPDATED_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId not equals to DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.notEquals=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId not equals to UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.notEquals=" + UPDATED_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId in DEFAULT_BASE_TABLE_ID or UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.in=" + DEFAULT_BASE_TABLE_ID + "," + UPDATED_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId equals to UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.in=" + UPDATED_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId is not null
        defaultCommonTableShouldBeFound("baseTableId.specified=true");

        // Get all the commonTableList where baseTableId is null
        defaultCommonTableShouldNotBeFound("baseTableId.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId is greater than or equal to DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.greaterThanOrEqual=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId is greater than or equal to UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.greaterThanOrEqual=" + UPDATED_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId is less than or equal to DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.lessThanOrEqual=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId is less than or equal to SMALLER_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.lessThanOrEqual=" + SMALLER_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId is less than DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.lessThan=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId is less than UPDATED_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.lessThan=" + UPDATED_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByBaseTableIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where baseTableId is greater than DEFAULT_BASE_TABLE_ID
        defaultCommonTableShouldNotBeFound("baseTableId.greaterThan=" + DEFAULT_BASE_TABLE_ID);

        // Get all the commonTableList where baseTableId is greater than SMALLER_BASE_TABLE_ID
        defaultCommonTableShouldBeFound("baseTableId.greaterThan=" + SMALLER_BASE_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth equals to DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.equals=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth equals to UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.equals=" + UPDATED_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth not equals to DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.notEquals=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth not equals to UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.notEquals=" + UPDATED_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth in DEFAULT_RECORD_ACTION_WIDTH or UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.in=" + DEFAULT_RECORD_ACTION_WIDTH + "," + UPDATED_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth equals to UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.in=" + UPDATED_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth is not null
        defaultCommonTableShouldBeFound("recordActionWidth.specified=true");

        // Get all the commonTableList where recordActionWidth is null
        defaultCommonTableShouldNotBeFound("recordActionWidth.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth is greater than or equal to DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.greaterThanOrEqual=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth is greater than or equal to UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.greaterThanOrEqual=" + UPDATED_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth is less than or equal to DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.lessThanOrEqual=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth is less than or equal to SMALLER_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.lessThanOrEqual=" + SMALLER_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth is less than DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.lessThan=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth is less than UPDATED_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.lessThan=" + UPDATED_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByRecordActionWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where recordActionWidth is greater than DEFAULT_RECORD_ACTION_WIDTH
        defaultCommonTableShouldNotBeFound("recordActionWidth.greaterThan=" + DEFAULT_RECORD_ACTION_WIDTH);

        // Get all the commonTableList where recordActionWidth is greater than SMALLER_RECORD_ACTION_WIDTH
        defaultCommonTableShouldBeFound("recordActionWidth.greaterThan=" + SMALLER_RECORD_ACTION_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEditInModalIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where editInModal equals to DEFAULT_EDIT_IN_MODAL
        defaultCommonTableShouldBeFound("editInModal.equals=" + DEFAULT_EDIT_IN_MODAL);

        // Get all the commonTableList where editInModal equals to UPDATED_EDIT_IN_MODAL
        defaultCommonTableShouldNotBeFound("editInModal.equals=" + UPDATED_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEditInModalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where editInModal not equals to DEFAULT_EDIT_IN_MODAL
        defaultCommonTableShouldNotBeFound("editInModal.notEquals=" + DEFAULT_EDIT_IN_MODAL);

        // Get all the commonTableList where editInModal not equals to UPDATED_EDIT_IN_MODAL
        defaultCommonTableShouldBeFound("editInModal.notEquals=" + UPDATED_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEditInModalIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where editInModal in DEFAULT_EDIT_IN_MODAL or UPDATED_EDIT_IN_MODAL
        defaultCommonTableShouldBeFound("editInModal.in=" + DEFAULT_EDIT_IN_MODAL + "," + UPDATED_EDIT_IN_MODAL);

        // Get all the commonTableList where editInModal equals to UPDATED_EDIT_IN_MODAL
        defaultCommonTableShouldNotBeFound("editInModal.in=" + UPDATED_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void getAllCommonTablesByEditInModalIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        // Get all the commonTableList where editInModal is not null
        defaultCommonTableShouldBeFound("editInModal.specified=true");

        // Get all the commonTableList where editInModal is null
        defaultCommonTableShouldNotBeFound("editInModal.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommonTablesByCommonTableFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);
        CommonTableField commonTableFields = CommonTableFieldResourceIT.createEntity();
        commonTable.addCommonTableFields(commonTableFields);
        commonTableRepository.insert(commonTable);
        Long commonTableFieldsId = commonTableFields.getId();

        // Get all the commonTableList where commonTableFields equals to commonTableFieldsId
        defaultCommonTableShouldBeFound("commonTableFieldsId.equals=" + commonTableFieldsId);

        // Get all the commonTableList where commonTableFields equals to (commonTableFieldsId + 1)
        defaultCommonTableShouldNotBeFound("commonTableFieldsId.equals=" + (commonTableFieldsId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTablesByRelationshipsIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);
        CommonTableRelationship relationships = CommonTableRelationshipResourceIT.createEntity();
        commonTable.addRelationships(relationships);
        commonTableRepository.insert(commonTable);
        Long relationshipsId = relationships.getId();

        // Get all the commonTableList where relationships equals to relationshipsId
        defaultCommonTableShouldBeFound("relationshipsId.equals=" + relationshipsId);

        // Get all the commonTableList where relationships equals to (relationshipsId + 1)
        defaultCommonTableShouldNotBeFound("relationshipsId.equals=" + (relationshipsId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTablesByMetaModelIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);
        CommonTable metaModel = CommonTableResourceIT.createEntity();
        commonTable.setMetaModel(metaModel);
        commonTableRepository.insert(commonTable);
        Long metaModelId = metaModel.getId();

        // Get all the commonTableList where metaModel equals to metaModelId
        defaultCommonTableShouldBeFound("metaModelId.equals=" + metaModelId);

        // Get all the commonTableList where metaModel equals to (metaModelId + 1)
        defaultCommonTableShouldNotBeFound("metaModelId.equals=" + (metaModelId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTablesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);
        User creator = UserResourceIT.createEntity();
        commonTable.setCreator(creator);
        commonTableRepository.insert(commonTable);
        Long creatorId = creator.getId();

        // Get all the commonTableList where creator equals to creatorId
        defaultCommonTableShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the commonTableList where creator equals to (creatorId + 1)
        defaultCommonTableShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTablesByBusinessTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);
        BusinessType businessType = BusinessTypeResourceIT.createEntity();
        commonTable.setBusinessType(businessType);
        commonTableRepository.insert(commonTable);
        Long businessTypeId = businessType.getId();

        // Get all the commonTableList where businessType equals to businessTypeId
        defaultCommonTableShouldBeFound("businessTypeId.equals=" + businessTypeId);

        // Get all the commonTableList where businessType equals to (businessTypeId + 1)
        defaultCommonTableShouldNotBeFound("businessTypeId.equals=" + (businessTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonTableShouldBeFound(String filter) throws Exception {
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].clazzName").value(hasItem(DEFAULT_CLAZZ_NAME)))
            .andExpect(jsonPath("$.[*].generated").value(hasItem(DEFAULT_GENERATED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatAt").value(hasItem(sameInstant(DEFAULT_CREAT_AT))))
            .andExpect(jsonPath("$.[*].generateAt").value(hasItem(sameInstant(DEFAULT_GENERATE_AT))))
            .andExpect(jsonPath("$.[*].generateClassAt").value(hasItem(sameInstant(DEFAULT_GENERATE_CLASS_AT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeTable").value(hasItem(DEFAULT_TREE_TABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].baseTableId").value(hasItem(DEFAULT_BASE_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].recordActionWidth").value(hasItem(DEFAULT_RECORD_ACTION_WIDTH)))
            .andExpect(jsonPath("$.[*].listConfig").value(hasItem(DEFAULT_LIST_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].formConfig").value(hasItem(DEFAULT_FORM_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].editInModal").value(hasItem(DEFAULT_EDIT_IN_MODAL.booleanValue())));

        // Check, that the count call also returns 1
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonTableShouldNotBeFound(String filter) throws Exception {
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonTableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonTable() throws Exception {
        // Get the commonTable
        restCommonTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonTable() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();

        // Update the commonTable
        CommonTable updatedCommonTable = commonTableRepository.findById(commonTable.getId()).get();

        // Disconnect from session so that the updates on updatedCommonTable are not directly saved in db

        updatedCommonTable
            .name(UPDATED_NAME)
            .entityName(UPDATED_ENTITY_NAME)
            .tableName(UPDATED_TABLE_NAME)
            .system(UPDATED_SYSTEM)
            .clazzName(UPDATED_CLAZZ_NAME)
            .generated(UPDATED_GENERATED)
            .creatAt(UPDATED_CREAT_AT)
            .generateAt(UPDATED_GENERATE_AT)
            .generateClassAt(UPDATED_GENERATE_CLASS_AT)
            .description(UPDATED_DESCRIPTION)
            .treeTable(UPDATED_TREE_TABLE)
            .baseTableId(UPDATED_BASE_TABLE_ID)
            .recordActionWidth(UPDATED_RECORD_ACTION_WIDTH)
            .listConfig(UPDATED_LIST_CONFIG)
            .formConfig(UPDATED_FORM_CONFIG)
            .editInModal(UPDATED_EDIT_IN_MODAL);
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(updatedCommonTable);

        restCommonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
        CommonTable testCommonTable = commonTableList.get(commonTableList.size() - 1);
        assertThat(testCommonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTable.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testCommonTable.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testCommonTable.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTable.getClazzName()).isEqualTo(UPDATED_CLAZZ_NAME);
        assertThat(testCommonTable.getGenerated()).isEqualTo(UPDATED_GENERATED);
        assertThat(testCommonTable.getCreatAt()).isEqualTo(UPDATED_CREAT_AT);
        assertThat(testCommonTable.getGenerateAt()).isEqualTo(UPDATED_GENERATE_AT);
        assertThat(testCommonTable.getGenerateClassAt()).isEqualTo(UPDATED_GENERATE_CLASS_AT);
        assertThat(testCommonTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonTable.getTreeTable()).isEqualTo(UPDATED_TREE_TABLE);
        assertThat(testCommonTable.getBaseTableId()).isEqualTo(UPDATED_BASE_TABLE_ID);
        assertThat(testCommonTable.getRecordActionWidth()).isEqualTo(UPDATED_RECORD_ACTION_WIDTH);
        assertThat(testCommonTable.getListConfig()).isEqualTo(UPDATED_LIST_CONFIG);
        assertThat(testCommonTable.getFormConfig()).isEqualTo(UPDATED_FORM_CONFIG);
        assertThat(testCommonTable.getEditInModal()).isEqualTo(UPDATED_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void putNonExistingngCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        restCommonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonTableWithPatch() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();

        // Update the commonTable using partial update
        CommonTable partialUpdatedCommonTable = new CommonTable();
        partialUpdatedCommonTable.setId(commonTable.getId());

        partialUpdatedCommonTable
            .name(UPDATED_NAME)
            .entityName(UPDATED_ENTITY_NAME)
            .system(UPDATED_SYSTEM)
            .generated(UPDATED_GENERATED)
            .generateAt(UPDATED_GENERATE_AT)
            .generateClassAt(UPDATED_GENERATE_CLASS_AT)
            .description(UPDATED_DESCRIPTION)
            .baseTableId(UPDATED_BASE_TABLE_ID)
            .recordActionWidth(UPDATED_RECORD_ACTION_WIDTH)
            .listConfig(UPDATED_LIST_CONFIG);

        restCommonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTable))
            )
            .andExpect(status().isOk());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
        CommonTable testCommonTable = commonTableList.get(commonTableList.size() - 1);
        assertThat(testCommonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTable.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testCommonTable.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testCommonTable.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTable.getClazzName()).isEqualTo(DEFAULT_CLAZZ_NAME);
        assertThat(testCommonTable.getGenerated()).isEqualTo(UPDATED_GENERATED);
        assertThat(testCommonTable.getCreatAt()).isEqualTo(DEFAULT_CREAT_AT);
        assertThat(testCommonTable.getGenerateAt()).isEqualTo(UPDATED_GENERATE_AT);
        assertThat(testCommonTable.getGenerateClassAt()).isEqualTo(UPDATED_GENERATE_CLASS_AT);
        assertThat(testCommonTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonTable.getTreeTable()).isEqualTo(DEFAULT_TREE_TABLE);
        assertThat(testCommonTable.getBaseTableId()).isEqualTo(UPDATED_BASE_TABLE_ID);
        assertThat(testCommonTable.getRecordActionWidth()).isEqualTo(UPDATED_RECORD_ACTION_WIDTH);
        assertThat(testCommonTable.getListConfig()).isEqualTo(UPDATED_LIST_CONFIG);
        assertThat(testCommonTable.getFormConfig()).isEqualTo(DEFAULT_FORM_CONFIG);
        assertThat(testCommonTable.getEditInModal()).isEqualTo(DEFAULT_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void fullUpdateCommonTableWithPatch() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();

        // Update the commonTable using partial update
        CommonTable partialUpdatedCommonTable = new CommonTable();
        partialUpdatedCommonTable.setId(commonTable.getId());

        partialUpdatedCommonTable
            .name(UPDATED_NAME)
            .entityName(UPDATED_ENTITY_NAME)
            .tableName(UPDATED_TABLE_NAME)
            .system(UPDATED_SYSTEM)
            .clazzName(UPDATED_CLAZZ_NAME)
            .generated(UPDATED_GENERATED)
            .creatAt(UPDATED_CREAT_AT)
            .generateAt(UPDATED_GENERATE_AT)
            .generateClassAt(UPDATED_GENERATE_CLASS_AT)
            .description(UPDATED_DESCRIPTION)
            .treeTable(UPDATED_TREE_TABLE)
            .baseTableId(UPDATED_BASE_TABLE_ID)
            .recordActionWidth(UPDATED_RECORD_ACTION_WIDTH)
            .listConfig(UPDATED_LIST_CONFIG)
            .formConfig(UPDATED_FORM_CONFIG)
            .editInModal(UPDATED_EDIT_IN_MODAL);

        restCommonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTable))
            )
            .andExpect(status().isOk());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
        CommonTable testCommonTable = commonTableList.get(commonTableList.size() - 1);
        assertThat(testCommonTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTable.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testCommonTable.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testCommonTable.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTable.getClazzName()).isEqualTo(UPDATED_CLAZZ_NAME);
        assertThat(testCommonTable.getGenerated()).isEqualTo(UPDATED_GENERATED);
        assertThat(testCommonTable.getCreatAt()).isEqualTo(UPDATED_CREAT_AT);
        assertThat(testCommonTable.getGenerateAt()).isEqualTo(UPDATED_GENERATE_AT);
        assertThat(testCommonTable.getGenerateClassAt()).isEqualTo(UPDATED_GENERATE_CLASS_AT);
        assertThat(testCommonTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonTable.getTreeTable()).isEqualTo(UPDATED_TREE_TABLE);
        assertThat(testCommonTable.getBaseTableId()).isEqualTo(UPDATED_BASE_TABLE_ID);
        assertThat(testCommonTable.getRecordActionWidth()).isEqualTo(UPDATED_RECORD_ACTION_WIDTH);
        assertThat(testCommonTable.getListConfig()).isEqualTo(UPDATED_LIST_CONFIG);
        assertThat(testCommonTable.getFormConfig()).isEqualTo(UPDATED_FORM_CONFIG);
        assertThat(testCommonTable.getEditInModal()).isEqualTo(UPDATED_EDIT_IN_MODAL);
    }

    @Test
    @Transactional
    void patchNonExistingCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonTableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonTable() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRepository.findAll().size();
        commonTable.setId(count.incrementAndGet());

        // Create the CommonTable
        CommonTableDTO commonTableDTO = commonTableMapper.toDto(commonTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commonTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTable in the database
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonTable() throws Exception {
        // Initialize the database
        commonTableRepository.insert(commonTable);

        int databaseSizeBeforeDelete = commonTableRepository.findAll().size();

        // Delete the commonTable
        restCommonTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonTable> commonTableList = commonTableRepository.findAll();
        assertThat(commonTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
