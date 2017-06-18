package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.IntrospectorManager;
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;
import com.blinkfox.adept.helpers.JdbcHelper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将'ResultSet'结果集的所有数据转换为`Java Bean的List集合`的处理器实现类.
 * @author blinkfox on 2017/6/17.
 */
public class BeanListHandler implements ResultHandler {

    /* 需要转换为Bean集合的class */
    private Class<?> beanClass;

    /**
     * 传入`JavaBean的class`的构造方法.
     * @param beanClass JavaBean的class
     */
    public BeanListHandler(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 传入`JavaBean的class`的获取实例方法.
     * @param beanClass JavaBean的class
     * @return BeanListHandler实例
     */
    public static BeanListHandler newInstance(Class<?> beanClass) {
        return new BeanListHandler(beanClass);
    }

    /**
     * 将`ResultSet`结果集的所有数据转换为`Java Bean的List集合`的方法.
     * @param rs ResultSet实例
     * @return 泛型T的实例
     */
    @Override
    public Object transform(ResultSet rs) {
        if (rs == null || this.beanClass == null) {
            return null;
        }

        // 遍历Resultset和元数据，将各行各列的数据存到'Java Bean'的ArrayList集合中
        List<Object> list = new ArrayList<Object>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, PropertyDescriptor> propMap = IntrospectorManager.newInstance().getPropMap(beanClass);
            while (rs.next()) {
                list.add(JdbcHelper.getBeanValue(rs, rsmd, beanClass, propMap));
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'Java Bean'出错!", e);
        }

        return list;
    }

}