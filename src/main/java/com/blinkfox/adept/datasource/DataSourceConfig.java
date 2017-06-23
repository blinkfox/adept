package com.blinkfox.adept.datasource;

import javax.sql.DataSource;

/**
 * 数据源配置的抽象类.
 * @author blinkfox on 2017/6/19.
 */
public abstract class DataSourceConfig<D extends DataSource> {

    /** 数据源. */
    protected D dataSource;

    /**
     * 获取数据源实例.
     * @return 数据源实例
     */
    public D getDataSource() {
        return dataSource;
    }

    /**
     * 设置数据源实例.
     * @param dataSource 数据源实例
     */
    public void setDataSource(D dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 关闭数据源.
     */
    public abstract void close();

}