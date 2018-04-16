package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.qigu.readword.domain.enumeration.MessageStatusEnum;

/**
 * A MessageStatus.
 */
@Entity
@Table(name = "message_status")
@Document(indexName = "messagestatus")
public class MessageStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatusEnum status;

    @OneToOne
    @JoinColumn(unique = true)
    private Message msg;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public MessageStatus status(MessageStatusEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public Message getMsg() {
        return msg;
    }

    public MessageStatus msg(Message message) {
        this.msg = message;
        return this;
    }

    public void setMsg(Message message) {
        this.msg = message;
    }

    public User getUser() {
        return user;
    }

    public MessageStatus user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageStatus messageStatus = (MessageStatus) o;
        if (messageStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
