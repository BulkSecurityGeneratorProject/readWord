package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.domain.User;
import com.qigu.readword.repository.VipOrderRepository;
import com.qigu.readword.service.VipOrderService;
import com.qigu.readword.repository.search.VipOrderSearchRepository;
import com.qigu.readword.service.dto.VipOrderDTO;
import com.qigu.readword.service.mapper.VipOrderMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.VipOrderCriteria;
import com.qigu.readword.service.VipOrderQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.qigu.readword.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VipOrderResource REST controller.
 *
 * @see VipOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class VipOrderResourceIntTest {

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAYMENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final Integer DEFAULT_MONTHS = 1;
    private static final Integer UPDATED_MONTHS = 2;

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OUT_TRADE_NO = "AAAAAAAAAA";
    private static final String UPDATED_OUT_TRADE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_RESULT = "BBBBBBBBBB";

    @Autowired
    private VipOrderRepository vipOrderRepository;

    @Autowired
    private VipOrderMapper vipOrderMapper;

    @Autowired
    private VipOrderService vipOrderService;

    @Autowired
    private VipOrderSearchRepository vipOrderSearchRepository;

    @Autowired
    private VipOrderQueryService vipOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVipOrderMockMvc;

    private VipOrder vipOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VipOrderResource vipOrderResource = new VipOrderResource(vipOrderService, vipOrderQueryService);
        this.restVipOrderMockMvc = MockMvcBuilders.standaloneSetup(vipOrderResource)
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
    public static VipOrder createEntity(EntityManager em) {
        VipOrder vipOrder = new VipOrder()
            .createTime(DEFAULT_CREATE_TIME)
            .paymentTime(DEFAULT_PAYMENT_TIME)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .months(DEFAULT_MONTHS)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .outTradeNo(DEFAULT_OUT_TRADE_NO)
            .tradeType(DEFAULT_TRADE_TYPE)
            .paymentResult(DEFAULT_PAYMENT_RESULT);
        return vipOrder;
    }

    @Before
    public void initTest() {
        vipOrderSearchRepository.deleteAll();
        vipOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createVipOrder() throws Exception {
        int databaseSizeBeforeCreate = vipOrderRepository.findAll().size();

        // Create the VipOrder
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);
        restVipOrderMockMvc.perform(post("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the VipOrder in the database
        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeCreate + 1);
        VipOrder testVipOrder = vipOrderList.get(vipOrderList.size() - 1);
        assertThat(testVipOrder.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testVipOrder.getPaymentTime()).isEqualTo(DEFAULT_PAYMENT_TIME);
        assertThat(testVipOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testVipOrder.getMonths()).isEqualTo(DEFAULT_MONTHS);
        assertThat(testVipOrder.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testVipOrder.getOutTradeNo()).isEqualTo(DEFAULT_OUT_TRADE_NO);
        assertThat(testVipOrder.getTradeType()).isEqualTo(DEFAULT_TRADE_TYPE);
        assertThat(testVipOrder.getPaymentResult()).isEqualTo(DEFAULT_PAYMENT_RESULT);

        // Validate the VipOrder in Elasticsearch
        VipOrder vipOrderEs = vipOrderSearchRepository.findOne(testVipOrder.getId());
        assertThat(vipOrderEs).isEqualToIgnoringGivenFields(testVipOrder);
    }

    @Test
    @Transactional
    public void createVipOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vipOrderRepository.findAll().size();

        // Create the VipOrder with an existing ID
        vipOrder.setId(1L);
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVipOrderMockMvc.perform(post("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VipOrder in the database
        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vipOrderRepository.findAll().size();
        // set the field null
        vipOrder.setCreateTime(null);

        // Create the VipOrder, which fails.
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);

        restVipOrderMockMvc.perform(post("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isBadRequest());

        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = vipOrderRepository.findAll().size();
        // set the field null
        vipOrder.setTotalPrice(null);

        // Create the VipOrder, which fails.
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);

        restVipOrderMockMvc.perform(post("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isBadRequest());

        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthsIsRequired() throws Exception {
        int databaseSizeBeforeTest = vipOrderRepository.findAll().size();
        // set the field null
        vipOrder.setMonths(null);

        // Create the VipOrder, which fails.
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);

        restVipOrderMockMvc.perform(post("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isBadRequest());

        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVipOrders() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList
        restVipOrderMockMvc.perform(get("/api/vip-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vipOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].months").value(hasItem(DEFAULT_MONTHS)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].outTradeNo").value(hasItem(DEFAULT_OUT_TRADE_NO.toString())))
            .andExpect(jsonPath("$.[*].tradeType").value(hasItem(DEFAULT_TRADE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentResult").value(hasItem(DEFAULT_PAYMENT_RESULT.toString())));
    }

    @Test
    @Transactional
    public void getVipOrder() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get the vipOrder
        restVipOrderMockMvc.perform(get("/api/vip-orders/{id}", vipOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vipOrder.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.paymentTime").value(DEFAULT_PAYMENT_TIME.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.months").value(DEFAULT_MONTHS))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.toString()))
            .andExpect(jsonPath("$.outTradeNo").value(DEFAULT_OUT_TRADE_NO.toString()))
            .andExpect(jsonPath("$.tradeType").value(DEFAULT_TRADE_TYPE.toString()))
            .andExpect(jsonPath("$.paymentResult").value(DEFAULT_PAYMENT_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getAllVipOrdersByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where createTime equals to DEFAULT_CREATE_TIME
        defaultVipOrderShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the vipOrderList where createTime equals to UPDATED_CREATE_TIME
        defaultVipOrderShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultVipOrderShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the vipOrderList where createTime equals to UPDATED_CREATE_TIME
        defaultVipOrderShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where createTime is not null
        defaultVipOrderShouldBeFound("createTime.specified=true");

        // Get all the vipOrderList where createTime is null
        defaultVipOrderShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByPaymentTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where paymentTime equals to DEFAULT_PAYMENT_TIME
        defaultVipOrderShouldBeFound("paymentTime.equals=" + DEFAULT_PAYMENT_TIME);

        // Get all the vipOrderList where paymentTime equals to UPDATED_PAYMENT_TIME
        defaultVipOrderShouldNotBeFound("paymentTime.equals=" + UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByPaymentTimeIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where paymentTime in DEFAULT_PAYMENT_TIME or UPDATED_PAYMENT_TIME
        defaultVipOrderShouldBeFound("paymentTime.in=" + DEFAULT_PAYMENT_TIME + "," + UPDATED_PAYMENT_TIME);

        // Get all the vipOrderList where paymentTime equals to UPDATED_PAYMENT_TIME
        defaultVipOrderShouldNotBeFound("paymentTime.in=" + UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByPaymentTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where paymentTime is not null
        defaultVipOrderShouldBeFound("paymentTime.specified=true");

        // Get all the vipOrderList where paymentTime is null
        defaultVipOrderShouldNotBeFound("paymentTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultVipOrderShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the vipOrderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultVipOrderShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultVipOrderShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the vipOrderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultVipOrderShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where totalPrice is not null
        defaultVipOrderShouldBeFound("totalPrice.specified=true");

        // Get all the vipOrderList where totalPrice is null
        defaultVipOrderShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where months equals to DEFAULT_MONTHS
        defaultVipOrderShouldBeFound("months.equals=" + DEFAULT_MONTHS);

        // Get all the vipOrderList where months equals to UPDATED_MONTHS
        defaultVipOrderShouldNotBeFound("months.equals=" + UPDATED_MONTHS);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where months in DEFAULT_MONTHS or UPDATED_MONTHS
        defaultVipOrderShouldBeFound("months.in=" + DEFAULT_MONTHS + "," + UPDATED_MONTHS);

        // Get all the vipOrderList where months equals to UPDATED_MONTHS
        defaultVipOrderShouldNotBeFound("months.in=" + UPDATED_MONTHS);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where months is not null
        defaultVipOrderShouldBeFound("months.specified=true");

        // Get all the vipOrderList where months is null
        defaultVipOrderShouldNotBeFound("months.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where months greater than or equals to DEFAULT_MONTHS
        defaultVipOrderShouldBeFound("months.greaterOrEqualThan=" + DEFAULT_MONTHS);

        // Get all the vipOrderList where months greater than or equals to UPDATED_MONTHS
        defaultVipOrderShouldNotBeFound("months.greaterOrEqualThan=" + UPDATED_MONTHS);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where months less than or equals to DEFAULT_MONTHS
        defaultVipOrderShouldNotBeFound("months.lessThan=" + DEFAULT_MONTHS);

        // Get all the vipOrderList where months less than or equals to UPDATED_MONTHS
        defaultVipOrderShouldBeFound("months.lessThan=" + UPDATED_MONTHS);
    }


    @Test
    @Transactional
    public void getAllVipOrdersByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultVipOrderShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the vipOrderList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultVipOrderShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultVipOrderShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the vipOrderList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultVipOrderShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where transactionId is not null
        defaultVipOrderShouldBeFound("transactionId.specified=true");

        // Get all the vipOrderList where transactionId is null
        defaultVipOrderShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByOutTradeNoIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where outTradeNo equals to DEFAULT_OUT_TRADE_NO
        defaultVipOrderShouldBeFound("outTradeNo.equals=" + DEFAULT_OUT_TRADE_NO);

        // Get all the vipOrderList where outTradeNo equals to UPDATED_OUT_TRADE_NO
        defaultVipOrderShouldNotBeFound("outTradeNo.equals=" + UPDATED_OUT_TRADE_NO);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByOutTradeNoIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where outTradeNo in DEFAULT_OUT_TRADE_NO or UPDATED_OUT_TRADE_NO
        defaultVipOrderShouldBeFound("outTradeNo.in=" + DEFAULT_OUT_TRADE_NO + "," + UPDATED_OUT_TRADE_NO);

        // Get all the vipOrderList where outTradeNo equals to UPDATED_OUT_TRADE_NO
        defaultVipOrderShouldNotBeFound("outTradeNo.in=" + UPDATED_OUT_TRADE_NO);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByOutTradeNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where outTradeNo is not null
        defaultVipOrderShouldBeFound("outTradeNo.specified=true");

        // Get all the vipOrderList where outTradeNo is null
        defaultVipOrderShouldNotBeFound("outTradeNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTradeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where tradeType equals to DEFAULT_TRADE_TYPE
        defaultVipOrderShouldBeFound("tradeType.equals=" + DEFAULT_TRADE_TYPE);

        // Get all the vipOrderList where tradeType equals to UPDATED_TRADE_TYPE
        defaultVipOrderShouldNotBeFound("tradeType.equals=" + UPDATED_TRADE_TYPE);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTradeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where tradeType in DEFAULT_TRADE_TYPE or UPDATED_TRADE_TYPE
        defaultVipOrderShouldBeFound("tradeType.in=" + DEFAULT_TRADE_TYPE + "," + UPDATED_TRADE_TYPE);

        // Get all the vipOrderList where tradeType equals to UPDATED_TRADE_TYPE
        defaultVipOrderShouldNotBeFound("tradeType.in=" + UPDATED_TRADE_TYPE);
    }

    @Test
    @Transactional
    public void getAllVipOrdersByTradeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);

        // Get all the vipOrderList where tradeType is not null
        defaultVipOrderShouldBeFound("tradeType.specified=true");

        // Get all the vipOrderList where tradeType is null
        defaultVipOrderShouldNotBeFound("tradeType.specified=false");
    }

    @Test
    @Transactional
    public void getAllVipOrdersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        vipOrder.setUser(user);
        vipOrderRepository.saveAndFlush(vipOrder);
        Long userId = user.getId();

        // Get all the vipOrderList where user equals to userId
        defaultVipOrderShouldBeFound("userId.equals=" + userId);

        // Get all the vipOrderList where user equals to userId + 1
        defaultVipOrderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVipOrderShouldBeFound(String filter) throws Exception {
        restVipOrderMockMvc.perform(get("/api/vip-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vipOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].months").value(hasItem(DEFAULT_MONTHS)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].outTradeNo").value(hasItem(DEFAULT_OUT_TRADE_NO.toString())))
            .andExpect(jsonPath("$.[*].tradeType").value(hasItem(DEFAULT_TRADE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentResult").value(hasItem(DEFAULT_PAYMENT_RESULT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVipOrderShouldNotBeFound(String filter) throws Exception {
        restVipOrderMockMvc.perform(get("/api/vip-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVipOrder() throws Exception {
        // Get the vipOrder
        restVipOrderMockMvc.perform(get("/api/vip-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVipOrder() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);
        vipOrderSearchRepository.save(vipOrder);
        int databaseSizeBeforeUpdate = vipOrderRepository.findAll().size();

        // Update the vipOrder
        VipOrder updatedVipOrder = vipOrderRepository.findOne(vipOrder.getId());
        // Disconnect from session so that the updates on updatedVipOrder are not directly saved in db
        em.detach(updatedVipOrder);
        updatedVipOrder
            .createTime(UPDATED_CREATE_TIME)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .months(UPDATED_MONTHS)
            .transactionId(UPDATED_TRANSACTION_ID)
            .outTradeNo(UPDATED_OUT_TRADE_NO)
            .tradeType(UPDATED_TRADE_TYPE)
            .paymentResult(UPDATED_PAYMENT_RESULT);
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(updatedVipOrder);

        restVipOrderMockMvc.perform(put("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isOk());

        // Validate the VipOrder in the database
        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeUpdate);
        VipOrder testVipOrder = vipOrderList.get(vipOrderList.size() - 1);
        assertThat(testVipOrder.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testVipOrder.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
        assertThat(testVipOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testVipOrder.getMonths()).isEqualTo(UPDATED_MONTHS);
        assertThat(testVipOrder.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testVipOrder.getOutTradeNo()).isEqualTo(UPDATED_OUT_TRADE_NO);
        assertThat(testVipOrder.getTradeType()).isEqualTo(UPDATED_TRADE_TYPE);
        assertThat(testVipOrder.getPaymentResult()).isEqualTo(UPDATED_PAYMENT_RESULT);

        // Validate the VipOrder in Elasticsearch
        VipOrder vipOrderEs = vipOrderSearchRepository.findOne(testVipOrder.getId());
        assertThat(vipOrderEs).isEqualToIgnoringGivenFields(testVipOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingVipOrder() throws Exception {
        int databaseSizeBeforeUpdate = vipOrderRepository.findAll().size();

        // Create the VipOrder
        VipOrderDTO vipOrderDTO = vipOrderMapper.toDto(vipOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVipOrderMockMvc.perform(put("/api/vip-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vipOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the VipOrder in the database
        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVipOrder() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);
        vipOrderSearchRepository.save(vipOrder);
        int databaseSizeBeforeDelete = vipOrderRepository.findAll().size();

        // Get the vipOrder
        restVipOrderMockMvc.perform(delete("/api/vip-orders/{id}", vipOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean vipOrderExistsInEs = vipOrderSearchRepository.exists(vipOrder.getId());
        assertThat(vipOrderExistsInEs).isFalse();

        // Validate the database is empty
        List<VipOrder> vipOrderList = vipOrderRepository.findAll();
        assertThat(vipOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVipOrder() throws Exception {
        // Initialize the database
        vipOrderRepository.saveAndFlush(vipOrder);
        vipOrderSearchRepository.save(vipOrder);

        // Search the vipOrder
        restVipOrderMockMvc.perform(get("/api/_search/vip-orders?query=id:" + vipOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vipOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].months").value(hasItem(DEFAULT_MONTHS)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].outTradeNo").value(hasItem(DEFAULT_OUT_TRADE_NO.toString())))
            .andExpect(jsonPath("$.[*].tradeType").value(hasItem(DEFAULT_TRADE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentResult").value(hasItem(DEFAULT_PAYMENT_RESULT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VipOrder.class);
        VipOrder vipOrder1 = new VipOrder();
        vipOrder1.setId(1L);
        VipOrder vipOrder2 = new VipOrder();
        vipOrder2.setId(vipOrder1.getId());
        assertThat(vipOrder1).isEqualTo(vipOrder2);
        vipOrder2.setId(2L);
        assertThat(vipOrder1).isNotEqualTo(vipOrder2);
        vipOrder1.setId(null);
        assertThat(vipOrder1).isNotEqualTo(vipOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VipOrderDTO.class);
        VipOrderDTO vipOrderDTO1 = new VipOrderDTO();
        vipOrderDTO1.setId(1L);
        VipOrderDTO vipOrderDTO2 = new VipOrderDTO();
        assertThat(vipOrderDTO1).isNotEqualTo(vipOrderDTO2);
        vipOrderDTO2.setId(vipOrderDTO1.getId());
        assertThat(vipOrderDTO1).isEqualTo(vipOrderDTO2);
        vipOrderDTO2.setId(2L);
        assertThat(vipOrderDTO1).isNotEqualTo(vipOrderDTO2);
        vipOrderDTO1.setId(null);
        assertThat(vipOrderDTO1).isNotEqualTo(vipOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vipOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vipOrderMapper.fromId(null)).isNull();
    }
}
