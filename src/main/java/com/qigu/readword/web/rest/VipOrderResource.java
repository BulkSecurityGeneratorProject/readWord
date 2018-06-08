package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.VipOrderService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.VipOrderDTO;
import com.qigu.readword.service.dto.VipOrderCriteria;
import com.qigu.readword.service.VipOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing VipOrder.
 */
@RestController
@RequestMapping("/api")
public class VipOrderResource {

    private final Logger log = LoggerFactory.getLogger(VipOrderResource.class);

    private static final String ENTITY_NAME = "vipOrder";

    private final VipOrderService vipOrderService;

    private final VipOrderQueryService vipOrderQueryService;

    public VipOrderResource(VipOrderService vipOrderService, VipOrderQueryService vipOrderQueryService) {
        this.vipOrderService = vipOrderService;
        this.vipOrderQueryService = vipOrderQueryService;
    }

    /**
     * POST  /vip-orders : Create a new vipOrder.
     *
     * @param vipOrderDTO the vipOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vipOrderDTO, or with status 400 (Bad Request) if the vipOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vip-orders")
    @Timed
    public ResponseEntity<VipOrderDTO> createVipOrder(@Valid @RequestBody VipOrderDTO vipOrderDTO) throws URISyntaxException {
        log.debug("REST request to save VipOrder : {}", vipOrderDTO);
        if (vipOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new vipOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VipOrderDTO result = vipOrderService.save(vipOrderDTO);
        return ResponseEntity.created(new URI("/api/vip-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vip-orders : Updates an existing vipOrder.
     *
     * @param vipOrderDTO the vipOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vipOrderDTO,
     * or with status 400 (Bad Request) if the vipOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the vipOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vip-orders")
    @Timed
    public ResponseEntity<VipOrderDTO> updateVipOrder(@Valid @RequestBody VipOrderDTO vipOrderDTO) throws URISyntaxException {
        log.debug("REST request to update VipOrder : {}", vipOrderDTO);
        if (vipOrderDTO.getId() == null) {
            return createVipOrder(vipOrderDTO);
        }
        VipOrderDTO result = vipOrderService.save(vipOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vipOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vip-orders : get all the vipOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of vipOrders in body
     */
    @GetMapping("/vip-orders")
    @Timed
    public ResponseEntity<List<VipOrderDTO>> getAllVipOrders(VipOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VipOrders by criteria: {}", criteria);
        Page<VipOrderDTO> page = vipOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vip-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vip-orders/:id : get the "id" vipOrder.
     *
     * @param id the id of the vipOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vipOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vip-orders/{id}")
    @Timed
    public ResponseEntity<VipOrderDTO> getVipOrder(@PathVariable Long id) {
        log.debug("REST request to get VipOrder : {}", id);
        VipOrderDTO vipOrderDTO = vipOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vipOrderDTO));
    }

    /**
     * DELETE  /vip-orders/:id : delete the "id" vipOrder.
     *
     * @param id the id of the vipOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vip-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteVipOrder(@PathVariable Long id) {
        log.debug("REST request to delete VipOrder : {}", id);
        vipOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vip-orders?query=:query : search for the vipOrder corresponding
     * to the query.
     *
     * @param query the query of the vipOrder search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/vip-orders")
    @Timed
    public ResponseEntity<List<VipOrderDTO>> searchVipOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of VipOrders for query {}", query);
        Page<VipOrderDTO> page = vipOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/vip-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
