package com.qigu.readword.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.qigu.readword.domain.enumeration.OrderStatus;

/**
 * A DTO for the VipOrder entity.
 */
public class VipOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createTime;

    private Instant paymentTime;

    @NotNull
    private Double totalPrice;

    @NotNull
    private Integer months;

    private String transactionId;

    private String outTradeNo;

    private String tradeType;

    @Lob
    private String paymentResult;

    @NotNull
    private OrderStatus status;

    @NotNull
    private String openId;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(String paymentResult) {
        this.paymentResult = paymentResult;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VipOrderDTO vipOrderDTO = (VipOrderDTO) o;
        if(vipOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vipOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VipOrderDTO{" +
            "id=" + getId() +
            ", createTime='" + getCreateTime() + "'" +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", months=" + getMonths() +
            ", transactionId='" + getTransactionId() + "'" +
            ", outTradeNo='" + getOutTradeNo() + "'" +
            ", tradeType='" + getTradeType() + "'" +
            ", paymentResult='" + getPaymentResult() + "'" +
            ", status='" + getStatus() + "'" +
            ", openId='" + getOpenId() + "'" +
            "}";
    }
}
