package com.blinkfox.adept.core;

import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.core.results.impl.MapHandler;
import com.blinkfox.adept.core.results.impl.MapListHandler;
import com.blinkfox.adept.exception.AdeptRuntimeException;
import com.blinkfox.adept.exception.NoDataSourceException;
import com.blinkfox.adept.exception.NullConnectionException;
import com.blinkfox.adept.helpers.ClassHelper;
import com.blinkfox.adept.helpers.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adept核心调用类.
 * @author blinkfox on 2017/6/5.
 */
public final class Adept {

    /* 配置信息 */
    private static final ConfigInfo configInfo = ConfigInfo.getInstance();

    private static final Logger log = LoggerFactory.getLogger(Adept.class);

    /* 数据库连接 */
    private static Connection conn;

    /* PreparedStatement对象引用 */
    private PreparedStatement pstmt;

    /* ResultSet结果集 */
    private ResultSet rs;

    /**
     * 私有构造方法.
     */
    private Adept() {
        super();
    }

    /**
     * rs的getter方法.
     * @return ResultSet实例.
     */
    public ResultSet getRs() {
        return rs;
    }

    /**
     * rs的setter方法.
     * @param rs ResultSet实例.
     */
    private Adept setRs(ResultSet rs) {
        this.rs = rs;
        return this;
    }

    /**
     * 获取数据源.
     */
    public static DataSource getDataSource() {
        DataSource ds = configInfo.getDataSource();
        if (ds == null) {
            throw new NoDataSourceException("未找到数据源信息!");
        }
        return ds;
    }

    /**
     * 创建新的Adept实例.
     * @return Adept实例.
     */
    public static Adept newInstance() {
        return new Adept();
    }

    /**
     * 关闭数据库连接等资源.
     */
    private void closeSource() {
        JdbcHelper.close(conn, pstmt, rs);
    }

    /**
     * 快速开始，即创建Adept实例并从数据源中获取数据库连接.
     * @return Adept实例.
     */
    public static Adept quickStart() {
        Adept adept = new Adept();
        conn = JdbcHelper.getConnection(getDataSource());
        if (conn == null) {
            throw new NullConnectionException("数据库连接Connection为null");
        }
        return adept;
    }

    /**
     * 得到并返回默认为'map的List集合'的结果.
     * @return maps集合
     */
    public List<Map<String, Object>> end() {
        return this.end(MapListHandler.newInstance());
    }

    /**
     * 得到并返回泛型T的结果,同时关闭资源.
     * @param handler 处理器
     * @return 泛型T
     */
    public <T> T end(ResultHandler<T> handler) {
        // 如果handler为null，执行转换并返回转换后的结果，最后关闭资源。否则抛出异常.
        if (handler != null) {
            T t = handler.transform(rs);
            this.closeSource();
            return t;
        }
        throw new AdeptRuntimeException("ResultsHandler实例为null");
    }

    /**
     * 得到并返回Object型的结果,由于会通过反射创建实例，需要Handler的构造方法不是private的.
     * @param handlerClazz ResultsHandler的Class
     * @return Object实例
     */
    @SuppressWarnings("unchecked")
    public Object end(Class<?> handlerClazz) {
        // 实例化class的实例为handler,如果handler不为空且是ResultsHandler的子实例，则可执行查询转换结果.
        Object handler = ClassHelper.newInstanceByClass(handlerClazz);
        if (handler != null && handler instanceof ResultHandler) {
            return this.end((ResultHandler) handler);
        }
        throw  new AdeptRuntimeException("实例化后的handler为空或不是ResultsHandler的实现类.");
    }

    /**
     * 得到并返回Map类型的结果,同时关闭资源.
     * @return Map实例
     */
    public Map<String, Object> end2Map() {
        return end(MapHandler.newInstance());
    }

    /**
     * 执行数据库查询.
     * @param sql sql语句
     * @param params 不定参数
     * @return ResultSet实例
     */
    public Adept query(String sql , Object... params) {
        // SQL为空则关闭连接，直接返回Adept实例.
        if (sql == null || sql.length() == 0) {
            JdbcHelper.close(conn);
            return this;
        }

        // 根据数据库连接、SQL语句及参数得到PreparedStatement实例，然后再得到ResultSet实例.
        log.info("执行的sql:{}\n参数params:{}", sql, params);
        pstmt = JdbcHelper.getPreparedStatement(conn, sql, params);
        return this.setRs(JdbcHelper.getQueryResultSet(pstmt));
    }

}