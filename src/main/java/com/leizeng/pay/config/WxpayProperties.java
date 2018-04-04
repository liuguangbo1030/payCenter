package com.leizeng.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/30 上午10:22
 */
@Component
@ConfigurationProperties(prefix = "wxpay")
public class WxpayProperties {
    private String unifiedorderurl;
    private Integer timeout;
    private String notifyurl;
    private String appid;
    private String key;
    private String mchid;
    private String appsecret;

    public String getUnifiedorderurl() {
        return unifiedorderurl;
    }

    public void setUnifiedorderurl(String unifiedorderurl) {
        this.unifiedorderurl = unifiedorderurl;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
