package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.WordGroupService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.WordGroupDTO;
import com.qigu.readword.service.dto.WordGroupCriteria;
import com.qigu.readword.service.WordGroupQueryService;
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
 * REST controller for managing WordGroup.
 */
@RestController
@RequestMapping("/api")
public class WordGroupResource {

    private final Logger log = LoggerFactory.getLogger(WordGroupResource.class);

    private static final String ENTITY_NAME = "wordGroup";

    private final WordGroupService wordGroupService;

    private final WordGroupQueryService wordGroupQueryService;

    public WordGroupResource(WordGroupService wordGroupService, WordGroupQueryService wordGroupQueryService) {
        this.wordGroupService = wordGroupService;
        this.wordGroupQueryService = wordGroupQueryService;
    }

    /**
     * POST  /word-groups : Create a new wordGroup.
     *
     * @param wordGroupDTO the wordGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wordGroupDTO, or with status 400 (Bad Request) if the wordGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/word-groups")
    @Timed
    public ResponseEntity<WordGroupDTO> createWordGroup(@Valid @RequestBody WordGroupDTO wordGroupDTO) throws URISyntaxException {
        log.debug("REST request to save WordGroup : {}", wordGroupDTO);
        if (wordGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new wordGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WordGroupDTO result = wordGroupService.save(wordGroupDTO);
        return ResponseEntity.created(new URI("/api/word-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /word-groups : Updates an existing wordGroup.
     *
     * @param wordGroupDTO the wordGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wordGroupDTO,
     * or with status 400 (Bad Request) if the wordGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the wordGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/word-groups")
    @Timed
    public ResponseEntity<WordGroupDTO> updateWordGroup(@Valid @RequestBody WordGroupDTO wordGroupDTO) throws URISyntaxException {
        log.debug("REST request to update WordGroup : {}", wordGroupDTO);
        if (wordGroupDTO.getId() == null) {
            return createWordGroup(wordGroupDTO);
        }
        WordGroupDTO result = wordGroupService.save(wordGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /word-groups : get all the wordGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wordGroups in body
     */
    @GetMapping("/word-groups")
    @Timed
    public ResponseEntity<List<WordGroupDTO>> getAllWordGroups(WordGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WordGroups by criteria: {}", criteria);
        Page<WordGroupDTO> page = wordGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/word-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /word-groups/:id : get the "id" wordGroup.
     *
     * @param id the id of the wordGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wordGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/word-groups/{id}")
    @Timed
    public ResponseEntity<WordGroupDTO> getWordGroup(@PathVariable Long id) {
        log.debug("REST request to get WordGroup : {}", id);
        WordGroupDTO wordGroupDTO = wordGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wordGroupDTO));
    }

    /**
     * DELETE  /word-groups/:id : delete the "id" wordGroup.
     *
     * @param id the id of the wordGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/word-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteWordGroup(@PathVariable Long id) {
        log.debug("REST request to delete WordGroup : {}", id);
        wordGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/word-groups?query=:query : search for the wordGroup corresponding
     * to the query.
     *
     * @param query the query of the wordGroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/word-groups")
    @Timed
    public ResponseEntity<List<WordGroupDTO>> searchWordGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WordGroups for query {}", query);
        Page<WordGroupDTO> page = wordGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/word-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
