package com.qigu.readword.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WordGroup entity.
 */
public class WordGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double rank;

    private Long imgId;

    private String imgName;

    private String imgUrl;

    private Long userId;

    private String userLogin;


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

        WordGroupDTO wordGroupDTO = (WordGroupDTO) o;
        if (wordGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wordGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WordGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", imgUrl=" + getImgUrl() +
            "}";
    }
}
