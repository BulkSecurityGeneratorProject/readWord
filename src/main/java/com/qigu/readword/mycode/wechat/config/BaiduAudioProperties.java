package com.qigu.readword.mycode.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "baidu.audio")
public class BaiduAudioProperties {

    private String appId;
    private String appKey;
    private String secretKey;
    private String savePrePath;
    private String urlPrePath;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSavePrePath() {
        return savePrePath;
    }

    public void setSavePrePath(String savePrePath) {
        this.savePrePath = savePrePath;
    }

    public String getUrlPrePath() {
        return urlPrePath;
    }

    public void setUrlPrePath(String urlPrePath) {
        this.urlPrePath = urlPrePath;
    }

    @Override
    public String toString() {
        return "BaiduAudioProperties{" +
            "appId='" + appId + '\'' +
            ", appKey='" + appKey + '\'' +
            ", secretKey='" + secretKey + '\'' +
            ", savePrePath='" + savePrePath + '\'' +
            ", urlPrePath='" + urlPrePath + '\'' +
            '}';
    }
}
