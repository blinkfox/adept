package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 将'ResultSet'结果集的第一列数据转换为'List集合'的处理器实现类.
 * @author blinkfox on 2017/6/17.
 */
public class ColumnsHandler implements ResultHandler<List<Object>> {

    /**
     * 获得新的实例.
     * @return ColumnsHandler实例
     */
    public static ColumnsHandler newInstance() {
        return new ColumnsHandler();
    }

    /**
     * 将'ResultSet'结果集的第一列数据转换为'List集合'的方法.
     * @param rs ResultSet实例
     * @param otherParams 其他参数
     * @return Object的List集合
     */
    @Override
    public List<Object> transform(ResultSet rs, Object... otherParams) {
        if (rs == null) {
            return null;
        }

        // 遍历Resultset，将第一列数据存到List集合中.
        List<Object> columns = new ArrayList<Object>();
        try {
            while (rs.next()) {
                columns.add(rs.getObject(1));
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'map的List集合'出错!", e);
        }

        return columns;
    }

}