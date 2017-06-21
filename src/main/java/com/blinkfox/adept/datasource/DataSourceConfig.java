package com.blinkfox.adept.datasource;

import javax.sql.DataSource;

/**
 * 数据源配置接口.
 * @author blinkfox on 2017/6/19.
 */
public abstract class DataSourceConfig {

    /** 数据源. */
    private DataSource dataSource;

    /**
     * 通过基础配置信息设置数据源并返回.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     * @return 数据源
     */
    public abstract DataSourceConfig buildDataSource(String driver, String url, String user, String password);

    /**
     * 关闭数据源,清楚缓存信息.
     */
    public abstract void close();

    /**
     * 获取数据源.
     * @return 数据源实例
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 设置并返回数据源.
     * @param dataSource 数据源实例
     */
    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}