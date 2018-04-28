package com.qigu.readword.mycode.baidu.service;

import java.util.HashMap;
import java.util.Optional;

public interface BaiduAudioService {

    Optional<String> createAudio(String id, String content);
    Optional<String> createAudioByOptions(String id, String content,HashMap<String,Object> options);
}
