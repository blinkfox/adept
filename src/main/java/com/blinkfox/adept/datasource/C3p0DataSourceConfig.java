package com.blinkfox.adept.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * C3P0数据库连接池配置实现类.
 * Created by blinkfox on 2017/6/24.
 */
public class C3p0DataSourceConfig extends DataSourceConfig<ComboPooledDataSource> {

    private static final Logger log = LoggerFactory.getLogger(C3p0DataSourceConfig.class);

    /**
     * 获取新的实例.
     * @return C3p0DataSourceConfig实例
     */
    public static C3p0DataSourceConfig newInstance() {
        return new C3p0DataSourceConfig();
    }

    /**
     * 通过基础配置信息构建DBCP数据源信息.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public ComboPooledDataSource buildDataSource(String driver, String url, String user, String password) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            log.error("设置C3P0数据源的DriverClass出错!", e);
        }
        dataSource.setJdbcUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * 关闭C3P0数据源.
     */
    @Override
    public void close() {
        if (super.dataSource != null) {
            super.dataSource.close();
            log.info("已关闭C3P0数据库连接池!");
        }
    }

}