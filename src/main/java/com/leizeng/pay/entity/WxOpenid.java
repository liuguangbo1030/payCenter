package com.leizeng.pay.entity;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/31 下午10:02
 */
public class WxOpenid {
    private Integer id;
    private String carnumber;
    private String appid;
    private String openid;
    private Integer createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Pay [id=" + id
                + ", carnumber=" + carnumber
                + ", appid=" + appid
                + ", openid=" + openid
                + ", createtime=" + createtime
                +"]";
    }
}
