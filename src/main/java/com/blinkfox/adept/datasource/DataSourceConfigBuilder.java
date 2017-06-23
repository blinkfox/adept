package com.blinkfox.adept.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.helpers.ClassHelper;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * DataSourceConfig类的工厂类，用于创建多种类别的数据源.
 * Created by blinkfox on 2017/6/21.
 */
public final class DataSourceConfigBuilder {

    /**
     * 私有构造方法.
     */
    private DataSourceConfigBuilder() {
        super();
    }

    /**
     * 得到新的实例.
     * @return DataSourceConfigFactory实例
     */
    public static DataSourceConfigBuilder newInstance() {
        return new DataSourceConfigBuilder();
    }

    /**
     * 通过数据库连接基础信息构建默认的HikariDataSource数据源并保存到配置信息中.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public HikariDataSource buildDefaultDataSource(String driver, String url, String user, String password) {
        return this.buildHikariDataSource(driver, url, user, password);
    }

    /**
     * 通过数据库连接基础信息构建HikariDataSource数据源并保存到配置信息中.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public HikariDataSource buildHikariDataSource(String driver, String url, String user, String password) {
        HikariDataSourceConfig hdsConfig = HikariDataSourceConfig.newInstance();
        return this.saveDataSource(hdsConfig, hdsConfig.buildDataSource(driver, url, user, password));
    }

    /**
     * 将HikariDataSource数据源保存到配置信息中.
     * @param dataSource 数据源
     * @return HikariDataSource实例
     */
    public HikariDataSource buildHikariDataSource(HikariDataSource dataSource) {
        return this.saveDataSource(HikariDataSourceConfig.newInstance(), dataSource);
    }

    /**
     * 通过数据库连接基础信息构建DruidDataSource数据源并保存到配置信息中.
     * @param driver 数据库连接的JDBC驱动
     * @param url 数据库连接的url
     * @param user 数据库连接的用户名
     * @param password 数据库连接的密码
     */
    public DruidDataSource buildDruidDataSource(String driver, String url, String user, String password) {
        DruidDataSourceConfig ddsConfig = DruidDataSourceConfig.newInstance();
        return this.saveDataSource(ddsConfig, ddsConfig.buildDataSource(driver, url, user, password));
    }

    /**
     * 将DruidDataSource数据源保存到配置信息中.
     * @param dataSource Druid数据源
     * @return DruidDataSource实例
     */
    public DruidDataSource buildDruidDataSource(DruidDataSource dataSource) {
        return this.saveDataSource(DruidDataSourceConfig.newInstance(), dataSource);
    }

    /**
     * 通过DataSourceConfig的子类类class和DataSource数据源保存到配置信息中.
     * @param dsClass DataSourceConfig的class
     * @param dataSource 数据源
     */
    public <C extends DataSourceConfig, D extends DataSource> D saveDataSource(Class<C> dsClass, D dataSource) {
        return this.saveDataSource((DataSourceConfig) ClassHelper.newInstanceByClass(dsClass), dataSource);
    }

    /**
     * 将DataSourceConfig和DataSource数据源保存到配置信息中.
     * @param dsConfig 数据源配置实例
     * @param dataSource 数据源实例
     * @param <C> 数据源配置的泛型C
     * @param <D> 数据源的泛型D
     * @return 数据源
     */
    @SuppressWarnings("unchecked")
    public <C extends DataSourceConfig, D extends DataSource> D saveDataSource(C dsConfig, D dataSource) {
        dsConfig.setDataSource(dataSource);
        ConfigInfo.getInstance().setDsConfig(dsConfig);
        return dataSource;
    }

}