package com.qigu.readword.mycode.cronjob;

import com.qigu.readword.domain.Audio;
import com.qigu.readword.mycode.baidu.service.BaiduAudioService;
import com.qigu.readword.mycode.util.ReadWordConstants;
import com.qigu.readword.repository.AudioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CronJobService {

    private final AudioRepository audioRepository;
    private final BaiduAudioService baiduAudioService;
    private final Logger log = LoggerFactory.getLogger(CronJobService.class);

    public CronJobService(AudioRepository audioRepository, BaiduAudioService baiduAudioService) {
        this.audioRepository = audioRepository;
        this.baiduAudioService = baiduAudioService;
    }

    @Scheduled(cron = "0 42 17 28 4 ?")//只执行一次
    void addAudioOneSpeedUrl() {
        log.info("****************addAudioOneSpeedUrl****************START****************");
        List<Audio> allByOneSpeedUrlIsNull = audioRepository.findAllByOneSpeedUrlIsNull();
        HashMap<String, Object> options = new HashMap<>();
        options.put(ReadWordConstants.BAIDU_VOICE_SPEED_KEY, 1);
        allByOneSpeedUrlIsNull.forEach(audio -> {
            baiduAudioService.createAudioByOptions(audio.getId().toString(), audio.getName(), options).ifPresent(oneSpeedUrl -> {
                audio.setOneSpeedUrl(oneSpeedUrl);
                audioRepository.save(audio);
            });
        });
        log.info("****************addAudioOneSpeedUrl****************END****************");

    }
}
