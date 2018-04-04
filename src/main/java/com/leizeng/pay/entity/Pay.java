package com.leizeng.pay.entity;

import java.math.BigDecimal;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/27 上午10:14
 */
public class Pay {
    private Integer id;
    private String payId;//唯一id
    private Integer orderId;//订单id
    private BigDecimal amount;//交易金额
    private String productName;//商品名称
    private Integer payType;//支付渠道(1：微信扫码支付；2：支付宝支付；)
    private Integer payStatus;//支付状态(1：未支付；2：已支付)
    private Integer createdId;//创建人id
    private Integer createdAt;//创建时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayid() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Pay [id=" + id
                + ", payId=" + payId
                + ", orderId=" + orderId
                + ", amount=" + amount
                + ", productName=" + productName
                + ", payType=" + payType
                + ", payStatus=" + payStatus
                + ", createdId=" + createdId
                + ", createdAt=" + createdAt
                +"]";
    }
}
