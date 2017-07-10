package com.blinkfox.adept.test;

import com.blinkfox.adept.config.AbstractAdeptConfig;
import com.blinkfox.adept.datasource.DataSourceConfigBuilder;
import com.blinkfox.adept.helpers.PropHelper;
import com.blinkfox.adept.test.datasource.BonecpDataSourceConfig;
import com.jolbox.bonecp.BoneCPDataSource;

import java.util.Properties;

/**
 * 我的Adept配置类.
 * @author blinkfox on 2017/5/30.
 */
public class MyAdeptConfig extends AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     * @param builder 数据源构建器
     */
    @Override
    public void configDataSource(DataSourceConfigBuilder builder) {
        Properties props = PropHelper.newInstance().loadPropFile("config.properties");
        
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass(props.getProperty("driver"));
        ds.setJdbcUrl(props.getProperty("url"));
        ds.setUsername(props.getProperty("username"));
        ds.setPassword(props.getProperty("password"));
        
        builder.saveDataSource(BonecpDataSourceConfig.class, ds);
    }

}