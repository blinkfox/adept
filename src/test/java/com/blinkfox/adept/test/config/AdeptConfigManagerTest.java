package com.blinkfox.adept.test.config;

import com.blinkfox.adept.config.AdeptConfigManager;
import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.test.MyAdeptConfig;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
    @BeforeClass
    public static void init() {
        configManager = AdeptConfigManager.getInstance();
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
     * 清除配置信息.
     */
    @AfterClass
    public static void destroy() {
        configManager.destroy();
    }

}