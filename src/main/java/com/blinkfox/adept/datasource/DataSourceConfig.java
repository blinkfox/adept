package com.blinkfox.adept.datasource;

import javax.sql.DataSource;

/**
 * 数据源配置接口.
 * @author blinkfox on 2017/6/19.
 */
public interface DataSourceConfig {

    /**
     * 保存数据源到配置信息中.
     * @param dataSource 数据源
     */
    void setDataSource(DataSource dataSource);

    /**
     * 获取数据源.
     * @return 数据源实例
     */
    DataSource getDataSource();

    /**
     * 关闭数据源.
     */
    void close();

}