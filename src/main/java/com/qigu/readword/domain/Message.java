package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "send_time", nullable = false)
    private Instant sendTime;

    @OneToOne
    @JoinColumn(unique = true)
    private Image img;

    @OneToOne
    @JoinColumn(unique = true)
    private MessageContent content;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Message name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getSendTime() {
        return sendTime;
    }

    public Message sendTime(Instant sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public void setSendTime(Instant sendTime) {
        this.sendTime = sendTime;
    }

    public Image getImg() {
        return img;
    }

    public Message img(Image image) {
        this.img = image;
        return this;
    }

    public void setImg(Image image) {
        this.img = image;
    }

    public MessageContent getContent() {
        return content;
    }

    public Message content(MessageContent messageContent) {
        this.content = messageContent;
        return this;
    }

    public void setContent(MessageContent messageContent) {
        this.content = messageContent;
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
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            "}";
    }
}
