package com.blinkfox.adept.test;

import com.blinkfox.adept.core.IntrospectorManager;
import com.blinkfox.adept.test.bean.UserInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * IntrospectorManager类的单元测试类.
 * @author blinkfox on 2017/6/17.
 */
public class IntrospectorManagerTest {

    /** IntrospectorManager实例. */
    private static IntrospectorManager introManager;

    /**
     * 初始化实例.
     */
    @BeforeClass
    public static void init() {
        introManager = IntrospectorManager.newInstance();
    }

    /**
     * 测试获取某个Bean的所有PropertyDescriptor.
     */
    @Test
    public void testGetPropertyDescriptors() throws IntrospectionException {
        PropertyDescriptor[] props = introManager.getPropertyDescriptors(UserInfo.class);
        Assert.assertNotNull(props);
    }

    /**
     * 测试获取某个Bean的所有PropertyDescriptor Map.
     */
    @Test
    public void testGetgetPropMap() throws IntrospectionException {
        Map<String, PropertyDescriptor> propMap = introManager.getPropMap(UserInfo.class);
        Assert.assertNotNull(propMap);
        Assert.assertEquals(7, propMap.size());
    }

    /**
     * 最后销毁实例.
     */
    @AfterClass
    public static void destroy() {
        introManager = null;
    }

}