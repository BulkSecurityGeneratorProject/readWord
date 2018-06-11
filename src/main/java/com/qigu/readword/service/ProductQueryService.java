package com.qigu.readword.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.qigu.readword.domain.Product;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.ProductRepository;
import com.qigu.readword.repository.search.ProductSearchRepository;
import com.qigu.readword.service.dto.ProductCriteria;

import com.qigu.readword.service.dto.ProductDTO;
import com.qigu.readword.service.mapper.ProductMapper;
import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);


    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Product> specification = createSpecification(criteria);
        final Page<Product> result = productRepository.findAll(specification, page);
        return result.map(productMapper::toDto);
    }

    /**
     * Function to convert ProductCriteria to a {@link Specifications}
     */
    private Specifications<Product> createSpecification(ProductCriteria criteria) {
        Specifications<Product> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getTotalMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalMonths(), Product_.totalMonths));
            }
            if (criteria.getRank() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRank(), Product_.rank));
            }
            if (criteria.getLifeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLifeStatus(), Product_.lifeStatus));
            }
        }
        return specification;
    }

}
