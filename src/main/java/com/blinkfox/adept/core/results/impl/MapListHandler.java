package com.blinkfox.adept.core.results.impl;

import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;
import com.blinkfox.adept.helpers.JdbcHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将'ResultSet'结果集转换为'Map的List集合'的处理器实现.
 * @author blinkfox on 2017/6/11.
 */
public class MapListHandler implements ResultHandler<List<Map<String, Object>>> {

    /**
     * 获得新的新实例.
     * @return MapListHandler实例
     */
    public static MapListHandler newInstance() {
        return new MapListHandler();
    }

    /**
     * 将'ResultSet'结果集转换为'Map的List集合'的方法.
     * @param rs ResultSet实例
     * @return Map的List集合
     */
    @Override
    public List<Map<String, Object>> transform(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        // 遍历Resultset和元数据，将每一行各列的数据存到Map中，然后将各行数据add到List集合中
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0, cols = rsmd.getColumnCount(); i < cols; i++)  {
                    map.put(JdbcHelper.getColumn(rsmd, i + 1), rs.getObject(i + 1));
                }
                maps.add(map);
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'map的List集合'出错!", e);
        }

        return maps;
    }

}