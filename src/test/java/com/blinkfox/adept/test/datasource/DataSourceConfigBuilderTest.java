package com.blinkfox.adept.test.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.datasource.DataSourceConfigBuilder;
import com.blinkfox.adept.datasource.HikariDataSourceConfig;
import com.blinkfox.adept.helpers.PropHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DataSourceFactory 的单元测试类.
 * Created by blinkfox on 2017/6/21.
 */
public class DataSourceConfigBuilderTest {

    /** DataSourceFactory全局实例. */
    private static DataSourceConfigBuilder dsBuilder;

    /** Properties全局实例. */
    private static Properties props;

    /**
     * 初始化DataSourceFactory实例.
     */
    @BeforeClass
    public static void init() {
        dsBuilder = DataSourceConfigBuilder.newInstance();
        props = PropHelper.newInstance().loadPropFile("config.properties");
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testBuildDefaultDataSource() {
        HikariDataSource hds = dsBuilder.buildDefaultDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        Assert.assertNotNull(hds);
    }

    /**
     * 测试构建Hikari的数据源.
     */
    @Test
    public void testBuildHikariDataSource() {
        HikariDataSource hds = dsBuilder.buildHikariDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        Assert.assertNotNull(hds);
    }

    /**
     * 测试构建Druid的数据源.
     */
    @Test
    public void testBuildDruidDataSource() {
        DruidDataSource dds = dsBuilder.buildDruidDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        Assert.assertNotNull(dds);
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testBuildDataSource() {
        DataSource ds = dsBuilder.buildDataSource(HikariDataSourceConfig.class, HikariDataSourceConfig.newInstance()
                .buildDataSource(props.getProperty("driver"), props.getProperty("url"), props.getProperty("username"),
                props.getProperty("password")));
        Assert.assertNotNull(ds);
    }

    /**
     * 结束前销毁实例.
     */
    @After
    public void clear() {
        ConfigInfo.getInstance().clear();
    }

    /**
     * 结束前销毁实例.
     */
    @AfterClass
    public static void destroy() {
        dsBuilder = null;
    }

}