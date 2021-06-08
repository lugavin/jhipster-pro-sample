package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.ResourceCategory;
import com.gavin.myapp.domain.ResourceCategory;
import com.gavin.myapp.domain.UploadFile;
import com.gavin.myapp.domain.UploadImage;
import com.gavin.myapp.repository.ResourceCategoryRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.ResourceCategoryQueryService;
import com.gavin.myapp.service.criteria.ResourceCategoryCriteria;
import com.gavin.myapp.service.dto.ResourceCategoryDTO;
import com.gavin.myapp.service.mapper.ResourceCategoryMapper;
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
 * Integration tests for the {@link ResourceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class ResourceCategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT = 1;
    private static final Integer UPDATED_SORT = 2;
    private static final Integer SMALLER_SORT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/resource-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourceCategoryRepository resourceCategoryRepository;

    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restResourceCategoryMockMvc;

    private ResourceCategory resourceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceCategory createEntity() {
        ResourceCategory resourceCategory = new ResourceCategory().title(DEFAULT_TITLE).code(DEFAULT_CODE).sort(DEFAULT_SORT);
        return resourceCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceCategory createUpdatedEntity() {
        ResourceCategory resourceCategory = new ResourceCategory().title(UPDATED_TITLE).code(UPDATED_CODE).sort(UPDATED_SORT);
        return resourceCategory;
    }

    @BeforeEach
    public void initTest() {
        resourceCategory = createEntity();
    }

    @Test
    @Transactional
    void createResourceCategory() throws Exception {
        int databaseSizeBeforeCreate = resourceCategoryRepository.findAll().size();
        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);
        restResourceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceCategory testResourceCategory = resourceCategoryList.get(resourceCategoryList.size() - 1);
        assertThat(testResourceCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResourceCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testResourceCategory.getSort()).isEqualTo(DEFAULT_SORT);
    }

    @Test
    @Transactional
    void createResourceCategoryWithExistingId() throws Exception {
        // Create the ResourceCategory with an existing ID
        resourceCategory.setId(1L);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        int databaseSizeBeforeCreate = resourceCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResourceCategories() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT)));
    }

    @Test
    @Transactional
    public void getResourceCategory() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get the resourceCategory
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceCategory.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.sort").value(DEFAULT_SORT));
    }

    @Test
    @Transactional
    public void getResourceCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        Long id = resourceCategory.getId();

        defaultResourceCategoryShouldBeFound("id.equals=" + id);
        defaultResourceCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultResourceCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResourceCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultResourceCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResourceCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title equals to DEFAULT_TITLE
        defaultResourceCategoryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the resourceCategoryList where title equals to UPDATED_TITLE
        defaultResourceCategoryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title not equals to DEFAULT_TITLE
        defaultResourceCategoryShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the resourceCategoryList where title not equals to UPDATED_TITLE
        defaultResourceCategoryShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultResourceCategoryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the resourceCategoryList where title equals to UPDATED_TITLE
        defaultResourceCategoryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title is not null
        defaultResourceCategoryShouldBeFound("title.specified=true");

        // Get all the resourceCategoryList where title is null
        defaultResourceCategoryShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleContainsSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title contains DEFAULT_TITLE
        defaultResourceCategoryShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the resourceCategoryList where title contains UPDATED_TITLE
        defaultResourceCategoryShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where title does not contain DEFAULT_TITLE
        defaultResourceCategoryShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the resourceCategoryList where title does not contain UPDATED_TITLE
        defaultResourceCategoryShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code equals to DEFAULT_CODE
        defaultResourceCategoryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the resourceCategoryList where code equals to UPDATED_CODE
        defaultResourceCategoryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code not equals to DEFAULT_CODE
        defaultResourceCategoryShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the resourceCategoryList where code not equals to UPDATED_CODE
        defaultResourceCategoryShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultResourceCategoryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the resourceCategoryList where code equals to UPDATED_CODE
        defaultResourceCategoryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code is not null
        defaultResourceCategoryShouldBeFound("code.specified=true");

        // Get all the resourceCategoryList where code is null
        defaultResourceCategoryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code contains DEFAULT_CODE
        defaultResourceCategoryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the resourceCategoryList where code contains UPDATED_CODE
        defaultResourceCategoryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where code does not contain DEFAULT_CODE
        defaultResourceCategoryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the resourceCategoryList where code does not contain UPDATED_CODE
        defaultResourceCategoryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort equals to DEFAULT_SORT
        defaultResourceCategoryShouldBeFound("sort.equals=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort equals to UPDATED_SORT
        defaultResourceCategoryShouldNotBeFound("sort.equals=" + UPDATED_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort not equals to DEFAULT_SORT
        defaultResourceCategoryShouldNotBeFound("sort.notEquals=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort not equals to UPDATED_SORT
        defaultResourceCategoryShouldBeFound("sort.notEquals=" + UPDATED_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsInShouldWork() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort in DEFAULT_SORT or UPDATED_SORT
        defaultResourceCategoryShouldBeFound("sort.in=" + DEFAULT_SORT + "," + UPDATED_SORT);

        // Get all the resourceCategoryList where sort equals to UPDATED_SORT
        defaultResourceCategoryShouldNotBeFound("sort.in=" + UPDATED_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort is not null
        defaultResourceCategoryShouldBeFound("sort.specified=true");

        // Get all the resourceCategoryList where sort is null
        defaultResourceCategoryShouldNotBeFound("sort.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort is greater than or equal to DEFAULT_SORT
        defaultResourceCategoryShouldBeFound("sort.greaterThanOrEqual=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort is greater than or equal to UPDATED_SORT
        defaultResourceCategoryShouldNotBeFound("sort.greaterThanOrEqual=" + UPDATED_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort is less than or equal to DEFAULT_SORT
        defaultResourceCategoryShouldBeFound("sort.lessThanOrEqual=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort is less than or equal to SMALLER_SORT
        defaultResourceCategoryShouldNotBeFound("sort.lessThanOrEqual=" + SMALLER_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsLessThanSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort is less than DEFAULT_SORT
        defaultResourceCategoryShouldNotBeFound("sort.lessThan=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort is less than UPDATED_SORT
        defaultResourceCategoryShouldBeFound("sort.lessThan=" + UPDATED_SORT);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesBySortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        // Get all the resourceCategoryList where sort is greater than DEFAULT_SORT
        defaultResourceCategoryShouldNotBeFound("sort.greaterThan=" + DEFAULT_SORT);

        // Get all the resourceCategoryList where sort is greater than SMALLER_SORT
        defaultResourceCategoryShouldBeFound("sort.greaterThan=" + SMALLER_SORT);
    }

    @Test
    @Transactional
    public void getAllResourceCategoriesByFilesIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);
        UploadFile files = UploadFileResourceIT.createEntity();
        resourceCategory.addFiles(files);
        resourceCategoryRepository.insert(resourceCategory);
        Long filesId = files.getId();

        // Get all the resourceCategoryList where files equals to filesId
        defaultResourceCategoryShouldBeFound("filesId.equals=" + filesId);

        // Get all the resourceCategoryList where files equals to (filesId + 1)
        defaultResourceCategoryShouldNotBeFound("filesId.equals=" + (filesId + 1));
    }

    @Test
    @Transactional
    public void getAllResourceCategoriesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);
        ResourceCategory children = ResourceCategoryResourceIT.createEntity();
        resourceCategory.addChildren(children);
        resourceCategoryRepository.insert(resourceCategory);
        Long childrenId = children.getId();

        // Get all the resourceCategoryList where children equals to childrenId
        defaultResourceCategoryShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the resourceCategoryList where children equals to (childrenId + 1)
        defaultResourceCategoryShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    public void getAllResourceCategoriesByImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);
        UploadImage images = UploadImageResourceIT.createEntity();
        resourceCategory.addImages(images);
        resourceCategoryRepository.insert(resourceCategory);
        Long imagesId = images.getId();

        // Get all the resourceCategoryList where images equals to imagesId
        defaultResourceCategoryShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the resourceCategoryList where images equals to (imagesId + 1)
        defaultResourceCategoryShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }

    @Test
    @Transactional
    public void getAllResourceCategoriesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);
        ResourceCategory parent = ResourceCategoryResourceIT.createEntity();
        resourceCategory.setParent(parent);
        resourceCategoryRepository.insert(resourceCategory);
        Long parentId = parent.getId();

        // Get all the resourceCategoryList where parent equals to parentId
        defaultResourceCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the resourceCategoryList where parent equals to (parentId + 1)
        defaultResourceCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceCategoryShouldBeFound(String filter) throws Exception {
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT)));

        // Check, that the count call also returns 1
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceCategoryShouldNotBeFound(String filter) throws Exception {
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResourceCategory() throws Exception {
        // Get the resourceCategory
        restResourceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResourceCategory() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();

        // Update the resourceCategory
        ResourceCategory updatedResourceCategory = resourceCategoryRepository.findById(resourceCategory.getId()).get();

        // Disconnect from session so that the updates on updatedResourceCategory are not directly saved in db

        updatedResourceCategory.title(UPDATED_TITLE).code(UPDATED_CODE).sort(UPDATED_SORT);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(updatedResourceCategory);

        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ResourceCategory testResourceCategory = resourceCategoryList.get(resourceCategoryList.size() - 1);
        assertThat(testResourceCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResourceCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testResourceCategory.getSort()).isEqualTo(UPDATED_SORT);
    }

    @Test
    @Transactional
    void putNonExistingngResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceCategoryWithPatch() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();

        // Update the resourceCategory using partial update
        ResourceCategory partialUpdatedResourceCategory = new ResourceCategory();
        partialUpdatedResourceCategory.setId(resourceCategory.getId());

        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ResourceCategory testResourceCategory = resourceCategoryList.get(resourceCategoryList.size() - 1);
        assertThat(testResourceCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResourceCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testResourceCategory.getSort()).isEqualTo(DEFAULT_SORT);
    }

    @Test
    @Transactional
    void fullUpdateResourceCategoryWithPatch() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();

        // Update the resourceCategory using partial update
        ResourceCategory partialUpdatedResourceCategory = new ResourceCategory();
        partialUpdatedResourceCategory.setId(resourceCategory.getId());

        partialUpdatedResourceCategory.title(UPDATED_TITLE).code(UPDATED_CODE).sort(UPDATED_SORT);

        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ResourceCategory testResourceCategory = resourceCategoryList.get(resourceCategoryList.size() - 1);
        assertThat(testResourceCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResourceCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testResourceCategory.getSort()).isEqualTo(UPDATED_SORT);
    }

    @Test
    @Transactional
    void patchNonExistingResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourceCategory() throws Exception {
        int databaseSizeBeforeUpdate = resourceCategoryRepository.findAll().size();
        resourceCategory.setId(count.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceCategory in the database
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourceCategory() throws Exception {
        // Initialize the database
        resourceCategoryRepository.insert(resourceCategory);

        int databaseSizeBeforeDelete = resourceCategoryRepository.findAll().size();

        // Delete the resourceCategory
        restResourceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAll();
        assertThat(resourceCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
