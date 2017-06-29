package com.blinkfox.adept.core;

import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.core.results.impl.BeanHandler;
import com.blinkfox.adept.core.results.impl.BeanListHandler;
import com.blinkfox.adept.core.results.impl.ColumnsHandler;
import com.blinkfox.adept.core.results.impl.MapHandler;
import com.blinkfox.adept.core.results.impl.MapListHandler;
import com.blinkfox.adept.core.results.impl.SingleHandler;
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
 * 数据库操作的Adept核心调用接口类.
 * @author blinkfox on 2017/6/5.
 */
public final class Adept {

    /** 配置信息. */
    private static final ConfigInfo configInfo = ConfigInfo.getInstance();

    private static final Logger log = LoggerFactory.getLogger(Adept.class);

    /** 数据库连接. */
    private Connection conn;

    /** PreparedStatement对象. */
    private PreparedStatement pstmt;

    /** ResultSet结果集. */
    private ResultSet rs;

    /**
     * 私有构造方法.
     */
    private Adept() {
        super();
    }

    /**
     * Connection实例引用`conn`的getter方法.
     * @return Connection实例
     */
    public Connection getConn() {
        return this.conn;
    }

    /**
     * PreparedStatement实例引用`pstmt`的getter方法.
     * @return PreparedStatement实例
     */
    public PreparedStatement getPstmt() {
        return pstmt;
    }

    /**
     * ResultSet实例引用`rs`的getter方法.
     * @return ResultSet实例.
     */
    public ResultSet getRs() {
        return rs;
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
     * 获取数据库连接.
     * @return Adept实例.
     */
    public Adept getConnection() {
        this.conn = JdbcHelper.getConnection(getDataSource());
        if (this.conn == null) {
            throw new NullConnectionException("数据库连接Connection为null");
        }
        return this;
    }

    /**
     * 校验和打印SQL语句.
     * @param sql SQL语句
     */
    private void validSql(String sql) {
        // SQL为空则关闭连接且抛出异常.
        if (sql == null || sql.length() == 0) {
            JdbcHelper.close(this.conn);
            throw new AdeptRuntimeException("sql语句为空!");
        }
        log.info("Adept执行的SQL:{}", sql);
    }

    /**
     * 获取数据库连接的预编译执行语句PreparedStatement实例.
     * @param sql SQL语句
     * @param params SQL对应的有序参数
     * @return PreparedStatement实例
     */
    public Adept getPreparedStatement(String sql, Object... params) {
        this.validSql(sql);
        log.info("Adept执行的SQL对应的参数params:{}", params);
        this.pstmt = JdbcHelper.getPreparedStatement(this.conn, sql, params);
        return this;
    }

    /**
     * 获取数据库连接的预编译执行语句PreparedStatement实例.
     * @param sql SQL语句
     * @param params SQL对应的有序参数
     * @return PreparedStatement实例
     */
    public Adept getBatchPreparedStatement(String sql, Object[]... params) {
        this.validSql(sql);
        this.pstmt = JdbcHelper.getBatchPreparedStatement(this.conn, sql, params);
        return this;
    }

    /**
     * 关闭数据库连接等资源.
     */
    public void closeSource() {
        JdbcHelper.close(this.conn, this.pstmt, this.rs);
    }

    /**
     * 快速开始，即创建Adept实例并从数据源中获取数据库连接.
     * @return Adept实例.
     */
    public static Adept quickStart() {
        return new Adept().getConnection();
    }

    /**
     * 执行数据库查询.
     * @param sql sql语句
     * @param params 不定参数
     * @return ResultSet实例
     */
    public Adept query(String sql , Object... params) {
        // 根据数据库连接、SQL语句及参数得到PreparedStatement实例，然后再得到ResultSet实例.
        this.rs = JdbcHelper.getQueryResultSet(this.conn, this.getPreparedStatement(sql, params).getPstmt());
        return this;
    }

    /**
     * 将sql语句的查询结果转换并返回对应ResultHandler的结果类型.
     * @param handler handler
     * @param sql sql
     * @param params SQL参数
     * @param <T> 泛型方法
     * @return 泛型结果T
     */
    public <T> T query(ResultHandler<T> handler, String sql, Object... params) {
        return this.query(sql, params).end(handler);
    }

    /**
     * 将sql语句的查询结果转换并返回对应`ResultHandler class`的结果类型.
     * @param handlerClass 结果处理器实例
     * @param sql SQL语句
     * @param params SQL参数
     * @param <T> 泛型方法
     * @return Object实例
     */
    public <T extends ResultHandler<R>, R> Object query(Class<T> handlerClass, String sql, Object... params) {
        return this.query(sql, params).end(handlerClass);
    }

    /**
     * 查询sql语句并将结果转换为Map.
     * @param sql SQL语句
     * @param params SQL参数
     * @return map实例
     */
    public Map<String, Object> queryForMap(String sql, Object... params) {
        return this.query(sql, params).end2Map();
    }

    /**
     * 查询sql语句并将结果转换为MapList.
     * @param sql SQL语句
     * @param params SQL参数
     * @return map实例
     */
    public List<Map<String, Object>> queryForMapList(String sql, Object... params) {
        return this.query(sql, params).end2MapList();
    }

    /**
     * 查询sql语句并将结果转换为Bean.
     * @param beanClass 实体bean的class
     * @param sql SQL语句
     * @param params SQL参数
     * @param <T> 泛型Bean
     * @return bean实例
     */
    public <T> T queryForBean(Class<T> beanClass, String sql, Object... params) {
        return this.query(sql, params).end2Bean(beanClass);
    }

    /**
     * 查询sql语句并将结果转换为Bean的List集合.
     * @param beanClass 实体bean的class
     * @param sql SQL语句
     * @param params SQL参数
     * @param <T> 泛型Bean
     * @return Bean的List集合
     */
    public <T> List<T> queryForBeanList(Class<T> beanClass, String sql, Object... params) {
        return this.query(sql, params).end2BeanList(beanClass);
    }

    /**
     * 查询sql语句并将结果转换为MapList.
     * @param sql SQL语句
     * @param params SQL参数
     * @return 第一列数据的实例集合
     */
    public List<Object> queryForColumns(String sql, Object... params) {
        return this.query(sql, params).end2Columns();
    }

    /**
     * 查询sql语句并将结果转换为MapList.
     * @param sql SQL语句
     * @param params SQL参数
     * @return 单个实例
     */
    public Object queryForSingle(String sql, Object... params) {
        return this.query(sql, params).end2Single();
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
     * @param <T> 定义泛型T
     * @return 泛型T
     */
    public <T> T end(ResultHandler<T> handler) {
        // 如果handler不为null，执行转换并返回转换后的结果，最后关闭资源。否则抛出异常.
        if (handler != null) {
            T t = handler.transform(this.rs);
            this.closeSource();
            return t;
        }
        throw new AdeptRuntimeException("ResultsHandler实例为null");
    }

    /**
     * 得到并返回Object型的结果,由于会通过反射创建实例，需要Handler的构造方法不是private的.
     * @param handlerClass ResultsHandler的Class
     * @param <T> 定义泛型T为ResultHandler的实现类
     * @param <R> 定义返回结果的范型R
     * @return Object实例
     */
    public <T extends ResultHandler<R>, R> Object end(Class<T> handlerClass) {
        return this.end(ClassHelper.newInstanceByClass(handlerClass));
    }

    /**
     * 得到并返回'有序Map'类型的结果,同时关闭资源.
     * @return Map实例
     */
    public Map<String, Object> end2Map() {
        return this.end(MapHandler.newInstance());
    }

    /**
     * 得到并返回'Map的List集合'类型的结果,同时关闭资源.
     * @return MapList实例
     */
    public List<Map<String, Object>> end2MapList() {
        return this.end(MapListHandler.newInstance());
    }

    /**
     * 得到并返回'实体Bean'类型的结果,同时关闭资源.
     * @param beanClass 结果Bean的class
     * @param <T> 定义泛型T
     * @return beanClass对应类的实例
     */
    public <T> T end2Bean(Class<T> beanClass) {
        return this.end(new BeanHandler<T>(beanClass));
    }

    /**
     * 得到并返回'JavaBean的List集合'类型的结果,同时关闭资源.
     * @param beanClass JavaBean的class
     * @param <T> 定义泛型T
     * @return JavaBean的List集合
     */
    public <T> List<T> end2BeanList(Class<T> beanClass) {
        return this.end(new BeanListHandler<T>(beanClass));
    }

    /**
     * 得到并返回'第一列数据的集合'类型的结果,同时关闭资源.
     * @return MapList实例
     */
    public List<Object> end2Columns() {
        return this.end(ColumnsHandler.newInstance());
    }

    /**
     * 得到并返回'第一列数据的集合'类型的结果,同时关闭资源.
     * @return MapList实例
     */
    public Object end2Single() {
        return this.end(SingleHandler.newInstance());
    }

    /**
     * 执行数据库的更新操作.
     * <p>执行数据库的增删改等操作，最后再结束前关闭资源.</p>
     * @param sql sql语句
     * @param params 不定参数
     */
    private void executeUpdate(String sql , Object... params) {
        JdbcHelper.executeUpdate(this.conn, this.getPreparedStatement(sql, params).getPstmt());
    }

    /**
     * 执行数据库的更新操作.
     * <p>执行数据库的增删改等操作，最后再结束前关闭资源.</p>
     * @param sql sql语句
     * @param params 不定参数
     */
    private void executeBatchUpdate(String sql , Object[]... params) {
        JdbcHelper.executeBatchUpdate(this.conn, this.getBatchPreparedStatement(sql, params).getPstmt());
    }

    /**
     * 执行数据库的插入操作.
     * <p>执行插入，再放回前关闭资源.</p>
     * @param sql sql语句
     * @param params 不定参数
     */
    public void insert(String sql , Object... params) {
        this.executeUpdate(sql, params);
    }

    /**
     * 执行数据库的更新操作.
     * <p>执行插入，再放回前关闭资源.</p>
     * @param sql sql语句
     * @param params 不定参数
     */
    public void update(String sql , Object... params) {
        this.executeUpdate(sql, params);
    }

    /**
     * 执行数据库的删除操作.
     * <p>执行插入，再放回前关闭资源.</p>
     * @param sql sql语句
     * @param params 不定参数
     */
    public void delete(String sql , Object... params) {
        this.executeUpdate(sql, params);
    }

    /**
     * 批量插入数据.
     * @param sql sql语句
     * @param params 数组的集合
     */
    public void batchInsert(String sql, List<Object[]> params) {
        this.executeBatchUpdate(sql, params.toArray());
    }

    /**
     * 批量更新数据.
     * @param sql sql语句
     * @param params 数组的集合
     */
    public void batchUpdate(String sql, List<Object[]> params) {
        this.executeBatchUpdate(sql, params.toArray());
    }

    /**
     * 批量删除数据.
     * @param sql sql语句
     * @param params 数组的集合
     */
    public void batchDelete(String sql, List<Object[]> params) {
        this.executeBatchUpdate(sql, params.toArray());
    }

}