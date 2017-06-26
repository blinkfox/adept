package com.blinkfox.adept.config;

import com.blinkfox.adept.datasource.DataSourceConfig;

import javax.sql.DataSource;

/**
 * Adept封装配置信息的实体类.
 * @author blinkfox on 2017/5/31.
 */
public final class ConfigInfo {

    /* ConfigInfo的唯一实例 */
    private static final ConfigInfo configInfo = new ConfigInfo();

    /** 数据源配置信息. */
    private DataSourceConfig<? extends DataSource> dsConfig;

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
     * 关闭数据源，清除数据源配置信息.
     */
    public void clear() {
        if (dsConfig != null) {
            if (dsConfig.getDataSource() != null) {
                dsConfig.close();
            }
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
     * 通过setter方法,设置数据源配置信息实例.
     * @param dsConfig DataSourceConfig多态实例
     */
    public <D extends DataSource> void setDsConfig(DataSourceConfig<D> dsConfig) {
        this.dsConfig = dsConfig;
    }

}