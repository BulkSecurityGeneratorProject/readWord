package com.qigu.readword.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * A DTO for the Slide entity.
 */
public class SlideDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double rank;

    private LifeStatus lifeStatus;

    private Long imgId;

    private String imgName;

    private String imgUrl;

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

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public LifeStatus getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SlideDTO slideDTO = (SlideDTO) o;
        if(slideDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SlideDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", lifeStatus='" + getLifeStatus() + "'" +
            "}";
    }
}
