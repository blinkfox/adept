package com.blinkfox.adept.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

/**
 * Adept封装配置信息的实体类.
 * @author blinkfox on 2017/5/31.
 */
public final class ConfigInfo {

    /* ConfigInfo的唯一实例 */
    private static final ConfigInfo configInfo = new ConfigInfo();

    /* 数据源 */
    private DataSource dataSource;

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
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        HikariDataSource hds = new HikariDataSource(config);
        this.setDataSource(hds);
        return hds;
    }

    /**
     * 清除配置信息.
     */
    public void clear() {
        dataSource = null;
    }

    /**
     * 获取数据源.
     * @return 数据源
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 仅仅子类可设置数据源的setter方法.
     * @param dataSource 数据源
     */
    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}