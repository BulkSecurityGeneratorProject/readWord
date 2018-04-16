package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.MessageContent;
import com.qigu.readword.repository.MessageContentRepository;
import com.qigu.readword.service.MessageContentService;
import com.qigu.readword.repository.search.MessageContentSearchRepository;
import com.qigu.readword.service.dto.MessageContentDTO;
import com.qigu.readword.service.mapper.MessageContentMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.MessageContentCriteria;
import com.qigu.readword.service.MessageContentQueryService;

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

/**
 * Test class for the MessageContentResource REST controller.
 *
 * @see MessageContentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class MessageContentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private MessageContentRepository messageContentRepository;

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Autowired
    private MessageContentService messageContentService;

    @Autowired
    private MessageContentSearchRepository messageContentSearchRepository;

    @Autowired
    private MessageContentQueryService messageContentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessageContentMockMvc;

    private MessageContent messageContent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageContentResource messageContentResource = new MessageContentResource(messageContentService, messageContentQueryService);
        this.restMessageContentMockMvc = MockMvcBuilders.standaloneSetup(messageContentResource)
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
    public static MessageContent createEntity(EntityManager em) {
        MessageContent messageContent = new MessageContent()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT);
        return messageContent;
    }

    @Before
    public void initTest() {
        messageContentSearchRepository.deleteAll();
        messageContent = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessageContent() throws Exception {
        int databaseSizeBeforeCreate = messageContentRepository.findAll().size();

        // Create the MessageContent
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(messageContent);
        restMessageContentMockMvc.perform(post("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageContent in the database
        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeCreate + 1);
        MessageContent testMessageContent = messageContentList.get(messageContentList.size() - 1);
        assertThat(testMessageContent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMessageContent.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the MessageContent in Elasticsearch
        MessageContent messageContentEs = messageContentSearchRepository.findOne(testMessageContent.getId());
        assertThat(messageContentEs).isEqualToIgnoringGivenFields(testMessageContent);
    }

    @Test
    @Transactional
    public void createMessageContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageContentRepository.findAll().size();

        // Create the MessageContent with an existing ID
        messageContent.setId(1L);
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(messageContent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageContentMockMvc.perform(post("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageContent in the database
        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageContentRepository.findAll().size();
        // set the field null
        messageContent.setName(null);

        // Create the MessageContent, which fails.
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(messageContent);

        restMessageContentMockMvc.perform(post("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isBadRequest());

        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageContentRepository.findAll().size();
        // set the field null
        messageContent.setContent(null);

        // Create the MessageContent, which fails.
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(messageContent);

        restMessageContentMockMvc.perform(post("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isBadRequest());

        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageContents() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);

        // Get all the messageContentList
        restMessageContentMockMvc.perform(get("/api/message-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getMessageContent() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);

        // Get the messageContent
        restMessageContentMockMvc.perform(get("/api/message-contents/{id}", messageContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messageContent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getAllMessageContentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);

        // Get all the messageContentList where name equals to DEFAULT_NAME
        defaultMessageContentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the messageContentList where name equals to UPDATED_NAME
        defaultMessageContentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMessageContentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);

        // Get all the messageContentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMessageContentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the messageContentList where name equals to UPDATED_NAME
        defaultMessageContentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMessageContentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);

        // Get all the messageContentList where name is not null
        defaultMessageContentShouldBeFound("name.specified=true");

        // Get all the messageContentList where name is null
        defaultMessageContentShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMessageContentShouldBeFound(String filter) throws Exception {
        restMessageContentMockMvc.perform(get("/api/message-contents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMessageContentShouldNotBeFound(String filter) throws Exception {
        restMessageContentMockMvc.perform(get("/api/message-contents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMessageContent() throws Exception {
        // Get the messageContent
        restMessageContentMockMvc.perform(get("/api/message-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageContent() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);
        messageContentSearchRepository.save(messageContent);
        int databaseSizeBeforeUpdate = messageContentRepository.findAll().size();

        // Update the messageContent
        MessageContent updatedMessageContent = messageContentRepository.findOne(messageContent.getId());
        // Disconnect from session so that the updates on updatedMessageContent are not directly saved in db
        em.detach(updatedMessageContent);
        updatedMessageContent
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT);
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(updatedMessageContent);

        restMessageContentMockMvc.perform(put("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isOk());

        // Validate the MessageContent in the database
        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeUpdate);
        MessageContent testMessageContent = messageContentList.get(messageContentList.size() - 1);
        assertThat(testMessageContent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMessageContent.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the MessageContent in Elasticsearch
        MessageContent messageContentEs = messageContentSearchRepository.findOne(testMessageContent.getId());
        assertThat(messageContentEs).isEqualToIgnoringGivenFields(testMessageContent);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageContent() throws Exception {
        int databaseSizeBeforeUpdate = messageContentRepository.findAll().size();

        // Create the MessageContent
        MessageContentDTO messageContentDTO = messageContentMapper.toDto(messageContent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMessageContentMockMvc.perform(put("/api/message-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageContentDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageContent in the database
        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMessageContent() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);
        messageContentSearchRepository.save(messageContent);
        int databaseSizeBeforeDelete = messageContentRepository.findAll().size();

        // Get the messageContent
        restMessageContentMockMvc.perform(delete("/api/message-contents/{id}", messageContent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean messageContentExistsInEs = messageContentSearchRepository.exists(messageContent.getId());
        assertThat(messageContentExistsInEs).isFalse();

        // Validate the database is empty
        List<MessageContent> messageContentList = messageContentRepository.findAll();
        assertThat(messageContentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMessageContent() throws Exception {
        // Initialize the database
        messageContentRepository.saveAndFlush(messageContent);
        messageContentSearchRepository.save(messageContent);

        // Search the messageContent
        restMessageContentMockMvc.perform(get("/api/_search/message-contents?query=id:" + messageContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageContent.class);
        MessageContent messageContent1 = new MessageContent();
        messageContent1.setId(1L);
        MessageContent messageContent2 = new MessageContent();
        messageContent2.setId(messageContent1.getId());
        assertThat(messageContent1).isEqualTo(messageContent2);
        messageContent2.setId(2L);
        assertThat(messageContent1).isNotEqualTo(messageContent2);
        messageContent1.setId(null);
        assertThat(messageContent1).isNotEqualTo(messageContent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageContentDTO.class);
        MessageContentDTO messageContentDTO1 = new MessageContentDTO();
        messageContentDTO1.setId(1L);
        MessageContentDTO messageContentDTO2 = new MessageContentDTO();
        assertThat(messageContentDTO1).isNotEqualTo(messageContentDTO2);
        messageContentDTO2.setId(messageContentDTO1.getId());
        assertThat(messageContentDTO1).isEqualTo(messageContentDTO2);
        messageContentDTO2.setId(2L);
        assertThat(messageContentDTO1).isNotEqualTo(messageContentDTO2);
        messageContentDTO1.setId(null);
        assertThat(messageContentDTO1).isNotEqualTo(messageContentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(messageContentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(messageContentMapper.fromId(null)).isNull();
    }
}
