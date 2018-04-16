package com.qigu.readword.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant sendTime;

    private Long imgId;

    private String imgName;

    private Long contentId;

    private String contentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getSendTime() {
        return sendTime;
    }

    public void setSendTime(Instant sendTime) {
        this.sendTime = sendTime;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imageId) {
        this.imgId = imageId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imageName) {
        this.imgName = imageName;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long messageContentId) {
        this.contentId = messageContentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String messageContentName) {
        this.contentName = messageContentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if(messageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            "}";
    }
}
