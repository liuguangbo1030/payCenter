package com.leizeng.pay.entity;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/2/2 上午11:47
 */
public class PayCallback {
    private Integer id;
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String openid;
    private String tradeType;
    private Integer totalFee;
    private String transactionId;
    private String outTradeNo;
    private String timeEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
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

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public String toString() {
        return "PayCallback [id=" + id
                + ", appid=" + appid
                + ", mchId=" + mchId
                + ", nonceStr=" + nonceStr
                + ", sign=" + sign
                + ", openid=" + openid
                + ", tradeType=" + tradeType
                + ", totalFee=" + totalFee
                + ", transactionId=" + transactionId
                + ", outTradeNo=" + outTradeNo
                + ", timeEnd=" + timeEnd
                +"]";
    }
}
