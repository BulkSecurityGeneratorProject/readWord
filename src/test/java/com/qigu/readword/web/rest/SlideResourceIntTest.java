package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.Slide;
import com.qigu.readword.domain.Image;
import com.qigu.readword.repository.SlideRepository;
import com.qigu.readword.service.SlideService;
import com.qigu.readword.repository.search.SlideSearchRepository;
import com.qigu.readword.service.dto.SlideDTO;
import com.qigu.readword.service.mapper.SlideMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.SlideCriteria;
import com.qigu.readword.service.SlideQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.qigu.readword.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qigu.readword.domain.enumeration.LifeStatus;
/**
 * Test class for the SlideResource REST controller.
 *
 * @see SlideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class SlideResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_RANK = 1D;
    private static final Double UPDATED_RANK = 2D;

    private static final LifeStatus DEFAULT_LIFE_STATUS = LifeStatus.DELETE;
    private static final LifeStatus UPDATED_LIFE_STATUS = LifeStatus.AVAILABLE;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private SlideMapper slideMapper;

    @Autowired
    private SlideService slideService;

    @Autowired
    private SlideSearchRepository slideSearchRepository;

    @Autowired
    private SlideQueryService slideQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSlideMockMvc;

    private Slide slide;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlideResource slideResource = new SlideResource(slideService, slideQueryService);
        this.restSlideMockMvc = MockMvcBuilders.standaloneSetup(slideResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slide createEntity(EntityManager em) {
        Slide slide = new Slide()
            .name(DEFAULT_NAME)
            .rank(DEFAULT_RANK)
            .lifeStatus(DEFAULT_LIFE_STATUS);
        return slide;
    }

    @Before
    public void initTest() {
        slideSearchRepository.deleteAll();
        slide = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlide() throws Exception {
        int databaseSizeBeforeCreate = slideRepository.findAll().size();

        // Create the Slide
        SlideDTO slideDTO = slideMapper.toDto(slide);
        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slideDTO)))
            .andExpect(status().isCreated());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeCreate + 1);
        Slide testSlide = slideList.get(slideList.size() - 1);
        assertThat(testSlide.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSlide.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testSlide.getLifeStatus()).isEqualTo(DEFAULT_LIFE_STATUS);

        // Validate the Slide in Elasticsearch
        Slide slideEs = slideSearchRepository.findOne(testSlide.getId());
        assertThat(slideEs).isEqualToIgnoringGivenFields(testSlide);
    }

    @Test
    @Transactional
    public void createSlideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slideRepository.findAll().size();

        // Create the Slide with an existing ID
        slide.setId(1L);
        SlideDTO slideDTO = slideMapper.toDto(slide);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = slideRepository.findAll().size();
        // set the field null
        slide.setName(null);

        // Create the Slide, which fails.
        SlideDTO slideDTO = slideMapper.toDto(slide);

        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slideDTO)))
            .andExpect(status().isBadRequest());

        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSlides() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList
        restSlideMockMvc.perform(get("/api/slides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slide.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get the slide
        restSlideMockMvc.perform(get("/api/slides/{id}", slide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slide.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.doubleValue()))
            .andExpect(jsonPath("$.lifeStatus").value(DEFAULT_LIFE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllSlidesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where name equals to DEFAULT_NAME
        defaultSlideShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the slideList where name equals to UPDATED_NAME
        defaultSlideShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSlidesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSlideShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the slideList where name equals to UPDATED_NAME
        defaultSlideShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSlidesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where name is not null
        defaultSlideShouldBeFound("name.specified=true");

        // Get all the slideList where name is null
        defaultSlideShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlidesByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where rank equals to DEFAULT_RANK
        defaultSlideShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the slideList where rank equals to UPDATED_RANK
        defaultSlideShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllSlidesByRankIsInShouldWork() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultSlideShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the slideList where rank equals to UPDATED_RANK
        defaultSlideShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllSlidesByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where rank is not null
        defaultSlideShouldBeFound("rank.specified=true");

        // Get all the slideList where rank is null
        defaultSlideShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlidesByLifeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where lifeStatus equals to DEFAULT_LIFE_STATUS
        defaultSlideShouldBeFound("lifeStatus.equals=" + DEFAULT_LIFE_STATUS);

        // Get all the slideList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultSlideShouldNotBeFound("lifeStatus.equals=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllSlidesByLifeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where lifeStatus in DEFAULT_LIFE_STATUS or UPDATED_LIFE_STATUS
        defaultSlideShouldBeFound("lifeStatus.in=" + DEFAULT_LIFE_STATUS + "," + UPDATED_LIFE_STATUS);

        // Get all the slideList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultSlideShouldNotBeFound("lifeStatus.in=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllSlidesByLifeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList where lifeStatus is not null
        defaultSlideShouldBeFound("lifeStatus.specified=true");

        // Get all the slideList where lifeStatus is null
        defaultSlideShouldNotBeFound("lifeStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlidesByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        Image img = ImageResourceIntTest.createEntity(em);
        em.persist(img);
        em.flush();
        slide.setImg(img);
        slideRepository.saveAndFlush(slide);
        Long imgId = img.getId();

        // Get all the slideList where img equals to imgId
        defaultSlideShouldBeFound("imgId.equals=" + imgId);

        // Get all the slideList where img equals to imgId + 1
        defaultSlideShouldNotBeFound("imgId.equals=" + (imgId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSlideShouldBeFound(String filter) throws Exception {
        restSlideMockMvc.perform(get("/api/slides?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slide.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSlideShouldNotBeFound(String filter) throws Exception {
        restSlideMockMvc.perform(get("/api/slides?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSlide() throws Exception {
        // Get the slide
        restSlideMockMvc.perform(get("/api/slides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);
        slideSearchRepository.save(slide);
        int databaseSizeBeforeUpdate = slideRepository.findAll().size();

        // Update the slide
        Slide updatedSlide = slideRepository.findOne(slide.getId());
        // Disconnect from session so that the updates on updatedSlide are not directly saved in db
        em.detach(updatedSlide);
        updatedSlide
            .name(UPDATED_NAME)
            .rank(UPDATED_RANK)
            .lifeStatus(UPDATED_LIFE_STATUS);
        SlideDTO slideDTO = slideMapper.toDto(updatedSlide);

        restSlideMockMvc.perform(put("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slideDTO)))
            .andExpect(status().isOk());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeUpdate);
        Slide testSlide = slideList.get(slideList.size() - 1);
        assertThat(testSlide.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSlide.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testSlide.getLifeStatus()).isEqualTo(UPDATED_LIFE_STATUS);

        // Validate the Slide in Elasticsearch
        Slide slideEs = slideSearchRepository.findOne(testSlide.getId());
        assertThat(slideEs).isEqualToIgnoringGivenFields(testSlide);
    }

    @Test
    @Transactional
    public void updateNonExistingSlide() throws Exception {
        int databaseSizeBeforeUpdate = slideRepository.findAll().size();

        // Create the Slide
        SlideDTO slideDTO = slideMapper.toDto(slide);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSlideMockMvc.perform(put("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slideDTO)))
            .andExpect(status().isCreated());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);
        slideSearchRepository.save(slide);
        int databaseSizeBeforeDelete = slideRepository.findAll().size();

        // Get the slide
        restSlideMockMvc.perform(delete("/api/slides/{id}", slide.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean slideExistsInEs = slideSearchRepository.exists(slide.getId());
        assertThat(slideExistsInEs).isFalse();

        // Validate the database is empty
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);
        slideSearchRepository.save(slide);

        // Search the slide
        restSlideMockMvc.perform(get("/api/_search/slides?query=id:" + slide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slide.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slide.class);
        Slide slide1 = new Slide();
        slide1.setId(1L);
        Slide slide2 = new Slide();
        slide2.setId(slide1.getId());
        assertThat(slide1).isEqualTo(slide2);
        slide2.setId(2L);
        assertThat(slide1).isNotEqualTo(slide2);
        slide1.setId(null);
        assertThat(slide1).isNotEqualTo(slide2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlideDTO.class);
        SlideDTO slideDTO1 = new SlideDTO();
        slideDTO1.setId(1L);
        SlideDTO slideDTO2 = new SlideDTO();
        assertThat(slideDTO1).isNotEqualTo(slideDTO2);
        slideDTO2.setId(slideDTO1.getId());
        assertThat(slideDTO1).isEqualTo(slideDTO2);
        slideDTO2.setId(2L);
        assertThat(slideDTO1).isNotEqualTo(slideDTO2);
        slideDTO1.setId(null);
        assertThat(slideDTO1).isNotEqualTo(slideDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(slideMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(slideMapper.fromId(null)).isNull();
    }
}
