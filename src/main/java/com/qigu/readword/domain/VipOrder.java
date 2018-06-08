package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A VipOrder.
 */
@Entity
@Table(name = "vip_order")
@Document(indexName = "viporder")
public class VipOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "payment_time")
    private Instant paymentTime;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @NotNull
    @Column(name = "months", nullable = false)
    private Integer months;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "out_trade_no")
    private String outTradeNo;

    @Column(name = "trade_type")
    private String tradeType;

    @Lob
    @Column(name = "payment_result")
    private String paymentResult;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public VipOrder createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getPaymentTime() {
        return paymentTime;
    }

    public VipOrder paymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
        return this;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public VipOrder totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getMonths() {
        return months;
    }

    public VipOrder months(Integer months) {
        this.months = months;
        return this;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public VipOrder transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public VipOrder outTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public VipOrder tradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPaymentResult() {
        return paymentResult;
    }

    public VipOrder paymentResult(String paymentResult) {
        this.paymentResult = paymentResult;
        return this;
    }

    public void setPaymentResult(String paymentResult) {
        this.paymentResult = paymentResult;
    }

    public User getUser() {
        return user;
    }

    public VipOrder user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VipOrder vipOrder = (VipOrder) o;
        if (vipOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vipOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VipOrder{" +
            "id=" + getId() +
            ", createTime='" + getCreateTime() + "'" +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", months=" + getMonths() +
            ", transactionId='" + getTransactionId() + "'" +
            ", outTradeNo='" + getOutTradeNo() + "'" +
            ", tradeType='" + getTradeType() + "'" +
            ", paymentResult='" + getPaymentResult() + "'" +
            "}";
    }
}
