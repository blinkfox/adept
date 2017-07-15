package com.blinkfox.adept.core.fields;

import com.blinkfox.adept.core.fields.impl.PrimitiveHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 列值处理器的链类.
 * @author blinkfox on 2017/6/17.
 */
public final class FieldHandlerChain {

    /**
     * 私有构造方法.
     */
    private FieldHandlerChain() {
        super();
    }
    
    /**
     * 获取新的 实例.
     * @return FieldHandlerChain实例
     */
    public static FieldHandlerChain newInstance() {
        return new FieldHandlerChain();
    }

    /**
     * 根据JavaBean属性类型名称得到ResultSet对应序号列的值.
     * @param rs ResultSet实例
     * @param colNum 结果集列序号
     * @param propType 属性类型
     * @return 字段值
     */
    public Object getColumnValue(ResultSet rs, int colNum, Class<?> propType) throws SQLException {
        if (propType.isPrimitive()) {
            return PrimitiveHandler.newInstance().getColumnValue(rs, colNum, propType);
        }

        return rs.getObject(colNum);
    }

}