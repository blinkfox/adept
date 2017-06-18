package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.IntrospectorManager;
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
public class BeanHandler implements ResultHandler {

    /* 需要转换为的Bean的Class */
    private Class<?> beanClass;

    /**
     * 传入JavaBean class的构造方法.
     * @param beanClass beanClass
     */
    public BeanHandler(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 创建新的BeanHandler实例.
     * @param beanClass beanClass
     * @return BeanHandler实例
     */
    public static BeanHandler newInstance(Class<?> beanClass) {
        return new BeanHandler(beanClass);
    }

    /**
     * 将'ResultSet'结果集的第一行数据转换为'Java Bean'.
     * @param rs ResultSet实例
     * @param otherParams 其他参数
     * @return 泛型T的实例
     */
    @Override
    public Object transform(ResultSet rs, Object... otherParams) {
        if (rs == null || this.beanClass == null) {
            return null;
        }

        // 遍历Resultset和元数据，将第一行各列的数据存到'Java Bean'中
        Object beanObj = null;
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, PropertyDescriptor> propMap = IntrospectorManager.newInstance().getPropMap(beanClass);
            if (rs.next()) {
                beanObj = JdbcHelper.getBeanValue(rs, rsmd, beanClass, propMap);
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'Java Bean'出错!", e);
        }

        return beanObj;
    }

}