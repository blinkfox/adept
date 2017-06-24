package com.blinkfox.adept.test.config;

import com.blinkfox.adept.config.AdeptConfigManager;
import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.exception.LoadAdeptConfigException;
import com.blinkfox.adept.test.MyAdeptConfig;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * AdeptConfigManager的单元测试.
 * @author blinkfox on 2017/5/30.
 */
public class AdeptConfigManagerTest {

    private static AdeptConfigManager configManager;

    /**
     * 初始化加载Adept配置.
     */
    @Before
    public void init() {
        configManager = AdeptConfigManager.newInstance();
    }

    /**
     * 加载配置信息.
     */
    @Test
    public void testInitLoad() {
        configManager.initLoad(MyAdeptConfig.class);
        Assert.assertNotNull(ConfigInfo.getInstance().getDataSource());
    }

    /**
     * 通过String型的Config路径加载配置信息.
     */
    @Test
    public void testInitLoadByClassName() {
        configManager.initLoad("com.blinkfox.adept.test.MyAdeptConfig");
        Assert.assertNotNull(ConfigInfo.getInstance().getDataSource());
    }

    /**
     * 通过String型的Config路径加载配置信息.
     */
    @Test(expected = LoadAdeptConfigException.class)
    public void testInitLoadException() {
        // 加载不存在的配置信息路径.
        configManager.initLoad("");
        Assert.assertNotNull(ConfigInfo.getInstance().getDataSource());
    }

    /**
     * 清除配置信息.
     */
    @After
    public void destroy() {
        configManager.destroy();
    }

}