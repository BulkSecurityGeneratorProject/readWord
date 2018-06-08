package com.qigu.readword.service;

import com.qigu.readword.service.dto.VipOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing VipOrder.
 */
public interface VipOrderService {

    /**
     * Save a vipOrder.
     *
     * @param vipOrderDTO the entity to save
     * @return the persisted entity
     */
    VipOrderDTO save(VipOrderDTO vipOrderDTO);

    /**
     * Get all the vipOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VipOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vipOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VipOrderDTO findOne(Long id);

    /**
     * Delete the "id" vipOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the vipOrder corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VipOrderDTO> search(String query, Pageable pageable);
}
