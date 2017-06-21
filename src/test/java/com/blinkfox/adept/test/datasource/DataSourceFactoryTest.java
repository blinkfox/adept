package com.blinkfox.adept.test.datasource;

import com.blinkfox.adept.config.ConfigInfo;
import com.blinkfox.adept.datasource.DataSourceFactory;
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
public class DataSourceFactoryTest {

    /** DataSourceFactory全局实例. */
    private static DataSourceFactory dsFactory;

    /** Properties全局实例. */
    private static Properties props;

    /**
     * 初始化DataSourceFactory实例.
     */
    @BeforeClass
    public static void init() {
        dsFactory = DataSourceFactory.newInstance();
        props = PropHelper.newInstance().loadPropFile("config.properties");
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testBuildDefaultDataSource() {
        HikariDataSource hds = dsFactory.buildDefaultDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        Assert.assertNotNull(hds);
    }

    /**
     * 测试构建Hikari的数据源.
     */
    @Test
    public void testBuildHikariDataSource() {
        HikariDataSource hds = dsFactory.buildHikariDataSource(props.getProperty("driver"), props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
        Assert.assertNotNull(hds);
    }

    /**
     * 测试构建默认的数据源.
     */
    @Test
    public void testBuildDataSource() {
        DataSource ds = dsFactory.buildDataSource(HikariDataSourceConfig.class,
                props.getProperty("driver"), props.getProperty("url"), props.getProperty("username"),
                props.getProperty("password"));
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
        dsFactory = null;
    }

}