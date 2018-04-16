package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.AudioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Audio and its DTO AudioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AudioMapper extends EntityMapper<AudioDTO, Audio> {



    default Audio fromId(Long id) {
        if (id == null) {
            return null;
        }
        Audio audio = new Audio();
        audio.setId(id);
        return audio;
    }
}
