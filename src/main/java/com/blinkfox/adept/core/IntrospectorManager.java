package com.blinkfox.adept.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaBean内省机制相关操作的类.
 * @author blinkfox on 2017/6/17.
 */
public class IntrospectorManager {

    /**
     * 私有构造方法.
     */
    private IntrospectorManager() {
        super();
    }

    /**
     * 获取新的实例.
     * @return IntrospectorManager实例
     */
    public static IntrospectorManager newInstance() {
        return new IntrospectorManager();
    }

    /**
     * 根据JavaBean的class获取其所有的PropertyDescriptor并作为一个数组返回.
     * @return PropertyDescriptor数组
     */
    public PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        return beanInfo.getPropertyDescriptors();
    }

    /**
     * 根据JavaBean的class获取其所有的PropertyDescriptor的名称和本对象作为一个Map返回.
     * @param clazz JavaBean的class
     * @return key为JavaBean的属性，值为PropertyDescriptor的Map
     */
    public Map<String, PropertyDescriptor> getPropMap(Class<?> clazz) throws IntrospectionException {
        Map<String, PropertyDescriptor> propMap = new HashMap<String, PropertyDescriptor>();
        PropertyDescriptor[] props = this.getPropertyDescriptors(clazz);
        for (PropertyDescriptor prop: props) {
            propMap.put(prop.getName(), prop);
        }
        return propMap;
    }

}