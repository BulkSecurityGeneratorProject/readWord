package com.qigu.readword.mycode.baidu.service;

import java.util.Optional;

public interface BaiduAudioService {

    Optional<String> createAudio(String id, String content);
}
