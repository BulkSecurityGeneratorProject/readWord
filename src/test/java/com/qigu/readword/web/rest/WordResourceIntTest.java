package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.Word;
import com.qigu.readword.domain.Image;
import com.qigu.readword.domain.Audio;
import com.qigu.readword.domain.User;
import com.qigu.readword.domain.WordGroup;
import com.qigu.readword.domain.Favorite;
import com.qigu.readword.repository.WordRepository;
import com.qigu.readword.service.WordService;
import com.qigu.readword.repository.search.WordSearchRepository;
import com.qigu.readword.service.dto.WordDTO;
import com.qigu.readword.service.mapper.WordMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.WordCriteria;
import com.qigu.readword.service.WordQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.qigu.readword.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qigu.readword.domain.enumeration.LifeStatus;
/**
 * Test class for the WordResource REST controller.
 *
 * @see WordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class WordResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_RANK = 1D;
    private static final Double UPDATED_RANK = 2D;

    private static final String DEFAULT_DESCTRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCTRIPTION = "BBBBBBBBBB";

    private static final LifeStatus DEFAULT_LIFE_STATUS = LifeStatus.DELETE;
    private static final LifeStatus UPDATED_LIFE_STATUS = LifeStatus.AVAILABLE;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordService wordService;

    @Autowired
    private WordSearchRepository wordSearchRepository;

    @Autowired
    private WordQueryService wordQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordMockMvc;

    private Word word;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WordResource wordResource = new WordResource(wordService, wordQueryService);
        this.restWordMockMvc = MockMvcBuilders.standaloneSetup(wordResource)
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
    public static Word createEntity(EntityManager em) {
        Word word = new Word()
            .name(DEFAULT_NAME)
            .rank(DEFAULT_RANK)
            .desctription(DEFAULT_DESCTRIPTION)
            .lifeStatus(DEFAULT_LIFE_STATUS);
        return word;
    }

    @Before
    public void initTest() {
        wordSearchRepository.deleteAll();
        word = createEntity(em);
    }

    @Test
    @Transactional
    public void createWord() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // Create the Word
        WordDTO wordDTO = wordMapper.toDto(word);
        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate + 1);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWord.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testWord.getDesctription()).isEqualTo(DEFAULT_DESCTRIPTION);
        assertThat(testWord.getLifeStatus()).isEqualTo(DEFAULT_LIFE_STATUS);

        // Validate the Word in Elasticsearch
        Word wordEs = wordSearchRepository.findOne(testWord.getId());
        assertThat(wordEs).isEqualToIgnoringGivenFields(testWord);
    }

    @Test
    @Transactional
    public void createWordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // Create the Word with an existing ID
        word.setId(1L);
        WordDTO wordDTO = wordMapper.toDto(word);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordRepository.findAll().size();
        // set the field null
        word.setName(null);

        // Create the Word, which fails.
        WordDTO wordDTO = wordMapper.toDto(word);

        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWords() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList
        restWordMockMvc.perform(get("/api/words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(word.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.doubleValue()))
            .andExpect(jsonPath("$.desctription").value(DEFAULT_DESCTRIPTION.toString()))
            .andExpect(jsonPath("$.lifeStatus").value(DEFAULT_LIFE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllWordsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where name equals to DEFAULT_NAME
        defaultWordShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the wordList where name equals to UPDATED_NAME
        defaultWordShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWordShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the wordList where name equals to UPDATED_NAME
        defaultWordShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where name is not null
        defaultWordShouldBeFound("name.specified=true");

        // Get all the wordList where name is null
        defaultWordShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where rank equals to DEFAULT_RANK
        defaultWordShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the wordList where rank equals to UPDATED_RANK
        defaultWordShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllWordsByRankIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultWordShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the wordList where rank equals to UPDATED_RANK
        defaultWordShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllWordsByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where rank is not null
        defaultWordShouldBeFound("rank.specified=true");

        // Get all the wordList where rank is null
        defaultWordShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByLifeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where lifeStatus equals to DEFAULT_LIFE_STATUS
        defaultWordShouldBeFound("lifeStatus.equals=" + DEFAULT_LIFE_STATUS);

        // Get all the wordList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultWordShouldNotBeFound("lifeStatus.equals=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllWordsByLifeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where lifeStatus in DEFAULT_LIFE_STATUS or UPDATED_LIFE_STATUS
        defaultWordShouldBeFound("lifeStatus.in=" + DEFAULT_LIFE_STATUS + "," + UPDATED_LIFE_STATUS);

        // Get all the wordList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultWordShouldNotBeFound("lifeStatus.in=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllWordsByLifeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where lifeStatus is not null
        defaultWordShouldBeFound("lifeStatus.specified=true");

        // Get all the wordList where lifeStatus is null
        defaultWordShouldNotBeFound("lifeStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        Image img = ImageResourceIntTest.createEntity(em);
        em.persist(img);
        em.flush();
        word.setImg(img);
        wordRepository.saveAndFlush(word);
        Long imgId = img.getId();

        // Get all the wordList where img equals to imgId
        defaultWordShouldBeFound("imgId.equals=" + imgId);

        // Get all the wordList where img equals to imgId + 1
        defaultWordShouldNotBeFound("imgId.equals=" + (imgId + 1));
    }


    @Test
    @Transactional
    public void getAllWordsByAudioIsEqualToSomething() throws Exception {
        // Initialize the database
        Audio audio = AudioResourceIntTest.createEntity(em);
        em.persist(audio);
        em.flush();
        word.setAudio(audio);
        wordRepository.saveAndFlush(word);
        Long audioId = audio.getId();

        // Get all the wordList where audio equals to audioId
        defaultWordShouldBeFound("audioId.equals=" + audioId);

        // Get all the wordList where audio equals to audioId + 1
        defaultWordShouldNotBeFound("audioId.equals=" + (audioId + 1));
    }


    @Test
    @Transactional
    public void getAllWordsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        word.setUser(user);
        wordRepository.saveAndFlush(word);
        Long userId = user.getId();

        // Get all the wordList where user equals to userId
        defaultWordShouldBeFound("userId.equals=" + userId);

        // Get all the wordList where user equals to userId + 1
        defaultWordShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllWordsByWordGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        WordGroup wordGroup = WordGroupResourceIntTest.createEntity(em);
        em.persist(wordGroup);
        em.flush();
        word.setWordGroup(wordGroup);
        wordRepository.saveAndFlush(word);
        Long wordGroupId = wordGroup.getId();

        // Get all the wordList where wordGroup equals to wordGroupId
        defaultWordShouldBeFound("wordGroupId.equals=" + wordGroupId);

        // Get all the wordList where wordGroup equals to wordGroupId + 1
        defaultWordShouldNotBeFound("wordGroupId.equals=" + (wordGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllWordsByFavoritesIsEqualToSomething() throws Exception {
        // Initialize the database
        Favorite favorites = FavoriteResourceIntTest.createEntity(em);
        em.persist(favorites);
        em.flush();
        word.addFavorites(favorites);
        wordRepository.saveAndFlush(word);
        Long favoritesId = favorites.getId();

        // Get all the wordList where favorites equals to favoritesId
        defaultWordShouldBeFound("favoritesId.equals=" + favoritesId);

        // Get all the wordList where favorites equals to favoritesId + 1
        defaultWordShouldNotBeFound("favoritesId.equals=" + (favoritesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWordShouldBeFound(String filter) throws Exception {
        restWordMockMvc.perform(get("/api/words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWordShouldNotBeFound(String filter) throws Exception {
        restWordMockMvc.perform(get("/api/words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWord() throws Exception {
        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);
        wordSearchRepository.save(word);
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word
        Word updatedWord = wordRepository.findOne(word.getId());
        // Disconnect from session so that the updates on updatedWord are not directly saved in db
        em.detach(updatedWord);
        updatedWord
            .name(UPDATED_NAME)
            .rank(UPDATED_RANK)
            .desctription(UPDATED_DESCTRIPTION)
            .lifeStatus(UPDATED_LIFE_STATUS);
        WordDTO wordDTO = wordMapper.toDto(updatedWord);

        restWordMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWord.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testWord.getDesctription()).isEqualTo(UPDATED_DESCTRIPTION);
        assertThat(testWord.getLifeStatus()).isEqualTo(UPDATED_LIFE_STATUS);

        // Validate the Word in Elasticsearch
        Word wordEs = wordSearchRepository.findOne(testWord.getId());
        assertThat(wordEs).isEqualToIgnoringGivenFields(testWord);
    }

    @Test
    @Transactional
    public void updateNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Create the Word
        WordDTO wordDTO = wordMapper.toDto(word);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWordMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);
        wordSearchRepository.save(word);
        int databaseSizeBeforeDelete = wordRepository.findAll().size();

        // Get the word
        restWordMockMvc.perform(delete("/api/words/{id}", word.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wordExistsInEs = wordSearchRepository.exists(word.getId());
        assertThat(wordExistsInEs).isFalse();

        // Validate the database is empty
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);
        wordSearchRepository.save(word);

        // Search the word
        restWordMockMvc.perform(get("/api/_search/words?query=id:" + word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Word.class);
        Word word1 = new Word();
        word1.setId(1L);
        Word word2 = new Word();
        word2.setId(word1.getId());
        assertThat(word1).isEqualTo(word2);
        word2.setId(2L);
        assertThat(word1).isNotEqualTo(word2);
        word1.setId(null);
        assertThat(word1).isNotEqualTo(word2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordDTO.class);
        WordDTO wordDTO1 = new WordDTO();
        wordDTO1.setId(1L);
        WordDTO wordDTO2 = new WordDTO();
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
        wordDTO2.setId(wordDTO1.getId());
        assertThat(wordDTO1).isEqualTo(wordDTO2);
        wordDTO2.setId(2L);
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
        wordDTO1.setId(null);
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(wordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(wordMapper.fromId(null)).isNull();
    }
}
