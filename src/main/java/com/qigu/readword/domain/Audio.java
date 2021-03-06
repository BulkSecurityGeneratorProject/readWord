package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Audio.
 */
@Entity
@Table(name = "audio")
@Document(indexName = "audio")
public class Audio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "one_speed_url")
    private String oneSpeedUrl;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Audio url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOneSpeedUrl() {
        return oneSpeedUrl;
    }

    public Audio oneSpeedUrl(String oneSpeedUrl) {
        this.oneSpeedUrl = oneSpeedUrl;
        return this;
    }

    public void setOneSpeedUrl(String oneSpeedUrl) {
        this.oneSpeedUrl = oneSpeedUrl;
    }

    public String getName() {
        return name;
    }

    public Audio name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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
        Audio audio = (Audio) o;
        if (audio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), audio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Audio{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", oneSpeedUrl='" + getOneSpeedUrl() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
