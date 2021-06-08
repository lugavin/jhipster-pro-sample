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
import com.gavin.myapp.domain.CommonTableRelationship;
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.domain.enumeration.EndUsedType;
import com.gavin.myapp.domain.enumeration.FixedType;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.domain.enumeration.SourceType;
import com.gavin.myapp.repository.CommonTableRelationshipRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.CommonTableRelationshipQueryService;
import com.gavin.myapp.service.criteria.CommonTableRelationshipCriteria;
import com.gavin.myapp.service.dto.CommonTableRelationshipDTO;
import com.gavin.myapp.service.mapper.CommonTableRelationshipMapper;
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
 * Integration tests for the {@link CommonTableRelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonTableRelationshipResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final RelationshipType DEFAULT_RELATIONSHIP_TYPE = RelationshipType.ONE_TO_MANY;
    private static final RelationshipType UPDATED_RELATIONSHIP_TYPE = RelationshipType.MANY_TO_ONE;

    private static final SourceType DEFAULT_SOURCE_TYPE = SourceType.ENTITY;
    private static final SourceType UPDATED_SOURCE_TYPE = SourceType.DATA_DICTIONARY;

    private static final String DEFAULT_OTHER_ENTITY_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_ENTITY_FIELD = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COLUMN_WIDTH = 1;
    private static final Integer UPDATED_COLUMN_WIDTH = 2;
    private static final Integer SMALLER_COLUMN_WIDTH = 1 - 1;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final FixedType DEFAULT_FIXED = FixedType.LEFT;
    private static final FixedType UPDATED_FIXED = FixedType.RIGHT;

    private static final Boolean DEFAULT_EDIT_IN_LIST = false;
    private static final Boolean UPDATED_EDIT_IN_LIST = true;

    private static final Boolean DEFAULT_ENABLE_FILTER = false;
    private static final Boolean UPDATED_ENABLE_FILTER = true;

    private static final Boolean DEFAULT_HIDE_IN_LIST = false;
    private static final Boolean UPDATED_HIDE_IN_LIST = true;

    private static final Boolean DEFAULT_HIDE_IN_FORM = false;
    private static final Boolean UPDATED_HIDE_IN_FORM = true;

    private static final Boolean DEFAULT_SYSTEM = false;
    private static final Boolean UPDATED_SYSTEM = true;

    private static final String DEFAULT_FONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_HELP = "AAAAAAAAAA";
    private static final String UPDATED_HELP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OWNER_SIDE = false;
    private static final Boolean UPDATED_OWNER_SIDE = true;

    private static final String DEFAULT_DATA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_COMPONENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_WEB_COMPONENT_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OTHER_ENTITY_IS_TREE = false;
    private static final Boolean UPDATED_OTHER_ENTITY_IS_TREE = true;

    private static final Boolean DEFAULT_SHOW_IN_FILTER_TREE = false;
    private static final Boolean UPDATED_SHOW_IN_FILTER_TREE = true;

    private static final String DEFAULT_DATA_DICTIONARY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_DICTIONARY_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CLIENT_READ_ONLY = false;
    private static final Boolean UPDATED_CLIENT_READ_ONLY = true;

    private static final EndUsedType DEFAULT_END_USED = EndUsedType.FILE_URL;
    private static final EndUsedType UPDATED_END_USED = EndUsedType.IMAGE_URL;

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/common-table-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonTableRelationshipRepository commonTableRelationshipRepository;

    @Autowired
    private CommonTableRelationshipMapper commonTableRelationshipMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restCommonTableRelationshipMockMvc;

    private CommonTableRelationship commonTableRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTableRelationship createEntity() {
        CommonTableRelationship commonTableRelationship = new CommonTableRelationship()
            .name(DEFAULT_NAME)
            .relationshipType(DEFAULT_RELATIONSHIP_TYPE)
            .sourceType(DEFAULT_SOURCE_TYPE)
            .otherEntityField(DEFAULT_OTHER_ENTITY_FIELD)
            .otherEntityName(DEFAULT_OTHER_ENTITY_NAME)
            .relationshipName(DEFAULT_RELATIONSHIP_NAME)
            .otherEntityRelationshipName(DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME)
            .columnWidth(DEFAULT_COLUMN_WIDTH)
            .order(DEFAULT_ORDER)
            .fixed(DEFAULT_FIXED)
            .editInList(DEFAULT_EDIT_IN_LIST)
            .enableFilter(DEFAULT_ENABLE_FILTER)
            .hideInList(DEFAULT_HIDE_IN_LIST)
            .hideInForm(DEFAULT_HIDE_IN_FORM)
            .system(DEFAULT_SYSTEM)
            .fontColor(DEFAULT_FONT_COLOR)
            .backgroundColor(DEFAULT_BACKGROUND_COLOR)
            .help(DEFAULT_HELP)
            .ownerSide(DEFAULT_OWNER_SIDE)
            .dataName(DEFAULT_DATA_NAME)
            .webComponentType(DEFAULT_WEB_COMPONENT_TYPE)
            .otherEntityIsTree(DEFAULT_OTHER_ENTITY_IS_TREE)
            .showInFilterTree(DEFAULT_SHOW_IN_FILTER_TREE)
            .dataDictionaryCode(DEFAULT_DATA_DICTIONARY_CODE)
            .clientReadOnly(DEFAULT_CLIENT_READ_ONLY)
            .endUsed(DEFAULT_END_USED)
            .options(DEFAULT_OPTIONS);
        return commonTableRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonTableRelationship createUpdatedEntity() {
        CommonTableRelationship commonTableRelationship = new CommonTableRelationship()
            .name(UPDATED_NAME)
            .relationshipType(UPDATED_RELATIONSHIP_TYPE)
            .sourceType(UPDATED_SOURCE_TYPE)
            .otherEntityField(UPDATED_OTHER_ENTITY_FIELD)
            .otherEntityName(UPDATED_OTHER_ENTITY_NAME)
            .relationshipName(UPDATED_RELATIONSHIP_NAME)
            .otherEntityRelationshipName(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .fixed(UPDATED_FIXED)
            .editInList(UPDATED_EDIT_IN_LIST)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .system(UPDATED_SYSTEM)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .help(UPDATED_HELP)
            .ownerSide(UPDATED_OWNER_SIDE)
            .dataName(UPDATED_DATA_NAME)
            .webComponentType(UPDATED_WEB_COMPONENT_TYPE)
            .otherEntityIsTree(UPDATED_OTHER_ENTITY_IS_TREE)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .dataDictionaryCode(UPDATED_DATA_DICTIONARY_CODE)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);
        return commonTableRelationship;
    }

    @BeforeEach
    public void initTest() {
        commonTableRelationship = createEntity();
    }

    @Test
    @Transactional
    void createCommonTableRelationship() throws Exception {
        int databaseSizeBeforeCreate = commonTableRelationshipRepository.findAll().size();
        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);
        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL_ID, commonTableRelationshipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        CommonTableRelationship testCommonTableRelationship = commonTableRelationshipList.get(commonTableRelationshipList.size() - 1);
        assertThat(testCommonTableRelationship.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonTableRelationship.getRelationshipType()).isEqualTo(DEFAULT_RELATIONSHIP_TYPE);
        assertThat(testCommonTableRelationship.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityField()).isEqualTo(DEFAULT_OTHER_ENTITY_FIELD);
        assertThat(testCommonTableRelationship.getOtherEntityName()).isEqualTo(DEFAULT_OTHER_ENTITY_NAME);
        assertThat(testCommonTableRelationship.getRelationshipName()).isEqualTo(DEFAULT_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getOtherEntityRelationshipName()).isEqualTo(DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getColumnWidth()).isEqualTo(DEFAULT_COLUMN_WIDTH);
        assertThat(testCommonTableRelationship.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCommonTableRelationship.getFixed()).isEqualTo(DEFAULT_FIXED);
        assertThat(testCommonTableRelationship.getEditInList()).isEqualTo(DEFAULT_EDIT_IN_LIST);
        assertThat(testCommonTableRelationship.getEnableFilter()).isEqualTo(DEFAULT_ENABLE_FILTER);
        assertThat(testCommonTableRelationship.getHideInList()).isEqualTo(DEFAULT_HIDE_IN_LIST);
        assertThat(testCommonTableRelationship.getHideInForm()).isEqualTo(DEFAULT_HIDE_IN_FORM);
        assertThat(testCommonTableRelationship.getSystem()).isEqualTo(DEFAULT_SYSTEM);
        assertThat(testCommonTableRelationship.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testCommonTableRelationship.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testCommonTableRelationship.getHelp()).isEqualTo(DEFAULT_HELP);
        assertThat(testCommonTableRelationship.getOwnerSide()).isEqualTo(DEFAULT_OWNER_SIDE);
        assertThat(testCommonTableRelationship.getDataName()).isEqualTo(DEFAULT_DATA_NAME);
        assertThat(testCommonTableRelationship.getWebComponentType()).isEqualTo(DEFAULT_WEB_COMPONENT_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityIsTree()).isEqualTo(DEFAULT_OTHER_ENTITY_IS_TREE);
        assertThat(testCommonTableRelationship.getShowInFilterTree()).isEqualTo(DEFAULT_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableRelationship.getDataDictionaryCode()).isEqualTo(DEFAULT_DATA_DICTIONARY_CODE);
        assertThat(testCommonTableRelationship.getClientReadOnly()).isEqualTo(DEFAULT_CLIENT_READ_ONLY);
        assertThat(testCommonTableRelationship.getEndUsed()).isEqualTo(DEFAULT_END_USED);
        assertThat(testCommonTableRelationship.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    @Transactional
    void createCommonTableRelationshipWithExistingId() throws Exception {
        // Create the CommonTableRelationship with an existing ID
        commonTableRelationship.setId(1L);
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        int databaseSizeBeforeCreate = commonTableRelationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setName(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setRelationshipType(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setSourceType(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOtherEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setOtherEntityName(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setRelationshipName(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonTableRelationshipRepository.findAll().size();
        // set the field null
        commonTableRelationship.setDataName(null);

        // Create the CommonTableRelationship, which fails.
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonTableRelationships() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTableRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].relationshipType").value(hasItem(DEFAULT_RELATIONSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].otherEntityField").value(hasItem(DEFAULT_OTHER_ENTITY_FIELD)))
            .andExpect(jsonPath("$.[*].otherEntityName").value(hasItem(DEFAULT_OTHER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].relationshipName").value(hasItem(DEFAULT_RELATIONSHIP_NAME)))
            .andExpect(jsonPath("$.[*].otherEntityRelationshipName").value(hasItem(DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME)))
            .andExpect(jsonPath("$.[*].columnWidth").value(hasItem(DEFAULT_COLUMN_WIDTH)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].fixed").value(hasItem(DEFAULT_FIXED.toString())))
            .andExpect(jsonPath("$.[*].editInList").value(hasItem(DEFAULT_EDIT_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].enableFilter").value(hasItem(DEFAULT_ENABLE_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInList").value(hasItem(DEFAULT_HIDE_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInForm").value(hasItem(DEFAULT_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].help").value(hasItem(DEFAULT_HELP)))
            .andExpect(jsonPath("$.[*].ownerSide").value(hasItem(DEFAULT_OWNER_SIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataName").value(hasItem(DEFAULT_DATA_NAME)))
            .andExpect(jsonPath("$.[*].webComponentType").value(hasItem(DEFAULT_WEB_COMPONENT_TYPE)))
            .andExpect(jsonPath("$.[*].otherEntityIsTree").value(hasItem(DEFAULT_OTHER_ENTITY_IS_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].showInFilterTree").value(hasItem(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataDictionaryCode").value(hasItem(DEFAULT_DATA_DICTIONARY_CODE)))
            .andExpect(jsonPath("$.[*].clientReadOnly").value(hasItem(DEFAULT_CLIENT_READ_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].endUsed").value(hasItem(DEFAULT_END_USED.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));
    }

    @Test
    @Transactional
    public void getCommonTableRelationship() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get the commonTableRelationship
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, commonTableRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonTableRelationship.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.relationshipType").value(DEFAULT_RELATIONSHIP_TYPE.toString()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.otherEntityField").value(DEFAULT_OTHER_ENTITY_FIELD))
            .andExpect(jsonPath("$.otherEntityName").value(DEFAULT_OTHER_ENTITY_NAME))
            .andExpect(jsonPath("$.relationshipName").value(DEFAULT_RELATIONSHIP_NAME))
            .andExpect(jsonPath("$.otherEntityRelationshipName").value(DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME))
            .andExpect(jsonPath("$.columnWidth").value(DEFAULT_COLUMN_WIDTH))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.fixed").value(DEFAULT_FIXED.toString()))
            .andExpect(jsonPath("$.editInList").value(DEFAULT_EDIT_IN_LIST.booleanValue()))
            .andExpect(jsonPath("$.enableFilter").value(DEFAULT_ENABLE_FILTER.booleanValue()))
            .andExpect(jsonPath("$.hideInList").value(DEFAULT_HIDE_IN_LIST.booleanValue()))
            .andExpect(jsonPath("$.hideInForm").value(DEFAULT_HIDE_IN_FORM.booleanValue()))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.fontColor").value(DEFAULT_FONT_COLOR))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.help").value(DEFAULT_HELP))
            .andExpect(jsonPath("$.ownerSide").value(DEFAULT_OWNER_SIDE.booleanValue()))
            .andExpect(jsonPath("$.dataName").value(DEFAULT_DATA_NAME))
            .andExpect(jsonPath("$.webComponentType").value(DEFAULT_WEB_COMPONENT_TYPE))
            .andExpect(jsonPath("$.otherEntityIsTree").value(DEFAULT_OTHER_ENTITY_IS_TREE.booleanValue()))
            .andExpect(jsonPath("$.showInFilterTree").value(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue()))
            .andExpect(jsonPath("$.dataDictionaryCode").value(DEFAULT_DATA_DICTIONARY_CODE))
            .andExpect(jsonPath("$.clientReadOnly").value(DEFAULT_CLIENT_READ_ONLY.booleanValue()))
            .andExpect(jsonPath("$.endUsed").value(DEFAULT_END_USED.toString()))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS));
    }

    @Test
    @Transactional
    public void getCommonTableRelationshipsByIdFiltering() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        Long id = commonTableRelationship.getId();

        defaultCommonTableRelationshipShouldBeFound("id.equals=" + id);
        defaultCommonTableRelationshipShouldNotBeFound("id.notEquals=" + id);

        defaultCommonTableRelationshipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonTableRelationshipShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonTableRelationshipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonTableRelationshipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name equals to DEFAULT_NAME
        defaultCommonTableRelationshipShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the commonTableRelationshipList where name equals to UPDATED_NAME
        defaultCommonTableRelationshipShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name not equals to DEFAULT_NAME
        defaultCommonTableRelationshipShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the commonTableRelationshipList where name not equals to UPDATED_NAME
        defaultCommonTableRelationshipShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCommonTableRelationshipShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the commonTableRelationshipList where name equals to UPDATED_NAME
        defaultCommonTableRelationshipShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name is not null
        defaultCommonTableRelationshipShouldBeFound("name.specified=true");

        // Get all the commonTableRelationshipList where name is null
        defaultCommonTableRelationshipShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name contains DEFAULT_NAME
        defaultCommonTableRelationshipShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the commonTableRelationshipList where name contains UPDATED_NAME
        defaultCommonTableRelationshipShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where name does not contain DEFAULT_NAME
        defaultCommonTableRelationshipShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the commonTableRelationshipList where name does not contain UPDATED_NAME
        defaultCommonTableRelationshipShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipType equals to DEFAULT_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldBeFound("relationshipType.equals=" + DEFAULT_RELATIONSHIP_TYPE);

        // Get all the commonTableRelationshipList where relationshipType equals to UPDATED_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("relationshipType.equals=" + UPDATED_RELATIONSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipType not equals to DEFAULT_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("relationshipType.notEquals=" + DEFAULT_RELATIONSHIP_TYPE);

        // Get all the commonTableRelationshipList where relationshipType not equals to UPDATED_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldBeFound("relationshipType.notEquals=" + UPDATED_RELATIONSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipType in DEFAULT_RELATIONSHIP_TYPE or UPDATED_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldBeFound("relationshipType.in=" + DEFAULT_RELATIONSHIP_TYPE + "," + UPDATED_RELATIONSHIP_TYPE);

        // Get all the commonTableRelationshipList where relationshipType equals to UPDATED_RELATIONSHIP_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("relationshipType.in=" + UPDATED_RELATIONSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipType is not null
        defaultCommonTableRelationshipShouldBeFound("relationshipType.specified=true");

        // Get all the commonTableRelationshipList where relationshipType is null
        defaultCommonTableRelationshipShouldNotBeFound("relationshipType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySourceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where sourceType equals to DEFAULT_SOURCE_TYPE
        defaultCommonTableRelationshipShouldBeFound("sourceType.equals=" + DEFAULT_SOURCE_TYPE);

        // Get all the commonTableRelationshipList where sourceType equals to UPDATED_SOURCE_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("sourceType.equals=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySourceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where sourceType not equals to DEFAULT_SOURCE_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("sourceType.notEquals=" + DEFAULT_SOURCE_TYPE);

        // Get all the commonTableRelationshipList where sourceType not equals to UPDATED_SOURCE_TYPE
        defaultCommonTableRelationshipShouldBeFound("sourceType.notEquals=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySourceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where sourceType in DEFAULT_SOURCE_TYPE or UPDATED_SOURCE_TYPE
        defaultCommonTableRelationshipShouldBeFound("sourceType.in=" + DEFAULT_SOURCE_TYPE + "," + UPDATED_SOURCE_TYPE);

        // Get all the commonTableRelationshipList where sourceType equals to UPDATED_SOURCE_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("sourceType.in=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySourceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where sourceType is not null
        defaultCommonTableRelationshipShouldBeFound("sourceType.specified=true");

        // Get all the commonTableRelationshipList where sourceType is null
        defaultCommonTableRelationshipShouldNotBeFound("sourceType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField equals to DEFAULT_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.equals=" + DEFAULT_OTHER_ENTITY_FIELD);

        // Get all the commonTableRelationshipList where otherEntityField equals to UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.equals=" + UPDATED_OTHER_ENTITY_FIELD);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField not equals to DEFAULT_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.notEquals=" + DEFAULT_OTHER_ENTITY_FIELD);

        // Get all the commonTableRelationshipList where otherEntityField not equals to UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.notEquals=" + UPDATED_OTHER_ENTITY_FIELD);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField in DEFAULT_OTHER_ENTITY_FIELD or UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.in=" + DEFAULT_OTHER_ENTITY_FIELD + "," + UPDATED_OTHER_ENTITY_FIELD);

        // Get all the commonTableRelationshipList where otherEntityField equals to UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.in=" + UPDATED_OTHER_ENTITY_FIELD);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField is not null
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.specified=true");

        // Get all the commonTableRelationshipList where otherEntityField is null
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField contains DEFAULT_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.contains=" + DEFAULT_OTHER_ENTITY_FIELD);

        // Get all the commonTableRelationshipList where otherEntityField contains UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.contains=" + UPDATED_OTHER_ENTITY_FIELD);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityFieldNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityField does not contain DEFAULT_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityField.doesNotContain=" + DEFAULT_OTHER_ENTITY_FIELD);

        // Get all the commonTableRelationshipList where otherEntityField does not contain UPDATED_OTHER_ENTITY_FIELD
        defaultCommonTableRelationshipShouldBeFound("otherEntityField.doesNotContain=" + UPDATED_OTHER_ENTITY_FIELD);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName equals to DEFAULT_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.equals=" + DEFAULT_OTHER_ENTITY_NAME);

        // Get all the commonTableRelationshipList where otherEntityName equals to UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.equals=" + UPDATED_OTHER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName not equals to DEFAULT_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.notEquals=" + DEFAULT_OTHER_ENTITY_NAME);

        // Get all the commonTableRelationshipList where otherEntityName not equals to UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.notEquals=" + UPDATED_OTHER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName in DEFAULT_OTHER_ENTITY_NAME or UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.in=" + DEFAULT_OTHER_ENTITY_NAME + "," + UPDATED_OTHER_ENTITY_NAME);

        // Get all the commonTableRelationshipList where otherEntityName equals to UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.in=" + UPDATED_OTHER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName is not null
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.specified=true");

        // Get all the commonTableRelationshipList where otherEntityName is null
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName contains DEFAULT_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.contains=" + DEFAULT_OTHER_ENTITY_NAME);

        // Get all the commonTableRelationshipList where otherEntityName contains UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.contains=" + UPDATED_OTHER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityName does not contain DEFAULT_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityName.doesNotContain=" + DEFAULT_OTHER_ENTITY_NAME);

        // Get all the commonTableRelationshipList where otherEntityName does not contain UPDATED_OTHER_ENTITY_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityName.doesNotContain=" + UPDATED_OTHER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName equals to DEFAULT_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("relationshipName.equals=" + DEFAULT_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where relationshipName equals to UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.equals=" + UPDATED_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName not equals to DEFAULT_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.notEquals=" + DEFAULT_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where relationshipName not equals to UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("relationshipName.notEquals=" + UPDATED_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName in DEFAULT_RELATIONSHIP_NAME or UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("relationshipName.in=" + DEFAULT_RELATIONSHIP_NAME + "," + UPDATED_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where relationshipName equals to UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.in=" + UPDATED_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName is not null
        defaultCommonTableRelationshipShouldBeFound("relationshipName.specified=true");

        // Get all the commonTableRelationshipList where relationshipName is null
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName contains DEFAULT_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("relationshipName.contains=" + DEFAULT_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where relationshipName contains UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.contains=" + UPDATED_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByRelationshipNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where relationshipName does not contain DEFAULT_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("relationshipName.doesNotContain=" + DEFAULT_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where relationshipName does not contain UPDATED_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("relationshipName.doesNotContain=" + UPDATED_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName equals to DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityRelationshipName.equals=" + DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName equals to UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityRelationshipName.equals=" + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName not equals to DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityRelationshipName.notEquals=" + DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName not equals to UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityRelationshipName.notEquals=" + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName in DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME or UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound(
            "otherEntityRelationshipName.in=" + DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME + "," + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        );

        // Get all the commonTableRelationshipList where otherEntityRelationshipName equals to UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityRelationshipName.in=" + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName is not null
        defaultCommonTableRelationshipShouldBeFound("otherEntityRelationshipName.specified=true");

        // Get all the commonTableRelationshipList where otherEntityRelationshipName is null
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityRelationshipName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName contains DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityRelationshipName.contains=" + DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName contains UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityRelationshipName.contains=" + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityRelationshipNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityRelationshipName does not contain DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldNotBeFound(
            "otherEntityRelationshipName.doesNotContain=" + DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME
        );

        // Get all the commonTableRelationshipList where otherEntityRelationshipName does not contain UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME
        defaultCommonTableRelationshipShouldBeFound("otherEntityRelationshipName.doesNotContain=" + UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth equals to DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.equals=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.equals=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth not equals to DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.notEquals=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth not equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.notEquals=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth in DEFAULT_COLUMN_WIDTH or UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.in=" + DEFAULT_COLUMN_WIDTH + "," + UPDATED_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth equals to UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.in=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth is not null
        defaultCommonTableRelationshipShouldBeFound("columnWidth.specified=true");

        // Get all the commonTableRelationshipList where columnWidth is null
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth is greater than or equal to DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.greaterThanOrEqual=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth is greater than or equal to UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.greaterThanOrEqual=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth is less than or equal to DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.lessThanOrEqual=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth is less than or equal to SMALLER_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.lessThanOrEqual=" + SMALLER_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth is less than DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.lessThan=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth is less than UPDATED_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.lessThan=" + UPDATED_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByColumnWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where columnWidth is greater than DEFAULT_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldNotBeFound("columnWidth.greaterThan=" + DEFAULT_COLUMN_WIDTH);

        // Get all the commonTableRelationshipList where columnWidth is greater than SMALLER_COLUMN_WIDTH
        defaultCommonTableRelationshipShouldBeFound("columnWidth.greaterThan=" + SMALLER_COLUMN_WIDTH);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order equals to DEFAULT_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order equals to UPDATED_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order not equals to DEFAULT_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order not equals to UPDATED_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the commonTableRelationshipList where order equals to UPDATED_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order is not null
        defaultCommonTableRelationshipShouldBeFound("order.specified=true");

        // Get all the commonTableRelationshipList where order is null
        defaultCommonTableRelationshipShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order is greater than or equal to DEFAULT_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order is greater than or equal to UPDATED_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order is less than or equal to DEFAULT_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order is less than or equal to SMALLER_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order is less than DEFAULT_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order is less than UPDATED_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where order is greater than DEFAULT_ORDER
        defaultCommonTableRelationshipShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the commonTableRelationshipList where order is greater than SMALLER_ORDER
        defaultCommonTableRelationshipShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFixedIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fixed equals to DEFAULT_FIXED
        defaultCommonTableRelationshipShouldBeFound("fixed.equals=" + DEFAULT_FIXED);

        // Get all the commonTableRelationshipList where fixed equals to UPDATED_FIXED
        defaultCommonTableRelationshipShouldNotBeFound("fixed.equals=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFixedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fixed not equals to DEFAULT_FIXED
        defaultCommonTableRelationshipShouldNotBeFound("fixed.notEquals=" + DEFAULT_FIXED);

        // Get all the commonTableRelationshipList where fixed not equals to UPDATED_FIXED
        defaultCommonTableRelationshipShouldBeFound("fixed.notEquals=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFixedIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fixed in DEFAULT_FIXED or UPDATED_FIXED
        defaultCommonTableRelationshipShouldBeFound("fixed.in=" + DEFAULT_FIXED + "," + UPDATED_FIXED);

        // Get all the commonTableRelationshipList where fixed equals to UPDATED_FIXED
        defaultCommonTableRelationshipShouldNotBeFound("fixed.in=" + UPDATED_FIXED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFixedIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fixed is not null
        defaultCommonTableRelationshipShouldBeFound("fixed.specified=true");

        // Get all the commonTableRelationshipList where fixed is null
        defaultCommonTableRelationshipShouldNotBeFound("fixed.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEditInListIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where editInList equals to DEFAULT_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("editInList.equals=" + DEFAULT_EDIT_IN_LIST);

        // Get all the commonTableRelationshipList where editInList equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("editInList.equals=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEditInListIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where editInList not equals to DEFAULT_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("editInList.notEquals=" + DEFAULT_EDIT_IN_LIST);

        // Get all the commonTableRelationshipList where editInList not equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("editInList.notEquals=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEditInListIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where editInList in DEFAULT_EDIT_IN_LIST or UPDATED_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("editInList.in=" + DEFAULT_EDIT_IN_LIST + "," + UPDATED_EDIT_IN_LIST);

        // Get all the commonTableRelationshipList where editInList equals to UPDATED_EDIT_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("editInList.in=" + UPDATED_EDIT_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEditInListIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where editInList is not null
        defaultCommonTableRelationshipShouldBeFound("editInList.specified=true");

        // Get all the commonTableRelationshipList where editInList is null
        defaultCommonTableRelationshipShouldNotBeFound("editInList.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEnableFilterIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where enableFilter equals to DEFAULT_ENABLE_FILTER
        defaultCommonTableRelationshipShouldBeFound("enableFilter.equals=" + DEFAULT_ENABLE_FILTER);

        // Get all the commonTableRelationshipList where enableFilter equals to UPDATED_ENABLE_FILTER
        defaultCommonTableRelationshipShouldNotBeFound("enableFilter.equals=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEnableFilterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where enableFilter not equals to DEFAULT_ENABLE_FILTER
        defaultCommonTableRelationshipShouldNotBeFound("enableFilter.notEquals=" + DEFAULT_ENABLE_FILTER);

        // Get all the commonTableRelationshipList where enableFilter not equals to UPDATED_ENABLE_FILTER
        defaultCommonTableRelationshipShouldBeFound("enableFilter.notEquals=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEnableFilterIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where enableFilter in DEFAULT_ENABLE_FILTER or UPDATED_ENABLE_FILTER
        defaultCommonTableRelationshipShouldBeFound("enableFilter.in=" + DEFAULT_ENABLE_FILTER + "," + UPDATED_ENABLE_FILTER);

        // Get all the commonTableRelationshipList where enableFilter equals to UPDATED_ENABLE_FILTER
        defaultCommonTableRelationshipShouldNotBeFound("enableFilter.in=" + UPDATED_ENABLE_FILTER);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEnableFilterIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where enableFilter is not null
        defaultCommonTableRelationshipShouldBeFound("enableFilter.specified=true");

        // Get all the commonTableRelationshipList where enableFilter is null
        defaultCommonTableRelationshipShouldNotBeFound("enableFilter.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInListIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInList equals to DEFAULT_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("hideInList.equals=" + DEFAULT_HIDE_IN_LIST);

        // Get all the commonTableRelationshipList where hideInList equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("hideInList.equals=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInListIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInList not equals to DEFAULT_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("hideInList.notEquals=" + DEFAULT_HIDE_IN_LIST);

        // Get all the commonTableRelationshipList where hideInList not equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("hideInList.notEquals=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInListIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInList in DEFAULT_HIDE_IN_LIST or UPDATED_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldBeFound("hideInList.in=" + DEFAULT_HIDE_IN_LIST + "," + UPDATED_HIDE_IN_LIST);

        // Get all the commonTableRelationshipList where hideInList equals to UPDATED_HIDE_IN_LIST
        defaultCommonTableRelationshipShouldNotBeFound("hideInList.in=" + UPDATED_HIDE_IN_LIST);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInListIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInList is not null
        defaultCommonTableRelationshipShouldBeFound("hideInList.specified=true");

        // Get all the commonTableRelationshipList where hideInList is null
        defaultCommonTableRelationshipShouldNotBeFound("hideInList.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInFormIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInForm equals to DEFAULT_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldBeFound("hideInForm.equals=" + DEFAULT_HIDE_IN_FORM);

        // Get all the commonTableRelationshipList where hideInForm equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldNotBeFound("hideInForm.equals=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInFormIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInForm not equals to DEFAULT_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldNotBeFound("hideInForm.notEquals=" + DEFAULT_HIDE_IN_FORM);

        // Get all the commonTableRelationshipList where hideInForm not equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldBeFound("hideInForm.notEquals=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInFormIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInForm in DEFAULT_HIDE_IN_FORM or UPDATED_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldBeFound("hideInForm.in=" + DEFAULT_HIDE_IN_FORM + "," + UPDATED_HIDE_IN_FORM);

        // Get all the commonTableRelationshipList where hideInForm equals to UPDATED_HIDE_IN_FORM
        defaultCommonTableRelationshipShouldNotBeFound("hideInForm.in=" + UPDATED_HIDE_IN_FORM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHideInFormIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where hideInForm is not null
        defaultCommonTableRelationshipShouldBeFound("hideInForm.specified=true");

        // Get all the commonTableRelationshipList where hideInForm is null
        defaultCommonTableRelationshipShouldNotBeFound("hideInForm.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySystemIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where system equals to DEFAULT_SYSTEM
        defaultCommonTableRelationshipShouldBeFound("system.equals=" + DEFAULT_SYSTEM);

        // Get all the commonTableRelationshipList where system equals to UPDATED_SYSTEM
        defaultCommonTableRelationshipShouldNotBeFound("system.equals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySystemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where system not equals to DEFAULT_SYSTEM
        defaultCommonTableRelationshipShouldNotBeFound("system.notEquals=" + DEFAULT_SYSTEM);

        // Get all the commonTableRelationshipList where system not equals to UPDATED_SYSTEM
        defaultCommonTableRelationshipShouldBeFound("system.notEquals=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySystemIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where system in DEFAULT_SYSTEM or UPDATED_SYSTEM
        defaultCommonTableRelationshipShouldBeFound("system.in=" + DEFAULT_SYSTEM + "," + UPDATED_SYSTEM);

        // Get all the commonTableRelationshipList where system equals to UPDATED_SYSTEM
        defaultCommonTableRelationshipShouldNotBeFound("system.in=" + UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsBySystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where system is not null
        defaultCommonTableRelationshipShouldBeFound("system.specified=true");

        // Get all the commonTableRelationshipList where system is null
        defaultCommonTableRelationshipShouldNotBeFound("system.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor equals to DEFAULT_FONT_COLOR
        defaultCommonTableRelationshipShouldBeFound("fontColor.equals=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableRelationshipList where fontColor equals to UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.equals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor not equals to DEFAULT_FONT_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.notEquals=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableRelationshipList where fontColor not equals to UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldBeFound("fontColor.notEquals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor in DEFAULT_FONT_COLOR or UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldBeFound("fontColor.in=" + DEFAULT_FONT_COLOR + "," + UPDATED_FONT_COLOR);

        // Get all the commonTableRelationshipList where fontColor equals to UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.in=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor is not null
        defaultCommonTableRelationshipShouldBeFound("fontColor.specified=true");

        // Get all the commonTableRelationshipList where fontColor is null
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor contains DEFAULT_FONT_COLOR
        defaultCommonTableRelationshipShouldBeFound("fontColor.contains=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableRelationshipList where fontColor contains UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.contains=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByFontColorNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where fontColor does not contain DEFAULT_FONT_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("fontColor.doesNotContain=" + DEFAULT_FONT_COLOR);

        // Get all the commonTableRelationshipList where fontColor does not contain UPDATED_FONT_COLOR
        defaultCommonTableRelationshipShouldBeFound("fontColor.doesNotContain=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor equals to DEFAULT_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.equals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableRelationshipList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.equals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor not equals to DEFAULT_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.notEquals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableRelationshipList where backgroundColor not equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.notEquals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor in DEFAULT_BACKGROUND_COLOR or UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.in=" + DEFAULT_BACKGROUND_COLOR + "," + UPDATED_BACKGROUND_COLOR);

        // Get all the commonTableRelationshipList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.in=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor is not null
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.specified=true");

        // Get all the commonTableRelationshipList where backgroundColor is null
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor contains DEFAULT_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.contains=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableRelationshipList where backgroundColor contains UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.contains=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByBackgroundColorNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where backgroundColor does not contain DEFAULT_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldNotBeFound("backgroundColor.doesNotContain=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the commonTableRelationshipList where backgroundColor does not contain UPDATED_BACKGROUND_COLOR
        defaultCommonTableRelationshipShouldBeFound("backgroundColor.doesNotContain=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help equals to DEFAULT_HELP
        defaultCommonTableRelationshipShouldBeFound("help.equals=" + DEFAULT_HELP);

        // Get all the commonTableRelationshipList where help equals to UPDATED_HELP
        defaultCommonTableRelationshipShouldNotBeFound("help.equals=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help not equals to DEFAULT_HELP
        defaultCommonTableRelationshipShouldNotBeFound("help.notEquals=" + DEFAULT_HELP);

        // Get all the commonTableRelationshipList where help not equals to UPDATED_HELP
        defaultCommonTableRelationshipShouldBeFound("help.notEquals=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help in DEFAULT_HELP or UPDATED_HELP
        defaultCommonTableRelationshipShouldBeFound("help.in=" + DEFAULT_HELP + "," + UPDATED_HELP);

        // Get all the commonTableRelationshipList where help equals to UPDATED_HELP
        defaultCommonTableRelationshipShouldNotBeFound("help.in=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help is not null
        defaultCommonTableRelationshipShouldBeFound("help.specified=true");

        // Get all the commonTableRelationshipList where help is null
        defaultCommonTableRelationshipShouldNotBeFound("help.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help contains DEFAULT_HELP
        defaultCommonTableRelationshipShouldBeFound("help.contains=" + DEFAULT_HELP);

        // Get all the commonTableRelationshipList where help contains UPDATED_HELP
        defaultCommonTableRelationshipShouldNotBeFound("help.contains=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByHelpNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where help does not contain DEFAULT_HELP
        defaultCommonTableRelationshipShouldNotBeFound("help.doesNotContain=" + DEFAULT_HELP);

        // Get all the commonTableRelationshipList where help does not contain UPDATED_HELP
        defaultCommonTableRelationshipShouldBeFound("help.doesNotContain=" + UPDATED_HELP);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOwnerSideIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where ownerSide equals to DEFAULT_OWNER_SIDE
        defaultCommonTableRelationshipShouldBeFound("ownerSide.equals=" + DEFAULT_OWNER_SIDE);

        // Get all the commonTableRelationshipList where ownerSide equals to UPDATED_OWNER_SIDE
        defaultCommonTableRelationshipShouldNotBeFound("ownerSide.equals=" + UPDATED_OWNER_SIDE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOwnerSideIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where ownerSide not equals to DEFAULT_OWNER_SIDE
        defaultCommonTableRelationshipShouldNotBeFound("ownerSide.notEquals=" + DEFAULT_OWNER_SIDE);

        // Get all the commonTableRelationshipList where ownerSide not equals to UPDATED_OWNER_SIDE
        defaultCommonTableRelationshipShouldBeFound("ownerSide.notEquals=" + UPDATED_OWNER_SIDE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOwnerSideIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where ownerSide in DEFAULT_OWNER_SIDE or UPDATED_OWNER_SIDE
        defaultCommonTableRelationshipShouldBeFound("ownerSide.in=" + DEFAULT_OWNER_SIDE + "," + UPDATED_OWNER_SIDE);

        // Get all the commonTableRelationshipList where ownerSide equals to UPDATED_OWNER_SIDE
        defaultCommonTableRelationshipShouldNotBeFound("ownerSide.in=" + UPDATED_OWNER_SIDE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOwnerSideIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where ownerSide is not null
        defaultCommonTableRelationshipShouldBeFound("ownerSide.specified=true");

        // Get all the commonTableRelationshipList where ownerSide is null
        defaultCommonTableRelationshipShouldNotBeFound("ownerSide.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName equals to DEFAULT_DATA_NAME
        defaultCommonTableRelationshipShouldBeFound("dataName.equals=" + DEFAULT_DATA_NAME);

        // Get all the commonTableRelationshipList where dataName equals to UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldNotBeFound("dataName.equals=" + UPDATED_DATA_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName not equals to DEFAULT_DATA_NAME
        defaultCommonTableRelationshipShouldNotBeFound("dataName.notEquals=" + DEFAULT_DATA_NAME);

        // Get all the commonTableRelationshipList where dataName not equals to UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldBeFound("dataName.notEquals=" + UPDATED_DATA_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName in DEFAULT_DATA_NAME or UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldBeFound("dataName.in=" + DEFAULT_DATA_NAME + "," + UPDATED_DATA_NAME);

        // Get all the commonTableRelationshipList where dataName equals to UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldNotBeFound("dataName.in=" + UPDATED_DATA_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName is not null
        defaultCommonTableRelationshipShouldBeFound("dataName.specified=true");

        // Get all the commonTableRelationshipList where dataName is null
        defaultCommonTableRelationshipShouldNotBeFound("dataName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName contains DEFAULT_DATA_NAME
        defaultCommonTableRelationshipShouldBeFound("dataName.contains=" + DEFAULT_DATA_NAME);

        // Get all the commonTableRelationshipList where dataName contains UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldNotBeFound("dataName.contains=" + UPDATED_DATA_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataName does not contain DEFAULT_DATA_NAME
        defaultCommonTableRelationshipShouldNotBeFound("dataName.doesNotContain=" + DEFAULT_DATA_NAME);

        // Get all the commonTableRelationshipList where dataName does not contain UPDATED_DATA_NAME
        defaultCommonTableRelationshipShouldBeFound("dataName.doesNotContain=" + UPDATED_DATA_NAME);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType equals to DEFAULT_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldBeFound("webComponentType.equals=" + DEFAULT_WEB_COMPONENT_TYPE);

        // Get all the commonTableRelationshipList where webComponentType equals to UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.equals=" + UPDATED_WEB_COMPONENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType not equals to DEFAULT_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.notEquals=" + DEFAULT_WEB_COMPONENT_TYPE);

        // Get all the commonTableRelationshipList where webComponentType not equals to UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldBeFound("webComponentType.notEquals=" + UPDATED_WEB_COMPONENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType in DEFAULT_WEB_COMPONENT_TYPE or UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldBeFound("webComponentType.in=" + DEFAULT_WEB_COMPONENT_TYPE + "," + UPDATED_WEB_COMPONENT_TYPE);

        // Get all the commonTableRelationshipList where webComponentType equals to UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.in=" + UPDATED_WEB_COMPONENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType is not null
        defaultCommonTableRelationshipShouldBeFound("webComponentType.specified=true");

        // Get all the commonTableRelationshipList where webComponentType is null
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType contains DEFAULT_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldBeFound("webComponentType.contains=" + DEFAULT_WEB_COMPONENT_TYPE);

        // Get all the commonTableRelationshipList where webComponentType contains UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.contains=" + UPDATED_WEB_COMPONENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByWebComponentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where webComponentType does not contain DEFAULT_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldNotBeFound("webComponentType.doesNotContain=" + DEFAULT_WEB_COMPONENT_TYPE);

        // Get all the commonTableRelationshipList where webComponentType does not contain UPDATED_WEB_COMPONENT_TYPE
        defaultCommonTableRelationshipShouldBeFound("webComponentType.doesNotContain=" + UPDATED_WEB_COMPONENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityIsTreeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityIsTree equals to DEFAULT_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldBeFound("otherEntityIsTree.equals=" + DEFAULT_OTHER_ENTITY_IS_TREE);

        // Get all the commonTableRelationshipList where otherEntityIsTree equals to UPDATED_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityIsTree.equals=" + UPDATED_OTHER_ENTITY_IS_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityIsTreeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityIsTree not equals to DEFAULT_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityIsTree.notEquals=" + DEFAULT_OTHER_ENTITY_IS_TREE);

        // Get all the commonTableRelationshipList where otherEntityIsTree not equals to UPDATED_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldBeFound("otherEntityIsTree.notEquals=" + UPDATED_OTHER_ENTITY_IS_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityIsTreeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityIsTree in DEFAULT_OTHER_ENTITY_IS_TREE or UPDATED_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldBeFound(
            "otherEntityIsTree.in=" + DEFAULT_OTHER_ENTITY_IS_TREE + "," + UPDATED_OTHER_ENTITY_IS_TREE
        );

        // Get all the commonTableRelationshipList where otherEntityIsTree equals to UPDATED_OTHER_ENTITY_IS_TREE
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityIsTree.in=" + UPDATED_OTHER_ENTITY_IS_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOtherEntityIsTreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where otherEntityIsTree is not null
        defaultCommonTableRelationshipShouldBeFound("otherEntityIsTree.specified=true");

        // Get all the commonTableRelationshipList where otherEntityIsTree is null
        defaultCommonTableRelationshipShouldNotBeFound("otherEntityIsTree.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByShowInFilterTreeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where showInFilterTree equals to DEFAULT_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldBeFound("showInFilterTree.equals=" + DEFAULT_SHOW_IN_FILTER_TREE);

        // Get all the commonTableRelationshipList where showInFilterTree equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldNotBeFound("showInFilterTree.equals=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByShowInFilterTreeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where showInFilterTree not equals to DEFAULT_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldNotBeFound("showInFilterTree.notEquals=" + DEFAULT_SHOW_IN_FILTER_TREE);

        // Get all the commonTableRelationshipList where showInFilterTree not equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldBeFound("showInFilterTree.notEquals=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByShowInFilterTreeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where showInFilterTree in DEFAULT_SHOW_IN_FILTER_TREE or UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldBeFound(
            "showInFilterTree.in=" + DEFAULT_SHOW_IN_FILTER_TREE + "," + UPDATED_SHOW_IN_FILTER_TREE
        );

        // Get all the commonTableRelationshipList where showInFilterTree equals to UPDATED_SHOW_IN_FILTER_TREE
        defaultCommonTableRelationshipShouldNotBeFound("showInFilterTree.in=" + UPDATED_SHOW_IN_FILTER_TREE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByShowInFilterTreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where showInFilterTree is not null
        defaultCommonTableRelationshipShouldBeFound("showInFilterTree.specified=true");

        // Get all the commonTableRelationshipList where showInFilterTree is null
        defaultCommonTableRelationshipShouldNotBeFound("showInFilterTree.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode equals to DEFAULT_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryCode.equals=" + DEFAULT_DATA_DICTIONARY_CODE);

        // Get all the commonTableRelationshipList where dataDictionaryCode equals to UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.equals=" + UPDATED_DATA_DICTIONARY_CODE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode not equals to DEFAULT_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.notEquals=" + DEFAULT_DATA_DICTIONARY_CODE);

        // Get all the commonTableRelationshipList where dataDictionaryCode not equals to UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryCode.notEquals=" + UPDATED_DATA_DICTIONARY_CODE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode in DEFAULT_DATA_DICTIONARY_CODE or UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldBeFound(
            "dataDictionaryCode.in=" + DEFAULT_DATA_DICTIONARY_CODE + "," + UPDATED_DATA_DICTIONARY_CODE
        );

        // Get all the commonTableRelationshipList where dataDictionaryCode equals to UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.in=" + UPDATED_DATA_DICTIONARY_CODE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode is not null
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryCode.specified=true");

        // Get all the commonTableRelationshipList where dataDictionaryCode is null
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode contains DEFAULT_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryCode.contains=" + DEFAULT_DATA_DICTIONARY_CODE);

        // Get all the commonTableRelationshipList where dataDictionaryCode contains UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.contains=" + UPDATED_DATA_DICTIONARY_CODE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByDataDictionaryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where dataDictionaryCode does not contain DEFAULT_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryCode.doesNotContain=" + DEFAULT_DATA_DICTIONARY_CODE);

        // Get all the commonTableRelationshipList where dataDictionaryCode does not contain UPDATED_DATA_DICTIONARY_CODE
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryCode.doesNotContain=" + UPDATED_DATA_DICTIONARY_CODE);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByClientReadOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where clientReadOnly equals to DEFAULT_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldBeFound("clientReadOnly.equals=" + DEFAULT_CLIENT_READ_ONLY);

        // Get all the commonTableRelationshipList where clientReadOnly equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldNotBeFound("clientReadOnly.equals=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByClientReadOnlyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where clientReadOnly not equals to DEFAULT_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldNotBeFound("clientReadOnly.notEquals=" + DEFAULT_CLIENT_READ_ONLY);

        // Get all the commonTableRelationshipList where clientReadOnly not equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldBeFound("clientReadOnly.notEquals=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByClientReadOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where clientReadOnly in DEFAULT_CLIENT_READ_ONLY or UPDATED_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldBeFound("clientReadOnly.in=" + DEFAULT_CLIENT_READ_ONLY + "," + UPDATED_CLIENT_READ_ONLY);

        // Get all the commonTableRelationshipList where clientReadOnly equals to UPDATED_CLIENT_READ_ONLY
        defaultCommonTableRelationshipShouldNotBeFound("clientReadOnly.in=" + UPDATED_CLIENT_READ_ONLY);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByClientReadOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where clientReadOnly is not null
        defaultCommonTableRelationshipShouldBeFound("clientReadOnly.specified=true");

        // Get all the commonTableRelationshipList where clientReadOnly is null
        defaultCommonTableRelationshipShouldNotBeFound("clientReadOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEndUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where endUsed equals to DEFAULT_END_USED
        defaultCommonTableRelationshipShouldBeFound("endUsed.equals=" + DEFAULT_END_USED);

        // Get all the commonTableRelationshipList where endUsed equals to UPDATED_END_USED
        defaultCommonTableRelationshipShouldNotBeFound("endUsed.equals=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEndUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where endUsed not equals to DEFAULT_END_USED
        defaultCommonTableRelationshipShouldNotBeFound("endUsed.notEquals=" + DEFAULT_END_USED);

        // Get all the commonTableRelationshipList where endUsed not equals to UPDATED_END_USED
        defaultCommonTableRelationshipShouldBeFound("endUsed.notEquals=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEndUsedIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where endUsed in DEFAULT_END_USED or UPDATED_END_USED
        defaultCommonTableRelationshipShouldBeFound("endUsed.in=" + DEFAULT_END_USED + "," + UPDATED_END_USED);

        // Get all the commonTableRelationshipList where endUsed equals to UPDATED_END_USED
        defaultCommonTableRelationshipShouldNotBeFound("endUsed.in=" + UPDATED_END_USED);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByEndUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where endUsed is not null
        defaultCommonTableRelationshipShouldBeFound("endUsed.specified=true");

        // Get all the commonTableRelationshipList where endUsed is null
        defaultCommonTableRelationshipShouldNotBeFound("endUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options equals to DEFAULT_OPTIONS
        defaultCommonTableRelationshipShouldBeFound("options.equals=" + DEFAULT_OPTIONS);

        // Get all the commonTableRelationshipList where options equals to UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldNotBeFound("options.equals=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options not equals to DEFAULT_OPTIONS
        defaultCommonTableRelationshipShouldNotBeFound("options.notEquals=" + DEFAULT_OPTIONS);

        // Get all the commonTableRelationshipList where options not equals to UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldBeFound("options.notEquals=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsIsInShouldWork() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options in DEFAULT_OPTIONS or UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldBeFound("options.in=" + DEFAULT_OPTIONS + "," + UPDATED_OPTIONS);

        // Get all the commonTableRelationshipList where options equals to UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldNotBeFound("options.in=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options is not null
        defaultCommonTableRelationshipShouldBeFound("options.specified=true");

        // Get all the commonTableRelationshipList where options is null
        defaultCommonTableRelationshipShouldNotBeFound("options.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options contains DEFAULT_OPTIONS
        defaultCommonTableRelationshipShouldBeFound("options.contains=" + DEFAULT_OPTIONS);

        // Get all the commonTableRelationshipList where options contains UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldNotBeFound("options.contains=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCommonTableRelationshipsByOptionsNotContainsSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        // Get all the commonTableRelationshipList where options does not contain DEFAULT_OPTIONS
        defaultCommonTableRelationshipShouldNotBeFound("options.doesNotContain=" + DEFAULT_OPTIONS);

        // Get all the commonTableRelationshipList where options does not contain UPDATED_OPTIONS
        defaultCommonTableRelationshipShouldBeFound("options.doesNotContain=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    public void getAllCommonTableRelationshipsByRelationEntityIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);
        CommonTable relationEntity = CommonTableResourceIT.createEntity();
        commonTableRelationship.setRelationEntity(relationEntity);
        commonTableRelationshipRepository.insert(commonTableRelationship);
        Long relationEntityId = relationEntity.getId();

        // Get all the commonTableRelationshipList where relationEntity equals to relationEntityId
        defaultCommonTableRelationshipShouldBeFound("relationEntityId.equals=" + relationEntityId);

        // Get all the commonTableRelationshipList where relationEntity equals to (relationEntityId + 1)
        defaultCommonTableRelationshipShouldNotBeFound("relationEntityId.equals=" + (relationEntityId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTableRelationshipsByDataDictionaryNodeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);
        DataDictionary dataDictionaryNode = DataDictionaryResourceIT.createEntity();
        commonTableRelationship.setDataDictionaryNode(dataDictionaryNode);
        commonTableRelationshipRepository.insert(commonTableRelationship);
        Long dataDictionaryNodeId = dataDictionaryNode.getId();

        // Get all the commonTableRelationshipList where dataDictionaryNode equals to dataDictionaryNodeId
        defaultCommonTableRelationshipShouldBeFound("dataDictionaryNodeId.equals=" + dataDictionaryNodeId);

        // Get all the commonTableRelationshipList where dataDictionaryNode equals to (dataDictionaryNodeId + 1)
        defaultCommonTableRelationshipShouldNotBeFound("dataDictionaryNodeId.equals=" + (dataDictionaryNodeId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTableRelationshipsByMetaModelIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);
        CommonTable metaModel = CommonTableResourceIT.createEntity();
        commonTableRelationship.setMetaModel(metaModel);
        commonTableRelationshipRepository.insert(commonTableRelationship);
        Long metaModelId = metaModel.getId();

        // Get all the commonTableRelationshipList where metaModel equals to metaModelId
        defaultCommonTableRelationshipShouldBeFound("metaModelId.equals=" + metaModelId);

        // Get all the commonTableRelationshipList where metaModel equals to (metaModelId + 1)
        defaultCommonTableRelationshipShouldNotBeFound("metaModelId.equals=" + (metaModelId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonTableRelationshipsByCommonTableIsEqualToSomething() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);
        CommonTable commonTable = CommonTableResourceIT.createEntity();
        commonTableRelationship.setCommonTable(commonTable);
        commonTableRelationshipRepository.insert(commonTableRelationship);
        Long commonTableId = commonTable.getId();

        // Get all the commonTableRelationshipList where commonTable equals to commonTableId
        defaultCommonTableRelationshipShouldBeFound("commonTableId.equals=" + commonTableId);

        // Get all the commonTableRelationshipList where commonTable equals to (commonTableId + 1)
        defaultCommonTableRelationshipShouldNotBeFound("commonTableId.equals=" + (commonTableId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonTableRelationshipShouldBeFound(String filter) throws Exception {
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonTableRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].relationshipType").value(hasItem(DEFAULT_RELATIONSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].otherEntityField").value(hasItem(DEFAULT_OTHER_ENTITY_FIELD)))
            .andExpect(jsonPath("$.[*].otherEntityName").value(hasItem(DEFAULT_OTHER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].relationshipName").value(hasItem(DEFAULT_RELATIONSHIP_NAME)))
            .andExpect(jsonPath("$.[*].otherEntityRelationshipName").value(hasItem(DEFAULT_OTHER_ENTITY_RELATIONSHIP_NAME)))
            .andExpect(jsonPath("$.[*].columnWidth").value(hasItem(DEFAULT_COLUMN_WIDTH)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].fixed").value(hasItem(DEFAULT_FIXED.toString())))
            .andExpect(jsonPath("$.[*].editInList").value(hasItem(DEFAULT_EDIT_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].enableFilter").value(hasItem(DEFAULT_ENABLE_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInList").value(hasItem(DEFAULT_HIDE_IN_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInForm").value(hasItem(DEFAULT_HIDE_IN_FORM.booleanValue())))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].help").value(hasItem(DEFAULT_HELP)))
            .andExpect(jsonPath("$.[*].ownerSide").value(hasItem(DEFAULT_OWNER_SIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataName").value(hasItem(DEFAULT_DATA_NAME)))
            .andExpect(jsonPath("$.[*].webComponentType").value(hasItem(DEFAULT_WEB_COMPONENT_TYPE)))
            .andExpect(jsonPath("$.[*].otherEntityIsTree").value(hasItem(DEFAULT_OTHER_ENTITY_IS_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].showInFilterTree").value(hasItem(DEFAULT_SHOW_IN_FILTER_TREE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataDictionaryCode").value(hasItem(DEFAULT_DATA_DICTIONARY_CODE)))
            .andExpect(jsonPath("$.[*].clientReadOnly").value(hasItem(DEFAULT_CLIENT_READ_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].endUsed").value(hasItem(DEFAULT_END_USED.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));

        // Check, that the count call also returns 1
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonTableRelationshipShouldNotBeFound(String filter) throws Exception {
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonTableRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonTableRelationship() throws Exception {
        // Get the commonTableRelationship
        restCommonTableRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonTableRelationship() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();

        // Update the commonTableRelationship
        CommonTableRelationship updatedCommonTableRelationship = commonTableRelationshipRepository
            .findById(commonTableRelationship.getId())
            .get();

        // Disconnect from session so that the updates on updatedCommonTableRelationship are not directly saved in db

        updatedCommonTableRelationship
            .name(UPDATED_NAME)
            .relationshipType(UPDATED_RELATIONSHIP_TYPE)
            .sourceType(UPDATED_SOURCE_TYPE)
            .otherEntityField(UPDATED_OTHER_ENTITY_FIELD)
            .otherEntityName(UPDATED_OTHER_ENTITY_NAME)
            .relationshipName(UPDATED_RELATIONSHIP_NAME)
            .otherEntityRelationshipName(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .fixed(UPDATED_FIXED)
            .editInList(UPDATED_EDIT_IN_LIST)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .system(UPDATED_SYSTEM)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .help(UPDATED_HELP)
            .ownerSide(UPDATED_OWNER_SIDE)
            .dataName(UPDATED_DATA_NAME)
            .webComponentType(UPDATED_WEB_COMPONENT_TYPE)
            .otherEntityIsTree(UPDATED_OTHER_ENTITY_IS_TREE)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .dataDictionaryCode(UPDATED_DATA_DICTIONARY_CODE)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(updatedCommonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableRelationshipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
        CommonTableRelationship testCommonTableRelationship = commonTableRelationshipList.get(commonTableRelationshipList.size() - 1);
        assertThat(testCommonTableRelationship.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTableRelationship.getRelationshipType()).isEqualTo(UPDATED_RELATIONSHIP_TYPE);
        assertThat(testCommonTableRelationship.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityField()).isEqualTo(UPDATED_OTHER_ENTITY_FIELD);
        assertThat(testCommonTableRelationship.getOtherEntityName()).isEqualTo(UPDATED_OTHER_ENTITY_NAME);
        assertThat(testCommonTableRelationship.getRelationshipName()).isEqualTo(UPDATED_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getOtherEntityRelationshipName()).isEqualTo(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getColumnWidth()).isEqualTo(UPDATED_COLUMN_WIDTH);
        assertThat(testCommonTableRelationship.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCommonTableRelationship.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testCommonTableRelationship.getEditInList()).isEqualTo(UPDATED_EDIT_IN_LIST);
        assertThat(testCommonTableRelationship.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableRelationship.getHideInList()).isEqualTo(UPDATED_HIDE_IN_LIST);
        assertThat(testCommonTableRelationship.getHideInForm()).isEqualTo(UPDATED_HIDE_IN_FORM);
        assertThat(testCommonTableRelationship.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTableRelationship.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testCommonTableRelationship.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testCommonTableRelationship.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableRelationship.getOwnerSide()).isEqualTo(UPDATED_OWNER_SIDE);
        assertThat(testCommonTableRelationship.getDataName()).isEqualTo(UPDATED_DATA_NAME);
        assertThat(testCommonTableRelationship.getWebComponentType()).isEqualTo(UPDATED_WEB_COMPONENT_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityIsTree()).isEqualTo(UPDATED_OTHER_ENTITY_IS_TREE);
        assertThat(testCommonTableRelationship.getShowInFilterTree()).isEqualTo(UPDATED_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableRelationship.getDataDictionaryCode()).isEqualTo(UPDATED_DATA_DICTIONARY_CODE);
        assertThat(testCommonTableRelationship.getClientReadOnly()).isEqualTo(UPDATED_CLIENT_READ_ONLY);
        assertThat(testCommonTableRelationship.getEndUsed()).isEqualTo(UPDATED_END_USED);
        assertThat(testCommonTableRelationship.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void putNonExistingngCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        restCommonTableRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonTableRelationshipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonTableRelationshipWithPatch() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();

        // Update the commonTableRelationship using partial update
        CommonTableRelationship partialUpdatedCommonTableRelationship = new CommonTableRelationship();
        partialUpdatedCommonTableRelationship.setId(commonTableRelationship.getId());

        partialUpdatedCommonTableRelationship
            .name(UPDATED_NAME)
            .relationshipType(UPDATED_RELATIONSHIP_TYPE)
            .sourceType(UPDATED_SOURCE_TYPE)
            .otherEntityField(UPDATED_OTHER_ENTITY_FIELD)
            .relationshipName(UPDATED_RELATIONSHIP_NAME)
            .otherEntityRelationshipName(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .help(UPDATED_HELP)
            .dataName(UPDATED_DATA_NAME)
            .webComponentType(UPDATED_WEB_COMPONENT_TYPE)
            .options(UPDATED_OPTIONS);

        restCommonTableRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTableRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTableRelationship))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
        CommonTableRelationship testCommonTableRelationship = commonTableRelationshipList.get(commonTableRelationshipList.size() - 1);
        assertThat(testCommonTableRelationship.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTableRelationship.getRelationshipType()).isEqualTo(UPDATED_RELATIONSHIP_TYPE);
        assertThat(testCommonTableRelationship.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityField()).isEqualTo(UPDATED_OTHER_ENTITY_FIELD);
        assertThat(testCommonTableRelationship.getOtherEntityName()).isEqualTo(DEFAULT_OTHER_ENTITY_NAME);
        assertThat(testCommonTableRelationship.getRelationshipName()).isEqualTo(UPDATED_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getOtherEntityRelationshipName()).isEqualTo(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getColumnWidth()).isEqualTo(DEFAULT_COLUMN_WIDTH);
        assertThat(testCommonTableRelationship.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCommonTableRelationship.getFixed()).isEqualTo(DEFAULT_FIXED);
        assertThat(testCommonTableRelationship.getEditInList()).isEqualTo(DEFAULT_EDIT_IN_LIST);
        assertThat(testCommonTableRelationship.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableRelationship.getHideInList()).isEqualTo(DEFAULT_HIDE_IN_LIST);
        assertThat(testCommonTableRelationship.getHideInForm()).isEqualTo(DEFAULT_HIDE_IN_FORM);
        assertThat(testCommonTableRelationship.getSystem()).isEqualTo(DEFAULT_SYSTEM);
        assertThat(testCommonTableRelationship.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testCommonTableRelationship.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testCommonTableRelationship.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableRelationship.getOwnerSide()).isEqualTo(DEFAULT_OWNER_SIDE);
        assertThat(testCommonTableRelationship.getDataName()).isEqualTo(UPDATED_DATA_NAME);
        assertThat(testCommonTableRelationship.getWebComponentType()).isEqualTo(UPDATED_WEB_COMPONENT_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityIsTree()).isEqualTo(DEFAULT_OTHER_ENTITY_IS_TREE);
        assertThat(testCommonTableRelationship.getShowInFilterTree()).isEqualTo(DEFAULT_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableRelationship.getDataDictionaryCode()).isEqualTo(DEFAULT_DATA_DICTIONARY_CODE);
        assertThat(testCommonTableRelationship.getClientReadOnly()).isEqualTo(DEFAULT_CLIENT_READ_ONLY);
        assertThat(testCommonTableRelationship.getEndUsed()).isEqualTo(DEFAULT_END_USED);
        assertThat(testCommonTableRelationship.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void fullUpdateCommonTableRelationshipWithPatch() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();

        // Update the commonTableRelationship using partial update
        CommonTableRelationship partialUpdatedCommonTableRelationship = new CommonTableRelationship();
        partialUpdatedCommonTableRelationship.setId(commonTableRelationship.getId());

        partialUpdatedCommonTableRelationship
            .name(UPDATED_NAME)
            .relationshipType(UPDATED_RELATIONSHIP_TYPE)
            .sourceType(UPDATED_SOURCE_TYPE)
            .otherEntityField(UPDATED_OTHER_ENTITY_FIELD)
            .otherEntityName(UPDATED_OTHER_ENTITY_NAME)
            .relationshipName(UPDATED_RELATIONSHIP_NAME)
            .otherEntityRelationshipName(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME)
            .columnWidth(UPDATED_COLUMN_WIDTH)
            .order(UPDATED_ORDER)
            .fixed(UPDATED_FIXED)
            .editInList(UPDATED_EDIT_IN_LIST)
            .enableFilter(UPDATED_ENABLE_FILTER)
            .hideInList(UPDATED_HIDE_IN_LIST)
            .hideInForm(UPDATED_HIDE_IN_FORM)
            .system(UPDATED_SYSTEM)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .help(UPDATED_HELP)
            .ownerSide(UPDATED_OWNER_SIDE)
            .dataName(UPDATED_DATA_NAME)
            .webComponentType(UPDATED_WEB_COMPONENT_TYPE)
            .otherEntityIsTree(UPDATED_OTHER_ENTITY_IS_TREE)
            .showInFilterTree(UPDATED_SHOW_IN_FILTER_TREE)
            .dataDictionaryCode(UPDATED_DATA_DICTIONARY_CODE)
            .clientReadOnly(UPDATED_CLIENT_READ_ONLY)
            .endUsed(UPDATED_END_USED)
            .options(UPDATED_OPTIONS);

        restCommonTableRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonTableRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonTableRelationship))
            )
            .andExpect(status().isOk());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
        CommonTableRelationship testCommonTableRelationship = commonTableRelationshipList.get(commonTableRelationshipList.size() - 1);
        assertThat(testCommonTableRelationship.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonTableRelationship.getRelationshipType()).isEqualTo(UPDATED_RELATIONSHIP_TYPE);
        assertThat(testCommonTableRelationship.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityField()).isEqualTo(UPDATED_OTHER_ENTITY_FIELD);
        assertThat(testCommonTableRelationship.getOtherEntityName()).isEqualTo(UPDATED_OTHER_ENTITY_NAME);
        assertThat(testCommonTableRelationship.getRelationshipName()).isEqualTo(UPDATED_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getOtherEntityRelationshipName()).isEqualTo(UPDATED_OTHER_ENTITY_RELATIONSHIP_NAME);
        assertThat(testCommonTableRelationship.getColumnWidth()).isEqualTo(UPDATED_COLUMN_WIDTH);
        assertThat(testCommonTableRelationship.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCommonTableRelationship.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testCommonTableRelationship.getEditInList()).isEqualTo(UPDATED_EDIT_IN_LIST);
        assertThat(testCommonTableRelationship.getEnableFilter()).isEqualTo(UPDATED_ENABLE_FILTER);
        assertThat(testCommonTableRelationship.getHideInList()).isEqualTo(UPDATED_HIDE_IN_LIST);
        assertThat(testCommonTableRelationship.getHideInForm()).isEqualTo(UPDATED_HIDE_IN_FORM);
        assertThat(testCommonTableRelationship.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testCommonTableRelationship.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testCommonTableRelationship.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testCommonTableRelationship.getHelp()).isEqualTo(UPDATED_HELP);
        assertThat(testCommonTableRelationship.getOwnerSide()).isEqualTo(UPDATED_OWNER_SIDE);
        assertThat(testCommonTableRelationship.getDataName()).isEqualTo(UPDATED_DATA_NAME);
        assertThat(testCommonTableRelationship.getWebComponentType()).isEqualTo(UPDATED_WEB_COMPONENT_TYPE);
        assertThat(testCommonTableRelationship.getOtherEntityIsTree()).isEqualTo(UPDATED_OTHER_ENTITY_IS_TREE);
        assertThat(testCommonTableRelationship.getShowInFilterTree()).isEqualTo(UPDATED_SHOW_IN_FILTER_TREE);
        assertThat(testCommonTableRelationship.getDataDictionaryCode()).isEqualTo(UPDATED_DATA_DICTIONARY_CODE);
        assertThat(testCommonTableRelationship.getClientReadOnly()).isEqualTo(UPDATED_CLIENT_READ_ONLY);
        assertThat(testCommonTableRelationship.getEndUsed()).isEqualTo(UPDATED_END_USED);
        assertThat(testCommonTableRelationship.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void patchNonExistingCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonTableRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonTableRelationshipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonTableRelationship() throws Exception {
        int databaseSizeBeforeUpdate = commonTableRelationshipRepository.findAll().size();
        commonTableRelationship.setId(count.incrementAndGet());

        // Create the CommonTableRelationship
        CommonTableRelationshipDTO commonTableRelationshipDTO = commonTableRelationshipMapper.toDto(commonTableRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonTableRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonTableRelationshipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonTableRelationship in the database
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonTableRelationship() throws Exception {
        // Initialize the database
        commonTableRelationshipRepository.insert(commonTableRelationship);

        int databaseSizeBeforeDelete = commonTableRelationshipRepository.findAll().size();

        // Delete the commonTableRelationship
        restCommonTableRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonTableRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonTableRelationship> commonTableRelationshipList = commonTableRelationshipRepository.findAll();
        assertThat(commonTableRelationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
