package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.WordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Word and its DTO WordDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, AudioMapper.class, UserMapper.class, WordGroupMapper.class})
public interface WordMapper extends EntityMapper<WordDTO, Word> {

    @Mapping(source = "img.id", target = "imgId")
    @Mapping(source = "img.name", target = "imgName")
    @Mapping(source = "img.url", target = "imgUrl")
    @Mapping(source = "audio.id", target = "audioId")
    @Mapping(source = "audio.name", target = "audioName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "wordGroup.id", target = "wordGroupId")
    @Mapping(source = "wordGroup.name", target = "wordGroupName")
    WordDTO toDto(Word word);

    @Mapping(source = "imgId", target = "img")
    @Mapping(source = "audioId", target = "audio")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "wordGroupId", target = "wordGroup")
    @Mapping(target = "favorites", ignore = true)
    Word toEntity(WordDTO wordDTO);

    default Word fromId(Long id) {
        if (id == null) {
            return null;
        }
        Word word = new Word();
        word.setId(id);
        return word;
    }
}
