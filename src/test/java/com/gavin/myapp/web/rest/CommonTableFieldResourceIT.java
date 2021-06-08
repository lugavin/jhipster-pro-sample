package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.domain.CommonTableField;
import com.gavin.myapp.domain.enumeration.CommonFieldType;
import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
import com.gavin.myapp.repository.CommonTableFieldRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonTableFieldQueryService;
import com.gavin.myapp.service.criteria.CommonTableFieldCriteria;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import com.gavin.myapp.service.mapper.CommonTableFieldMapper;
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
 * Integration tests for the {@link CommonTableFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonTableFieldResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_FIELD_NAME = "BBBBBBBBBB";

    private static final CommonFieldType DEFAULT_TYPE = CommonFieldType.INTEGER;
    private static final CommonFieldType UPDATED_TYPE = CommonFieldType.LONG;

    private static final String DEFAULT_TABLE_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_COLUMN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COLUMN_WIDTH = 0;
    private static final Integer UPDATED_COLUMN_WIDTH = 1;
    private static final Integer SMALLER_COLUMN_WIDTH = 0 - 1;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Boolean DEFAULT_EDIT_IN_LIST = false;
    private static final Boolean UPDATED_EDIT_IN_LIST = true;

    private static final Boolean DEFAULT_HIDE_IN_LIST = false;
    private static final Boolean UPDATED_HIDE_IN_LIST = true;

    private static final Boolean DEFAULT_HIDE_IN_FORM = false;
    private static final Boolean UPDATED_HIDE_IN_FORM = true;

    private static final Boolean DEFAULT_ENABLE_FILTER = false;
    private static final Boolean UPDATED_ENABLE_FILTER = true;

    private static final String DEFAULT_VALIDATE_RULES = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATE_RULES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_IN_FILTER_TREE = false;
    private static final Boolean UPDATED_SHOW_IN_FILTER_TREE = true;

    private static final FixedType DEFAULT_FIXED = FixedType.LEFT;
    private static final FixedType UPDATED_FIXED = FixedType.RIGHT;

    private static final Boolean DEFAULT_SORTABLE = false;
    private static final Boolean UPDATED_SORTABLE = true;

    private static final Boolean DEFAULT_TREE_INDICATOR = false;
    private static final Boolean UPDATED_TREE_INDICATOR = true;

    private static final Boolean DEFAULT_CLIENT_READ_ONLY = false;
    private static final Boolean UPDATED_CLIENT_READ_ONLY = true;

    private static final String DEFAULT_FIELD_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NOT_NULL = false;
    private static final Boolean UPDATED_NOT_NULL = true;

    private static final Boolean DEFAULT_SYSTEM = false;
    private static final Boolean UPDATED_SYSTEM = true;

    private static final String DEFAULT_HELP = "AAAAAAAAAA";
    private static final String UPDATED_HELP = "BBBBBBBBBB";

    private static final String DEFAULT_FONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NULL_HIDE_IN_FORM = false;
    private static final Boolean UPDATED_NULL_HIDE_IN_FORM = true;

    private static final EndUsedType DEFAULT_END_USED = EndUsedType.FILE_URL;
    private static final EndUsedType UPDATED_END_USED = EndUsedType.IMAGE_URL;

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/common-table-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonTableFieldRepository commonTableFieldRepository;

    @Autowired
    private CommonTableFieldMapper commonTableFieldMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restCommonTableFieldMockMvc;

    private CommonTableField commonTableField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTableField createEntity() {
        CommonTableField commonTableField = new CommonTableField()
            .title(DEFAULT_TITLE)
            .entityFieldName(DEFAULT_ENTITY_FIELD_NAME)
            .type(DEFAULT_TYPE)
            .tableColumnName(DEFAULT_TABLE_COLUMN_NAME)
            .columnWidth(DEFAULT_COLUMN_WIDTH)
            .order(DEFAULT_ORDER)
            .editInList(DEFAULT_EDIT_IN_LIST)
            .hideInList(DEFAULT_HIDE_IN_LIST)
            .hideInForm(DEFAULT_HIDE_IN_FORM)
            .enableFilter(DEFAULT_ENABLE_FILTER)
            .validateRules(DEFAULT_VALIDATE_RULES)
            .showInFilterTree(DEFAULT_SHOW_IN_FILTER_TREE)
            .fixed(DEFAULT_FIXED)
            .sortable(DEFAULT_SORTABLE)
            .treeIndicator(DEFAULT_TREE_INDICATOR)
            .clientReadOnly(DEFAULT_CLIENT_READ_ONLY)
            .fieldValues(DEFAULT_FIELD_VALUES)
            .notNull(DEFAULT_NOT_NULL)
            .system(DEFAULT_SYSTEM)
            .help(DEFAULT_HELP)
            .fontColor(DEFAULT_FONT_COLOR)
            .backgroundColor(DEFAULT_BACKGROUND_COLOR)
            .nullHideInForm(DEFAULT_NULL_HIDE_IN_FORM)
            .endUsed(DEFAULT_END_USED)
            .options(DEFAULT_OPTIONS);
        return commonTableField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTableField createUpdatedEntity() {
        CommonTableField commonTableField = new CommonTableField()
            .title(UPDATED_TITLE)
            .entityFieldName(UPDATED_ENTITY_FIELD_NAME)
            .type(UPDATED_TYPE)
            .tableColumnName(UPDATED_TABLE_COLUMN_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .editInList(UPDATED_EDIT_IN_LIST)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .validateRules(UPDATED_VALIDATE_RULES)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .fixed(UPDATED_FIXED)
            .sortable(UPDATED_SORTABLE)
            .treeIndicator(UPDATED_TREE_INDICATOR)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .fieldValues(UPDATED_FIELD_VALUES)
            .notNull(UPDATED_NOT_NULL)
            .system(UPDATED_SYSTEM)
            .help(UPDATED_HELP)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .nullHideInForm(UPDATED_NULL_HIDE_IN_FORM)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);
        return commonTableField;
    }

    @BeforeEach
    public void initTest() {
        commonTableField = createEntity();
    }

    @Test
    @Transactional
    void createCommonTableField() throws Exception {
        int databaseSizeBeforeCreate = commonTableFieldRepository.findAll().size();
        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);
        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL_ID, commonTableFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeCreate + 1);
        CommonTableField testCommonTableField = commonTableFieldList.get(commonTableFieldList.size() - 1);
        assertThat(testCommonTableField.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCommonTableField.getEntityFieldName()).isEqualTo(DEFAULT_ENTITY_FIELD_NAME);
        assertThat(testCommonTableField.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCommonTableField.getTableColumnName()).isEqualTo(DEFAULT_TABLE_COLUMN_NAME);
        assertThat(testCommonTableField.getColumnWidth()).isEqualTo(DEFAULT_COLUMN_WIDTH);
        assertThat(testCommonTableField.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCommonTableField.getEditInList()).isEqualTo(DEFAULT_EDIT_IN_LIST);
        assertThat(testCommonTableField.getHideInList()).isEqualTo(DEFAULT_HIDE_IN_LIST);
        assertThat(testCommonTableField.getHideInForm()).isEqualTo(DEFAULT_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEnableFilter()).isEqualTo(DEFAULT_ENABLE_FILTER);
        assertThat(testCommonTableField.getValidateRules()).isEqualTo(DEFAULT_VALIDATE_RULES);
        assertThat(testCommonTableField.getShowInFilterTree()).isEqualTo(DEFAULT_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableField.getFixed()).isEqualTo(DEFAULT_FIXED);
        assertThat(testCommonTableField.getSortable()).isEqualTo(DEFAULT_SORTABLE);
        assertThat(testCommonTableField.getTreeIndicator()).isEqualTo(DEFAULT_TREE_INDICATOR);
        assertThat(testCommonTableField.getClientReadOnly()).isEqualTo(DEFAULT_CLIENT_READ_ONLY);
        assertThat(testCommonTableField.getFieldValues()).isEqualTo(DEFAULT_FIELD_VALUES);
        assertThat(testCommonTableField.getNotNull()).isEqualTo(DEFAULT_NOT_NULL);
        assertThat(testCommonTableField.getSystem()).isEqualTo(DEFAULT_SYSTEM);
        assertThat(testCommonTableField.getHelp()).isEqualTo(DEFAULT_HELP);
        assertThat(testCommonTableField.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testCommonTableField.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testCommonTableField.getNullHideInForm()).isEqualTo(DEFAULT_NULL_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEndUsed()).isEqualTo(DEFAULT_END_USED);
        assertThat(testCommonTableField.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    @Transactional
    void createCommonTableFieldWithExistingId() throws Exception {
        // Create the CommonTableField with an existing ID
        commonTableField.setId(1L);
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        int databaseSizeBeforeCreate = commonTableFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableFieldRepository.findAll().size();
        // set the field null
        commonTableField.setTitle(null);

        // Create the CommonTableField, which fails.
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableFieldRepository.findAll().size();
        // set the field null
        commonTableField.setEntityFieldName(null);

        // Create the CommonTableField, which fails.
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableFieldRepository.findAll().size();
        // set the field null
        commonTableField.setType(null);

        // Create the CommonTableField, which fails.
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableColumnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableFieldRepository.findAll().size();
        // set the field null
        commonTableField.setTableColumnName(null);

        // Create the CommonTableField, which fails.
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        restCommonTableFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonTableFields() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTableField.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].entityFieldName").value(hasItem(DEFAULT_ENTITY_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tableColumnName").value(hasItem(DEFAULT_TABLE_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].columnWidth").value(hasItem(DEFAULT_COLUMN_WIDTH)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].editInList").value(hasItem(DEFAULT_EDIT_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInList").value(hasItem(DEFAULT_HIDE_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInForm").value(hasItem(DEFAULT_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].enableFilter").value(hasItem(DEFAULT_ENABLE_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].validateRules").value(hasItem(DEFAULT_VALIDATE_RULES)))
            .andExpect(jsonPath("$.[*].showInFilterTree").value(hasItem(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].fixed").value(hasItem(DEFAULT_FIXED.toString())))
            .andExpect(jsonPath("$.[*].sortable").value(hasItem(DEFAULT_SORTABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].treeIndicator").value(hasItem(DEFAULT_TREE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].clientReadOnly").value(hasItem(DEFAULT_CLIENT_READ_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].fieldValues").value(hasItem(DEFAULT_FIELD_VALUES)))
            .andExpect(jsonPath("$.[*].notNull").value(hasItem(DEFAULT_NOT_NULL.booleanValue())))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].help").value(hasItem(DEFAULT_HELP)))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].nullHideInForm").value(hasItem(DEFAULT_NULL_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].endUsed").value(hasItem(DEFAULT_END_USED.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));
    }

    @Test
    @Transactional
    public void getCommonTableField() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get the commonTableField
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, commonTableField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonTableField.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.entityFieldName").value(DEFAULT_ENTITY_FIELD_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.tableColumnName").value(DEFAULT_TABLE_COLUMN_NAME))
            .andExpect(jsonPath("$.columnWidth").value(DEFAULT_COLUMN_WIDTH))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.editInList").value(DEFAULT_EDIT_IN_LIST.booleanValue()))
            .andExpect(jsonPath("$.hideInList").value(DEFAULT_HIDE_IN_LIST.booleanValue()))
            .andExpect(jsonPath("$.hideInForm").value(DEFAULT_HIDE_IN_FORM.booleanValue()))
            .andExpect(jsonPath("$.enableFilter").value(DEFAULT_ENABLE_FILTER.booleanValue()))
            .andExpect(jsonPath("$.validateRules").value(DEFAULT_VALIDATE_RULES))
            .andExpect(jsonPath("$.showInFilterTree").value(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue()))
            .andExpect(jsonPath("$.fixed").value(DEFAULT_FIXED.toString()))
            .andExpect(jsonPath("$.sortable").value(DEFAULT_SORTABLE.booleanValue()))
            .andExpect(jsonPath("$.treeIndicator").value(DEFAULT_TREE_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.clientReadOnly").value(DEFAULT_CLIENT_READ_ONLY.booleanValue()))
            .andExpect(jsonPath("$.fieldValues").value(DEFAULT_FIELD_VALUES))
            .andExpect(jsonPath("$.notNull").value(DEFAULT_NOT_NULL.booleanValue()))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.help").value(DEFAULT_HELP))
            .andExpect(jsonPath("$.fontColor").value(DEFAULT_FONT_COLOR))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.nullHideInForm").value(DEFAULT_NULL_HIDE_IN_FORM.booleanValue()))
            .andExpect(jsonPath("$.endUsed").value(DEFAULT_END_USED.toString()))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS));
    }

    @Test
    @Transactional
    public void getCommonTableFieldsByIdFiltering() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        Long id = commonTableField.getId();

        defaultCommonTableFieldShouldBeFound("id.equals=" + id);
        defaultCommonTableFieldShouldNotBeFound("id.notEquals=" + id);

        defaultCommonTableFieldShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonTableFieldShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonTableFieldShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonTableFieldShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title equals to DEFAULT_TITLE
        defaultCommonTableFieldShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the commonTableFieldList where title equals to UPDATED_TITLE
        defaultCommonTableFieldShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title not equals to DEFAULT_TITLE
        defaultCommonTableFieldShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the commonTableFieldList where title not equals to UPDATED_TITLE
        defaultCommonTableFieldShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCommonTableFieldShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the commonTableFieldList where title equals to UPDATED_TITLE
        defaultCommonTableFieldShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title is not null
        defaultCommonTableFieldShouldBeFound("title.specified=true");

        // Get all the commonTableFieldList where title is null
        defaultCommonTableFieldShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title contains DEFAULT_TITLE
        defaultCommonTableFieldShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the commonTableFieldList where title contains UPDATED_TITLE
        defaultCommonTableFieldShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where title does not contain DEFAULT_TITLE
        defaultCommonTableFieldShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the commonTableFieldList where title does not contain UPDATED_TITLE
        defaultCommonTableFieldShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName equals to DEFAULT_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldBeFound("entityFieldName.equals=" + DEFAULT_ENTITY_FIELD_NAME);

        // Get all the commonTableFieldList where entityFieldName equals to UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.equals=" + UPDATED_ENTITY_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName not equals to DEFAULT_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.notEquals=" + DEFAULT_ENTITY_FIELD_NAME);

        // Get all the commonTableFieldList where entityFieldName not equals to UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldBeFound("entityFieldName.notEquals=" + UPDATED_ENTITY_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName in DEFAULT_ENTITY_FIELD_NAME or UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldBeFound("entityFieldName.in=" + DEFAULT_ENTITY_FIELD_NAME + "," + UPDATED_ENTITY_FIELD_NAME);

        // Get all the commonTableFieldList where entityFieldName equals to UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.in=" + UPDATED_ENTITY_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName is not null
        defaultCommonTableFieldShouldBeFound("entityFieldName.specified=true");

        // Get all the commonTableFieldList where entityFieldName is null
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName contains DEFAULT_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldBeFound("entityFieldName.contains=" + DEFAULT_ENTITY_FIELD_NAME);

        // Get all the commonTableFieldList where entityFieldName contains UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.contains=" + UPDATED_ENTITY_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEntityFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where entityFieldName does not contain DEFAULT_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldNotBeFound("entityFieldName.doesNotContain=" + DEFAULT_ENTITY_FIELD_NAME);

        // Get all the commonTableFieldList where entityFieldName does not contain UPDATED_ENTITY_FIELD_NAME
        defaultCommonTableFieldShouldBeFound("entityFieldName.doesNotContain=" + UPDATED_ENTITY_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where type equals to DEFAULT_TYPE
        defaultCommonTableFieldShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the commonTableFieldList where type equals to UPDATED_TYPE
        defaultCommonTableFieldShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where type not equals to DEFAULT_TYPE
        defaultCommonTableFieldShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the commonTableFieldList where type not equals to UPDATED_TYPE
        defaultCommonTableFieldShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCommonTableFieldShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the commonTableFieldList where type equals to UPDATED_TYPE
        defaultCommonTableFieldShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where type is not null
        defaultCommonTableFieldShouldBeFound("type.specified=true");

        // Get all the commonTableFieldList where type is null
        defaultCommonTableFieldShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName equals to DEFAULT_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldBeFound("tableColumnName.equals=" + DEFAULT_TABLE_COLUMN_NAME);

        // Get all the commonTableFieldList where tableColumnName equals to UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.equals=" + UPDATED_TABLE_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName not equals to DEFAULT_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.notEquals=" + DEFAULT_TABLE_COLUMN_NAME);

        // Get all the commonTableFieldList where tableColumnName not equals to UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldBeFound("tableColumnName.notEquals=" + UPDATED_TABLE_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName in DEFAULT_TABLE_COLUMN_NAME or UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldBeFound("tableColumnName.in=" + DEFAULT_TABLE_COLUMN_NAME + "," + UPDATED_TABLE_COLUMN_NAME);

        // Get all the commonTableFieldList where tableColumnName equals to UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.in=" + UPDATED_TABLE_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName is not null
        defaultCommonTableFieldShouldBeFound("tableColumnName.specified=true");

        // Get all the commonTableFieldList where tableColumnName is null
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName contains DEFAULT_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldBeFound("tableColumnName.contains=" + DEFAULT_TABLE_COLUMN_NAME);

        // Get all the commonTableFieldList where tableColumnName contains UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.contains=" + UPDATED_TABLE_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTableColumnNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where tableColumnName does not contain DEFAULT_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldNotBeFound("tableColumnName.doesNotContain=" + DEFAULT_TABLE_COLUMN_NAME);

        // Get all the commonTableFieldList where tableColumnName does not contain UPDATED_TABLE_COLUMN_NAME
        defaultCommonTableFieldShouldBeFound("tableColumnName.doesNotContain=" + UPDATED_TABLE_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth equals to DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.equals=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.equals=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth not equals to DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.notEquals=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth not equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.notEquals=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth in DEFAULT_COLUMN_WIDTH or UPDATED_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.in=" + DEFAULT_COLUMN_WIDTH + "," + UPDATED_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.in=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth is not null
        defaultCommonTableFieldShouldBeFound("columnWidth.specified=true");

        // Get all the commonTableFieldList where columnWidth is null
        defaultCommonTableFieldShouldNotBeFound("columnWidth.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth is greater than or equal to DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.greaterThanOrEqual=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth is greater than or equal to (DEFAULT_COLUMN_WIDTH + 1)
        defaultCommonTableFieldShouldNotBeFound("columnWidth.greaterThanOrEqual=" + (DEFAULT_COLUMN_WIDTH + 1));
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth is less than or equal to DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.lessThanOrEqual=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth is less than or equal to SMALLER_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.lessThanOrEqual=" + SMALLER_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth is less than DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.lessThan=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth is less than (DEFAULT_COLUMN_WIDTH + 1)
        defaultCommonTableFieldShouldBeFound("columnWidth.lessThan=" + (DEFAULT_COLUMN_WIDTH + 1));
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByColumnWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where columnWidth is greater than DEFAULT_COLUMN_WIDTH
        defaultCommonTableFieldShouldNotBeFound("columnWidth.greaterThan=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableFieldList where columnWidth is greater than SMALLER_COLUMN_WIDTH
        defaultCommonTableFieldShouldBeFound("columnWidth.greaterThan=" + SMALLER_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order equals to DEFAULT_ORDER
        defaultCommonTableFieldShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order equals to UPDATED_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order not equals to DEFAULT_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order not equals to UPDATED_ORDER
        defaultCommonTableFieldShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultCommonTableFieldShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the commonTableFieldList where order equals to UPDATED_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order is not null
        defaultCommonTableFieldShouldBeFound("order.specified=true");

        // Get all the commonTableFieldList where order is null
        defaultCommonTableFieldShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order is greater than or equal to DEFAULT_ORDER
        defaultCommonTableFieldShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order is greater than or equal to UPDATED_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order is less than or equal to DEFAULT_ORDER
        defaultCommonTableFieldShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order is less than or equal to SMALLER_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order is less than DEFAULT_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order is less than UPDATED_ORDER
        defaultCommonTableFieldShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where order is greater than DEFAULT_ORDER
        defaultCommonTableFieldShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the commonTableFieldList where order is greater than SMALLER_ORDER
        defaultCommonTableFieldShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEditInListIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where editInList equals to DEFAULT_EDIT_IN_LIST
        defaultCommonTableFieldShouldBeFound("editInList.equals=" + DEFAULT_EDIT_IN_LIST);

        // Get all the commonTableFieldList where editInList equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("editInList.equals=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEditInListIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where editInList not equals to DEFAULT_EDIT_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("editInList.notEquals=" + DEFAULT_EDIT_IN_LIST);

        // Get all the commonTableFieldList where editInList not equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableFieldShouldBeFound("editInList.notEquals=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEditInListIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where editInList in DEFAULT_EDIT_IN_LIST or UPDATED_EDIT_IN_LIST
        defaultCommonTableFieldShouldBeFound("editInList.in=" + DEFAULT_EDIT_IN_LIST + "," + UPDATED_EDIT_IN_LIST);

        // Get all the commonTableFieldList where editInList equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("editInList.in=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEditInListIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where editInList is not null
        defaultCommonTableFieldShouldBeFound("editInList.specified=true");

        // Get all the commonTableFieldList where editInList is null
        defaultCommonTableFieldShouldNotBeFound("editInList.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInListIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInList equals to DEFAULT_HIDE_IN_LIST
        defaultCommonTableFieldShouldBeFound("hideInList.equals=" + DEFAULT_HIDE_IN_LIST);

        // Get all the commonTableFieldList where hideInList equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("hideInList.equals=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInListIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInList not equals to DEFAULT_HIDE_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("hideInList.notEquals=" + DEFAULT_HIDE_IN_LIST);

        // Get all the commonTableFieldList where hideInList not equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableFieldShouldBeFound("hideInList.notEquals=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInListIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInList in DEFAULT_HIDE_IN_LIST or UPDATED_HIDE_IN_LIST
        defaultCommonTableFieldShouldBeFound("hideInList.in=" + DEFAULT_HIDE_IN_LIST + "," + UPDATED_HIDE_IN_LIST);

        // Get all the commonTableFieldList where hideInList equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableFieldShouldNotBeFound("hideInList.in=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInListIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInList is not null
        defaultCommonTableFieldShouldBeFound("hideInList.specified=true");

        // Get all the commonTableFieldList where hideInList is null
        defaultCommonTableFieldShouldNotBeFound("hideInList.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInFormIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInForm equals to DEFAULT_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("hideInForm.equals=" + DEFAULT_HIDE_IN_FORM);

        // Get all the commonTableFieldList where hideInForm equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("hideInForm.equals=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInFormIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInForm not equals to DEFAULT_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("hideInForm.notEquals=" + DEFAULT_HIDE_IN_FORM);

        // Get all the commonTableFieldList where hideInForm not equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("hideInForm.notEquals=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInFormIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInForm in DEFAULT_HIDE_IN_FORM or UPDATED_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("hideInForm.in=" + DEFAULT_HIDE_IN_FORM + "," + UPDATED_HIDE_IN_FORM);

        // Get all the commonTableFieldList where hideInForm equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("hideInForm.in=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHideInFormIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where hideInForm is not null
        defaultCommonTableFieldShouldBeFound("hideInForm.specified=true");

        // Get all the commonTableFieldList where hideInForm is null
        defaultCommonTableFieldShouldNotBeFound("hideInForm.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEnableFilterIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where enableFilter equals to DEFAULT_ENABLE_FILTER
        defaultCommonTableFieldShouldBeFound("enableFilter.equals=" + DEFAULT_ENABLE_FILTER);

        // Get all the commonTableFieldList where enableFilter equals to UPDATED_ENABLE_FILTER
        defaultCommonTableFieldShouldNotBeFound("enableFilter.equals=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEnableFilterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where enableFilter not equals to DEFAULT_ENABLE_FILTER
        defaultCommonTableFieldShouldNotBeFound("enableFilter.notEquals=" + DEFAULT_ENABLE_FILTER);

        // Get all the commonTableFieldList where enableFilter not equals to UPDATED_ENABLE_FILTER
        defaultCommonTableFieldShouldBeFound("enableFilter.notEquals=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEnableFilterIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where enableFilter in DEFAULT_ENABLE_FILTER or UPDATED_ENABLE_FILTER
        defaultCommonTableFieldShouldBeFound("enableFilter.in=" + DEFAULT_ENABLE_FILTER + "," + UPDATED_ENABLE_FILTER);

        // Get all the commonTableFieldList where enableFilter equals to UPDATED_ENABLE_FILTER
        defaultCommonTableFieldShouldNotBeFound("enableFilter.in=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEnableFilterIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where enableFilter is not null
        defaultCommonTableFieldShouldBeFound("enableFilter.specified=true");

        // Get all the commonTableFieldList where enableFilter is null
        defaultCommonTableFieldShouldNotBeFound("enableFilter.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules equals to DEFAULT_VALIDATE_RULES
        defaultCommonTableFieldShouldBeFound("validateRules.equals=" + DEFAULT_VALIDATE_RULES);

        // Get all the commonTableFieldList where validateRules equals to UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldNotBeFound("validateRules.equals=" + UPDATED_VALIDATE_RULES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules not equals to DEFAULT_VALIDATE_RULES
        defaultCommonTableFieldShouldNotBeFound("validateRules.notEquals=" + DEFAULT_VALIDATE_RULES);

        // Get all the commonTableFieldList where validateRules not equals to UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldBeFound("validateRules.notEquals=" + UPDATED_VALIDATE_RULES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules in DEFAULT_VALIDATE_RULES or UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldBeFound("validateRules.in=" + DEFAULT_VALIDATE_RULES + "," + UPDATED_VALIDATE_RULES);

        // Get all the commonTableFieldList where validateRules equals to UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldNotBeFound("validateRules.in=" + UPDATED_VALIDATE_RULES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules is not null
        defaultCommonTableFieldShouldBeFound("validateRules.specified=true");

        // Get all the commonTableFieldList where validateRules is null
        defaultCommonTableFieldShouldNotBeFound("validateRules.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules contains DEFAULT_VALIDATE_RULES
        defaultCommonTableFieldShouldBeFound("validateRules.contains=" + DEFAULT_VALIDATE_RULES);

        // Get all the commonTableFieldList where validateRules contains UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldNotBeFound("validateRules.contains=" + UPDATED_VALIDATE_RULES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByValidateRulesNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where validateRules does not contain DEFAULT_VALIDATE_RULES
        defaultCommonTableFieldShouldNotBeFound("validateRules.doesNotContain=" + DEFAULT_VALIDATE_RULES);

        // Get all the commonTableFieldList where validateRules does not contain UPDATED_VALIDATE_RULES
        defaultCommonTableFieldShouldBeFound("validateRules.doesNotContain=" + UPDATED_VALIDATE_RULES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByShowInFilterTreeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where showInFilterTree equals to DEFAULT_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldBeFound("showInFilterTree.equals=" + DEFAULT_SHOW_IN_FILTER_TREE);

        // Get all the commonTableFieldList where showInFilterTree equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldNotBeFound("showInFilterTree.equals=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByShowInFilterTreeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where showInFilterTree not equals to DEFAULT_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldNotBeFound("showInFilterTree.notEquals=" + DEFAULT_SHOW_IN_FILTER_TREE);

        // Get all the commonTableFieldList where showInFilterTree not equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldBeFound("showInFilterTree.notEquals=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByShowInFilterTreeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where showInFilterTree in DEFAULT_SHOW_IN_FILTER_TREE or UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldBeFound("showInFilterTree.in=" + DEFAULT_SHOW_IN_FILTER_TREE + "," + UPDATED_SHOW_IN_FILTER_TREE);

        // Get all the commonTableFieldList where showInFilterTree equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableFieldShouldNotBeFound("showInFilterTree.in=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByShowInFilterTreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where showInFilterTree is not null
        defaultCommonTableFieldShouldBeFound("showInFilterTree.specified=true");

        // Get all the commonTableFieldList where showInFilterTree is null
        defaultCommonTableFieldShouldNotBeFound("showInFilterTree.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFixedIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fixed equals to DEFAULT_FIXED
        defaultCommonTableFieldShouldBeFound("fixed.equals=" + DEFAULT_FIXED);

        // Get all the commonTableFieldList where fixed equals to UPDATED_FIXED
        defaultCommonTableFieldShouldNotBeFound("fixed.equals=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFixedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fixed not equals to DEFAULT_FIXED
        defaultCommonTableFieldShouldNotBeFound("fixed.notEquals=" + DEFAULT_FIXED);

        // Get all the commonTableFieldList where fixed not equals to UPDATED_FIXED
        defaultCommonTableFieldShouldBeFound("fixed.notEquals=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFixedIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fixed in DEFAULT_FIXED or UPDATED_FIXED
        defaultCommonTableFieldShouldBeFound("fixed.in=" + DEFAULT_FIXED + "," + UPDATED_FIXED);

        // Get all the commonTableFieldList where fixed equals to UPDATED_FIXED
        defaultCommonTableFieldShouldNotBeFound("fixed.in=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFixedIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fixed is not null
        defaultCommonTableFieldShouldBeFound("fixed.specified=true");

        // Get all the commonTableFieldList where fixed is null
        defaultCommonTableFieldShouldNotBeFound("fixed.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySortableIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where sortable equals to DEFAULT_SORTABLE
        defaultCommonTableFieldShouldBeFound("sortable.equals=" + DEFAULT_SORTABLE);

        // Get all the commonTableFieldList where sortable equals to UPDATED_SORTABLE
        defaultCommonTableFieldShouldNotBeFound("sortable.equals=" + UPDATED_SORTABLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySortableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where sortable not equals to DEFAULT_SORTABLE
        defaultCommonTableFieldShouldNotBeFound("sortable.notEquals=" + DEFAULT_SORTABLE);

        // Get all the commonTableFieldList where sortable not equals to UPDATED_SORTABLE
        defaultCommonTableFieldShouldBeFound("sortable.notEquals=" + UPDATED_SORTABLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySortableIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where sortable in DEFAULT_SORTABLE or UPDATED_SORTABLE
        defaultCommonTableFieldShouldBeFound("sortable.in=" + DEFAULT_SORTABLE + "," + UPDATED_SORTABLE);

        // Get all the commonTableFieldList where sortable equals to UPDATED_SORTABLE
        defaultCommonTableFieldShouldNotBeFound("sortable.in=" + UPDATED_SORTABLE);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySortableIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where sortable is not null
        defaultCommonTableFieldShouldBeFound("sortable.specified=true");

        // Get all the commonTableFieldList where sortable is null
        defaultCommonTableFieldShouldNotBeFound("sortable.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTreeIndicatorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where treeIndicator equals to DEFAULT_TREE_INDICATOR
        defaultCommonTableFieldShouldBeFound("treeIndicator.equals=" + DEFAULT_TREE_INDICATOR);

        // Get all the commonTableFieldList where treeIndicator equals to UPDATED_TREE_INDICATOR
        defaultCommonTableFieldShouldNotBeFound("treeIndicator.equals=" + UPDATED_TREE_INDICATOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTreeIndicatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where treeIndicator not equals to DEFAULT_TREE_INDICATOR
        defaultCommonTableFieldShouldNotBeFound("treeIndicator.notEquals=" + DEFAULT_TREE_INDICATOR);

        // Get all the commonTableFieldList where treeIndicator not equals to UPDATED_TREE_INDICATOR
        defaultCommonTableFieldShouldBeFound("treeIndicator.notEquals=" + UPDATED_TREE_INDICATOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTreeIndicatorIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where treeIndicator in DEFAULT_TREE_INDICATOR or UPDATED_TREE_INDICATOR
        defaultCommonTableFieldShouldBeFound("treeIndicator.in=" + DEFAULT_TREE_INDICATOR + "," + UPDATED_TREE_INDICATOR);

        // Get all the commonTableFieldList where treeIndicator equals to UPDATED_TREE_INDICATOR
        defaultCommonTableFieldShouldNotBeFound("treeIndicator.in=" + UPDATED_TREE_INDICATOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByTreeIndicatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where treeIndicator is not null
        defaultCommonTableFieldShouldBeFound("treeIndicator.specified=true");

        // Get all the commonTableFieldList where treeIndicator is null
        defaultCommonTableFieldShouldNotBeFound("treeIndicator.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByClientReadOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where clientReadOnly equals to DEFAULT_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldBeFound("clientReadOnly.equals=" + DEFAULT_CLIENT_READ_ONLY);

        // Get all the commonTableFieldList where clientReadOnly equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldNotBeFound("clientReadOnly.equals=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByClientReadOnlyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where clientReadOnly not equals to DEFAULT_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldNotBeFound("clientReadOnly.notEquals=" + DEFAULT_CLIENT_READ_ONLY);

        // Get all the commonTableFieldList where clientReadOnly not equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldBeFound("clientReadOnly.notEquals=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByClientReadOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where clientReadOnly in DEFAULT_CLIENT_READ_ONLY or UPDATED_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldBeFound("clientReadOnly.in=" + DEFAULT_CLIENT_READ_ONLY + "," + UPDATED_CLIENT_READ_ONLY);

        // Get all the commonTableFieldList where clientReadOnly equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableFieldShouldNotBeFound("clientReadOnly.in=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByClientReadOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where clientReadOnly is not null
        defaultCommonTableFieldShouldBeFound("clientReadOnly.specified=true");

        // Get all the commonTableFieldList where clientReadOnly is null
        defaultCommonTableFieldShouldNotBeFound("clientReadOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues equals to DEFAULT_FIELD_VALUES
        defaultCommonTableFieldShouldBeFound("fieldValues.equals=" + DEFAULT_FIELD_VALUES);

        // Get all the commonTableFieldList where fieldValues equals to UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldNotBeFound("fieldValues.equals=" + UPDATED_FIELD_VALUES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues not equals to DEFAULT_FIELD_VALUES
        defaultCommonTableFieldShouldNotBeFound("fieldValues.notEquals=" + DEFAULT_FIELD_VALUES);

        // Get all the commonTableFieldList where fieldValues not equals to UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldBeFound("fieldValues.notEquals=" + UPDATED_FIELD_VALUES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues in DEFAULT_FIELD_VALUES or UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldBeFound("fieldValues.in=" + DEFAULT_FIELD_VALUES + "," + UPDATED_FIELD_VALUES);

        // Get all the commonTableFieldList where fieldValues equals to UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldNotBeFound("fieldValues.in=" + UPDATED_FIELD_VALUES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues is not null
        defaultCommonTableFieldShouldBeFound("fieldValues.specified=true");

        // Get all the commonTableFieldList where fieldValues is null
        defaultCommonTableFieldShouldNotBeFound("fieldValues.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues contains DEFAULT_FIELD_VALUES
        defaultCommonTableFieldShouldBeFound("fieldValues.contains=" + DEFAULT_FIELD_VALUES);

        // Get all the commonTableFieldList where fieldValues contains UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldNotBeFound("fieldValues.contains=" + UPDATED_FIELD_VALUES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFieldValuesNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fieldValues does not contain DEFAULT_FIELD_VALUES
        defaultCommonTableFieldShouldNotBeFound("fieldValues.doesNotContain=" + DEFAULT_FIELD_VALUES);

        // Get all the commonTableFieldList where fieldValues does not contain UPDATED_FIELD_VALUES
        defaultCommonTableFieldShouldBeFound("fieldValues.doesNotContain=" + UPDATED_FIELD_VALUES);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNotNullIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where notNull equals to DEFAULT_NOT_NULL
        defaultCommonTableFieldShouldBeFound("notNull.equals=" + DEFAULT_NOT_NULL);

        // Get all the commonTableFieldList where notNull equals to UPDATED_NOT_NULL
        defaultCommonTableFieldShouldNotBeFound("notNull.equals=" + UPDATED_NOT_NULL);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNotNullIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where notNull not equals to DEFAULT_NOT_NULL
        defaultCommonTableFieldShouldNotBeFound("notNull.notEquals=" + DEFAULT_NOT_NULL);

        // Get all the commonTableFieldList where notNull not equals to UPDATED_NOT_NULL
        defaultCommonTableFieldShouldBeFound("notNull.notEquals=" + UPDATED_NOT_NULL);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNotNullIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where notNull in DEFAULT_NOT_NULL or UPDATED_NOT_NULL
        defaultCommonTableFieldShouldBeFound("notNull.in=" + DEFAULT_NOT_NULL + "," + UPDATED_NOT_NULL);

        // Get all the commonTableFieldList where notNull equals to UPDATED_NOT_NULL
        defaultCommonTableFieldShouldNotBeFound("notNull.in=" + UPDATED_NOT_NULL);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNotNullIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where notNull is not null
        defaultCommonTableFieldShouldBeFound("notNull.specified=true");

        // Get all the commonTableFieldList where notNull is null
        defaultCommonTableFieldShouldNotBeFound("notNull.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySystemIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where system equals to DEFAULT_SYSTEM
        defaultCommonTableFieldShouldBeFound("system.equals=" + DEFAULT_SYSTEM);

        // Get all the commonTableFieldList where system equals to UPDATED_SYSTEM
        defaultCommonTableFieldShouldNotBeFound("system.equals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySystemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where system not equals to DEFAULT_SYSTEM
        defaultCommonTableFieldShouldNotBeFound("system.notEquals=" + DEFAULT_SYSTEM);

        // Get all the commonTableFieldList where system not equals to UPDATED_SYSTEM
        defaultCommonTableFieldShouldBeFound("system.notEquals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySystemIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where system in DEFAULT_SYSTEM or UPDATED_SYSTEM
        defaultCommonTableFieldShouldBeFound("system.in=" + DEFAULT_SYSTEM + "," + UPDATED_SYSTEM);

        // Get all the commonTableFieldList where system equals to UPDATED_SYSTEM
        defaultCommonTableFieldShouldNotBeFound("system.in=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsBySystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where system is not null
        defaultCommonTableFieldShouldBeFound("system.specified=true");

        // Get all the commonTableFieldList where system is null
        defaultCommonTableFieldShouldNotBeFound("system.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help equals to DEFAULT_HELP
        defaultCommonTableFieldShouldBeFound("help.equals=" + DEFAULT_HELP);

        // Get all the commonTableFieldList where help equals to UPDATED_HELP
        defaultCommonTableFieldShouldNotBeFound("help.equals=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help not equals to DEFAULT_HELP
        defaultCommonTableFieldShouldNotBeFound("help.notEquals=" + DEFAULT_HELP);

        // Get all the commonTableFieldList where help not equals to UPDATED_HELP
        defaultCommonTableFieldShouldBeFound("help.notEquals=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help in DEFAULT_HELP or UPDATED_HELP
        defaultCommonTableFieldShouldBeFound("help.in=" + DEFAULT_HELP + "," + UPDATED_HELP);

        // Get all the commonTableFieldList where help equals to UPDATED_HELP
        defaultCommonTableFieldShouldNotBeFound("help.in=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help is not null
        defaultCommonTableFieldShouldBeFound("help.specified=true");

        // Get all the commonTableFieldList where help is null
        defaultCommonTableFieldShouldNotBeFound("help.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help contains DEFAULT_HELP
        defaultCommonTableFieldShouldBeFound("help.contains=" + DEFAULT_HELP);

        // Get all the commonTableFieldList where help contains UPDATED_HELP
        defaultCommonTableFieldShouldNotBeFound("help.contains=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByHelpNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where help does not contain DEFAULT_HELP
        defaultCommonTableFieldShouldNotBeFound("help.doesNotContain=" + DEFAULT_HELP);

        // Get all the commonTableFieldList where help does not contain UPDATED_HELP
        defaultCommonTableFieldShouldBeFound("help.doesNotContain=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor equals to DEFAULT_FONT_COLOR
        defaultCommonTableFieldShouldBeFound("fontColor.equals=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableFieldList where fontColor equals to UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldNotBeFound("fontColor.equals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor not equals to DEFAULT_FONT_COLOR
        defaultCommonTableFieldShouldNotBeFound("fontColor.notEquals=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableFieldList where fontColor not equals to UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldBeFound("fontColor.notEquals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor in DEFAULT_FONT_COLOR or UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldBeFound("fontColor.in=" + DEFAULT_FONT_COLOR + "," + UPDATED_FONT_COLOR);

        // Get all the commonTableFieldList where fontColor equals to UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldNotBeFound("fontColor.in=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor is not null
        defaultCommonTableFieldShouldBeFound("fontColor.specified=true");

        // Get all the commonTableFieldList where fontColor is null
        defaultCommonTableFieldShouldNotBeFound("fontColor.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor contains DEFAULT_FONT_COLOR
        defaultCommonTableFieldShouldBeFound("fontColor.contains=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableFieldList where fontColor contains UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldNotBeFound("fontColor.contains=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByFontColorNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where fontColor does not contain DEFAULT_FONT_COLOR
        defaultCommonTableFieldShouldNotBeFound("fontColor.doesNotContain=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableFieldList where fontColor does not contain UPDATED_FONT_COLOR
        defaultCommonTableFieldShouldBeFound("fontColor.doesNotContain=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor equals to DEFAULT_BACKGROUND_COLOR
        defaultCommonTableFieldShouldBeFound("backgroundColor.equals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableFieldList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.equals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor not equals to DEFAULT_BACKGROUND_COLOR
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.notEquals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableFieldList where backgroundColor not equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldBeFound("backgroundColor.notEquals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor in DEFAULT_BACKGROUND_COLOR or UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldBeFound("backgroundColor.in=" + DEFAULT_BACKGROUND_COLOR + "," + UPDATED_BACKGROUND_COLOR);

        // Get all the commonTableFieldList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.in=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor is not null
        defaultCommonTableFieldShouldBeFound("backgroundColor.specified=true");

        // Get all the commonTableFieldList where backgroundColor is null
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor contains DEFAULT_BACKGROUND_COLOR
        defaultCommonTableFieldShouldBeFound("backgroundColor.contains=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableFieldList where backgroundColor contains UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.contains=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByBackgroundColorNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where backgroundColor does not contain DEFAULT_BACKGROUND_COLOR
        defaultCommonTableFieldShouldNotBeFound("backgroundColor.doesNotContain=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableFieldList where backgroundColor does not contain UPDATED_BACKGROUND_COLOR
        defaultCommonTableFieldShouldBeFound("backgroundColor.doesNotContain=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNullHideInFormIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where nullHideInForm equals to DEFAULT_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("nullHideInForm.equals=" + DEFAULT_NULL_HIDE_IN_FORM);

        // Get all the commonTableFieldList where nullHideInForm equals to UPDATED_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("nullHideInForm.equals=" + UPDATED_NULL_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNullHideInFormIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where nullHideInForm not equals to DEFAULT_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("nullHideInForm.notEquals=" + DEFAULT_NULL_HIDE_IN_FORM);

        // Get all the commonTableFieldList where nullHideInForm not equals to UPDATED_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("nullHideInForm.notEquals=" + UPDATED_NULL_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNullHideInFormIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where nullHideInForm in DEFAULT_NULL_HIDE_IN_FORM or UPDATED_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldBeFound("nullHideInForm.in=" + DEFAULT_NULL_HIDE_IN_FORM + "," + UPDATED_NULL_HIDE_IN_FORM);

        // Get all the commonTableFieldList where nullHideInForm equals to UPDATED_NULL_HIDE_IN_FORM
        defaultCommonTableFieldShouldNotBeFound("nullHideInForm.in=" + UPDATED_NULL_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByNullHideInFormIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where nullHideInForm is not null
        defaultCommonTableFieldShouldBeFound("nullHideInForm.specified=true");

        // Get all the commonTableFieldList where nullHideInForm is null
        defaultCommonTableFieldShouldNotBeFound("nullHideInForm.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEndUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where endUsed equals to DEFAULT_END_USED
        defaultCommonTableFieldShouldBeFound("endUsed.equals=" + DEFAULT_END_USED);

        // Get all the commonTableFieldList where endUsed equals to UPDATED_END_USED
        defaultCommonTableFieldShouldNotBeFound("endUsed.equals=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEndUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where endUsed not equals to DEFAULT_END_USED
        defaultCommonTableFieldShouldNotBeFound("endUsed.notEquals=" + DEFAULT_END_USED);

        // Get all the commonTableFieldList where endUsed not equals to UPDATED_END_USED
        defaultCommonTableFieldShouldBeFound("endUsed.notEquals=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEndUsedIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where endUsed in DEFAULT_END_USED or UPDATED_END_USED
        defaultCommonTableFieldShouldBeFound("endUsed.in=" + DEFAULT_END_USED + "," + UPDATED_END_USED);

        // Get all the commonTableFieldList where endUsed equals to UPDATED_END_USED
        defaultCommonTableFieldShouldNotBeFound("endUsed.in=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByEndUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where endUsed is not null
        defaultCommonTableFieldShouldBeFound("endUsed.specified=true");

        // Get all the commonTableFieldList where endUsed is null
        defaultCommonTableFieldShouldNotBeFound("endUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options equals to DEFAULT_OPTIONS
        defaultCommonTableFieldShouldBeFound("options.equals=" + DEFAULT_OPTIONS);

        // Get all the commonTableFieldList where options equals to UPDATED_OPTIONS
        defaultCommonTableFieldShouldNotBeFound("options.equals=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options not equals to DEFAULT_OPTIONS
        defaultCommonTableFieldShouldNotBeFound("options.notEquals=" + DEFAULT_OPTIONS);

        // Get all the commonTableFieldList where options not equals to UPDATED_OPTIONS
        defaultCommonTableFieldShouldBeFound("options.notEquals=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options in DEFAULT_OPTIONS or UPDATED_OPTIONS
        defaultCommonTableFieldShouldBeFound("options.in=" + DEFAULT_OPTIONS + "," + UPDATED_OPTIONS);

        // Get all the commonTableFieldList where options equals to UPDATED_OPTIONS
        defaultCommonTableFieldShouldNotBeFound("options.in=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options is not null
        defaultCommonTableFieldShouldBeFound("options.specified=true");

        // Get all the commonTableFieldList where options is null
        defaultCommonTableFieldShouldNotBeFound("options.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options contains DEFAULT_OPTIONS
        defaultCommonTableFieldShouldBeFound("options.contains=" + DEFAULT_OPTIONS);

        // Get all the commonTableFieldList where options contains UPDATED_OPTIONS
        defaultCommonTableFieldShouldNotBeFound("options.contains=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableFieldsByOptionsNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        // Get all the commonTableFieldList where options does not contain DEFAULT_OPTIONS
        defaultCommonTableFieldShouldNotBeFound("options.doesNotContain=" + DEFAULT_OPTIONS);

        // Get all the commonTableFieldList where options does not contain UPDATED_OPTIONS
        defaultCommonTableFieldShouldBeFound("options.doesNotContain=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    public void getAllCommonTableFieldsByMetaModelIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);
        CommonTable metaModel = CommonTableResourceIT.createEntity();
        commonTableField.setMetaModel(metaModel);
        commonTableFieldRepository.insert(commonTableField);
        Long metaModelId = metaModel.getId();

        // Get all the commonTableFieldList where metaModel equals to metaModelId
        defaultCommonTableFieldShouldBeFound("metaModelId.equals=" + metaModelId);

        // Get all the commonTableFieldList where metaModel equals to (metaModelId + 1)
        defaultCommonTableFieldShouldNotBeFound("metaModelId.equals=" + (metaModelId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTableFieldsByCommonTableIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);
        CommonTable commonTable = CommonTableResourceIT.createEntity();
        commonTableField.setCommonTable(commonTable);
        commonTableFieldRepository.insert(commonTableField);
        Long commonTableId = commonTable.getId();

        // Get all the commonTableFieldList where commonTable equals to commonTableId
        defaultCommonTableFieldShouldBeFound("commonTableId.equals=" + commonTableId);

        // Get all the commonTableFieldList where commonTable equals to (commonTableId + 1)
        defaultCommonTableFieldShouldNotBeFound("commonTableId.equals=" + (commonTableId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonTableFieldShouldBeFound(String filter) throws Exception {
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTableField.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].entityFieldName").value(hasItem(DEFAULT_ENTITY_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tableColumnName").value(hasItem(DEFAULT_TABLE_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].columnWidth").value(hasItem(DEFAULT_COLUMN_WIDTH)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].editInList").value(hasItem(DEFAULT_EDIT_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInList").value(hasItem(DEFAULT_HIDE_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInForm").value(hasItem(DEFAULT_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].enableFilter").value(hasItem(DEFAULT_ENABLE_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].validateRules").value(hasItem(DEFAULT_VALIDATE_RULES)))
            .andExpect(jsonPath("$.[*].showInFilterTree").value(hasItem(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].fixed").value(hasItem(DEFAULT_FIXED.toString())))
            .andExpect(jsonPath("$.[*].sortable").value(hasItem(DEFAULT_SORTABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].treeIndicator").value(hasItem(DEFAULT_TREE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].clientReadOnly").value(hasItem(DEFAULT_CLIENT_READ_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].fieldValues").value(hasItem(DEFAULT_FIELD_VALUES)))
            .andExpect(jsonPath("$.[*].notNull").value(hasItem(DEFAULT_NOT_NULL.booleanValue())))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].help").value(hasItem(DEFAULT_HELP)))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].nullHideInForm").value(hasItem(DEFAULT_NULL_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].endUsed").value(hasItem(DEFAULT_END_USED.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));

        // Check, that the count call also returns 1
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonTableFieldShouldNotBeFound(String filter) throws Exception {
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonTableFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonTableField() throws Exception {
        // Get the commonTableField
        restCommonTableFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonTableField() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();

        // Update the commonTableField
        CommonTableField updatedCommonTableField = commonTableFieldRepository.findById(commonTableField.getId()).get();

        // Disconnect from session so that the updates on updatedCommonTableField are not directly saved in db

        updatedCommonTableField
            .title(UPDATED_TITLE)
            .entityFieldName(UPDATED_ENTITY_FIELD_NAME)
            .type(UPDATED_TYPE)
            .tableColumnName(UPDATED_TABLE_COLUMN_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .editInList(UPDATED_EDIT_IN_LIST)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .validateRules(UPDATED_VALIDATE_RULES)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .fixed(UPDATED_FIXED)
            .sortable(UPDATED_SORTABLE)
            .treeIndicator(UPDATED_TREE_INDICATOR)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .fieldValues(UPDATED_FIELD_VALUES)
            .notNull(UPDATED_NOT_NULL)
            .system(UPDATED_SYSTEM)
            .help(UPDATED_HELP)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .nullHideInForm(UPDATED_NULL_HIDE_IN_FORM)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(updatedCommonTableField);

        restCommonTableFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
        CommonTableField testCommonTableField = commonTableFieldList.get(commonTableFieldList.size() - 1);
        assertThat(testCommonTableField.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommonTableField.getEntityFieldName()).isEqualTo(UPDATED_ENTITY_FIELD_NAME);
        assertThat(testCommonTableField.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCommonTableField.getTableColumnName()).isEqualTo(UPDATED_TABLE_COLUMN_NAME);
        assertThat(testCommonTableField.getColumnWidth()).isEqualTo(UPDATED_COLUMN_WIDTH);
        assertThat(testCommonTableField.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCommonTableField.getEditInList()).isEqualTo(UPDATED_EDIT_IN_LIST);
        assertThat(testCommonTableField.getHideInList()).isEqualTo(UPDATED_HIDE_IN_LIST);
        assertThat(testCommonTableField.getHideInForm()).isEqualTo(UPDATED_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableField.getValidateRules()).isEqualTo(UPDATED_VALIDATE_RULES);
        assertThat(testCommonTableField.getShowInFilterTree()).isEqualTo(UPDATED_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableField.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testCommonTableField.getSortable()).isEqualTo(UPDATED_SORTABLE);
        assertThat(testCommonTableField.getTreeIndicator()).isEqualTo(UPDATED_TREE_INDICATOR);
        assertThat(testCommonTableField.getClientReadOnly()).isEqualTo(UPDATED_CLIENT_READ_ONLY);
        assertThat(testCommonTableField.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testCommonTableField.getNotNull()).isEqualTo(UPDATED_NOT_NULL);
        assertThat(testCommonTableField.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTableField.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableField.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testCommonTableField.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testCommonTableField.getNullHideInForm()).isEqualTo(UPDATED_NULL_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEndUsed()).isEqualTo(UPDATED_END_USED);
        assertThat(testCommonTableField.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void putNonExistingngCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        restCommonTableFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableFieldMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonTableFieldWithPatch() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();

        // Update the commonTableField using partial update
        CommonTableField partialUpdatedCommonTableField = new CommonTableField();
        partialUpdatedCommonTableField.setId(commonTableField.getId());

        partialUpdatedCommonTableField
            .title(UPDATED_TITLE)
            .entityFieldName(UPDATED_ENTITY_FIELD_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .fixed(UPDATED_FIXED)
            .sortable(UPDATED_SORTABLE)
            .fieldValues(UPDATED_FIELD_VALUES)
            .system(UPDATED_SYSTEM)
            .help(UPDATED_HELP)
            .fontColor(UPDATED_FONT_COLOR);

        restCommonTableFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTableField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTableField))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
        CommonTableField testCommonTableField = commonTableFieldList.get(commonTableFieldList.size() - 1);
        assertThat(testCommonTableField.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommonTableField.getEntityFieldName()).isEqualTo(UPDATED_ENTITY_FIELD_NAME);
        assertThat(testCommonTableField.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCommonTableField.getTableColumnName()).isEqualTo(DEFAULT_TABLE_COLUMN_NAME);
        assertThat(testCommonTableField.getColumnWidth()).isEqualTo(UPDATED_COLUMN_WIDTH);
        assertThat(testCommonTableField.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCommonTableField.getEditInList()).isEqualTo(DEFAULT_EDIT_IN_LIST);
        assertThat(testCommonTableField.getHideInList()).isEqualTo(UPDATED_HIDE_IN_LIST);
        assertThat(testCommonTableField.getHideInForm()).isEqualTo(UPDATED_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableField.getValidateRules()).isEqualTo(DEFAULT_VALIDATE_RULES);
        assertThat(testCommonTableField.getShowInFilterTree()).isEqualTo(UPDATED_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableField.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testCommonTableField.getSortable()).isEqualTo(UPDATED_SORTABLE);
        assertThat(testCommonTableField.getTreeIndicator()).isEqualTo(DEFAULT_TREE_INDICATOR);
        assertThat(testCommonTableField.getClientReadOnly()).isEqualTo(DEFAULT_CLIENT_READ_ONLY);
        assertThat(testCommonTableField.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testCommonTableField.getNotNull()).isEqualTo(DEFAULT_NOT_NULL);
        assertThat(testCommonTableField.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTableField.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableField.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testCommonTableField.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testCommonTableField.getNullHideInForm()).isEqualTo(DEFAULT_NULL_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEndUsed()).isEqualTo(DEFAULT_END_USED);
        assertThat(testCommonTableField.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    @Transactional
    void fullUpdateCommonTableFieldWithPatch() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();

        // Update the commonTableField using partial update
        CommonTableField partialUpdatedCommonTableField = new CommonTableField();
        partialUpdatedCommonTableField.setId(commonTableField.getId());

        partialUpdatedCommonTableField
            .title(UPDATED_TITLE)
            .entityFieldName(UPDATED_ENTITY_FIELD_NAME)
            .type(UPDATED_TYPE)
            .tableColumnName(UPDATED_TABLE_COLUMN_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .editInList(UPDATED_EDIT_IN_LIST)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .validateRules(UPDATED_VALIDATE_RULES)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .fixed(UPDATED_FIXED)
            .sortable(UPDATED_SORTABLE)
            .treeIndicator(UPDATED_TREE_INDICATOR)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .fieldValues(UPDATED_FIELD_VALUES)
            .notNull(UPDATED_NOT_NULL)
            .system(UPDATED_SYSTEM)
            .help(UPDATED_HELP)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .nullHideInForm(UPDATED_NULL_HIDE_IN_FORM)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);

        restCommonTableFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTableField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTableField))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
        CommonTableField testCommonTableField = commonTableFieldList.get(commonTableFieldList.size() - 1);
        assertThat(testCommonTableField.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommonTableField.getEntityFieldName()).isEqualTo(UPDATED_ENTITY_FIELD_NAME);
        assertThat(testCommonTableField.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCommonTableField.getTableColumnName()).isEqualTo(UPDATED_TABLE_COLUMN_NAME);
        assertThat(testCommonTableField.getColumnWidth()).isEqualTo(UPDATED_COLUMN_WIDTH);
        assertThat(testCommonTableField.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCommonTableField.getEditInList()).isEqualTo(UPDATED_EDIT_IN_LIST);
        assertThat(testCommonTableField.getHideInList()).isEqualTo(UPDATED_HIDE_IN_LIST);
        assertThat(testCommonTableField.getHideInForm()).isEqualTo(UPDATED_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableField.getValidateRules()).isEqualTo(UPDATED_VALIDATE_RULES);
        assertThat(testCommonTableField.getShowInFilterTree()).isEqualTo(UPDATED_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableField.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testCommonTableField.getSortable()).isEqualTo(UPDATED_SORTABLE);
        assertThat(testCommonTableField.getTreeIndicator()).isEqualTo(UPDATED_TREE_INDICATOR);
        assertThat(testCommonTableField.getClientReadOnly()).isEqualTo(UPDATED_CLIENT_READ_ONLY);
        assertThat(testCommonTableField.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testCommonTableField.getNotNull()).isEqualTo(UPDATED_NOT_NULL);
        assertThat(testCommonTableField.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTableField.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableField.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testCommonTableField.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testCommonTableField.getNullHideInForm()).isEqualTo(UPDATED_NULL_HIDE_IN_FORM);
        assertThat(testCommonTableField.getEndUsed()).isEqualTo(UPDATED_END_USED);
        assertThat(testCommonTableField.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void patchNonExistingCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonTableFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonTableFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonTableField() throws Exception {
        int databaseSizeBeforeUpdate = commonTableFieldRepository.findAll().size();
        commonTableField.setId(count.incrementAndGet());

        // Create the CommonTableField
        CommonTableFieldDTO commonTableFieldDTO = commonTableFieldMapper.toDto(commonTableField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableFieldMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTableField in the database
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonTableField() throws Exception {
        // Initialize the database
        commonTableFieldRepository.insert(commonTableField);

        int databaseSizeBeforeDelete = commonTableFieldRepository.findAll().size();

        // Delete the commonTableField
        restCommonTableFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonTableField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonTableField> commonTableFieldList = commonTableFieldRepository.findAll();
        assertThat(commonTableFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
