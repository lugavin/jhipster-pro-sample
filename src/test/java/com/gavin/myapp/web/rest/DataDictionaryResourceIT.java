package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.domain.DataDictionary;
import com.gavin.myapp.repository.DataDictionaryRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.DataDictionaryQueryService;
import com.gavin.myapp.service.criteria.DataDictionaryCriteria;
import com.gavin.myapp.service.dto.DataDictionaryDTO;
import com.gavin.myapp.service.mapper.DataDictionaryMapper;
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
 * Integration tests for the {@link DataDictionaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class DataDictionaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-dictionaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restDataDictionaryMockMvc;

    private DataDictionary dataDictionary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDictionary createEntity() {
        DataDictionary dataDictionary = new DataDictionary()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .fontColor(DEFAULT_FONT_COLOR)
            .backgroundColor(DEFAULT_BACKGROUND_COLOR);
        return dataDictionary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDictionary createUpdatedEntity() {
        DataDictionary dataDictionary = new DataDictionary()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR);
        return dataDictionary;
    }

    @BeforeEach
    public void initTest() {
        dataDictionary = createEntity();
    }

    @Test
    @Transactional
    void createDataDictionary() throws Exception {
        int databaseSizeBeforeCreate = dataDictionaryRepository.findAll().size();
        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);
        restDataDictionaryMockMvc
            .perform(
                post(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeCreate + 1);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void createDataDictionaryWithExistingId() throws Exception {
        // Create the DataDictionary with an existing ID
        dataDictionary.setId(1L);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        int databaseSizeBeforeCreate = dataDictionaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataDictionaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataDictionaries() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataDictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));
    }

    @Test
    @Transactional
    public void getDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get the dataDictionary
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL_ID, dataDictionary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataDictionary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fontColor").value(DEFAULT_FONT_COLOR))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR));
    }

    @Test
    @Transactional
    public void getDataDictionariesByIdFiltering() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        Long id = dataDictionary.getId();

        defaultDataDictionaryShouldBeFound("id.equals=" + id);
        defaultDataDictionaryShouldNotBeFound("id.notEquals=" + id);

        defaultDataDictionaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDataDictionaryShouldNotBeFound("id.greaterThan=" + id);

        defaultDataDictionaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDataDictionaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name equals to DEFAULT_NAME
        defaultDataDictionaryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name equals to UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name not equals to DEFAULT_NAME
        defaultDataDictionaryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name not equals to UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dataDictionaryList where name equals to UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name is not null
        defaultDataDictionaryShouldBeFound("name.specified=true");

        // Get all the dataDictionaryList where name is null
        defaultDataDictionaryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name contains DEFAULT_NAME
        defaultDataDictionaryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name contains UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name does not contain DEFAULT_NAME
        defaultDataDictionaryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name does not contain UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code equals to DEFAULT_CODE
        defaultDataDictionaryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code equals to UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code not equals to DEFAULT_CODE
        defaultDataDictionaryShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code not equals to UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the dataDictionaryList where code equals to UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code is not null
        defaultDataDictionaryShouldBeFound("code.specified=true");

        // Get all the dataDictionaryList where code is null
        defaultDataDictionaryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code contains DEFAULT_CODE
        defaultDataDictionaryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code contains UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code does not contain DEFAULT_CODE
        defaultDataDictionaryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code does not contain UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description equals to DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description not equals to DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description not equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the dataDictionaryList where description equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description is not null
        defaultDataDictionaryShouldBeFound("description.specified=true");

        // Get all the dataDictionaryList where description is null
        defaultDataDictionaryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description contains DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description contains UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description does not contain DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description does not contain UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor equals to DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.equals=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.equals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor not equals to DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.notEquals=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor not equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.notEquals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor in DEFAULT_FONT_COLOR or UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.in=" + DEFAULT_FONT_COLOR + "," + UPDATED_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.in=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor is not null
        defaultDataDictionaryShouldBeFound("fontColor.specified=true");

        // Get all the dataDictionaryList where fontColor is null
        defaultDataDictionaryShouldNotBeFound("fontColor.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor contains DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.contains=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor contains UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.contains=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor does not contain DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.doesNotContain=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor does not contain UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.doesNotContain=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor equals to DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.equals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.equals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor not equals to DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.notEquals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor not equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.notEquals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor in DEFAULT_BACKGROUND_COLOR or UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.in=" + DEFAULT_BACKGROUND_COLOR + "," + UPDATED_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.in=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor is not null
        defaultDataDictionaryShouldBeFound("backgroundColor.specified=true");

        // Get all the dataDictionaryList where backgroundColor is null
        defaultDataDictionaryShouldNotBeFound("backgroundColor.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor contains DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.contains=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor contains UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.contains=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor does not contain DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.doesNotContain=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor does not contain UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.doesNotContain=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    public void getAllDataDictionariesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);
        DataDictionary children = DataDictionaryResourceIT.createEntity();
        dataDictionary.addChildren(children);
        dataDictionaryRepository.insert(dataDictionary);
        Long childrenId = children.getId();

        // Get all the dataDictionaryList where children equals to childrenId
        defaultDataDictionaryShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the dataDictionaryList where children equals to (childrenId + 1)
        defaultDataDictionaryShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    public void getAllDataDictionariesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);
        DataDictionary parent = DataDictionaryResourceIT.createEntity();
        dataDictionary.setParent(parent);
        dataDictionaryRepository.insert(dataDictionary);
        Long parentId = parent.getId();

        // Get all the dataDictionaryList where parent equals to parentId
        defaultDataDictionaryShouldBeFound("parentId.equals=" + parentId);

        // Get all the dataDictionaryList where parent equals to (parentId + 1)
        defaultDataDictionaryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDataDictionaryShouldBeFound(String filter) throws Exception {
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataDictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));

        // Check, that the count call also returns 1
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDataDictionaryShouldNotBeFound(String filter) throws Exception {
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDataDictionary() throws Exception {
        // Get the dataDictionary
        restDataDictionaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary
        DataDictionary updatedDataDictionary = dataDictionaryRepository.findById(dataDictionary.getId()).get();

        // Disconnect from session so that the updates on updatedDataDictionary are not directly saved in db

        updatedDataDictionary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(updatedDataDictionary);

        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void putNonExistingngDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataDictionaryWithPatch() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary using partial update
        DataDictionary partialUpdatedDataDictionary = new DataDictionary();
        partialUpdatedDataDictionary.setId(dataDictionary.getId());

        partialUpdatedDataDictionary.description(UPDATED_DESCRIPTION).backgroundColor(UPDATED_BACKGROUND_COLOR);

        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataDictionary))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void fullUpdateDataDictionaryWithPatch() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary using partial update
        DataDictionary partialUpdatedDataDictionary = new DataDictionary();
        partialUpdatedDataDictionary.setId(dataDictionary.getId());

        partialUpdatedDataDictionary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .fontColor(UPDATED_FONT_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR);

        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataDictionary))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void patchNonExistingDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeDelete = dataDictionaryRepository.findAll().size();

        // Delete the dataDictionary
        restDataDictionaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataDictionary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
