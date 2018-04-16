package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, MessageContentMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "img.id", target = "imgId")
    @Mapping(source = "img.name", target = "imgName")
    @Mapping(source = "content.id", target = "contentId")
    @Mapping(source = "content.name", target = "contentName")
    MessageDTO toDto(Message message);

    @Mapping(source = "imgId", target = "img")
    @Mapping(source = "contentId", target = "content")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
