package com.qigu.readword.mycode.wechat.utils;

import java.util.UUID;

public class MyStringUtils {
    public static String get32RandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
