package com.qigu.readword.service.dto;

import java.io.Serializable;
import com.qigu.readword.domain.enumeration.MessageStatusEnum;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the MessageStatus entity. This class is used in MessageStatusResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /message-statuses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageStatusCriteria implements Serializable {
    /**
     * Class for filtering MessageStatusEnum
     */
    public static class MessageStatusEnumFilter extends Filter<MessageStatusEnum> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private MessageStatusEnumFilter status;

    private LongFilter msgId;

    private LongFilter userId;

    public MessageStatusCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public MessageStatusEnumFilter getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnumFilter status) {
        this.status = status;
    }

    public LongFilter getMsgId() {
        return msgId;
    }

    public void setMsgId(LongFilter msgId) {
        this.msgId = msgId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MessageStatusCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (msgId != null ? "msgId=" + msgId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
