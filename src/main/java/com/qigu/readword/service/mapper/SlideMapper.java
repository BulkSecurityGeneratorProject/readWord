package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.SlideDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Slide and its DTO SlideDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface SlideMapper extends EntityMapper<SlideDTO, Slide> {

    @Mapping(source = "img.id", target = "imgId")
    @Mapping(source = "img.name", target = "imgName")
    @Mapping(source = "img.url", target = "imgUrl")
    SlideDTO toDto(Slide slide);

    @Mapping(source = "imgId", target = "img")
    Slide toEntity(SlideDTO slideDTO);

    default Slide fromId(Long id) {
        if (id == null) {
            return null;
        }
        Slide slide = new Slide();
        slide.setId(id);
        return slide;
    }
}
