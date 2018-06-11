package com.qigu.readword.service.dto;

import java.io.Serializable;
import com.qigu.readword.domain.enumeration.VipOrderStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the VipOrder entity. This class is used in VipOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /vip-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VipOrderCriteria implements Serializable {
    /**
     * Class for filtering VipOrderStatus
     */
    public static class VipOrderStatusFilter extends Filter<VipOrderStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter createTime;

    private InstantFilter paymentTime;

    private DoubleFilter totalPrice;

    private IntegerFilter months;

    private StringFilter transactionId;

    private StringFilter outTradeNo;

    private StringFilter tradeType;

    private VipOrderStatusFilter status;

    private StringFilter openId;

    private LongFilter productId;

    private LongFilter userId;

    public VipOrderCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreateTime() {
        return createTime;
    }

    public void setCreateTime(InstantFilter createTime) {
        this.createTime = createTime;
    }

    public InstantFilter getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(InstantFilter paymentTime) {
        this.paymentTime = paymentTime;
    }

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public IntegerFilter getMonths() {
        return months;
    }

    public void setMonths(IntegerFilter months) {
        this.months = months;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public StringFilter getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(StringFilter outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public StringFilter getTradeType() {
        return tradeType;
    }

    public void setTradeType(StringFilter tradeType) {
        this.tradeType = tradeType;
    }

    public VipOrderStatusFilter getStatus() {
        return status;
    }

    public void setStatus(VipOrderStatusFilter status) {
        this.status = status;
    }

    public StringFilter getOpenId() {
        return openId;
    }

    public void setOpenId(StringFilter openId) {
        this.openId = openId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VipOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createTime != null ? "createTime=" + createTime + ", " : "") +
                (paymentTime != null ? "paymentTime=" + paymentTime + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (months != null ? "months=" + months + ", " : "") +
                (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
                (outTradeNo != null ? "outTradeNo=" + outTradeNo + ", " : "") +
                (tradeType != null ? "tradeType=" + tradeType + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (openId != null ? "openId=" + openId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
