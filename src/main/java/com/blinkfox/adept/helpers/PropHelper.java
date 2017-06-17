package com.blinkfox.adept.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.pmw.tinylog.Logger;

/**
 * Properties属性读取工具类.
 * @author blinkfox on 2017/5/21.
 */
public enum PropHelper {

    /**
     * 唯一实例.
     */
    INSTANCE {

        private final Properties props = new Properties();

        @Override
        public Properties loadPropFile(String fileName) {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            try {
                props.load(in);
                Logger.info("加载properties配置文件完成!");
            } catch (IOException e) {
                Logger.error(e, "加载properties配置文件出错！");
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.error(e, "关闭IO流出错！");
                }
            }
            return props;
        }

        @Override
        public String getProperty(String key) {
            return props.getProperty(key);
        }

        @Override
        public String getProperty(String key, String defaultValue) {
            return props.getProperty(key, defaultValue);
        }

    };

    /**
     * 私有构造方法.
     */
    private PropHelper() {}

    /**
     * 加载properties属性配置文件.
     * @param fileName 文件名
     */
    public abstract Properties loadPropFile(String fileName);

    /**
     * 根据属性的key读取value.
     * @param key key
     * @return 字符串值
     */
    public abstract String getProperty(String key);

    /**
     * 根据属性的key读取value.
     * @param key key
     * @param defaultValue 默认值
     * @return 字符串值
     */
    public abstract String getProperty(String key, String defaultValue);

}