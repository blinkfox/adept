package com.blinkfox.adept.helpers;

import com.blinkfox.adept.core.fields.FieldHandlerChain;
import com.blinkfox.adept.exception.BuildStatementException;
import com.blinkfox.adept.exception.ExecuteSqlException;
import com.blinkfox.adept.exception.NoDataSourceException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import javax.sql.DataSource;

import org.pmw.tinylog.Logger;

/**
 * Connection工具类.
 * @author blinkfox on 2017/6/7.
 */
public final class JdbcHelper {

    /**
     * 私有构造方法.
     */
    private JdbcHelper() {
        super();
    }

    /**
     * 从数据源中获取数据库连接.
     * @param ds 数据源
     * @return Connection实例
     */
    public static Connection getConnection(DataSource ds) {
        if (ds == null) {
            throw new NoDataSourceException("未传递有效的数据源参数实例!");
        }

        try {
            return ds.getConnection();
        } catch (SQLException e) {
            Logger.error(e, "从数据源（连接池）中获取数据库连接失败.");
            return null;
        }
    }

    /**
     * 从数据库连接和sql语句中得到JDBC的'PreparedStatement'实例.
     * @param conn 数据库连接实例
     * @param sql SQL语句
     * @param params 不定长参数
     * @return PreparedStatement实例
     */
    public static PreparedStatement getPreparedStatement(Connection conn, String sql, Object... params) {
        try {
            return setPreparedStatementParams(conn.prepareStatement(sql), params);
        } catch (Exception e) {
            throw new BuildStatementException("", e);
        }
    }

    /**
     * 在'PreparedStatement'中设置SQL语句参数.
     * @param pstmt PreparedStatement实例
     * @param params 不定长参数
     * @return PreparedStatement实例
     * @throws SQLException SQL异常
     */
    private static PreparedStatement setPreparedStatementParams(PreparedStatement pstmt, Object... params)
            throws SQLException {
        if (params == null) {
            return pstmt;
        }

        for (int i = 0, len = params.length; i < len; i++) {
            Object param = params[i];
            if (param != null) {
                pstmt.setObject(i + 1, param);
            } else {
                pstmt.setNull(i + 1, Types.VARCHAR);
            }
        }
        return pstmt;
    }

    /**
     * 得到查询SQL语句的ResultSet结果集.
     * @param pstmt PreparedStatement实例.
     * @return ResultSet实例
     */
    public static ResultSet getQueryResultSet(PreparedStatement pstmt) {
        try {
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ExecuteSqlException("执行查询的SQL语句出错!", e);
        }
    }

    /**
     * 根据ResultSetMetaData实例获取指定列数colNum的列名.
     * <p>如果sql列查询有`AS`，则采用`getColumnLabel`获取，否则则获取列本身的名称，即通过`getColumnName`来获取</p>
     * @param rsmd 结果集元数据
     * @param colNum 列数
     * @return 列名
     */
    public static String getColumn(ResultSetMetaData rsmd, int colNum) throws SQLException {
        String columnName = rsmd.getColumnLabel(colNum);
        return columnName == null || columnName.length() == 0 ? rsmd.getColumnName(colNum) : columnName;
    }

    /**
     * 将ResultSet转换为指定class的Bean.
     * @param rs ResultSet实例
     * @param rsmd ResultSet元数据
     * @param beanClass Bean的class
     * @param propMap Bean属性对应的Map
     * @return 值
     * @throws IllegalAccessException IllegalAccessException
     * @throws InstantiationException InstantiationException
     * @throws SQLException SQLException
     * @throws InvocationTargetException InvocationTargetException
     */
    public static Object getBeanValue(ResultSet rs, ResultSetMetaData rsmd, Class<?> beanClass,
            Map<String, PropertyDescriptor> propMap) throws IllegalAccessException, InstantiationException,
            SQLException, InvocationTargetException {
        Object beanObj = beanClass.newInstance();
        for (int i = 0, cols = rsmd.getColumnCount(); i < cols; i++) {
            String columnName = JdbcHelper.getColumn(rsmd, i + 1);
            if (propMap.containsKey(columnName)) {
                PropertyDescriptor prop = propMap.get(columnName);
                // 获取并调用setter方法.
                Method propSetter = prop.getWriteMethod();
                if (propSetter == null || propSetter.getParameterTypes().length != 1) {
                    Logger.warn("类'{}'的属性'{}'没有标准的setter方法", beanClass.getName(), columnName);
                    continue;
                }

                // 得到属性类型并将该数据库列值转成Java对应类型的值
                Class<?> propType = prop.getPropertyType();
                Object value = FieldHandlerChain.newInstance().getColumnValue(rs, i + 1, propType);
                propSetter.invoke(beanObj, value);
            }
        }
        return beanObj;
    }

    /**
     * '关闭'数据库连接Connection.
     * @param conn 数据库连接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                Logger.error(e, "关闭数据库连接Connection失败！");
            }
        }
    }

    /**
     * 关闭PreparedStatement.
     * @param pstmt PreparedStatement
     */
    public static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                Logger.error(e, "关闭PreparedStatement失败！");
            }
        }
    }

    /**
     * 关闭ResultSet.
     * @param rs PreparedStatement
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.error(e, "关闭ResultSet失败！");
            }
        }
    }

    /**
     * 关闭各种资源.
     * @param conn connection实例
     * @param pstmt PreparedStatement实例
     * @param rs PreparedStatement实例
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        close(rs);
        close(pstmt);
        close(conn);
    }

}