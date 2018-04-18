package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.WordGroup;
import com.qigu.readword.domain.Image;
import com.qigu.readword.domain.User;
import com.qigu.readword.repository.WordGroupRepository;
import com.qigu.readword.service.WordGroupService;
import com.qigu.readword.repository.search.WordGroupSearchRepository;
import com.qigu.readword.service.dto.WordGroupDTO;
import com.qigu.readword.service.mapper.WordGroupMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.WordGroupCriteria;
import com.qigu.readword.service.WordGroupQueryService;

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
 * Test class for the WordGroupResource REST controller.
 *
 * @see WordGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class WordGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_RANK = 1D;
    private static final Double UPDATED_RANK = 2D;

    private static final LifeStatus DEFAULT_LIFE_STATUS = LifeStatus.DELETE;
    private static final LifeStatus UPDATED_LIFE_STATUS = LifeStatus.AVAILABLE;

    @Autowired
    private WordGroupRepository wordGroupRepository;

    @Autowired
    private WordGroupMapper wordGroupMapper;

    @Autowired
    private WordGroupService wordGroupService;

    @Autowired
    private WordGroupSearchRepository wordGroupSearchRepository;

    @Autowired
    private WordGroupQueryService wordGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordGroupMockMvc;

    private WordGroup wordGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WordGroupResource wordGroupResource = new WordGroupResource(wordGroupService, wordGroupQueryService);
        this.restWordGroupMockMvc = MockMvcBuilders.standaloneSetup(wordGroupResource)
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
    public static WordGroup createEntity(EntityManager em) {
        WordGroup wordGroup = new WordGroup()
            .name(DEFAULT_NAME)
            .rank(DEFAULT_RANK)
            .lifeStatus(DEFAULT_LIFE_STATUS);
        return wordGroup;
    }

    @Before
    public void initTest() {
        wordGroupSearchRepository.deleteAll();
        wordGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createWordGroup() throws Exception {
        int databaseSizeBeforeCreate = wordGroupRepository.findAll().size();

        // Create the WordGroup
        WordGroupDTO wordGroupDTO = wordGroupMapper.toDto(wordGroup);
        restWordGroupMockMvc.perform(post("/api/word-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the WordGroup in the database
        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeCreate + 1);
        WordGroup testWordGroup = wordGroupList.get(wordGroupList.size() - 1);
        assertThat(testWordGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWordGroup.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testWordGroup.getLifeStatus()).isEqualTo(DEFAULT_LIFE_STATUS);

        // Validate the WordGroup in Elasticsearch
        WordGroup wordGroupEs = wordGroupSearchRepository.findOne(testWordGroup.getId());
        assertThat(wordGroupEs).isEqualToIgnoringGivenFields(testWordGroup);
    }

    @Test
    @Transactional
    public void createWordGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordGroupRepository.findAll().size();

        // Create the WordGroup with an existing ID
        wordGroup.setId(1L);
        WordGroupDTO wordGroupDTO = wordGroupMapper.toDto(wordGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordGroupMockMvc.perform(post("/api/word-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WordGroup in the database
        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordGroupRepository.findAll().size();
        // set the field null
        wordGroup.setName(null);

        // Create the WordGroup, which fails.
        WordGroupDTO wordGroupDTO = wordGroupMapper.toDto(wordGroup);

        restWordGroupMockMvc.perform(post("/api/word-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordGroupDTO)))
            .andExpect(status().isBadRequest());

        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWordGroups() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList
        restWordGroupMockMvc.perform(get("/api/word-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getWordGroup() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get the wordGroup
        restWordGroupMockMvc.perform(get("/api/word-groups/{id}", wordGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wordGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.doubleValue()))
            .andExpect(jsonPath("$.lifeStatus").value(DEFAULT_LIFE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllWordGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where name equals to DEFAULT_NAME
        defaultWordGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the wordGroupList where name equals to UPDATED_NAME
        defaultWordGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWordGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the wordGroupList where name equals to UPDATED_NAME
        defaultWordGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where name is not null
        defaultWordGroupShouldBeFound("name.specified=true");

        // Get all the wordGroupList where name is null
        defaultWordGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordGroupsByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where rank equals to DEFAULT_RANK
        defaultWordGroupShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the wordGroupList where rank equals to UPDATED_RANK
        defaultWordGroupShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByRankIsInShouldWork() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultWordGroupShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the wordGroupList where rank equals to UPDATED_RANK
        defaultWordGroupShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where rank is not null
        defaultWordGroupShouldBeFound("rank.specified=true");

        // Get all the wordGroupList where rank is null
        defaultWordGroupShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordGroupsByLifeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where lifeStatus equals to DEFAULT_LIFE_STATUS
        defaultWordGroupShouldBeFound("lifeStatus.equals=" + DEFAULT_LIFE_STATUS);

        // Get all the wordGroupList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultWordGroupShouldNotBeFound("lifeStatus.equals=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByLifeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where lifeStatus in DEFAULT_LIFE_STATUS or UPDATED_LIFE_STATUS
        defaultWordGroupShouldBeFound("lifeStatus.in=" + DEFAULT_LIFE_STATUS + "," + UPDATED_LIFE_STATUS);

        // Get all the wordGroupList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultWordGroupShouldNotBeFound("lifeStatus.in=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllWordGroupsByLifeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);

        // Get all the wordGroupList where lifeStatus is not null
        defaultWordGroupShouldBeFound("lifeStatus.specified=true");

        // Get all the wordGroupList where lifeStatus is null
        defaultWordGroupShouldNotBeFound("lifeStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordGroupsByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        Image img = ImageResourceIntTest.createEntity(em);
        em.persist(img);
        em.flush();
        wordGroup.setImg(img);
        wordGroupRepository.saveAndFlush(wordGroup);
        Long imgId = img.getId();

        // Get all the wordGroupList where img equals to imgId
        defaultWordGroupShouldBeFound("imgId.equals=" + imgId);

        // Get all the wordGroupList where img equals to imgId + 1
        defaultWordGroupShouldNotBeFound("imgId.equals=" + (imgId + 1));
    }


    @Test
    @Transactional
    public void getAllWordGroupsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        wordGroup.setUser(user);
        wordGroupRepository.saveAndFlush(wordGroup);
        Long userId = user.getId();

        // Get all the wordGroupList where user equals to userId
        defaultWordGroupShouldBeFound("userId.equals=" + userId);

        // Get all the wordGroupList where user equals to userId + 1
        defaultWordGroupShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWordGroupShouldBeFound(String filter) throws Exception {
        restWordGroupMockMvc.perform(get("/api/word-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWordGroupShouldNotBeFound(String filter) throws Exception {
        restWordGroupMockMvc.perform(get("/api/word-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWordGroup() throws Exception {
        // Get the wordGroup
        restWordGroupMockMvc.perform(get("/api/word-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWordGroup() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);
        wordGroupSearchRepository.save(wordGroup);
        int databaseSizeBeforeUpdate = wordGroupRepository.findAll().size();

        // Update the wordGroup
        WordGroup updatedWordGroup = wordGroupRepository.findOne(wordGroup.getId());
        // Disconnect from session so that the updates on updatedWordGroup are not directly saved in db
        em.detach(updatedWordGroup);
        updatedWordGroup
            .name(UPDATED_NAME)
            .rank(UPDATED_RANK)
            .lifeStatus(UPDATED_LIFE_STATUS);
        WordGroupDTO wordGroupDTO = wordGroupMapper.toDto(updatedWordGroup);

        restWordGroupMockMvc.perform(put("/api/word-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordGroupDTO)))
            .andExpect(status().isOk());

        // Validate the WordGroup in the database
        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeUpdate);
        WordGroup testWordGroup = wordGroupList.get(wordGroupList.size() - 1);
        assertThat(testWordGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWordGroup.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testWordGroup.getLifeStatus()).isEqualTo(UPDATED_LIFE_STATUS);

        // Validate the WordGroup in Elasticsearch
        WordGroup wordGroupEs = wordGroupSearchRepository.findOne(testWordGroup.getId());
        assertThat(wordGroupEs).isEqualToIgnoringGivenFields(testWordGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingWordGroup() throws Exception {
        int databaseSizeBeforeUpdate = wordGroupRepository.findAll().size();

        // Create the WordGroup
        WordGroupDTO wordGroupDTO = wordGroupMapper.toDto(wordGroup);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWordGroupMockMvc.perform(put("/api/word-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the WordGroup in the database
        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWordGroup() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);
        wordGroupSearchRepository.save(wordGroup);
        int databaseSizeBeforeDelete = wordGroupRepository.findAll().size();

        // Get the wordGroup
        restWordGroupMockMvc.perform(delete("/api/word-groups/{id}", wordGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wordGroupExistsInEs = wordGroupSearchRepository.exists(wordGroup.getId());
        assertThat(wordGroupExistsInEs).isFalse();

        // Validate the database is empty
        List<WordGroup> wordGroupList = wordGroupRepository.findAll();
        assertThat(wordGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWordGroup() throws Exception {
        // Initialize the database
        wordGroupRepository.saveAndFlush(wordGroup);
        wordGroupSearchRepository.save(wordGroup);

        // Search the wordGroup
        restWordGroupMockMvc.perform(get("/api/_search/word-groups?query=id:" + wordGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordGroup.class);
        WordGroup wordGroup1 = new WordGroup();
        wordGroup1.setId(1L);
        WordGroup wordGroup2 = new WordGroup();
        wordGroup2.setId(wordGroup1.getId());
        assertThat(wordGroup1).isEqualTo(wordGroup2);
        wordGroup2.setId(2L);
        assertThat(wordGroup1).isNotEqualTo(wordGroup2);
        wordGroup1.setId(null);
        assertThat(wordGroup1).isNotEqualTo(wordGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordGroupDTO.class);
        WordGroupDTO wordGroupDTO1 = new WordGroupDTO();
        wordGroupDTO1.setId(1L);
        WordGroupDTO wordGroupDTO2 = new WordGroupDTO();
        assertThat(wordGroupDTO1).isNotEqualTo(wordGroupDTO2);
        wordGroupDTO2.setId(wordGroupDTO1.getId());
        assertThat(wordGroupDTO1).isEqualTo(wordGroupDTO2);
        wordGroupDTO2.setId(2L);
        assertThat(wordGroupDTO1).isNotEqualTo(wordGroupDTO2);
        wordGroupDTO1.setId(null);
        assertThat(wordGroupDTO1).isNotEqualTo(wordGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(wordGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(wordGroupMapper.fromId(null)).isNull();
    }
}
