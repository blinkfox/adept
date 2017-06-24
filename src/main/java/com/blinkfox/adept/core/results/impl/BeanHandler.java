package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.IntrospectorManager;
import com.blinkfox.adept.core.results.BeanComponent;
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;
import com.blinkfox.adept.helpers.JdbcHelper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

/**
 * 将'ResultSet'结果集的第一行数据转换为'Java Bean'的处理器实现.
 * @author blinkfox on 2017/6/15.
 */
public class BeanHandler<T> extends BeanComponent<T> implements ResultHandler<T> {

    /**
     * 传入泛型T实例的class的构造方法.
     * @param beanClass 泛型T实例的class
     */
    public BeanHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 创建新的BeanHandler实例.
     * @param beanClass T类的beanClass
     * @param <T> 泛型方法
     * @return BeanHandler实例
     */
    public static <T> BeanHandler<T> newInstance(Class<T> beanClass) {
        return new BeanHandler<T>(beanClass);
    }

    /**
     * 将'ResultSet'结果集的第一行数据转换为'Java Bean'.
     * @param rs ResultSet实例
     * @return 泛型T的实例
     */
    @Override
    public T transform(ResultSet rs) {
        if (!isValid(rs)) {
            return null;
        }

        // 遍历Resultset和元数据，将第一行各列的数据存到'Java Bean'中
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, PropertyDescriptor> propMap = IntrospectorManager.newInstance().getPropMap(beanClass);
            if (rs.next()) {
                return JdbcHelper.getBeanValue(rs, rsmd, beanClass, propMap);
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'Java Bean'出错!", e);
        }

        return null;
    }

}