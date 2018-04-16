package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.MessageStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MessageStatus and its DTO MessageStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserMapper.class})
public interface MessageStatusMapper extends EntityMapper<MessageStatusDTO, MessageStatus> {

    @Mapping(source = "msg.id", target = "msgId")
    @Mapping(source = "msg.name", target = "msgName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    MessageStatusDTO toDto(MessageStatus messageStatus);

    @Mapping(source = "msgId", target = "msg")
    @Mapping(source = "userId", target = "user")
    MessageStatus toEntity(MessageStatusDTO messageStatusDTO);

    default MessageStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        MessageStatus messageStatus = new MessageStatus();
        messageStatus.setId(id);
        return messageStatus;
    }
}
