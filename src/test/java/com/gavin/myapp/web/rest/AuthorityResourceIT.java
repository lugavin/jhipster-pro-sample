package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.Authority;
import com.gavin.myapp.domain.Authority;
import com.gavin.myapp.domain.User;
import com.gavin.myapp.domain.ViewPermission;
import com.gavin.myapp.repository.AuthorityRepository;
import com.gavin.myapp.service.AuthorityQueryService;
import com.gavin.myapp.service.AuthorityService;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.dto.AuthorityCriteria;
import com.gavin.myapp.service.dto.AuthorityDTO;
import com.gavin.myapp.service.mapper.AuthorityMapper;
import com.gavin.myapp.web.rest.errors.ExceptionTranslator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link AuthorityResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApplicationApp.class)
public class AuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Boolean DEFAULT_DISPLAY = false;
    private static final Boolean UPDATED_DISPLAY = true;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Mock
    private AuthorityRepository authorityRepositoryMock;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Mock
    private AuthorityService authorityServiceMock;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private AuthorityQueryService authorityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Autowired
    private MockMvc restAuthorityMockMvc;

    private Authority authority;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorityResource authorityResource = new AuthorityResource(
            authorityService,
            commonConditionQueryService,
            authorityQueryService
        );
        this.restAuthorityMockMvc =
            MockMvcBuilders
                .standaloneSetup(authorityResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator)
                .build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createEntity() {
        Authority authority = new Authority()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .info(DEFAULT_INFO)
            .order(DEFAULT_ORDER)
            .display(DEFAULT_DISPLAY);
        return authority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createUpdatedEntity() {
        Authority authority = new Authority()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .info(UPDATED_INFO)
            .order(UPDATED_ORDER)
            .display(UPDATED_DISPLAY);
        return authority;
    }

    @BeforeEach
    public void initTest() {
        authority = createEntity();
    }

    @Test
    @Transactional
    public void createAuthority() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);
        restAuthorityMockMvc
            .perform(
                post("/api/authorities").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate + 1);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthority.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAuthority.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testAuthority.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testAuthority.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
    }

    @Test
    @Transactional
    public void createAuthorityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority with an existing ID
        authority.setId(1L);
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityMockMvc
            .perform(
                post("/api/authorities").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAuthorities() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList
        restAuthorityMockMvc
            .perform(get("/api/authorities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllAuthoritiesWithEagerRelationshipsIsEnabled() throws Exception {
        AuthorityResource authorityResource = new AuthorityResource(
            authorityServiceMock,
            commonConditionQueryService,
            authorityQueryService
        );
        when(authorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        MockMvc restAuthorityMockMvc = MockMvcBuilders
            .standaloneSetup(authorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

        restAuthorityMockMvc.perform(get("/api/authorities?eagerload=true")).andExpect(status().isOk());

        verify(authorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllAuthoritiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        AuthorityResource authorityResource = new AuthorityResource(
            authorityServiceMock,
            commonConditionQueryService,
            authorityQueryService
        );
        when(authorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new Page().setRecords(new ArrayList<>()));
        MockMvc restAuthorityMockMvc = MockMvcBuilders
            .standaloneSetup(authorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

        restAuthorityMockMvc.perform(get("/api/authorities?eagerload=true")).andExpect(status().isOk());

        verify(authorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAuthority() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get the authority
        restAuthorityMockMvc
            .perform(get("/api/authorities/{id}", authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY.booleanValue()));
    }

    @Test
    @Transactional
    public void getAuthoritiesByIdFiltering() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        Long id = authority.getId();

        defaultAuthorityShouldBeFound("id.equals=" + id);
        defaultAuthorityShouldNotBeFound("id.notEquals=" + id);

        defaultAuthorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuthorityShouldNotBeFound("id.greaterThan=" + id);

        defaultAuthorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuthorityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name equals to DEFAULT_NAME
        defaultAuthorityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the authorityList where name equals to UPDATED_NAME
        defaultAuthorityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name not equals to DEFAULT_NAME
        defaultAuthorityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the authorityList where name not equals to UPDATED_NAME
        defaultAuthorityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAuthorityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the authorityList where name equals to UPDATED_NAME
        defaultAuthorityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name is not null
        defaultAuthorityShouldBeFound("name.specified=true");

        // Get all the authorityList where name is null
        defaultAuthorityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name contains DEFAULT_NAME
        defaultAuthorityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the authorityList where name contains UPDATED_NAME
        defaultAuthorityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where name does not contain DEFAULT_NAME
        defaultAuthorityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the authorityList where name does not contain UPDATED_NAME
        defaultAuthorityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code equals to DEFAULT_CODE
        defaultAuthorityShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the authorityList where code equals to UPDATED_CODE
        defaultAuthorityShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code not equals to DEFAULT_CODE
        defaultAuthorityShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the authorityList where code not equals to UPDATED_CODE
        defaultAuthorityShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAuthorityShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the authorityList where code equals to UPDATED_CODE
        defaultAuthorityShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code is not null
        defaultAuthorityShouldBeFound("code.specified=true");

        // Get all the authorityList where code is null
        defaultAuthorityShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code contains DEFAULT_CODE
        defaultAuthorityShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the authorityList where code contains UPDATED_CODE
        defaultAuthorityShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where code does not contain DEFAULT_CODE
        defaultAuthorityShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the authorityList where code does not contain UPDATED_CODE
        defaultAuthorityShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info equals to DEFAULT_INFO
        defaultAuthorityShouldBeFound("info.equals=" + DEFAULT_INFO);

        // Get all the authorityList where info equals to UPDATED_INFO
        defaultAuthorityShouldNotBeFound("info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info not equals to DEFAULT_INFO
        defaultAuthorityShouldNotBeFound("info.notEquals=" + DEFAULT_INFO);

        // Get all the authorityList where info not equals to UPDATED_INFO
        defaultAuthorityShouldBeFound("info.notEquals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info in DEFAULT_INFO or UPDATED_INFO
        defaultAuthorityShouldBeFound("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO);

        // Get all the authorityList where info equals to UPDATED_INFO
        defaultAuthorityShouldNotBeFound("info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info is not null
        defaultAuthorityShouldBeFound("info.specified=true");

        // Get all the authorityList where info is null
        defaultAuthorityShouldNotBeFound("info.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info contains DEFAULT_INFO
        defaultAuthorityShouldBeFound("info.contains=" + DEFAULT_INFO);

        // Get all the authorityList where info contains UPDATED_INFO
        defaultAuthorityShouldNotBeFound("info.contains=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByInfoNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where info does not contain DEFAULT_INFO
        defaultAuthorityShouldNotBeFound("info.doesNotContain=" + DEFAULT_INFO);

        // Get all the authorityList where info does not contain UPDATED_INFO
        defaultAuthorityShouldBeFound("info.doesNotContain=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order equals to DEFAULT_ORDER
        defaultAuthorityShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the authorityList where order equals to UPDATED_ORDER
        defaultAuthorityShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order not equals to DEFAULT_ORDER
        defaultAuthorityShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the authorityList where order not equals to UPDATED_ORDER
        defaultAuthorityShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultAuthorityShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the authorityList where order equals to UPDATED_ORDER
        defaultAuthorityShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order is not null
        defaultAuthorityShouldBeFound("order.specified=true");

        // Get all the authorityList where order is null
        defaultAuthorityShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order is greater than or equal to DEFAULT_ORDER
        defaultAuthorityShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the authorityList where order is greater than or equal to UPDATED_ORDER
        defaultAuthorityShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order is less than or equal to DEFAULT_ORDER
        defaultAuthorityShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the authorityList where order is less than or equal to SMALLER_ORDER
        defaultAuthorityShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order is less than DEFAULT_ORDER
        defaultAuthorityShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the authorityList where order is less than UPDATED_ORDER
        defaultAuthorityShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where order is greater than DEFAULT_ORDER
        defaultAuthorityShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the authorityList where order is greater than SMALLER_ORDER
        defaultAuthorityShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByDisplayIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where display equals to DEFAULT_DISPLAY
        defaultAuthorityShouldBeFound("display.equals=" + DEFAULT_DISPLAY);

        // Get all the authorityList where display equals to UPDATED_DISPLAY
        defaultAuthorityShouldNotBeFound("display.equals=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByDisplayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where display not equals to DEFAULT_DISPLAY
        defaultAuthorityShouldNotBeFound("display.notEquals=" + DEFAULT_DISPLAY);

        // Get all the authorityList where display not equals to UPDATED_DISPLAY
        defaultAuthorityShouldBeFound("display.notEquals=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByDisplayIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where display in DEFAULT_DISPLAY or UPDATED_DISPLAY
        defaultAuthorityShouldBeFound("display.in=" + DEFAULT_DISPLAY + "," + UPDATED_DISPLAY);

        // Get all the authorityList where display equals to UPDATED_DISPLAY
        defaultAuthorityShouldNotBeFound("display.in=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByDisplayIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        // Get all the authorityList where display is not null
        defaultAuthorityShouldBeFound("display.specified=true");

        // Get all the authorityList where display is null
        defaultAuthorityShouldNotBeFound("display.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);
        Authority children = AuthorityResourceIT.createEntity();
        authority.addChildren(children);
        authorityRepository.insert(authority);
        Long childrenId = children.getId();

        // Get all the authorityList where children equals to childrenId
        defaultAuthorityShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the authorityList where children equals to childrenId + 1
        defaultAuthorityShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);
        User users = UserResourceIT.createEntity();
        authority.addUsers(users);
        authorityRepository.insert(authority);
        Long usersId = users.getId();

        // Get all the authorityList where users equals to usersId
        defaultAuthorityShouldBeFound("usersId.equals=" + usersId);

        // Get all the authorityList where users equals to usersId + 1
        defaultAuthorityShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByViewPermissionIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);
        ViewPermission viewPermission = ViewPermissionResourceIT.createEntity();
        authority.addViewPermissions(viewPermission);
        authorityRepository.insert(authority);
        Long viewPermissionId = viewPermission.getId();

        // Get all the authorityList where viewPermission equals to viewPermissionId
        defaultAuthorityShouldBeFound("viewPermissionId.equals=" + viewPermissionId);

        // Get all the authorityList where viewPermission equals to viewPermissionId + 1
        defaultAuthorityShouldNotBeFound("viewPermissionId.equals=" + (viewPermissionId + 1));
    }

    @Test
    @Transactional
    public void getAllAuthoritiesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);
        Authority parent = AuthorityResourceIT.createEntity();
        authority.setParent(parent);
        authorityRepository.insert(authority);
        Long parentId = parent.getId();

        // Get all the authorityList where parent equals to parentId
        defaultAuthorityShouldBeFound("parentId.equals=" + parentId);

        // Get all the authorityList where parent equals to parentId + 1
        defaultAuthorityShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuthorityShouldBeFound(String filter) throws Exception {
        restAuthorityMockMvc
            .perform(get("/api/authorities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY.booleanValue())));

        // Check, that the count call also returns 1
        restAuthorityMockMvc
            .perform(get("/api/authorities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuthorityShouldNotBeFound(String filter) throws Exception {
        restAuthorityMockMvc
            .perform(get("/api/authorities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuthorityMockMvc
            .perform(get("/api/authorities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuthority() throws Exception {
        // Get the authority
        restAuthorityMockMvc.perform(get("/api/authorities/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthority() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Update the authority
        Authority updatedAuthority = authorityRepository.findById(authority.getId()).get();
        // Disconnect from session so that the updates on updatedAuthority are not directly saved in db
        updatedAuthority.name(UPDATED_NAME).code(UPDATED_CODE).info(UPDATED_INFO).order(UPDATED_ORDER).display(UPDATED_DISPLAY);
        AuthorityDTO authorityDTO = authorityMapper.toDto(updatedAuthority);

        restAuthorityMockMvc
            .perform(
                put("/api/authorities").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthority.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAuthority.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testAuthority.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testAuthority.getDisplay()).isEqualTo(UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthority() throws Exception {
        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(
                put("/api/authorities").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuthority() throws Exception {
        // Initialize the database
        authorityRepository.insert(authority);

        int databaseSizeBeforeDelete = authorityRepository.findAll().size();

        // Delete the authority
        restAuthorityMockMvc
            .perform(delete("/api/authorities/{id}", authority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
