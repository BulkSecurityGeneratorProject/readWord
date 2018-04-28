package com.qigu.readword.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Audio entity.
 */
public class AudioDTO implements Serializable {

    private Long id;

    private String url;

    private String oneSpeedUrl;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOneSpeedUrl() {
        return oneSpeedUrl;
    }

    public void setOneSpeedUrl(String oneSpeedUrl) {
        this.oneSpeedUrl = oneSpeedUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AudioDTO audioDTO = (AudioDTO) o;
        if(audioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), audioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AudioDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", oneSpeedUrl='" + getOneSpeedUrl() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
