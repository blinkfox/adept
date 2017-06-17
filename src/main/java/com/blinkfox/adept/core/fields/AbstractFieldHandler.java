package com.blinkfox.adept.core.fields;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将数据查询出的某一列属性转换成Java对应类型结果的处理器抽象.
 * @author blinkfox on 2017/6/17.
 */
public abstract class AbstractFieldHandler {

    /** 下一个`ColumnHandler`处理器. */
    private AbstractFieldHandler nextColumnHandler;

    /**
     * nextColumnHandler的getter方法.
     * @return AbstractColumnHandler实例
     */
    public AbstractFieldHandler getNextColumnHandler() {
        return nextColumnHandler;
    }

    /**
     * nextColumnHandler的setter方法.
     * @param nextColumnHandler nextColumnHandler实例
     */
    public void setNextColumnHandler(AbstractFieldHandler nextColumnHandler) {
        this.nextColumnHandler = nextColumnHandler;
    }

    /**
     * 根据JavaBean属性类型得到ResultSet对应序号列的值.
     * @param rs ResultSet实例
     * @param colNum 结果集列序号
     * @param propType 属性类型
     * @return 字段值
     */
    public abstract Object getColumnValue(ResultSet rs, int colNum, Class<?> propType) throws SQLException;

}