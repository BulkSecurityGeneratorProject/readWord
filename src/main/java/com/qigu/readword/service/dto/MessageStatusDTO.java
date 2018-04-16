package com.qigu.readword.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.qigu.readword.domain.enumeration.MessageStatusEnum;

/**
 * A DTO for the MessageStatus entity.
 */
public class MessageStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private MessageStatusEnum status;

    private Long msgId;

    private String msgName;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long messageId) {
        this.msgId = messageId;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String messageName) {
        this.msgName = messageName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageStatusDTO messageStatusDTO = (MessageStatusDTO) o;
        if(messageStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageStatusDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
