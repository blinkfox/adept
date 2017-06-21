package com.blinkfox.adept.test.helpers;

import com.blinkfox.adept.helpers.PropHelper;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Properties属性读取工具类的单元测试类.
 * Created by blinkfox on 2017/6/21.
 */
public class PropHelperTest {

    private static PropHelper propHelper;

    /**
     * 初始化PropHelper实例.
     */
    @BeforeClass
    public static void init() {
        propHelper = PropHelper.newInstance();
    }

    /**
     * 测试加载properties文件并获取对应实例.
     */
    @Test
    public void testLoadPropFile() {
        Properties props = propHelper.loadPropFile("config.properties");
        Assert.assertNotNull(props);
        Assert.assertEquals(propHelper.getProps(), props);
    }

    /**
     * 测试获取properties中的配置属性.
     */
    @Test
    public void testGetProperty() {
        propHelper.loadPropFile("config.properties");
        Assert.assertEquals("20", propHelper.getProperty("maxTotal"));
        Assert.assertEquals("world", propHelper.getProperty("hello", "world"));
    }

    /**
     * 结束前销毁实例.
     */
    @AfterClass
    public static void destroy() {
        propHelper.clear();
        propHelper = null;
    }

}