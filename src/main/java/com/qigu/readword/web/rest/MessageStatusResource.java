package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.MessageStatusService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.MessageStatusDTO;
import com.qigu.readword.service.dto.MessageStatusCriteria;
import com.qigu.readword.service.MessageStatusQueryService;
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
 * REST controller for managing MessageStatus.
 */
@RestController
@RequestMapping("/api")
public class MessageStatusResource {

    private final Logger log = LoggerFactory.getLogger(MessageStatusResource.class);

    private static final String ENTITY_NAME = "messageStatus";

    private final MessageStatusService messageStatusService;

    private final MessageStatusQueryService messageStatusQueryService;

    public MessageStatusResource(MessageStatusService messageStatusService, MessageStatusQueryService messageStatusQueryService) {
        this.messageStatusService = messageStatusService;
        this.messageStatusQueryService = messageStatusQueryService;
    }

    /**
     * POST  /message-statuses : Create a new messageStatus.
     *
     * @param messageStatusDTO the messageStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messageStatusDTO, or with status 400 (Bad Request) if the messageStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/message-statuses")
    @Timed
    public ResponseEntity<MessageStatusDTO> createMessageStatus(@Valid @RequestBody MessageStatusDTO messageStatusDTO) throws URISyntaxException {
        log.debug("REST request to save MessageStatus : {}", messageStatusDTO);
        if (messageStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new messageStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageStatusDTO result = messageStatusService.save(messageStatusDTO);
        return ResponseEntity.created(new URI("/api/message-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /message-statuses : Updates an existing messageStatus.
     *
     * @param messageStatusDTO the messageStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messageStatusDTO,
     * or with status 400 (Bad Request) if the messageStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the messageStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/message-statuses")
    @Timed
    public ResponseEntity<MessageStatusDTO> updateMessageStatus(@Valid @RequestBody MessageStatusDTO messageStatusDTO) throws URISyntaxException {
        log.debug("REST request to update MessageStatus : {}", messageStatusDTO);
        if (messageStatusDTO.getId() == null) {
            return createMessageStatus(messageStatusDTO);
        }
        MessageStatusDTO result = messageStatusService.save(messageStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, messageStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /message-statuses : get all the messageStatuses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of messageStatuses in body
     */
    @GetMapping("/message-statuses")
    @Timed
    public ResponseEntity<List<MessageStatusDTO>> getAllMessageStatuses(MessageStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MessageStatuses by criteria: {}", criteria);
        Page<MessageStatusDTO> page = messageStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/message-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /message-statuses/:id : get the "id" messageStatus.
     *
     * @param id the id of the messageStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messageStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/message-statuses/{id}")
    @Timed
    public ResponseEntity<MessageStatusDTO> getMessageStatus(@PathVariable Long id) {
        log.debug("REST request to get MessageStatus : {}", id);
        MessageStatusDTO messageStatusDTO = messageStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(messageStatusDTO));
    }

    /**
     * DELETE  /message-statuses/:id : delete the "id" messageStatus.
     *
     * @param id the id of the messageStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/message-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteMessageStatus(@PathVariable Long id) {
        log.debug("REST request to delete MessageStatus : {}", id);
        messageStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/message-statuses?query=:query : search for the messageStatus corresponding
     * to the query.
     *
     * @param query the query of the messageStatus search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/message-statuses")
    @Timed
    public ResponseEntity<List<MessageStatusDTO>> searchMessageStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MessageStatuses for query {}", query);
        Page<MessageStatusDTO> page = messageStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/message-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
