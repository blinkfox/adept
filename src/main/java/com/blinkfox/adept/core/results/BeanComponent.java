package com.blinkfox.adept.core.results;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转换Bean结果相关的Handler需要的公共属性和方法的类.
 * Created by blinkfox on 2017/6/24.
 */
public abstract class BeanComponent<T> {

    private static final Logger log = LoggerFactory.getLogger(BeanComponent.class);

    /* 待转换Bean的class */
    protected Class<T> beanClass;

    /**
     * 判断需要转换的数据是否有效.
     * <p>当ResultSet和bean都为空时，视为无效.</p>
     * @param rs ResultSet结果集.
     * @return 布尔值
     */
    protected boolean isValid(ResultSet rs) {
        if (rs == null) {
            return false;
        }

        if (this.beanClass == null) {
            log.info("要转换的实例bean的class为null");
            return false;
        }
        return true;
    }

}