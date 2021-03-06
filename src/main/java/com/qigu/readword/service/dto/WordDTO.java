package com.qigu.readword.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * A DTO for the Word entity.
 */
public class WordDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double rank;

    @Lob
    private String desctription;

    private LifeStatus lifeStatus;

    private Long imgId;

    private String imgName;

    private String imgUrl;

    private Long audioId;

    private String audioName;

    private String audioUrl;

    private String audio1Url;

    private Long userId;

    private String userLogin;

    private Long wordGroupId;

    private String wordGroupName;

    private Boolean isFavorite;

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

    public String getDesctription() {
        return desctription;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
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

    public Long getAudioId() {
        return audioId;
    }

    public void setAudioId(Long audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
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

    public Long getWordGroupId() {
        return wordGroupId;
    }

    public void setWordGroupId(Long wordGroupId) {
        this.wordGroupId = wordGroupId;
    }

    public String getWordGroupName() {
        return wordGroupName;
    }

    public void setWordGroupName(String wordGroupName) {
        this.wordGroupName = wordGroupName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudio1Url() {
        return audio1Url;
    }

    public void setAudio1Url(String audio1Url) {
        this.audio1Url = audio1Url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordDTO wordDTO = (WordDTO) o;
        if (wordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WordDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", desctription='" + getDesctription() + "'" +
            ", lifeStatus='" + getLifeStatus() + "'" +
            "}";
    }
}
