package com.qigu.readword.mycode.baidu.service.impl;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.qigu.readword.mycode.baidu.service.BaiduAudioService;
import com.qigu.readword.mycode.util.JDateUtils;
import com.qigu.readword.mycode.wechat.config.BaiduAudioProperties;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BaiduAudioServiceImpl implements BaiduAudioService {
    private final BaiduAudioProperties baiduAudioProperties;
    private final Logger log = LoggerFactory.getLogger(BaiduAudioService.class);
    private final AipSpeech client;

    public BaiduAudioServiceImpl(BaiduAudioProperties baiduAudioProperties) {
        this.baiduAudioProperties = baiduAudioProperties;
        this.client = new AipSpeech(baiduAudioProperties.getAppId(), baiduAudioProperties.getAppKey(), baiduAudioProperties.getSecretKey());
    }

    @Override
    public Optional<String> createAudio(String id, String content) {
        return createAudioByOptions(id, content, null);
    }


    @Override
    public Optional<String> createAudioByOptions(String id, String content, HashMap<String, Object> options) {

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        TtsResponse res = client.synthesis(content, "zh", 1, options);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Date now = new Date();
                String nowStr = JDateUtils.format(now, JDateUtils.C_DATA_PATTON_YYYYMMDD);
                String parent = baiduAudioProperties.getSavePrePath() + nowStr;
                File parentFile = new File(parent);
                if (!parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                    log.info("###############" + parent + " " + mkdirs);
                }
                final StringBuilder optionNames = new StringBuilder();
                if (options != null && !options.isEmpty()) {
                    optionNames.append("-");
                    options.keySet().stream().sorted().forEach(key -> {
                        optionNames.append(key).append(options.get(key));
                    });

                }
                String savePath = parent + File.separator + id + optionNames.toString() + ".mp3";
                String urlPath = baiduAudioProperties.getUrlPrePath() + nowStr + "/" + id + optionNames.toString() + ".mp3 ";
                Util.writeBytesToFileSystem(data, savePath);
                return Optional.of(urlPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            try {
                log.info(res1.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

}
