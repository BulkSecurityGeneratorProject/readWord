package com.qigu.readword.web.rest;

import com.qigu.readword.ReadWordApp;

import com.qigu.readword.domain.Product;
import com.qigu.readword.repository.ProductRepository;
import com.qigu.readword.service.ProductService;
import com.qigu.readword.repository.search.ProductSearchRepository;
import com.qigu.readword.service.dto.ProductDTO;
import com.qigu.readword.service.mapper.ProductMapper;
import com.qigu.readword.web.rest.errors.ExceptionTranslator;
import com.qigu.readword.service.dto.ProductCriteria;
import com.qigu.readword.service.ProductQueryService;

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
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadWordApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_TOTAL_MONTHS = 1;
    private static final Integer UPDATED_TOTAL_MONTHS = 2;

    private static final Double DEFAULT_RANK = 1D;
    private static final Double UPDATED_RANK = 2D;

    private static final String DEFAULT_DESCTRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCTRIPTION = "BBBBBBBBBB";

    private static final LifeStatus DEFAULT_LIFE_STATUS = LifeStatus.DELETE;
    private static final LifeStatus UPDATED_LIFE_STATUS = LifeStatus.AVAILABLE;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .totalMonths(DEFAULT_TOTAL_MONTHS)
            .rank(DEFAULT_RANK)
            .desctription(DEFAULT_DESCTRIPTION)
            .lifeStatus(DEFAULT_LIFE_STATUS);
        return product;
    }

    @Before
    public void initTest() {
        productSearchRepository.deleteAll();
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getTotalMonths()).isEqualTo(DEFAULT_TOTAL_MONTHS);
        assertThat(testProduct.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testProduct.getDesctription()).isEqualTo(DEFAULT_DESCTRIPTION);
        assertThat(testProduct.getLifeStatus()).isEqualTo(DEFAULT_LIFE_STATUS);

        // Validate the Product in Elasticsearch
        Product productEs = productSearchRepository.findOne(testProduct.getId());
        assertThat(productEs).isEqualToIgnoringGivenFields(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalMonthsIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setTotalMonths(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalMonths").value(hasItem(DEFAULT_TOTAL_MONTHS)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.totalMonths").value(DEFAULT_TOTAL_MONTHS))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.doubleValue()))
            .andExpect(jsonPath("$.desctription").value(DEFAULT_DESCTRIPTION.toString()))
            .andExpect(jsonPath("$.lifeStatus").value(DEFAULT_LIFE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByTotalMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where totalMonths equals to DEFAULT_TOTAL_MONTHS
        defaultProductShouldBeFound("totalMonths.equals=" + DEFAULT_TOTAL_MONTHS);

        // Get all the productList where totalMonths equals to UPDATED_TOTAL_MONTHS
        defaultProductShouldNotBeFound("totalMonths.equals=" + UPDATED_TOTAL_MONTHS);
    }

    @Test
    @Transactional
    public void getAllProductsByTotalMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where totalMonths in DEFAULT_TOTAL_MONTHS or UPDATED_TOTAL_MONTHS
        defaultProductShouldBeFound("totalMonths.in=" + DEFAULT_TOTAL_MONTHS + "," + UPDATED_TOTAL_MONTHS);

        // Get all the productList where totalMonths equals to UPDATED_TOTAL_MONTHS
        defaultProductShouldNotBeFound("totalMonths.in=" + UPDATED_TOTAL_MONTHS);
    }

    @Test
    @Transactional
    public void getAllProductsByTotalMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where totalMonths is not null
        defaultProductShouldBeFound("totalMonths.specified=true");

        // Get all the productList where totalMonths is null
        defaultProductShouldNotBeFound("totalMonths.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByTotalMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where totalMonths greater than or equals to DEFAULT_TOTAL_MONTHS
        defaultProductShouldBeFound("totalMonths.greaterOrEqualThan=" + DEFAULT_TOTAL_MONTHS);

        // Get all the productList where totalMonths greater than or equals to UPDATED_TOTAL_MONTHS
        defaultProductShouldNotBeFound("totalMonths.greaterOrEqualThan=" + UPDATED_TOTAL_MONTHS);
    }

    @Test
    @Transactional
    public void getAllProductsByTotalMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where totalMonths less than or equals to DEFAULT_TOTAL_MONTHS
        defaultProductShouldNotBeFound("totalMonths.lessThan=" + DEFAULT_TOTAL_MONTHS);

        // Get all the productList where totalMonths less than or equals to UPDATED_TOTAL_MONTHS
        defaultProductShouldBeFound("totalMonths.lessThan=" + UPDATED_TOTAL_MONTHS);
    }


    @Test
    @Transactional
    public void getAllProductsByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rank equals to DEFAULT_RANK
        defaultProductShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the productList where rank equals to UPDATED_RANK
        defaultProductShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllProductsByRankIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultProductShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the productList where rank equals to UPDATED_RANK
        defaultProductShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    public void getAllProductsByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rank is not null
        defaultProductShouldBeFound("rank.specified=true");

        // Get all the productList where rank is null
        defaultProductShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByLifeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lifeStatus equals to DEFAULT_LIFE_STATUS
        defaultProductShouldBeFound("lifeStatus.equals=" + DEFAULT_LIFE_STATUS);

        // Get all the productList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultProductShouldNotBeFound("lifeStatus.equals=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByLifeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lifeStatus in DEFAULT_LIFE_STATUS or UPDATED_LIFE_STATUS
        defaultProductShouldBeFound("lifeStatus.in=" + DEFAULT_LIFE_STATUS + "," + UPDATED_LIFE_STATUS);

        // Get all the productList where lifeStatus equals to UPDATED_LIFE_STATUS
        defaultProductShouldNotBeFound("lifeStatus.in=" + UPDATED_LIFE_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByLifeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lifeStatus is not null
        defaultProductShouldBeFound("lifeStatus.specified=true");

        // Get all the productList where lifeStatus is null
        defaultProductShouldNotBeFound("lifeStatus.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalMonths").value(hasItem(DEFAULT_TOTAL_MONTHS)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        productSearchRepository.save(product);
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findOne(product.getId());
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .totalMonths(UPDATED_TOTAL_MONTHS)
            .rank(UPDATED_RANK)
            .desctription(UPDATED_DESCTRIPTION)
            .lifeStatus(UPDATED_LIFE_STATUS);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getTotalMonths()).isEqualTo(UPDATED_TOTAL_MONTHS);
        assertThat(testProduct.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testProduct.getDesctription()).isEqualTo(UPDATED_DESCTRIPTION);
        assertThat(testProduct.getLifeStatus()).isEqualTo(UPDATED_LIFE_STATUS);

        // Validate the Product in Elasticsearch
        Product productEs = productSearchRepository.findOne(testProduct.getId());
        assertThat(productEs).isEqualToIgnoringGivenFields(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        productSearchRepository.save(product);
        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean productExistsInEs = productSearchRepository.exists(product.getId());
        assertThat(productExistsInEs).isFalse();

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        productSearchRepository.save(product);

        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalMonths").value(hasItem(DEFAULT_TOTAL_MONTHS)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.doubleValue())))
            .andExpect(jsonPath("$.[*].desctription").value(hasItem(DEFAULT_DESCTRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lifeStatus").value(hasItem(DEFAULT_LIFE_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
