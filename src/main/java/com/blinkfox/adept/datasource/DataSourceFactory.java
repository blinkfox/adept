package com.blinkfox.adept.datasource;

import com.blinkfox.adept.helpers.ClassHelper;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * DataSourceConfig类的工厂类，用于创建多种类别的数据源.
 * Created by blinkfox on 2017/6/21.
 */
public final class DataSourceFactory {

    /**
     * 私有构造方法.
     */
    private DataSourceFactory() {
        super();
    }

    /**
     * 得到新的实例.
     * @return DataSourceConfigFactory实例
     */
    public static DataSourceFactory newInstance() {
        return new DataSourceFactory();
    }

    /**
     * 构建默认的HikariDataSource并返回实例.
     * @return HikariDataSource实例
     */
    public HikariDataSource buildDefaultDataSource(String driver, String url, String user, String password) {
        return this.buildHikariDataSource(driver, url, user, password);
    }

    /**
     * 构建并返回HikariDataSource实例.
     * @return HikariDataSource实例
     */
    public HikariDataSource buildHikariDataSource(String driver, String url, String user, String password) {
        return (HikariDataSource) HikariDataSourceConfig.newInstance()
                .buildDataSource(driver, url, user, password)
                .getDataSource();
    }

    /**
     * 构建并返回自定义的DataSource实例.
     * @return 自定义的DataSource实例
     */
    public DataSource buildDataSource(Class<? extends DataSourceConfig> dsClass,
            String driver, String url, String user, String password) {
        DataSourceConfig dsConfig = (DataSourceConfig) ClassHelper.newInstanceByClass(dsClass);
        return dsConfig.buildDataSource(driver, url, user, password).getDataSource();
    }

}