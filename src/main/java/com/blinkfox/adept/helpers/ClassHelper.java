package com.blinkfox.adept.helpers;

import com.blinkfox.adept.exception.AdeptRuntimeException;

import java.lang.reflect.Constructor;

/**
 * class、反射等的工具类.
 * @author blinkfox on 2017/6/12.
 */
public final class ClassHelper {

    /**
     * 私有构造方法.
     */
    private ClassHelper() {
        super();
    }

    /**
     * 根据class生成该class对应类的新实例.
     * @param clazz class
     * @return 泛型T
     */
    public static Object newInstanceByClass(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new AdeptRuntimeException("实例化class出错！", e);
        }
    }

    /**
     * 根据class生成该class对应类的新实例.
     * @param className 类全路径名
     * @return 泛型T
     */
    public static Object newInstanceByClassName(String className) {
        try {
            Class clazz = Class.forName(className);
            return newInstanceByClass(clazz);
        } catch (ClassNotFoundException e) {
            throw new AdeptRuntimeException("未找到对应的类,className为:" + className, e);
        }
    }

}