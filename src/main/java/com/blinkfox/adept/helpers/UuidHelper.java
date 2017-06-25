package com.blinkfox.adept.helpers;

import java.util.UUID;

/**
 * 生成UUID的工具类.
 * Created by blinkfox on 2017/6/25.
 */
public final class UuidHelper {

    /**
     * 私有构造方法.
     */
    private UuidHelper() {
        super();
    }

    /**
     * 获取一个默认长度(32位)的UUID.
     * @return UUID字符串
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}