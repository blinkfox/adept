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
     * @param <T> 泛型方法
     * @return 泛型T
     */
    public static <T> T newInstanceByClass(Class<T> clazz) {
        if (clazz == null) {
            throw new AdeptRuntimeException("需要实例化的class为null.");
        }

        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new AdeptRuntimeException("实例化class出错！class为:{}" + clazz.getName(), e);
        }
    }

    /**
     * 根据class生成该class对应类的新实例.
     * @param className 类全路径名
     * @return 对象
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