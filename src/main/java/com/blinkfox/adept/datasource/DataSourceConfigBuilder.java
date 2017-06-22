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
        return (HikariDataSource) this.buildDataSource(hdsConfig,
                hdsConfig.buildDataSource(driver, url, user, password));
    }

    /**
     * 将HikariDataSource数据源保存到配置信息中.
     * @param dataSource 数据源
     * @return HikariDataSource实例
     */
    private HikariDataSource buildHikariDataSource(DataSource dataSource) {
        return (HikariDataSource) this.buildDataSource(HikariDataSourceConfig.newInstance(), dataSource);
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
        return (DruidDataSource) this.buildDataSource(ddsConfig,
                ddsConfig.buildDataSource(driver, url, user, password));
    }

    /**
     * 将DruidDataSource数据源保存到配置信息中.
     * @param dataSource 数据源
     * @return HikariDataSource实例
     */
    private DruidDataSource buildDruidDataSource(DataSource dataSource) {
        return (DruidDataSource) this.buildDataSource(DruidDataSourceConfig.newInstance(), dataSource);
    }

    /**
     * 通过DataSourceConfig的子类类class和DataSource数据源保存到配置信息中.
     * @param dsClass DataSourceConfig的class
     * @param dataSource 数据源
     */
    public DataSource buildDataSource(Class<? extends DataSourceConfig> dsClass, DataSource dataSource) {
        return this.buildDataSource((DataSourceConfig) ClassHelper.newInstanceByClass(dsClass), dataSource);
    }

    /**
     * 通过DataSourceConfig实例和DataSource数据源保存到配置信息中.
     * @param dsConfig DataSourceConfig的子类
     * @param dataSource 数据源
     */
    public DataSource buildDataSource(DataSourceConfig dsConfig, DataSource dataSource) {
        dsConfig.saveDataSource(dataSource);
        ConfigInfo.getInstance().setDsConfig(dsConfig);
        return dataSource;
    }

}