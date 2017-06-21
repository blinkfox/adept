package com.blinkfox.adept.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.blinkfox.adept.config.ConfigInfo;

/**
 * Druid数据库连接池的配置实现类.
 * Created by blinkfox on 2017/6/22.
 */
public class DruidDataSourceConfig extends DataSourceConfig {

    /** Druid数据源实例. */
    private DruidDataSource dataSource;

    /**
     * 获取新的实例.
     * @return DruidDataSourceConfig实例
     */
    public static DruidDataSourceConfig newInstance() {
        return new DruidDataSourceConfig();
    }

    /**
     * 通过基础配置信息构建Druid数据源信息.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    @Override
    public DruidDataSourceConfig buildDataSource(String driver, String url, String user, String password) {
        this.dataSource = new DruidDataSource();
        this.dataSource.setDriverClassName(driver);
        this.dataSource.setUrl(url);
        this.dataSource.setUsername(user);
        this.dataSource.setPassword(password);
        super.setDataSource(dataSource);
        ConfigInfo.getInstance().setDsConfig(this);
        return this;
    }

    /**
     * 关闭Druid数据源.
     */
    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}