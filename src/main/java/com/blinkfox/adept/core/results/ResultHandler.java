package com.blinkfox.adept.core.results;

import java.sql.ResultSet;

/**
 * 将'ResultSet'结果集转换为某种类型结果的处理器接口.
 * @author by blinkfox on 2017/6/10.
 */
public interface ResultHandler<T> {

    /**
     * 将结果集转换为泛型T类型结果的方法.
     * @param rs ResultSet实例
     * @param otherParams 其他参数
     * @return 泛型T的实例
     */
    T transform(ResultSet rs, Object... otherParams);

}