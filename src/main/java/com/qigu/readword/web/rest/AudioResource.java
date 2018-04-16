package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.AudioService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.AudioDTO;
import com.qigu.readword.service.dto.AudioCriteria;
import com.qigu.readword.service.AudioQueryService;
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
 * REST controller for managing Audio.
 */
@RestController
@RequestMapping("/api")
public class AudioResource {

    private final Logger log = LoggerFactory.getLogger(AudioResource.class);

    private static final String ENTITY_NAME = "audio";

    private final AudioService audioService;

    private final AudioQueryService audioQueryService;

    public AudioResource(AudioService audioService, AudioQueryService audioQueryService) {
        this.audioService = audioService;
        this.audioQueryService = audioQueryService;
    }

    /**
     * POST  /audio : Create a new audio.
     *
     * @param audioDTO the audioDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new audioDTO, or with status 400 (Bad Request) if the audio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/audio")
    @Timed
    public ResponseEntity<AudioDTO> createAudio(@Valid @RequestBody AudioDTO audioDTO) throws URISyntaxException {
        log.debug("REST request to save Audio : {}", audioDTO);
        if (audioDTO.getId() != null) {
            throw new BadRequestAlertException("A new audio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AudioDTO result = audioService.save(audioDTO);
        return ResponseEntity.created(new URI("/api/audio/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audio : Updates an existing audio.
     *
     * @param audioDTO the audioDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated audioDTO,
     * or with status 400 (Bad Request) if the audioDTO is not valid,
     * or with status 500 (Internal Server Error) if the audioDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/audio")
    @Timed
    public ResponseEntity<AudioDTO> updateAudio(@Valid @RequestBody AudioDTO audioDTO) throws URISyntaxException {
        log.debug("REST request to update Audio : {}", audioDTO);
        if (audioDTO.getId() == null) {
            return createAudio(audioDTO);
        }
        AudioDTO result = audioService.save(audioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, audioDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audio : get all the audio.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of audio in body
     */
    @GetMapping("/audio")
    @Timed
    public ResponseEntity<List<AudioDTO>> getAllAudio(AudioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Audio by criteria: {}", criteria);
        Page<AudioDTO> page = audioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audio");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /audio/:id : get the "id" audio.
     *
     * @param id the id of the audioDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the audioDTO, or with status 404 (Not Found)
     */
    @GetMapping("/audio/{id}")
    @Timed
    public ResponseEntity<AudioDTO> getAudio(@PathVariable Long id) {
        log.debug("REST request to get Audio : {}", id);
        AudioDTO audioDTO = audioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(audioDTO));
    }

    /**
     * DELETE  /audio/:id : delete the "id" audio.
     *
     * @param id the id of the audioDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/audio/{id}")
    @Timed
    public ResponseEntity<Void> deleteAudio(@PathVariable Long id) {
        log.debug("REST request to delete Audio : {}", id);
        audioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/audio?query=:query : search for the audio corresponding
     * to the query.
     *
     * @param query the query of the audio search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/audio")
    @Timed
    public ResponseEntity<List<AudioDTO>> searchAudio(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Audio for query {}", query);
        Page<AudioDTO> page = audioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/audio");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
