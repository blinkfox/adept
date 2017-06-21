package com.blinkfox.adept.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties属性读取工具类.
 * @author blinkfox on 2017/5/21.
 */
public class PropHelper {

    /** Properties实例. */
    private static Properties props;

    private static final Logger log = LoggerFactory.getLogger(PropHelper.class);

    /**
     * 私有构造方法.
     */
    private PropHelper() {
        super();
    }

    /**
     * 获取新的实例.
     * @return PropHelper实例
     */
    public static PropHelper newInstance() {
        props = new Properties();
        return new PropHelper();
    }

    /**
     * 加载properties属性配置文件.
     * @param fileName properties文件全路径名
     */
    public Properties loadPropFile(String fileName) {
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            props.load(in);
            log.info("加载properties配置文件完成!");
        } catch (IOException e) {
            log.error("加载properties配置文件出错！", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭IO流出错！", e);
                }
            }
        }
        return props;
    }

    /**
     * 根据属性的key读取value.
     * @param key key
     * @return 字符串值
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * 根据属性的key读取value.
     * @param key key
     * @param defaultValue 默认值
     * @return 字符串值
     */
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

}