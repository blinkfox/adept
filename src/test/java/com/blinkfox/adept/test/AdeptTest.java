package com.blinkfox.adept.test;

import com.blinkfox.adept.config.AdeptConfigManager;
import com.blinkfox.adept.core.Adept;
import com.blinkfox.adept.core.results.impl.BeanHandler;
import com.blinkfox.adept.core.results.impl.BeanListHandler;
import com.blinkfox.adept.core.results.impl.ColumnsHandler;
import com.blinkfox.adept.core.results.impl.MapHandler;
import com.blinkfox.adept.core.results.impl.MapListHandler;
import com.blinkfox.adept.core.results.impl.SingleHandler;
import com.blinkfox.adept.test.bean.UserInfo;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adept测试类.
 * @author blinkfox on 2017/6/5.
 */
public class AdeptTest {

    private static final Logger log = LoggerFactory.getLogger(AdeptTest.class);

    private static final String ALL_USER_SQL = "SELECT * FROM t_user";

    private static final String USER_INFO_SQL = "SELECT c_id AS id, c_name AS name, c_nickname AS nickName,"
            + " c_email AS email, n_sex AS sex, c_birthday AS birthday FROM t_user AS u limit 0, ?";

    private static final String USER_BY_AGE_SQL = "SELECT * FROM t_user AS u WHERE u.n_age > ?";

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
        Assert.assertNotNull(Adept.quickStart().query(USER_BY_AGE_SQL, 19).getRs());
    }

    /**
     * 测试泛型的`query`方法.
     */
    @Test
    public void testQuery2() {
        Assert.assertNotNull(Adept.quickStart().query(MapListHandler.newInstance(), USER_BY_AGE_SQL, 19));
        Assert.assertNotNull(Adept.quickStart().query(MapListHandler.class, USER_BY_AGE_SQL, 19));
    }

    /**
     * 测试获取mapList的实例.
     */
    @Test
    public void testToMapList() {
        String sql = "SELECT c_id AS bh, c_name AS myName, c_email AS myEmail, c_birthday "
                + "FROM t_user AS u WHERE u.n_age > ?";
        List<Map<String, Object>> maps = Adept.quickStart().query(sql, 19).end();
        Assert.assertNotNull(maps);
    }

    /**
     * 测试获取mapList的实例.
     */
    @Test
    public void testToMap() {
        String sql = "SELECT COUNT(*) AS user_count FROM t_user AS u";
        Map<String, Object> map = Adept.quickStart().query(sql).end(MapHandler.newInstance());
        // 获取用户总数的map，并断言数量
        Assert.assertNotNull(map);
        Assert.assertEquals(3, map.get("user_count"));
    }

    /**
     * 测试通过`MapHandler.class`来生成得到Map的实例.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testEndByClass() {
        Map<String, Object> map = (Map<String, Object>) Adept.quickStart().query(ALL_USER_SQL).end(MapHandler.class);
        Assert.assertNotNull(map);
        Assert.assertEquals("闪烁之狐", map.get("c_nickname"));
    }

    /**
     * 测试通过`BeanHandler`来生成得到Bean的实例.
     */
    @Test
    public void testToBean() {
        UserInfo userInfo = Adept.quickStart()
                .query(USER_INFO_SQL, 3)
                .end(BeanHandler.newInstance(UserInfo.class));
        Assert.assertNotNull(userInfo);
        log.info(userInfo.toString());
    }

    /**
     * 测试通过`BeanListHandler`来生成得到Bean List集合的实例.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToBeanList() {
        List<UserInfo> userInfos = Adept.quickStart()
                .query(USER_INFO_SQL, 5)
                .end(BeanListHandler.newInstance(UserInfo.class));
        Assert.assertNotNull(userInfos);
        Assert.assertTrue(userInfos.size() >= 3);
        log.info(userInfos.toString());
    }

    /**
     * 测试通过`ColumnsHandler`来生成得到List集合的实例.
     */
    @Test
    public void testToColumnList() {
        List<Object> nickNames = Adept.quickStart()
                .query("SELECT c_name, c_nickname FROM t_user")
                .end(ColumnsHandler.newInstance());
        Assert.assertNotNull(nickNames);
        log.info(nickNames.toString());
    }

    /**
     * 测试通过`SingleHandler`来生成得到单个对象的实例.
     */
    @Test
    public void testToSingle() {
        int count = (Integer) Adept.quickStart()
                .query("SELECT MAX(u.n_age) FROM t_user AS u")
                .end(SingleHandler.newInstance());
        log.info("最大年龄是:{}", count);
        Assert.assertTrue(count >= 27);
    }

    /**
     * 测试`end2Map`方法.
     */
    @Test
    public void testEnd2Map() {
        Map<String, Object> ageMap = Adept.quickStart()
                .query("SELECT MAX(u.n_age) AS maxAge, MIN(u.n_age) AS minAge FROM t_user AS u")
                .end2Map();
        Assert.assertNotNull(ageMap);
        log.info("end2Map方法结果:{}", ageMap);
    }

    /**
     * 测试`end2MapList`方法.
     */
    @Test
    public void testEnd2MapList() {
        List<Map<String, Object>> userMaps = Adept.quickStart()
                .query("SELECT u.c_name AS name, u.n_age AS age FROM t_user AS u").end2MapList();
        Assert.assertNotNull(userMaps);
    }

    /**
     * 测试`end2Bean`方法.
     */
    @Test
    public void testEnd2Bean() {
        Assert.assertNotNull(Adept.quickStart().query(USER_INFO_SQL, 5).end2Bean(UserInfo.class));
        Assert.assertNotNull(Adept.quickStart().query(USER_INFO_SQL, 5).end2Bean(new UserInfo()));
    }

    /**
     * 测试`end2BeanList`方法.
     */
    @Test
    public void testEnd2BeanList() {
        Assert.assertNotNull(Adept.quickStart().query(USER_INFO_SQL, 5).end2BeanList(UserInfo.class));
        Assert.assertNotNull(Adept.quickStart().query(USER_INFO_SQL, 5).end2BeanList(new UserInfo()));
    }

    /**
     * 清除配置信息.
     */
    @AfterClass
    public static void destroy() {
        AdeptConfigManager.getInstance().destroy();
    }

}