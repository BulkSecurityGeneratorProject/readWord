package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.MessageContentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MessageContent and its DTO MessageContentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MessageContentMapper extends EntityMapper<MessageContentDTO, MessageContent> {



    default MessageContent fromId(Long id) {
        if (id == null) {
            return null;
        }
        MessageContent messageContent = new MessageContent();
        messageContent.setId(id);
        return messageContent;
    }
}
