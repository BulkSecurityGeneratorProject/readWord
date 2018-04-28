package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.Audio;
import com.qigu.readword.repository.AudioRepository;
import com.qigu.readword.service.AudioService;
import com.qigu.readword.repository.search.AudioSearchRepository;
import com.qigu.readword.service.dto.AudioDTO;
import com.qigu.readword.service.mapper.AudioMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.AudioCriteria;
import com.qigu.readword.service.AudioQueryService;

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

/**
 * Test class for the AudioResource REST controller.
 *
 * @see AudioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class AudioResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ONE_SPEED_URL = "AAAAAAAAAA";
    private static final String UPDATED_ONE_SPEED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioMapper audioMapper;

    @Autowired
    private AudioService audioService;

    @Autowired
    private AudioSearchRepository audioSearchRepository;

    @Autowired
    private AudioQueryService audioQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAudioMockMvc;

    private Audio audio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AudioResource audioResource = new AudioResource(audioService, audioQueryService);
        this.restAudioMockMvc = MockMvcBuilders.standaloneSetup(audioResource)
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
    public static Audio createEntity(EntityManager em) {
        Audio audio = new Audio()
            .url(DEFAULT_URL)
            .oneSpeedUrl(DEFAULT_ONE_SPEED_URL)
            .name(DEFAULT_NAME);
        return audio;
    }

    @Before
    public void initTest() {
        audioSearchRepository.deleteAll();
        audio = createEntity(em);
    }

    @Test
    @Transactional
    public void createAudio() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);
        restAudioMockMvc.perform(post("/api/audio")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isCreated());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate + 1);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testAudio.getOneSpeedUrl()).isEqualTo(DEFAULT_ONE_SPEED_URL);
        assertThat(testAudio.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Audio in Elasticsearch
        Audio audioEs = audioSearchRepository.findOne(testAudio.getId());
        assertThat(audioEs).isEqualToIgnoringGivenFields(testAudio);
    }

    @Test
    @Transactional
    public void createAudioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();

        // Create the Audio with an existing ID
        audio.setId(1L);
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAudioMockMvc.perform(post("/api/audio")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = audioRepository.findAll().size();
        // set the field null
        audio.setName(null);

        // Create the Audio, which fails.
        AudioDTO audioDTO = audioMapper.toDto(audio);

        restAudioMockMvc.perform(post("/api/audio")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isBadRequest());

        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList
        restAudioMockMvc.perform(get("/api/audio?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audio.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].oneSpeedUrl").value(hasItem(DEFAULT_ONE_SPEED_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get the audio
        restAudioMockMvc.perform(get("/api/audio/{id}", audio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(audio.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.oneSpeedUrl").value(DEFAULT_ONE_SPEED_URL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAudioByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where url equals to DEFAULT_URL
        defaultAudioShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the audioList where url equals to UPDATED_URL
        defaultAudioShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAudioByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where url in DEFAULT_URL or UPDATED_URL
        defaultAudioShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the audioList where url equals to UPDATED_URL
        defaultAudioShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAudioByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where url is not null
        defaultAudioShouldBeFound("url.specified=true");

        // Get all the audioList where url is null
        defaultAudioShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllAudioByOneSpeedUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where oneSpeedUrl equals to DEFAULT_ONE_SPEED_URL
        defaultAudioShouldBeFound("oneSpeedUrl.equals=" + DEFAULT_ONE_SPEED_URL);

        // Get all the audioList where oneSpeedUrl equals to UPDATED_ONE_SPEED_URL
        defaultAudioShouldNotBeFound("oneSpeedUrl.equals=" + UPDATED_ONE_SPEED_URL);
    }

    @Test
    @Transactional
    public void getAllAudioByOneSpeedUrlIsInShouldWork() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where oneSpeedUrl in DEFAULT_ONE_SPEED_URL or UPDATED_ONE_SPEED_URL
        defaultAudioShouldBeFound("oneSpeedUrl.in=" + DEFAULT_ONE_SPEED_URL + "," + UPDATED_ONE_SPEED_URL);

        // Get all the audioList where oneSpeedUrl equals to UPDATED_ONE_SPEED_URL
        defaultAudioShouldNotBeFound("oneSpeedUrl.in=" + UPDATED_ONE_SPEED_URL);
    }

    @Test
    @Transactional
    public void getAllAudioByOneSpeedUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where oneSpeedUrl is not null
        defaultAudioShouldBeFound("oneSpeedUrl.specified=true");

        // Get all the audioList where oneSpeedUrl is null
        defaultAudioShouldNotBeFound("oneSpeedUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllAudioByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where name equals to DEFAULT_NAME
        defaultAudioShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the audioList where name equals to UPDATED_NAME
        defaultAudioShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAudioByNameIsInShouldWork() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAudioShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the audioList where name equals to UPDATED_NAME
        defaultAudioShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAudioByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList where name is not null
        defaultAudioShouldBeFound("name.specified=true");

        // Get all the audioList where name is null
        defaultAudioShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAudioShouldBeFound(String filter) throws Exception {
        restAudioMockMvc.perform(get("/api/audio?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audio.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].oneSpeedUrl").value(hasItem(DEFAULT_ONE_SPEED_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAudioShouldNotBeFound(String filter) throws Exception {
        restAudioMockMvc.perform(get("/api/audio?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAudio() throws Exception {
        // Get the audio
        restAudioMockMvc.perform(get("/api/audio/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);
        audioSearchRepository.save(audio);
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio
        Audio updatedAudio = audioRepository.findOne(audio.getId());
        // Disconnect from session so that the updates on updatedAudio are not directly saved in db
        em.detach(updatedAudio);
        updatedAudio
            .url(UPDATED_URL)
            .oneSpeedUrl(UPDATED_ONE_SPEED_URL)
            .name(UPDATED_NAME);
        AudioDTO audioDTO = audioMapper.toDto(updatedAudio);

        restAudioMockMvc.perform(put("/api/audio")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testAudio.getOneSpeedUrl()).isEqualTo(UPDATED_ONE_SPEED_URL);
        assertThat(testAudio.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Audio in Elasticsearch
        Audio audioEs = audioSearchRepository.findOne(testAudio.getId());
        assertThat(audioEs).isEqualToIgnoringGivenFields(testAudio);
    }

    @Test
    @Transactional
    public void updateNonExistingAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAudioMockMvc.perform(put("/api/audio")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isCreated());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);
        audioSearchRepository.save(audio);
        int databaseSizeBeforeDelete = audioRepository.findAll().size();

        // Get the audio
        restAudioMockMvc.perform(delete("/api/audio/{id}", audio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean audioExistsInEs = audioSearchRepository.exists(audio.getId());
        assertThat(audioExistsInEs).isFalse();

        // Validate the database is empty
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);
        audioSearchRepository.save(audio);

        // Search the audio
        restAudioMockMvc.perform(get("/api/_search/audio?query=id:" + audio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audio.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].oneSpeedUrl").value(hasItem(DEFAULT_ONE_SPEED_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Audio.class);
        Audio audio1 = new Audio();
        audio1.setId(1L);
        Audio audio2 = new Audio();
        audio2.setId(audio1.getId());
        assertThat(audio1).isEqualTo(audio2);
        audio2.setId(2L);
        assertThat(audio1).isNotEqualTo(audio2);
        audio1.setId(null);
        assertThat(audio1).isNotEqualTo(audio2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AudioDTO.class);
        AudioDTO audioDTO1 = new AudioDTO();
        audioDTO1.setId(1L);
        AudioDTO audioDTO2 = new AudioDTO();
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO2.setId(audioDTO1.getId());
        assertThat(audioDTO1).isEqualTo(audioDTO2);
        audioDTO2.setId(2L);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO1.setId(null);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(audioMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(audioMapper.fromId(null)).isNull();
    }
}
