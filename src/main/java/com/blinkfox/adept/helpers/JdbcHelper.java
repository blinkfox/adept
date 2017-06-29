package com.blinkfox.adept.helpers;

import com.blinkfox.adept.core.fields.FieldHandlerChain;
import com.blinkfox.adept.exception.AdeptRuntimeException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection工具类.
 * @author blinkfox on 2017/6/7.
 */
public final class JdbcHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcHelper.class);

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
            throw new AdeptRuntimeException("从数据源（连接池）中获取数据库连接失败.", e);
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
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            return setPreparedStatementParams(pstmt, params);
        } catch (Exception e) {
            // 抛出异常前关闭数据库连接资源.
            close(conn, pstmt);
            throw new BuildStatementException("构建prepareStatement语句出错", e);
        }
    }

    /**
     * 从数据库连接和sql语句中得到JDBC的'PreparedStatement'实例.
     * @param conn 数据库连接实例
     * @param sql SQL语句
     * @param params 不定长参数
     * @return PreparedStatement实例
     */
    public static PreparedStatement getBatchPreparedStatement(Connection conn, String sql, Object[]... params) {
        PreparedStatement pstmt = null;
        try {
            // 设置自动提交为false，从而加快批量执行的速度.
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            return setBatchPreparedStatementParams(pstmt, params);
        } catch (Exception e) {
            // 抛出异常前关闭数据库连接资源.
            close(conn, pstmt);
            throw new BuildStatementException("构建prepareStatement语句出错", e);
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

        return setParams(pstmt, params);
    }

    /**
     * 在'PreparedStatement'中设置SQL语句参数.
     * @param pstmt PreparedStatement实例
     * @param paramsArr 不定长数组参数
     * @return PreparedStatement实例
     * @throws SQLException SQL异常
     */
    private static PreparedStatement setBatchPreparedStatementParams(PreparedStatement pstmt,
            Object[]... paramsArr) throws SQLException {
        if (paramsArr == null) {
            return pstmt;
        }

        // 遍历批量执行每条语句的数组参数.
        for (Object[] params: paramsArr) {
            if (params == null) {
                continue;
            }
            setParams(pstmt, params);
            pstmt.addBatch();
        }

        return pstmt;
    }

    /**
     * 循环设置PreparedStatement的参数.
     * @param pstmt PreparedStatement实例
     * @param params 数组参数
     * @return PreparedStatement实例
     * @throws SQLException SQL异常
     */
    private static PreparedStatement setParams(PreparedStatement pstmt, Object[] params) throws SQLException {
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
    public static ResultSet getQueryResultSet(Connection conn,PreparedStatement pstmt) {
        try {
            return pstmt.executeQuery();
        } catch (SQLException e) {
            close(conn, pstmt);
            throw new ExecuteSqlException("执行查询的SQL语句出错!", e);
        }
    }

    /**
     * 得到查询SQL语句的ResultSet结果集.
     * @param pstmt PreparedStatement实例.
     */
    public static void executeUpdate(Connection conn, PreparedStatement pstmt) {
        try {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExecuteSqlException("执行新增的SQL语句出错!", e);
        } finally {
            close(conn, pstmt);
        }
    }

    /**
     * 批量执行数据库的'增删改'操作.
     * @param conn 数据库连接
     * @param pstmt PreparedStatement实例
     */
    public static void executeBatchUpdate(Connection conn, PreparedStatement pstmt) {
        try {
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            throw new ExecuteSqlException("执行新增的SQL语句出错!", e);
        } finally {
            close(conn, pstmt);
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
     * @param beanClass 空属性的Bean的class
     * @param propMap Bean属性对应的Map
     * @param <T> 泛型方法
     * @return 值
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InvocationTargetException InvocationTargetException
     * @throws InstantiationException InstantiationException
     */
    public static <T> T getBeanValue(ResultSet rs, ResultSetMetaData rsmd, Class<T> beanClass,
            Map<String, PropertyDescriptor> propMap) throws IllegalAccessException, SQLException,
            InvocationTargetException, InstantiationException {
        T bean = beanClass.newInstance();
        for (int i = 0, cols = rsmd.getColumnCount(); i < cols; i++) {
            String columnName = JdbcHelper.getColumn(rsmd, i + 1);
            if (propMap.containsKey(columnName)) {
                PropertyDescriptor prop = propMap.get(columnName);
                // 获取并调用setter方法.
                Method propSetter = prop.getWriteMethod();
                if (propSetter == null || propSetter.getParameterTypes().length != 1) {
                    log.warn("类'{}'的属性'{}'没有标准的setter方法", beanClass.getName(), columnName);
                    continue;
                }

                // 得到属性类型并将该数据库列值转成Java对应类型的值
                Class<?> propType = prop.getPropertyType();
                Object value = FieldHandlerChain.newInstance().getColumnValue(rs, i + 1, propType);
                propSetter.invoke(bean, value);
            }
        }
        return bean;
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
                log.error("关闭数据库连接Connection失败！", e);
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
                log.error("关闭PreparedStatement失败！", e);
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
                log.error("关闭ResultSet失败！", e);
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

    /**
     * 关闭`Connection`和`PreparedStatement`资源.
     * @param conn connection实例
     * @param pstmt PreparedStatement实例
     */
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(pstmt);
        close(conn);
    }

}