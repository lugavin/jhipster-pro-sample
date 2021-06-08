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
import com.gavin.myapp.domain.CommonCondition;
import com.gavin.myapp.domain.CommonConditionItem;
import com.gavin.myapp.domain.CommonTable;
import com.gavin.myapp.repository.CommonConditionRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.criteria.CommonConditionCriteria;
import com.gavin.myapp.service.dto.CommonConditionDTO;
import com.gavin.myapp.service.mapper.CommonConditionMapper;
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
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link CommonConditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonConditionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/common-conditions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonConditionRepository commonConditionRepository;

    @Autowired
    private CommonConditionMapper commonConditionMapper;

    @Autowired
    private MockMvc restCommonConditionMockMvc;

    private CommonCondition commonCondition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonCondition createEntity() {
        CommonCondition commonCondition = new CommonCondition()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME);
        return commonCondition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonCondition createUpdatedEntity() {
        CommonCondition commonCondition = new CommonCondition()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        return commonCondition;
    }

    @BeforeEach
    public void initTest() {
        commonCondition = createEntity();
    }

    @Test
    @Transactional
    void createCommonCondition() throws Exception {
        int databaseSizeBeforeCreate = commonConditionRepository.findAll().size();
        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);
        restCommonConditionMockMvc
            .perform(
                post(ENTITY_API_URL_ID, commonConditionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeCreate + 1);
        CommonCondition testCommonCondition = commonConditionList.get(commonConditionList.size() - 1);
        assertThat(testCommonCondition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonCondition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommonCondition.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void createCommonConditionWithExistingId() throws Exception {
        // Create the CommonCondition with an existing ID
        commonCondition.setId(1L);
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        int databaseSizeBeforeCreate = commonConditionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonConditionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonConditionRepository.findAll().size();
        // set the field null
        commonCondition.setName(null);

        // Create the CommonCondition, which fails.
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        restCommonConditionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonConditions() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_TIME))));
    }

    @Test
    @Transactional
    public void getCommonCondition() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get the commonCondition
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL_ID, commonCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonCondition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.lastModifiedTime").value(sameInstant(DEFAULT_LAST_MODIFIED_TIME)));
    }

    @Test
    @Transactional
    public void getCommonConditionsByIdFiltering() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        Long id = commonCondition.getId();

        defaultCommonConditionShouldBeFound("id.equals=" + id);
        defaultCommonConditionShouldNotBeFound("id.notEquals=" + id);

        defaultCommonConditionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonConditionShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonConditionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonConditionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name equals to DEFAULT_NAME
        defaultCommonConditionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the commonConditionList where name equals to UPDATED_NAME
        defaultCommonConditionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name not equals to DEFAULT_NAME
        defaultCommonConditionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the commonConditionList where name not equals to UPDATED_NAME
        defaultCommonConditionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCommonConditionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the commonConditionList where name equals to UPDATED_NAME
        defaultCommonConditionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name is not null
        defaultCommonConditionShouldBeFound("name.specified=true");

        // Get all the commonConditionList where name is null
        defaultCommonConditionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameContainsSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name contains DEFAULT_NAME
        defaultCommonConditionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the commonConditionList where name contains UPDATED_NAME
        defaultCommonConditionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where name does not contain DEFAULT_NAME
        defaultCommonConditionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the commonConditionList where name does not contain UPDATED_NAME
        defaultCommonConditionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description equals to DEFAULT_DESCRIPTION
        defaultCommonConditionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the commonConditionList where description equals to UPDATED_DESCRIPTION
        defaultCommonConditionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description not equals to DEFAULT_DESCRIPTION
        defaultCommonConditionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the commonConditionList where description not equals to UPDATED_DESCRIPTION
        defaultCommonConditionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCommonConditionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the commonConditionList where description equals to UPDATED_DESCRIPTION
        defaultCommonConditionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description is not null
        defaultCommonConditionShouldBeFound("description.specified=true");

        // Get all the commonConditionList where description is null
        defaultCommonConditionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description contains DEFAULT_DESCRIPTION
        defaultCommonConditionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the commonConditionList where description contains UPDATED_DESCRIPTION
        defaultCommonConditionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where description does not contain DEFAULT_DESCRIPTION
        defaultCommonConditionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the commonConditionList where description does not contain UPDATED_DESCRIPTION
        defaultCommonConditionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime equals to DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.equals=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime equals to UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.equals=" + UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime not equals to DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.notEquals=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime not equals to UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.notEquals=" + UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime in DEFAULT_LAST_MODIFIED_TIME or UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.in=" + DEFAULT_LAST_MODIFIED_TIME + "," + UPDATED_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime equals to UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.in=" + UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime is not null
        defaultCommonConditionShouldBeFound("lastModifiedTime.specified=true");

        // Get all the commonConditionList where lastModifiedTime is null
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime is greater than or equal to DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime is greater than or equal to UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime is less than or equal to DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime is less than or equal to SMALLER_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime is less than DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.lessThan=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime is less than UPDATED_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.lessThan=" + UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void getAllCommonConditionsByLastModifiedTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        // Get all the commonConditionList where lastModifiedTime is greater than DEFAULT_LAST_MODIFIED_TIME
        defaultCommonConditionShouldNotBeFound("lastModifiedTime.greaterThan=" + DEFAULT_LAST_MODIFIED_TIME);

        // Get all the commonConditionList where lastModifiedTime is greater than SMALLER_LAST_MODIFIED_TIME
        defaultCommonConditionShouldBeFound("lastModifiedTime.greaterThan=" + SMALLER_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void getAllCommonConditionsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);
        CommonConditionItem items = CommonConditionItemResourceIT.createEntity();
        commonCondition.addItems(items);
        commonConditionRepository.insert(commonCondition);
        Long itemsId = items.getId();

        // Get all the commonConditionList where items equals to itemsId
        defaultCommonConditionShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the commonConditionList where items equals to (itemsId + 1)
        defaultCommonConditionShouldNotBeFound("itemsId.equals=" + (itemsId + 1));
    }

    @Test
    @Transactional
    public void getAllCommonConditionsByCommonTableIsEqualToSomething() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);
        CommonTable commonTable = CommonTableResourceIT.createEntity();
        commonCondition.setCommonTable(commonTable);
        commonConditionRepository.insert(commonCondition);
        Long commonTableId = commonTable.getId();

        // Get all the commonConditionList where commonTable equals to commonTableId
        defaultCommonConditionShouldBeFound("commonTableId.equals=" + commonTableId);

        // Get all the commonConditionList where commonTable equals to (commonTableId + 1)
        defaultCommonConditionShouldNotBeFound("commonTableId.equals=" + (commonTableId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonConditionShouldBeFound(String filter) throws Exception {
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_TIME))));

        // Check, that the count call also returns 1
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonConditionShouldNotBeFound(String filter) throws Exception {
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonCondition() throws Exception {
        // Get the commonCondition
        restCommonConditionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonCondition() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();

        // Update the commonCondition
        CommonCondition updatedCommonCondition = commonConditionRepository.findById(commonCondition.getId()).get();

        // Disconnect from session so that the updates on updatedCommonCondition are not directly saved in db

        updatedCommonCondition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(updatedCommonCondition);

        restCommonConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonConditionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
        CommonCondition testCommonCondition = commonConditionList.get(commonConditionList.size() - 1);
        assertThat(testCommonCondition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonCondition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonCondition.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingngCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        restCommonConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonConditionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonConditionWithPatch() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();

        // Update the commonCondition using partial update
        CommonCondition partialUpdatedCommonCondition = new CommonCondition();
        partialUpdatedCommonCondition.setId(commonCondition.getId());

        partialUpdatedCommonCondition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCommonConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonCondition))
            )
            .andExpect(status().isOk());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
        CommonCondition testCommonCondition = commonConditionList.get(commonConditionList.size() - 1);
        assertThat(testCommonCondition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonCondition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonCondition.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateCommonConditionWithPatch() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();

        // Update the commonCondition using partial update
        CommonCondition partialUpdatedCommonCondition = new CommonCondition();
        partialUpdatedCommonCondition.setId(commonCondition.getId());

        partialUpdatedCommonCondition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);

        restCommonConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonCondition))
            )
            .andExpect(status().isOk());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
        CommonCondition testCommonCondition = commonConditionList.get(commonConditionList.size() - 1);
        assertThat(testCommonCondition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonCondition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonCondition.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonConditionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonCondition() throws Exception {
        int databaseSizeBeforeUpdate = commonConditionRepository.findAll().size();
        commonCondition.setId(count.incrementAndGet());

        // Create the CommonCondition
        CommonConditionDTO commonConditionDTO = commonConditionMapper.toDto(commonCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonConditionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonConditionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonCondition in the database
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonCondition() throws Exception {
        // Initialize the database
        commonConditionRepository.insert(commonCondition);

        int databaseSizeBeforeDelete = commonConditionRepository.findAll().size();

        // Delete the commonCondition
        restCommonConditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonCondition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonCondition> commonConditionList = commonConditionRepository.findAll();
        assertThat(commonConditionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
