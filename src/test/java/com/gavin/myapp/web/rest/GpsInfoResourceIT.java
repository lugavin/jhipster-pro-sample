package com.gavin.myapp.web.rest;

import static com.gavin.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavin.myapp.IntegrationTest;
import com.gavin.myapp.JhipsterApplicationApp;
import com.gavin.myapp.domain.GpsInfo;
import com.gavin.myapp.domain.enumeration.GpsType;
import com.gavin.myapp.repository.GpsInfoRepository;
import com.gavin.myapp.service.CommonConditionQueryService;
import com.gavin.myapp.service.GpsInfoQueryService;
import com.gavin.myapp.service.criteria.GpsInfoCriteria;
import com.gavin.myapp.service.dto.GpsInfoDTO;
import com.gavin.myapp.service.mapper.GpsInfoMapper;
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
 * Integration tests for the {@link GpsInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class GpsInfoResourceIT {

    private static final GpsType DEFAULT_TYPE = GpsType.BAIDU;
    private static final GpsType UPDATED_TYPE = GpsType.GOOGLE_MAP;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gps-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GpsInfoRepository gpsInfoRepository;

    @Autowired
    private GpsInfoMapper gpsInfoMapper;

    @Autowired
    private CommonConditionQueryService commonConditionQueryService;

    @Autowired
    private MockMvc restGpsInfoMockMvc;

    private GpsInfo gpsInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GpsInfo createEntity() {
        GpsInfo gpsInfo = new GpsInfo().type(DEFAULT_TYPE).latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE).address(DEFAULT_ADDRESS);
        return gpsInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GpsInfo createUpdatedEntity() {
        GpsInfo gpsInfo = new GpsInfo().type(UPDATED_TYPE).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).address(UPDATED_ADDRESS);
        return gpsInfo;
    }

    @BeforeEach
    public void initTest() {
        gpsInfo = createEntity();
    }

    @Test
    @Transactional
    void createGpsInfo() throws Exception {
        int databaseSizeBeforeCreate = gpsInfoRepository.findAll().size();
        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);
        restGpsInfoMockMvc
            .perform(
                post(ENTITY_API_URL_ID, gpsInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeCreate + 1);
        GpsInfo testGpsInfo = gpsInfoList.get(gpsInfoList.size() - 1);
        assertThat(testGpsInfo.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGpsInfo.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testGpsInfo.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testGpsInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createGpsInfoWithExistingId() throws Exception {
        // Create the GpsInfo with an existing ID
        gpsInfo.setId(1L);
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        int databaseSizeBeforeCreate = gpsInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGpsInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGpsInfos() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gpsInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    public void getGpsInfo() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get the gpsInfo
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, gpsInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gpsInfo.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    public void getGpsInfosByIdFiltering() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        Long id = gpsInfo.getId();

        defaultGpsInfoShouldBeFound("id.equals=" + id);
        defaultGpsInfoShouldNotBeFound("id.notEquals=" + id);

        defaultGpsInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGpsInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultGpsInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGpsInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGpsInfosByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where type equals to DEFAULT_TYPE
        defaultGpsInfoShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the gpsInfoList where type equals to UPDATED_TYPE
        defaultGpsInfoShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where type not equals to DEFAULT_TYPE
        defaultGpsInfoShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the gpsInfoList where type not equals to UPDATED_TYPE
        defaultGpsInfoShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultGpsInfoShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the gpsInfoList where type equals to UPDATED_TYPE
        defaultGpsInfoShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where type is not null
        defaultGpsInfoShouldBeFound("type.specified=true");

        // Get all the gpsInfoList where type is null
        defaultGpsInfoShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude equals to DEFAULT_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude equals to UPDATED_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude not equals to DEFAULT_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude not equals to UPDATED_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the gpsInfoList where latitude equals to UPDATED_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude is not null
        defaultGpsInfoShouldBeFound("latitude.specified=true");

        // Get all the gpsInfoList where latitude is null
        defaultGpsInfoShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude is less than or equal to SMALLER_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude is less than DEFAULT_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude is less than UPDATED_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where latitude is greater than DEFAULT_LATITUDE
        defaultGpsInfoShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the gpsInfoList where latitude is greater than SMALLER_LATITUDE
        defaultGpsInfoShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude equals to DEFAULT_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude equals to UPDATED_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude not equals to DEFAULT_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude not equals to UPDATED_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the gpsInfoList where longitude equals to UPDATED_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude is not null
        defaultGpsInfoShouldBeFound("longitude.specified=true");

        // Get all the gpsInfoList where longitude is null
        defaultGpsInfoShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude is less than DEFAULT_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude is less than UPDATED_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where longitude is greater than DEFAULT_LONGITUDE
        defaultGpsInfoShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the gpsInfoList where longitude is greater than SMALLER_LONGITUDE
        defaultGpsInfoShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address equals to DEFAULT_ADDRESS
        defaultGpsInfoShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the gpsInfoList where address equals to UPDATED_ADDRESS
        defaultGpsInfoShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address not equals to DEFAULT_ADDRESS
        defaultGpsInfoShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the gpsInfoList where address not equals to UPDATED_ADDRESS
        defaultGpsInfoShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultGpsInfoShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the gpsInfoList where address equals to UPDATED_ADDRESS
        defaultGpsInfoShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address is not null
        defaultGpsInfoShouldBeFound("address.specified=true");

        // Get all the gpsInfoList where address is null
        defaultGpsInfoShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressContainsSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address contains DEFAULT_ADDRESS
        defaultGpsInfoShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the gpsInfoList where address contains UPDATED_ADDRESS
        defaultGpsInfoShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGpsInfosByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        // Get all the gpsInfoList where address does not contain DEFAULT_ADDRESS
        defaultGpsInfoShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the gpsInfoList where address does not contain UPDATED_ADDRESS
        defaultGpsInfoShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGpsInfoShouldBeFound(String filter) throws Exception {
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gpsInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGpsInfoShouldNotBeFound(String filter) throws Exception {
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGpsInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGpsInfo() throws Exception {
        // Get the gpsInfo
        restGpsInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGpsInfo() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();

        // Update the gpsInfo
        GpsInfo updatedGpsInfo = gpsInfoRepository.findById(gpsInfo.getId()).get();

        // Disconnect from session so that the updates on updatedGpsInfo are not directly saved in db

        updatedGpsInfo.type(UPDATED_TYPE).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).address(UPDATED_ADDRESS);
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(updatedGpsInfo);

        restGpsInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gpsInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
        GpsInfo testGpsInfo = gpsInfoList.get(gpsInfoList.size() - 1);
        assertThat(testGpsInfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGpsInfo.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGpsInfo.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testGpsInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingngGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        restGpsInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gpsInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGpsInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGpsInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGpsInfoWithPatch() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();

        // Update the gpsInfo using partial update
        GpsInfo partialUpdatedGpsInfo = new GpsInfo();
        partialUpdatedGpsInfo.setId(gpsInfo.getId());

        partialUpdatedGpsInfo.type(UPDATED_TYPE).latitude(UPDATED_LATITUDE).address(UPDATED_ADDRESS);

        restGpsInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGpsInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGpsInfo))
            )
            .andExpect(status().isOk());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
        GpsInfo testGpsInfo = gpsInfoList.get(gpsInfoList.size() - 1);
        assertThat(testGpsInfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGpsInfo.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGpsInfo.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testGpsInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateGpsInfoWithPatch() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();

        // Update the gpsInfo using partial update
        GpsInfo partialUpdatedGpsInfo = new GpsInfo();
        partialUpdatedGpsInfo.setId(gpsInfo.getId());

        partialUpdatedGpsInfo.type(UPDATED_TYPE).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).address(UPDATED_ADDRESS);

        restGpsInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGpsInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGpsInfo))
            )
            .andExpect(status().isOk());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
        GpsInfo testGpsInfo = gpsInfoList.get(gpsInfoList.size() - 1);
        assertThat(testGpsInfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGpsInfo.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGpsInfo.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testGpsInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGpsInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gpsInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGpsInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGpsInfo() throws Exception {
        int databaseSizeBeforeUpdate = gpsInfoRepository.findAll().size();
        gpsInfo.setId(count.incrementAndGet());

        // Create the GpsInfo
        GpsInfoDTO gpsInfoDTO = gpsInfoMapper.toDto(gpsInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGpsInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gpsInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GpsInfo in the database
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGpsInfo() throws Exception {
        // Initialize the database
        gpsInfoRepository.insert(gpsInfo);

        int databaseSizeBeforeDelete = gpsInfoRepository.findAll().size();

        // Delete the gpsInfo
        restGpsInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, gpsInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GpsInfo> gpsInfoList = gpsInfoRepository.findAll();
        assertThat(gpsInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
