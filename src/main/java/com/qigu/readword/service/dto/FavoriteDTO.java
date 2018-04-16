package com.qigu.readword.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Favorite entity.
 */
public class FavoriteDTO implements Serializable {

    private Long id;

    private Long userId;

    private String userLogin;

    private Set<WordDTO> words = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<WordDTO> getWords() {
        return words;
    }

    public void setWords(Set<WordDTO> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FavoriteDTO favoriteDTO = (FavoriteDTO) o;
        if(favoriteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favoriteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavoriteDTO{" +
            "id=" + getId() +
            "}";
    }
}
