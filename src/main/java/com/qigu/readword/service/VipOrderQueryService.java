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

import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.VipOrderRepository;
import com.qigu.readword.repository.search.VipOrderSearchRepository;
import com.qigu.readword.service.dto.VipOrderCriteria;

import com.qigu.readword.service.dto.VipOrderDTO;
import com.qigu.readword.service.mapper.VipOrderMapper;
import com.qigu.readword.domain.enumeration.VipOrderStatus;

/**
 * Service for executing complex queries for VipOrder entities in the database.
 * The main input is a {@link VipOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VipOrderDTO} or a {@link Page} of {@link VipOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VipOrderQueryService extends QueryService<VipOrder> {

    private final Logger log = LoggerFactory.getLogger(VipOrderQueryService.class);


    private final VipOrderRepository vipOrderRepository;

    private final VipOrderMapper vipOrderMapper;

    private final VipOrderSearchRepository vipOrderSearchRepository;

    public VipOrderQueryService(VipOrderRepository vipOrderRepository, VipOrderMapper vipOrderMapper, VipOrderSearchRepository vipOrderSearchRepository) {
        this.vipOrderRepository = vipOrderRepository;
        this.vipOrderMapper = vipOrderMapper;
        this.vipOrderSearchRepository = vipOrderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link VipOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VipOrderDTO> findByCriteria(VipOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<VipOrder> specification = createSpecification(criteria);
        return vipOrderMapper.toDto(vipOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VipOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VipOrderDTO> findByCriteria(VipOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<VipOrder> specification = createSpecification(criteria);
        final Page<VipOrder> result = vipOrderRepository.findAll(specification, page);
        return result.map(vipOrderMapper::toDto);
    }

    /**
     * Function to convert VipOrderCriteria to a {@link Specifications}
     */
    private Specifications<VipOrder> createSpecification(VipOrderCriteria criteria) {
        Specifications<VipOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), VipOrder_.id));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), VipOrder_.createTime));
            }
            if (criteria.getPaymentTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentTime(), VipOrder_.paymentTime));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), VipOrder_.totalPrice));
            }
            if (criteria.getMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonths(), VipOrder_.months));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionId(), VipOrder_.transactionId));
            }
            if (criteria.getOutTradeNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutTradeNo(), VipOrder_.outTradeNo));
            }
            if (criteria.getTradeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTradeType(), VipOrder_.tradeType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), VipOrder_.status));
            }
            if (criteria.getOpenId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpenId(), VipOrder_.openId));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), VipOrder_.product, Product_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), VipOrder_.user, User_.id));
            }
        }
        return specification;
    }

}
