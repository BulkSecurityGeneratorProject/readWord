package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.MessageContentService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.MessageContentDTO;
import com.qigu.readword.service.dto.MessageContentCriteria;
import com.qigu.readword.service.MessageContentQueryService;
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
 * REST controller for managing MessageContent.
 */
@RestController
@RequestMapping("/api")
public class MessageContentResource {

    private final Logger log = LoggerFactory.getLogger(MessageContentResource.class);

    private static final String ENTITY_NAME = "messageContent";

    private final MessageContentService messageContentService;

    private final MessageContentQueryService messageContentQueryService;

    public MessageContentResource(MessageContentService messageContentService, MessageContentQueryService messageContentQueryService) {
        this.messageContentService = messageContentService;
        this.messageContentQueryService = messageContentQueryService;
    }

    /**
     * POST  /message-contents : Create a new messageContent.
     *
     * @param messageContentDTO the messageContentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messageContentDTO, or with status 400 (Bad Request) if the messageContent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/message-contents")
    @Timed
    public ResponseEntity<MessageContentDTO> createMessageContent(@Valid @RequestBody MessageContentDTO messageContentDTO) throws URISyntaxException {
        log.debug("REST request to save MessageContent : {}", messageContentDTO);
        if (messageContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new messageContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageContentDTO result = messageContentService.save(messageContentDTO);
        return ResponseEntity.created(new URI("/api/message-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /message-contents : Updates an existing messageContent.
     *
     * @param messageContentDTO the messageContentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messageContentDTO,
     * or with status 400 (Bad Request) if the messageContentDTO is not valid,
     * or with status 500 (Internal Server Error) if the messageContentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/message-contents")
    @Timed
    public ResponseEntity<MessageContentDTO> updateMessageContent(@Valid @RequestBody MessageContentDTO messageContentDTO) throws URISyntaxException {
        log.debug("REST request to update MessageContent : {}", messageContentDTO);
        if (messageContentDTO.getId() == null) {
            return createMessageContent(messageContentDTO);
        }
        MessageContentDTO result = messageContentService.save(messageContentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, messageContentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /message-contents : get all the messageContents.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of messageContents in body
     */
    @GetMapping("/message-contents")
    @Timed
    public ResponseEntity<List<MessageContentDTO>> getAllMessageContents(MessageContentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MessageContents by criteria: {}", criteria);
        Page<MessageContentDTO> page = messageContentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/message-contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /message-contents/:id : get the "id" messageContent.
     *
     * @param id the id of the messageContentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messageContentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/message-contents/{id}")
    @Timed
    public ResponseEntity<MessageContentDTO> getMessageContent(@PathVariable Long id) {
        log.debug("REST request to get MessageContent : {}", id);
        MessageContentDTO messageContentDTO = messageContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(messageContentDTO));
    }

    /**
     * DELETE  /message-contents/:id : delete the "id" messageContent.
     *
     * @param id the id of the messageContentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/message-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteMessageContent(@PathVariable Long id) {
        log.debug("REST request to delete MessageContent : {}", id);
        messageContentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/message-contents?query=:query : search for the messageContent corresponding
     * to the query.
     *
     * @param query the query of the messageContent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/message-contents")
    @Timed
    public ResponseEntity<List<MessageContentDTO>> searchMessageContents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MessageContents for query {}", query);
        Page<MessageContentDTO> page = messageContentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/message-contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
