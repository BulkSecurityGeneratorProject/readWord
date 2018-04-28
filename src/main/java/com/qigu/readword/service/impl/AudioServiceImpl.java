package com.qigu.readword.service.impl;

import com.qigu.readword.mycode.baidu.service.BaiduAudioService;
import com.qigu.readword.mycode.util.ReadWordConstants;
import com.qigu.readword.service.AudioService;
import com.qigu.readword.domain.Audio;
import com.qigu.readword.repository.AudioRepository;
import com.qigu.readword.repository.search.AudioSearchRepository;
import com.qigu.readword.service.dto.AudioDTO;
import com.qigu.readword.service.mapper.AudioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Audio.
 */
@Service
@Transactional
public class AudioServiceImpl implements AudioService {

    private final Logger log = LoggerFactory.getLogger(AudioServiceImpl.class);

    private final AudioRepository audioRepository;

    private final AudioMapper audioMapper;

    private final AudioSearchRepository audioSearchRepository;

    private final BaiduAudioService baiduAudioService;

    public AudioServiceImpl(AudioRepository audioRepository, AudioMapper audioMapper, AudioSearchRepository audioSearchRepository, BaiduAudioService baiduAudioService) {
        this.audioRepository = audioRepository;
        this.audioMapper = audioMapper;
        this.audioSearchRepository = audioSearchRepository;
        this.baiduAudioService = baiduAudioService;
    }

    /**
     * Save a audio.
     *
     * @param audioDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AudioDTO save(AudioDTO audioDTO) {
        log.debug("Request to save Audio : {}", audioDTO);
        Audio audio = audioMapper.toEntity(audioDTO);
        audio = audioRepository.save(audio);
        Optional<String> audioPath = baiduAudioService.createAudio(audio.getId().toString(), audio.getName());
        HashMap<String, Object> options = new HashMap<>();
        options.put(ReadWordConstants.BAIDU_VOICE_SPEED_KEY, 1);
        Optional<String> audioSpeedOnePath = baiduAudioService.createAudioByOptions(audio.getId().toString(), audio.getName(), options);
        if (audioPath.isPresent()) {
            audio.setUrl(audioPath.get());
            audio = audioRepository.save(audio);
        }
        if (audioSpeedOnePath.isPresent()) {
            audio.setOneSpeedUrl(audioSpeedOnePath.get());
            audio = audioRepository.save(audio);
        }
        AudioDTO result = audioMapper.toDto(audio);
        audioSearchRepository.save(audio);
        return result;
    }

    /**
     * Get all the audio.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AudioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Audio");
        return audioRepository.findAll(pageable)
            .map(audioMapper::toDto);
    }

    /**
     * Get one audio by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AudioDTO findOne(Long id) {
        log.debug("Request to get Audio : {}", id);
        Audio audio = audioRepository.findOne(id);
        return audioMapper.toDto(audio);
    }

    /**
     * Delete the audio by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Audio : {}", id);
        audioRepository.delete(id);
        audioSearchRepository.delete(id);
    }

    /**
     * Search for the audio corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AudioDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Audio for query {}", query);
        Page<Audio> result = audioSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(audioMapper::toDto);
    }
}
