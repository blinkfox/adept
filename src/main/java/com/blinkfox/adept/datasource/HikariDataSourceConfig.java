package com.blinkfox.adept.datasource;

import com.blinkfox.adept.exception.AdeptRuntimeException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * HikariCP数据库连接池的配置实现类.
 * @author blinkfox on 2017/6/19.
 */
public class HikariDataSourceConfig implements DataSourceConfig {

    /** HikariCP数据源. */
    private HikariDataSource dataSource;

    /**
     * 获取新的实例.
     * @return HikariDataSourceConfig实例
     */
    public static HikariDataSourceConfig newInstance() {
        return new HikariDataSourceConfig();
    }

    /**
     * 通过基础配置信息构建Hikari数据源信息.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public HikariDataSource buildDataSource(String driver, String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    /**
     * 设置Hikari数据源到配置信息中.
     * @param dataSource 数据源.
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof HikariDataSource) {
            this.dataSource = (HikariDataSource) dataSource;
        } else {
            throw new AdeptRuntimeException("setDataSource参数非DruidDataSource实例.");
        }
    }

    /**
     * 获取Hikari数据源.
     * @return 数据源实例
     */
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 关闭Hikari数据源.
     */
    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}