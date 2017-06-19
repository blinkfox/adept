package com.blinkfox.adept.config;

import com.blinkfox.adept.datasource.DataSourceConfig;
import com.blinkfox.adept.datasource.HikariDataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

/**
 * Adept封装配置信息的实体类.
 * @author blinkfox on 2017/5/31.
 */
public final class ConfigInfo {

    /* ConfigInfo的唯一实例 */
    private static final ConfigInfo configInfo = new ConfigInfo();

    /** 数据源配置信息. */
    private DataSourceConfig dsConfig;

    /**
     * 私有构造方法.
     */
    private ConfigInfo() {
        super();
    }

    /**
     * 得到ConfigInfo的唯一实例.
     * @return ConfigInfo实例
     */
    public static ConfigInfo getInstance() {
        return configInfo;
    }

    /**
     * 使用默认的数据库连接池.
     * @return HikariDataSource实例
     */
    public HikariDataSource useDefaultDataSource(String driver, String url, String user, String password) {
        HikariDataSourceConfig.newInstance().buildDataSource(driver, url, user, password).saveConfig();
        return (HikariDataSource) dsConfig.getDataSource();
    }

    /**
     * 关闭数据源，清除数据源配置信息.
     */
    void clear() {
        if (dsConfig != null) {
            dsConfig.close();
            dsConfig = null;
        }
    }

    /**
     * 获取数据源.
     * @return 数据源
     */
    public DataSource getDataSource() {
        return dsConfig.getDataSource();
    }

    /**
     * 获取数据源配置信息的多态实例.
     * @return DataSourceConfig多态实例
     */
    public DataSourceConfig getDsConfig() {
        return dsConfig;
    }

    /**
     * 通过setter方法,设置数据源配置信息实例.
     * @param dsConfig DataSourceConfig多态实例
     */
    public void setDsConfig(DataSourceConfig dsConfig) {
        this.dsConfig = dsConfig;
    }

}