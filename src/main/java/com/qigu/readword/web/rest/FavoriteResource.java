package com.qigu.readword.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qigu.readword.service.FavoriteService;
import com.qigu.readword.web.rest.errors.BadRequestAlertException;
import com.qigu.readword.web.rest.util.HeaderUtil;
import com.qigu.readword.web.rest.util.PaginationUtil;
import com.qigu.readword.service.dto.FavoriteDTO;
import com.qigu.readword.service.dto.FavoriteCriteria;
import com.qigu.readword.service.FavoriteQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Favorite.
 */
@RestController
@RequestMapping("/api")
public class FavoriteResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteResource.class);

    private static final String ENTITY_NAME = "favorite";

    private final FavoriteService favoriteService;

    private final FavoriteQueryService favoriteQueryService;

    public FavoriteResource(FavoriteService favoriteService, FavoriteQueryService favoriteQueryService) {
        this.favoriteService = favoriteService;
        this.favoriteQueryService = favoriteQueryService;
    }

    /**
     * POST  /favorites : Create a new favorite.
     *
     * @param favoriteDTO the favoriteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favoriteDTO, or with status 400 (Bad Request) if the favorite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favorites")
    @Timed
    public ResponseEntity<FavoriteDTO> createFavorite(@RequestBody FavoriteDTO favoriteDTO) throws URISyntaxException {
        log.debug("REST request to save Favorite : {}", favoriteDTO);
        if (favoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new favorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteDTO result = favoriteService.save(favoriteDTO);
        return ResponseEntity.created(new URI("/api/favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favorites : Updates an existing favorite.
     *
     * @param favoriteDTO the favoriteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favoriteDTO,
     * or with status 400 (Bad Request) if the favoriteDTO is not valid,
     * or with status 500 (Internal Server Error) if the favoriteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/favorites")
    @Timed
    public ResponseEntity<FavoriteDTO> updateFavorite(@RequestBody FavoriteDTO favoriteDTO) throws URISyntaxException {
        log.debug("REST request to update Favorite : {}", favoriteDTO);
        if (favoriteDTO.getId() == null) {
            return createFavorite(favoriteDTO);
        }
        FavoriteDTO result = favoriteService.save(favoriteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favoriteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favorites : get all the favorites.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of favorites in body
     */
    @GetMapping("/favorites")
    @Timed
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites(FavoriteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Favorites by criteria: {}", criteria);
        Page<FavoriteDTO> page = favoriteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favorites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /favorites/:id : get the "id" favorite.
     *
     * @param id the id of the favoriteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favoriteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/favorites/{id}")
    @Timed
    public ResponseEntity<FavoriteDTO> getFavorite(@PathVariable Long id) {
        log.debug("REST request to get Favorite : {}", id);
        FavoriteDTO favoriteDTO = favoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(favoriteDTO));
    }

    /**
     * DELETE  /favorites/:id : delete the "id" favorite.
     *
     * @param id the id of the favoriteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favorites/{id}")
    @Timed
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        log.debug("REST request to delete Favorite : {}", id);
        favoriteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/favorites?query=:query : search for the favorite corresponding
     * to the query.
     *
     * @param query the query of the favorite search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/favorites")
    @Timed
    public ResponseEntity<List<FavoriteDTO>> searchFavorites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Favorites for query {}", query);
        Page<FavoriteDTO> page = favoriteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/favorites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
