package com.blinkfox.adept.datasource;

import com.blinkfox.adept.config.ConfigInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP数据库连接池的配置实现类.
 * @author blinkfox on 2017/6/19.
 */
public class HikariDataSourceConfig extends DataSourceConfig {

    /** HikariCP数据源实例. */
    private HikariDataSource dataSource;

    /**
     * 获取新的实例.
     * @return HikariDataSourceConfig实例
     */
    public static HikariDataSourceConfig newInstance() {
        return new HikariDataSourceConfig();
    }

    /**
     * 通过基础配置信息构建数据源信息.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    @Override
    public HikariDataSourceConfig buildDataSource(String driver, String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        this.dataSource = new HikariDataSource(config);
        super.setDataSource(dataSource);
        ConfigInfo.getInstance().setDsConfig(this);
        return this;
    }

    /**
     * 关闭HikariCP数据源.
     */
    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}