package com.blinkfox.adept.test.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.datasource.DataSourceConfigBuilder;
import com.blinkfox.adept.datasource.HikariDataSourceConfig;
import com.blinkfox.adept.helpers.PropHelper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
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

    /** HikariDataSource实例. */
    private static HikariDataSource hds;

    /** DruidDataSource实例. */
    private static DruidDataSource dds;

    /** Dbcp数据源实例. */
    private static BasicDataSource dbcpDs;

    /** C3P0数据源实例. */
    private static ComboPooledDataSource c3p0Ds;

    /** Properties全局实例. */
    private static Properties props;

    /**
     * 初始化DataSourceFactory实例.
     */
    @BeforeClass
    public static void init() {
        dsBuilder = DataSourceConfigBuilder.newInstance();
        props = PropHelper.newInstance().loadPropFile("config.properties");
        hds = dsBuilder.buildHikariDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        dds = dsBuilder.buildDruidDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        dbcpDs = dsBuilder.buildDbcpDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        c3p0Ds = dsBuilder.buildC3p0DataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
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
        Assert.assertNotNull(dsBuilder.buildHikariDataSource(hds));
    }

    /**
     * 测试构建Druid的数据源.
     */
    @Test
    public void testBuildDruidDataSource() {
        Assert.assertNotNull(dsBuilder.buildDruidDataSource(dds));
    }

    /**
     * 测试构建DBCP的数据源.
     */
    @Test
    public void testBuildDbcpDataSource() {
        Assert.assertNotNull(dsBuilder.buildDbcpDataSource(dbcpDs));
    }

    /**
     * 测试构建C3P0的数据源.
     */
    @Test
    public void testBuildC3p0DataSource() {
        Assert.assertNotNull(dsBuilder.buildC3p0DataSource(c3p0Ds));
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testSaveDataSource() {
        DataSource ds = dsBuilder.saveDataSource(HikariDataSourceConfig.class, HikariDataSourceConfig.newInstance()
                .buildDataSource(props.getProperty("driver"), props.getProperty("url"), props.getProperty("username"),
                props.getProperty("password")));
        Assert.assertNotNull(ds);
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testSaveDataSource2() {
        DataSource ds = dsBuilder.saveDataSource(HikariDataSourceConfig.newInstance(),
                HikariDataSourceConfig.newInstance().buildDataSource(props.getProperty("driver"),
                        props.getProperty("url"), props.getProperty("username"), props.getProperty("password")));
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