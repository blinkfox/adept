package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;

import java.sql.ResultSet;

/**
 * 将'ResultSet'结果集的第一行和第一列的单个数据转换为'单个Object对象'的处理器实现类.
 * @author blinkfox on 2017/6/17.
 */
public class SingleHandler implements ResultHandler<Object> {

    /**
     * 获得新的实例.
     * @return SingleHandler实例
     */
    public static SingleHandler newInstance() {
        return new SingleHandler();
    }

    /**
     * 将'ResultSet'结果集的第一行、第一列的单个数据转换为'单个Object对象'的方法.
     * @param rs ResultSet实例
     * @param otherParams 其他参数
     * @return Object对象
     */
    @Override
    public Object transform(ResultSet rs, Object... otherParams) {
        if (rs == null) {
            return null;
        }

        // 遍历Resultset，返回第一行、第一列的单个数据.
        try {
            return rs.next() && rs.first() ? rs.getObject(1) : null;
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'map的List集合'出错!", e);
        }
    }

}