package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.MessageStatus;
import com.qigu.readword.domain.Message;
import com.qigu.readword.domain.User;
import com.qigu.readword.repository.MessageStatusRepository;
import com.qigu.readword.service.MessageStatusService;
import com.qigu.readword.repository.search.MessageStatusSearchRepository;
import com.qigu.readword.service.dto.MessageStatusDTO;
import com.qigu.readword.service.mapper.MessageStatusMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.MessageStatusCriteria;
import com.qigu.readword.service.MessageStatusQueryService;

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

import com.qigu.readword.domain.enumeration.MessageStatusEnum;
/**
 * Test class for the MessageStatusResource REST controller.
 *
 * @see MessageStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class MessageStatusResourceIntTest {

    private static final MessageStatusEnum DEFAULT_STATUS = MessageStatusEnum.READ;
    private static final MessageStatusEnum UPDATED_STATUS = MessageStatusEnum.DELETE;

    @Autowired
    private MessageStatusRepository messageStatusRepository;

    @Autowired
    private MessageStatusMapper messageStatusMapper;

    @Autowired
    private MessageStatusService messageStatusService;

    @Autowired
    private MessageStatusSearchRepository messageStatusSearchRepository;

    @Autowired
    private MessageStatusQueryService messageStatusQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessageStatusMockMvc;

    private MessageStatus messageStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageStatusResource messageStatusResource = new MessageStatusResource(messageStatusService, messageStatusQueryService);
        this.restMessageStatusMockMvc = MockMvcBuilders.standaloneSetup(messageStatusResource)
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
    public static MessageStatus createEntity(EntityManager em) {
        MessageStatus messageStatus = new MessageStatus()
            .status(DEFAULT_STATUS);
        return messageStatus;
    }

    @Before
    public void initTest() {
        messageStatusSearchRepository.deleteAll();
        messageStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessageStatus() throws Exception {
        int databaseSizeBeforeCreate = messageStatusRepository.findAll().size();

        // Create the MessageStatus
        MessageStatusDTO messageStatusDTO = messageStatusMapper.toDto(messageStatus);
        restMessageStatusMockMvc.perform(post("/api/message-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageStatus in the database
        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeCreate + 1);
        MessageStatus testMessageStatus = messageStatusList.get(messageStatusList.size() - 1);
        assertThat(testMessageStatus.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MessageStatus in Elasticsearch
        MessageStatus messageStatusEs = messageStatusSearchRepository.findOne(testMessageStatus.getId());
        assertThat(messageStatusEs).isEqualToIgnoringGivenFields(testMessageStatus);
    }

    @Test
    @Transactional
    public void createMessageStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageStatusRepository.findAll().size();

        // Create the MessageStatus with an existing ID
        messageStatus.setId(1L);
        MessageStatusDTO messageStatusDTO = messageStatusMapper.toDto(messageStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageStatusMockMvc.perform(post("/api/message-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageStatus in the database
        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageStatusRepository.findAll().size();
        // set the field null
        messageStatus.setStatus(null);

        // Create the MessageStatus, which fails.
        MessageStatusDTO messageStatusDTO = messageStatusMapper.toDto(messageStatus);

        restMessageStatusMockMvc.perform(post("/api/message-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageStatusDTO)))
            .andExpect(status().isBadRequest());

        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageStatuses() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);

        // Get all the messageStatusList
        restMessageStatusMockMvc.perform(get("/api/message-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getMessageStatus() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);

        // Get the messageStatus
        restMessageStatusMockMvc.perform(get("/api/message-statuses/{id}", messageStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messageStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllMessageStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);

        // Get all the messageStatusList where status equals to DEFAULT_STATUS
        defaultMessageStatusShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the messageStatusList where status equals to UPDATED_STATUS
        defaultMessageStatusShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMessageStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);

        // Get all the messageStatusList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMessageStatusShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the messageStatusList where status equals to UPDATED_STATUS
        defaultMessageStatusShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMessageStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);

        // Get all the messageStatusList where status is not null
        defaultMessageStatusShouldBeFound("status.specified=true");

        // Get all the messageStatusList where status is null
        defaultMessageStatusShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageStatusesByMsgIsEqualToSomething() throws Exception {
        // Initialize the database
        Message msg = MessageResourceIntTest.createEntity(em);
        em.persist(msg);
        em.flush();
        messageStatus.setMsg(msg);
        messageStatusRepository.saveAndFlush(messageStatus);
        Long msgId = msg.getId();

        // Get all the messageStatusList where msg equals to msgId
        defaultMessageStatusShouldBeFound("msgId.equals=" + msgId);

        // Get all the messageStatusList where msg equals to msgId + 1
        defaultMessageStatusShouldNotBeFound("msgId.equals=" + (msgId + 1));
    }


    @Test
    @Transactional
    public void getAllMessageStatusesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        messageStatus.setUser(user);
        messageStatusRepository.saveAndFlush(messageStatus);
        Long userId = user.getId();

        // Get all the messageStatusList where user equals to userId
        defaultMessageStatusShouldBeFound("userId.equals=" + userId);

        // Get all the messageStatusList where user equals to userId + 1
        defaultMessageStatusShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMessageStatusShouldBeFound(String filter) throws Exception {
        restMessageStatusMockMvc.perform(get("/api/message-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMessageStatusShouldNotBeFound(String filter) throws Exception {
        restMessageStatusMockMvc.perform(get("/api/message-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMessageStatus() throws Exception {
        // Get the messageStatus
        restMessageStatusMockMvc.perform(get("/api/message-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageStatus() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);
        messageStatusSearchRepository.save(messageStatus);
        int databaseSizeBeforeUpdate = messageStatusRepository.findAll().size();

        // Update the messageStatus
        MessageStatus updatedMessageStatus = messageStatusRepository.findOne(messageStatus.getId());
        // Disconnect from session so that the updates on updatedMessageStatus are not directly saved in db
        em.detach(updatedMessageStatus);
        updatedMessageStatus
            .status(UPDATED_STATUS);
        MessageStatusDTO messageStatusDTO = messageStatusMapper.toDto(updatedMessageStatus);

        restMessageStatusMockMvc.perform(put("/api/message-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageStatusDTO)))
            .andExpect(status().isOk());

        // Validate the MessageStatus in the database
        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeUpdate);
        MessageStatus testMessageStatus = messageStatusList.get(messageStatusList.size() - 1);
        assertThat(testMessageStatus.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MessageStatus in Elasticsearch
        MessageStatus messageStatusEs = messageStatusSearchRepository.findOne(testMessageStatus.getId());
        assertThat(messageStatusEs).isEqualToIgnoringGivenFields(testMessageStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageStatus() throws Exception {
        int databaseSizeBeforeUpdate = messageStatusRepository.findAll().size();

        // Create the MessageStatus
        MessageStatusDTO messageStatusDTO = messageStatusMapper.toDto(messageStatus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMessageStatusMockMvc.perform(put("/api/message-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageStatus in the database
        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMessageStatus() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);
        messageStatusSearchRepository.save(messageStatus);
        int databaseSizeBeforeDelete = messageStatusRepository.findAll().size();

        // Get the messageStatus
        restMessageStatusMockMvc.perform(delete("/api/message-statuses/{id}", messageStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean messageStatusExistsInEs = messageStatusSearchRepository.exists(messageStatus.getId());
        assertThat(messageStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<MessageStatus> messageStatusList = messageStatusRepository.findAll();
        assertThat(messageStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMessageStatus() throws Exception {
        // Initialize the database
        messageStatusRepository.saveAndFlush(messageStatus);
        messageStatusSearchRepository.save(messageStatus);

        // Search the messageStatus
        restMessageStatusMockMvc.perform(get("/api/_search/message-statuses?query=id:" + messageStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageStatus.class);
        MessageStatus messageStatus1 = new MessageStatus();
        messageStatus1.setId(1L);
        MessageStatus messageStatus2 = new MessageStatus();
        messageStatus2.setId(messageStatus1.getId());
        assertThat(messageStatus1).isEqualTo(messageStatus2);
        messageStatus2.setId(2L);
        assertThat(messageStatus1).isNotEqualTo(messageStatus2);
        messageStatus1.setId(null);
        assertThat(messageStatus1).isNotEqualTo(messageStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageStatusDTO.class);
        MessageStatusDTO messageStatusDTO1 = new MessageStatusDTO();
        messageStatusDTO1.setId(1L);
        MessageStatusDTO messageStatusDTO2 = new MessageStatusDTO();
        assertThat(messageStatusDTO1).isNotEqualTo(messageStatusDTO2);
        messageStatusDTO2.setId(messageStatusDTO1.getId());
        assertThat(messageStatusDTO1).isEqualTo(messageStatusDTO2);
        messageStatusDTO2.setId(2L);
        assertThat(messageStatusDTO1).isNotEqualTo(messageStatusDTO2);
        messageStatusDTO1.setId(null);
        assertThat(messageStatusDTO1).isNotEqualTo(messageStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(messageStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(messageStatusMapper.fromId(null)).isNull();
    }
}
