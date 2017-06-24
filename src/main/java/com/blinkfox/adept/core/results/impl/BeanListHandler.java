package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.IntrospectorManager;
import com.blinkfox.adept.core.results.BeanComponent;
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;
import com.blinkfox.adept.helpers.ClassHelper;
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
public class BeanListHandler<T> extends BeanComponent<T> implements ResultHandler<List<T>> {

    /**
     * 传入Bean实例的`BeanListHandler`构造方法.
     * @param bean bean实例
     */
    public BeanListHandler(T bean) {
        this.bean = bean;
    }

    /**
     * 传入Bean实例class的`BeanListHandler`构造方法.
     * @param beanClass bean实例的class
     */
    public BeanListHandler(Class<T> beanClass) {
        this.bean = ClassHelper.newInstanceByClass(beanClass);
    }

    /**
     * 通过Bean实例来获取新的`BeanListHandler`实例.
     * @param bean T类的bean
     * @param <T> 泛型方法
     * @return BeanHandler实例
     */
    public static <T> BeanListHandler<T> newInstance(T bean) {
        return new BeanListHandler<T>(bean);
    }

    /**
     * 传入Bean实例class来获取`BeanListHandler`实例.
     * @param beanClass T类的beanClass
     * @param <T> 泛型方法
     * @return BeanHandler实例
     */
    public static <T> BeanListHandler<T> newInstance(Class<T> beanClass) {
        return new BeanListHandler<T>(beanClass);
    }

    /**
     * 将`ResultSet`结果集的所有数据转换为`Java Bean的List集合`的方法.
     * @param rs ResultSet实例
     * @return 泛型T的实例
     */
    public List<T> transform(ResultSet rs) {
        if (!super.isValid(rs)) {
            return null;
        }

        // 遍历Resultset和元数据，将各行各列的数据存到'Java Bean'的ArrayList集合中
        List<T> beanList = new ArrayList<T>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, PropertyDescriptor> propMap = IntrospectorManager.newInstance()
                    .getPropMap(bean.getClass());
            while (rs.next()) {
                beanList.add(JdbcHelper.getBeanValue(rs, rsmd, bean, propMap));
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'Java Bean集合'出错!", e);
        }

        return beanList;
    }

}