package com.blinkfox.adept.test;

import com.blinkfox.adept.config.AdeptConfigManager;
import com.blinkfox.adept.core.Adept;
import com.blinkfox.adept.core.results.impl.BeanHandler;
import com.blinkfox.adept.core.results.impl.BeanListHandler;
import com.blinkfox.adept.core.results.impl.ColumnsHandler;
import com.blinkfox.adept.core.results.impl.MapHandler;
import com.blinkfox.adept.core.results.impl.SingleHandler;
import com.blinkfox.adept.test.bean.UserInfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Logger;

/**
 * Adept测试类.
 * @author blinkfox on 2017/6/5.
 */
public class AdeptTest {

    /* 查询所有用户的SQL语句. */
    private static final String ALL_USER_SQL = "SELECT * FROM user";

    /**
     * 初始化加载Adept配置.
     */
    @BeforeClass
    public static void init() {
        AdeptConfigManager.getInstance().initLoad(MyAdeptConfig.class);
    }

    /**
     * 测试获取数据源.
     */
    @Test
    public void testGetDataSource() {
        DataSource ds = Adept.getDataSource();
        Assert.assertNotNull(ds);
    }

    /**
     * 测试获取实例.
     */
    @Test
    public void testNewInstance() {
        Adept adept = Adept.newInstance();
        Assert.assertNotNull(adept);
    }

    /**
     * 测试获取实例.
     */
    @Test
    public void testQuery() {
        String sql = "SELECT * FROM user AS u WHERE u.age > ?";
        ResultSet rs = Adept.quickStart().query(sql, 19).getRs();
        Assert.assertNotNull(rs);
    }

    /**
     * 测试获取mapList的实例.
     */
    @Test
    public void testToMapList() {
        String sql = "SELECT id AS bh, name AS myName, email AS myEmail, birthday FROM user AS u WHERE u.age > ?";
        List<Map<String, Object>> maps = Adept.quickStart().query(sql, 19).end();
        Assert.assertNotNull(maps);
    }

    /**
     * 测试获取mapList的实例.
     */
    @Test
    public void testToMap() {
        String sql = "SELECT COUNT(*) AS user_count FROM user AS u";
        Map<String, Object> map = Adept.quickStart().query(sql).end(MapHandler.newInstance());
        // 获取用户总数的map，并断言数量
        Assert.assertNotNull(map);
        Assert.assertEquals(6L, map.get("user_count"));
    }

    /**
     * 测试通过`MapHandler.class`来生成得到Map的实例.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testEndByClass() {
        Map<String, Object> map = (Map<String, Object>) Adept.quickStart().query(ALL_USER_SQL).end(MapHandler.class);
        Assert.assertNotNull(map);
    }

    /**
     * 测试通过`BeanHandler`来生成得到Bean的实例.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToBean() {
        UserInfo userInfo = (UserInfo) Adept.quickStart()
                .query("SELECT id, name, nickname AS nickname, email AS email, sex, birthday FROM user AS u limit 0, 1")
                .end(BeanHandler.newInstance(UserInfo.class));
        Assert.assertNotNull(userInfo);
        Logger.info(userInfo);
    }

    /**
     * 测试通过`BeanListHandler`来生成得到Bean List集合的实例.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToBeanList() {
        List<UserInfo> userInfos = (List<UserInfo>) Adept.quickStart()
                .query("SELECT id, name, nickname AS nickname, email AS email, sex, birthday FROM user AS u")
                .end(BeanListHandler.newInstance(UserInfo.class));
        Assert.assertNotNull(userInfos);
        Logger.info(userInfos);
    }

    /**
     * 测试通过`ColumnsHandler`来生成得到List集合的实例.
     */
    @Test
    public void testToColumnList() {
        List<Object> nickNames = Adept.quickStart()
                .query("SELECT id FROM user AS u")
                .end(ColumnsHandler.newInstance());
        Assert.assertNotNull(nickNames);
        Logger.info(nickNames);
    }

    /**
     * 测试通过`SingleHandler`来生成得到单个对象的实例.
     */
    @Test
    public void testToSingle() {
        Object count = Adept.quickStart()
                .query("SELECT COUNT(*) FROM user AS u")
                .end(SingleHandler.newInstance());
        Assert.assertEquals(6L, count);
    }

    /**
     * 清除配置信息.
     */
    @AfterClass
    public static void destroy() {
        AdeptConfigManager.getInstance().destroy();
    }

}