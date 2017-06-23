package com.blinkfox.adept.datasource;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * commons dbcp数据库连接池的配置实现类.
 * Created by blinkfox on 2017/6/23.
 */
public class DbcpDataSourceConfig extends DataSourceConfig<BasicDataSource> {

    private static final Logger log = LoggerFactory.getLogger(DbcpDataSourceConfig.class);

    /**
     * 获取新的实例.
     * @return DbcpDataSourceConfig实例
     */
    public static DbcpDataSourceConfig newInstance() {
        return new DbcpDataSourceConfig();
    }

    /**
     * 通过基础配置信息构建DBCP数据源信息.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public BasicDataSource buildDataSource(String driver, String url, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * 关闭DBCP数据源.
     */
    @Override
    public void close() {
        try {
            super.dataSource.close();
        } catch (SQLException e) {
            log.error("关闭DBCP数据源出错!", e);
        }
    }

}