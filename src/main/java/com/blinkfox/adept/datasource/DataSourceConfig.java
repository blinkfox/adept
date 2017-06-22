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
     * 保存数据源到配置信息中.
     * @param dataSource 数据源
     */
    public abstract void saveDataSource(DataSource dataSource);

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

}