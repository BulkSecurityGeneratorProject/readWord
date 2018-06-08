package com.qigu.readword.service.impl;

import com.qigu.readword.service.VipOrderService;
import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.repository.VipOrderRepository;
import com.qigu.readword.repository.search.VipOrderSearchRepository;
import com.qigu.readword.service.dto.VipOrderDTO;
import com.qigu.readword.service.mapper.VipOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing VipOrder.
 */
@Service
@Transactional
public class VipOrderServiceImpl implements VipOrderService {

    private final Logger log = LoggerFactory.getLogger(VipOrderServiceImpl.class);

    private final VipOrderRepository vipOrderRepository;

    private final VipOrderMapper vipOrderMapper;

    private final VipOrderSearchRepository vipOrderSearchRepository;

    public VipOrderServiceImpl(VipOrderRepository vipOrderRepository, VipOrderMapper vipOrderMapper, VipOrderSearchRepository vipOrderSearchRepository) {
        this.vipOrderRepository = vipOrderRepository;
        this.vipOrderMapper = vipOrderMapper;
        this.vipOrderSearchRepository = vipOrderSearchRepository;
    }

    /**
     * Save a vipOrder.
     *
     * @param vipOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VipOrderDTO save(VipOrderDTO vipOrderDTO) {
        log.debug("Request to save VipOrder : {}", vipOrderDTO);
        VipOrder vipOrder = vipOrderMapper.toEntity(vipOrderDTO);
        vipOrder = vipOrderRepository.save(vipOrder);
        VipOrderDTO result = vipOrderMapper.toDto(vipOrder);
        vipOrderSearchRepository.save(vipOrder);
        return result;
    }

    /**
     * Get all the vipOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VipOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VipOrders");
        return vipOrderRepository.findAll(pageable)
            .map(vipOrderMapper::toDto);
    }

    /**
     * Get one vipOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VipOrderDTO findOne(Long id) {
        log.debug("Request to get VipOrder : {}", id);
        VipOrder vipOrder = vipOrderRepository.findOne(id);
        return vipOrderMapper.toDto(vipOrder);
    }

    /**
     * Delete the vipOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VipOrder : {}", id);
        vipOrderRepository.delete(id);
        vipOrderSearchRepository.delete(id);
    }

    /**
     * Search for the vipOrder corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VipOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of VipOrders for query {}", query);
        Page<VipOrder> result = vipOrderSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(vipOrderMapper::toDto);
    }
}
