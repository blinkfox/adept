package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;
import com.blinkfox.adept.helpers.JdbcHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将'ResultSet'结果集的第一行数据转换为'有序Map'的处理器实现.
 * @author blinkfox on 2017/6/11.
 */
public class MapHandler implements ResultHandler<Map<String, Object>> {

    /**
     * 获得新的新实例.
     * @return MapHandler实例
     */
    public static MapHandler newInstance() {
        return new MapHandler();
    }

    /**
     * 将'ResultSet'结果集的第一行数据转换为'有序Map'.
     * @param rs ResultSet实例
     * @return Map
     */
    @Override
    public Map<String, Object> transform(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        // 遍历Resultset和元数据，将第一行各列的数据存到LinkedHashMap中
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                for (int i = 0, cols = rsmd.getColumnCount(); i < cols; i++)  {
                    map.put(JdbcHelper.getColumn(rsmd, i + 1), rs.getObject(i + 1));
                }
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'有序Map'出错!", e);
        }

        return map;
    }

}