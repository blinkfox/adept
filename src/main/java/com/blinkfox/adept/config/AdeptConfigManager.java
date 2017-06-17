package com.blinkfox.adept.config;

import com.blinkfox.adept.exception.LoadAdeptConfigException;
import com.blinkfox.adept.helpers.ClassHelper;

import org.pmw.tinylog.Logger;

/**
 * Adept配置信息管理器类.
 * @author blinkfox on 2017/5/30.
 */
public class AdeptConfigManager {

    /* AdeptConfigManager的唯一实例 */
    private static final AdeptConfigManager manager = new AdeptConfigManager();

    /* 配置信息 */
    private static final ConfigInfo configInfo = ConfigInfo.getInstance();

    /**
     * 私有构造方法.
     */
    private AdeptConfigManager() {
        super();
    }

    /**
     * 获取AdeptConfigManager的唯一实例.
     * @return AdeptConfigManager实例
     */
    public static AdeptConfigManager getInstance() {
        return manager;
    }

    /**
     * 初始化加载Adept的配置信息到内存缓存中.
     * @param configClass 系统中Adept的class全路径
     */
    public void initLoad(String configClass) {
        this.getAndLoadConfig(configClass);
        Logger.info("Adept的配置信息加载完成!");
    }

    /**
     * 初始化加载Adept的配置信息到内存缓存中.
     * @param clazz Adept的配置类
     */
    public void initLoad(Class<? extends AbstractAdeptConfig> clazz) {
        this.initLoad(clazz.getName());
    }

    /**
     * 从内存缓存中清楚Adept的相关数据.
     */
    public void destroy() {
        configInfo.clear();
        Logger.info("清除了Adept的配置信息！");
    }

    /**
     * 获取AdeptConfig的实例.
     * @param configClass 配置类的class路径
     */
    private void getAndLoadConfig(String configClass) {
        Logger.info("Adept开始加载，Zealot配置类为:{}", configClass);
        if (configClass == null || configClass.length() == 0) {
            throw new LoadAdeptConfigException("未获取到 AdeptConfig 配置信息!");
        }

        // 根据class类名得到初始化实例,判断获取到的类是否是AbstractAdeptConfig的子类，如果是，则加载配置信息
        Object adeptConfig = ClassHelper.newInstanceByClassName(configClass);
        if (adeptConfig != null && adeptConfig instanceof AbstractAdeptConfig) {
            this.load((AbstractAdeptConfig) adeptConfig);
        }
    }

    /**
     * 加载AdeptConfig的子类信息，并将配置信息加载到内存缓存中.
     * @param config 配置类
     */
    private void load(AbstractAdeptConfig config) {
        config.configDataSource(configInfo);
    }

}