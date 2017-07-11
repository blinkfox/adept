package com.blinkfox.adept.test.core.results;

import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 将'ResultSet'结果集的第一行数据转换为'对象数组'的处理器.
 * Created by blinkfox on 2017/7/11.
 */
public class ArrayHandler implements ResultHandler<Object[]> {

    /**
     * 将'ResultSet'结果集的第一行数据转换为'对象数组'.
     * @param rs ResultSet实例
     * @return 对象数组
     */
    @Override
    public Object[] transform(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        try {
            // 获取Resultset元数据和查询的列数.
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            // 初始化列数长度的数组,将第一行各列的数据存到'对象数组'中.
            if (rs.next()) {
                Object[] objArr = new Object[cols];
                for (int i = 0; i < cols; i++)  {
                    objArr[i] = rs.getObject(i + 1);
                }
                return objArr;
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'有序Map'出错!", e);
        }

        return null;
    }

}