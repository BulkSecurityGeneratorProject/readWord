package com.qigu.readword.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Message entity. This class is used in MessageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private InstantFilter sendTime;

    private LongFilter imgId;

    private LongFilter contentId;

    public MessageCriteria() {
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

    public InstantFilter getSendTime() {
        return sendTime;
    }

    public void setSendTime(InstantFilter sendTime) {
        this.sendTime = sendTime;
    }

    public LongFilter getImgId() {
        return imgId;
    }

    public void setImgId(LongFilter imgId) {
        this.imgId = imgId;
    }

    public LongFilter getContentId() {
        return contentId;
    }

    public void setContentId(LongFilter contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return "MessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (sendTime != null ? "sendTime=" + sendTime + ", " : "") +
                (imgId != null ? "imgId=" + imgId + ", " : "") +
                (contentId != null ? "contentId=" + contentId + ", " : "") +
            "}";
    }

}
