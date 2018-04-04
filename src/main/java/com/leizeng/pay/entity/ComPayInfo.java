package com.leizeng.pay.entity;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/30 上午11:46
 */
public class ComPayInfo {
    private Integer id;
    private Integer comid;
    private String appid;
    private String mchId;
    private String key;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComid() {
        return comid;
    }

    public void setComid(Integer comid) {
        this.comid = comid;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ComPayInfo [id=" + id
                + ",comid=" + comid
                + ",appid=" + appid
                + ",mchId=" + mchId
                + ",key=" + key
                + ",type=" + type
                + "]";
    }
}
