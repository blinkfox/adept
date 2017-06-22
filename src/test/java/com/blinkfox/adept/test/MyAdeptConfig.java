package com.blinkfox.adept.test;

import com.blinkfox.adept.config.AbstractAdeptConfig;
import com.blinkfox.adept.datasource.DataSourceConfigBuilder;
import com.blinkfox.adept.helpers.PropHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

/**
 * 我的Adept配置类.
 * @author blinkfox on 2017/5/30.
 */
public class MyAdeptConfig extends AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     */
    @Override
    public void configDataSource(DataSourceConfigBuilder dsFactory) {
        Properties props = PropHelper.newInstance().loadPropFile("config.properties");
        HikariDataSource hds = dsFactory.buildDefaultDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        hds.setMaximumPoolSize(20);
    }

}