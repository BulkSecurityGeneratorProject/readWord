package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.WordGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WordGroup and its DTO WordGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, UserMapper.class})
public interface WordGroupMapper extends EntityMapper<WordGroupDTO, WordGroup> {

    @Mapping(source = "img.id", target = "imgId")
    @Mapping(source = "img.name", target = "imgName")
    @Mapping(source = "img.url", target = "imgUrl")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    WordGroupDTO toDto(WordGroup wordGroup);

    @Mapping(source = "imgId", target = "img")
    @Mapping(source = "userId", target = "user")
    WordGroup toEntity(WordGroupDTO wordGroupDTO);

    default WordGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        WordGroup wordGroup = new WordGroup();
        wordGroup.setId(id);
        return wordGroup;
    }
}
