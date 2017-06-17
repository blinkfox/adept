package com.blinkfox.adept.core.fields.impl;

import com.blinkfox.adept.core.fields.AbstractFieldHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java 8种原始类型对应的JDBC结果处理器实现类.
 * @author blinkfox on 2017/6/17.
 */
public class PrimitiveHandler extends AbstractFieldHandler {

    /**
     * 获取新的`PrimitiveHandler`实例.
     * @return `PrimitiveHandler`实例
     */
    public static AbstractFieldHandler newInstance() {
        return new PrimitiveHandler();
    }

    /**
     * 根据JavaBean属性类型名称得到Java8种原始类型下的ResultSet对应序号列的值.
     * @param rs ResultSet实例
     * @param colNum 结果集列序号
     * @param propType 属性类型名称
     * @return 字段值
     */
    @Override
    public Object getColumnValue(ResultSet rs, int colNum, Class<?> propType) throws SQLException {
        // 将几种常用原始类型放前面优先判断并直接返回结果
        String propTypeName = propType.getName();
        if ("int".equals(propTypeName)) {
            return rs.getInt(colNum);
        } else if ("long".equals(propTypeName)) {
            return rs.getLong(colNum);
        } else if ("double".equals(propTypeName)) {
            return rs.getDouble(colNum);
        } else if ("boolean".equals(propTypeName)) {
            return rs.getBoolean(colNum);
        } else if ("byte".equals(propTypeName)) {
            return rs.getByte(colNum);
        } else if ("short".equals(propTypeName)) {
            return rs.getShort(colNum);
        } else if ("float".equals(propTypeName)) {
            return rs.getFloat(colNum);
        } else if ("char".equals(propTypeName)) {
            return rs.getString(colNum);
        }
        return null;
    }

}