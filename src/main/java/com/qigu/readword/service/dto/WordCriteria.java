package com.qigu.readword.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Word entity. This class is used in WordResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /words?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WordCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private DoubleFilter rank;

    private LongFilter imgId;

    private LongFilter audioId;

    private LongFilter userId;

    private LongFilter wordGroupId;

    private LongFilter favoritesId;

    public WordCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getRank() {
        return rank;
    }

    public void setRank(DoubleFilter rank) {
        this.rank = rank;
    }

    public LongFilter getImgId() {
        return imgId;
    }

    public void setImgId(LongFilter imgId) {
        this.imgId = imgId;
    }

    public LongFilter getAudioId() {
        return audioId;
    }

    public void setAudioId(LongFilter audioId) {
        this.audioId = audioId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getWordGroupId() {
        return wordGroupId;
    }

    public void setWordGroupId(LongFilter wordGroupId) {
        this.wordGroupId = wordGroupId;
    }

    public LongFilter getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(LongFilter favoritesId) {
        this.favoritesId = favoritesId;
    }

    @Override
    public String toString() {
        return "WordCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (rank != null ? "rank=" + rank + ", " : "") +
                (imgId != null ? "imgId=" + imgId + ", " : "") +
                (audioId != null ? "audioId=" + audioId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (wordGroupId != null ? "wordGroupId=" + wordGroupId + ", " : "") +
                (favoritesId != null ? "favoritesId=" + favoritesId + ", " : "") +
            "}";
    }

}
