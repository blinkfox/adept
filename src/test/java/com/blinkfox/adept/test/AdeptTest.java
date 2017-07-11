package com.blinkfox.adept.test;

import com.blinkfox.adept.config.AdeptConfigManager;
import com.blinkfox.adept.core.Adept;
import com.blinkfox.adept.core.results.impl.BeanHandler;
import com.blinkfox.adept.core.results.impl.BeanListHandler;
import com.blinkfox.adept.core.results.impl.ColumnsHandler;
import com.blinkfox.adept.core.results.impl.MapHandler;
import com.blinkfox.adept.core.results.impl.MapListHandler;
import com.blinkfox.adept.core.results.impl.SingleHandler;
import com.blinkfox.adept.helpers.JdbcHelper;
import com.blinkfox.adept.helpers.UuidHelper;
import com.blinkfox.adept.test.bean.UserInfo;
import com.blinkfox.adept.test.core.results.ArrayHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adept测试类.
 * @author blinkfox on 2017/6/5.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdeptTest {

    private static final Logger log = LoggerFactory.getLogger(AdeptTest.class);

    private static final String ALL_USER_SQL = "SELECT * FROM t_user";

    private static final String ALL_USER_COUNT_SQL = "SELECT COUNT(*) AS user_count FROM t_user AS u";

    private static final String ALL_USER_NAME_SQL = "SELECT c_name, c_nickname FROM t_user";

    private static final String MAX_USER_AGE_SQL = "SELECT MAX(u.n_age) FROM t_user AS u";

    private static final String USER_INFO_SQL = "SELECT c_id AS id, c_name AS name, c_nickname AS nickName,"
            + " c_email AS email, n_sex AS sex, c_birthday AS birthday FROM t_user AS u limit 0, ?";

    private static final String USER_BY_AGE_SQL = "SELECT * FROM t_user AS u WHERE u.n_age > ?";

    private static final String INSERT_USER_SQL = "INSERT INTO t_user "
            + "(c_id, c_name, c_nickname, c_password, c_email, c_birthday, n_age, n_sex, n_status, c_remark) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * 初始化加载Adept配置.
     */
    @BeforeClass
    public static void init() {
        AdeptConfigManager.newInstance().initLoad(MyAdeptConfig.class);
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
     * 测试获取Connection实例.
     */
    @Test
    public void testGetConnection() {
        Connection conn = Adept.newInstance().getConnection().getConn();
        Assert.assertNotNull(conn);
        JdbcHelper.close(conn);
    }

    /**
     * 测试获取PreparedStatement实例.
     */
    @Test
    public void testGetPreparedStatement() {
        Adept adept = Adept.newInstance();
        PreparedStatement pstmt = adept.getConnection().getPreparedStatement(USER_BY_AGE_SQL, 26).getPstmt();
        Assert.assertNotNull(pstmt);
        adept.closeSource();
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
        Map<String, Object> map = Adept.quickStart().query(ALL_USER_COUNT_SQL).end(MapHandler.newInstance());
        // 获取用户总数的map，并断言数量
        Assert.assertNotNull(map);
        Assert.assertTrue((Integer) map.get("user_count") >= 3);
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
        UserInfo userInfo = Adept.quickStart().query(USER_INFO_SQL, 3)
                .end(BeanHandler.newInstance(UserInfo.class));
        Assert.assertNotNull(userInfo);
        log.info(userInfo.toString());
    }

    /**
     * 测试通过`BeanListHandler`来生成得到Bean List集合的实例.
     */
    @Test
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
        List<Object> nickNames = Adept.quickStart().query(ALL_USER_NAME_SQL).end(ColumnsHandler.newInstance());
        Assert.assertNotNull(nickNames);
        log.info(nickNames.toString());
    }

    /**
     * 测试通过`SingleHandler`来生成得到单个对象的实例.
     */
    @Test
    public void testToSingle() {
        int count = (Integer) Adept.quickStart().query(MAX_USER_AGE_SQL).end(SingleHandler.newInstance());
        log.info("最大年龄是:{}", count);
        Assert.assertTrue(count >= 27);
    }

    /**
     * 测试`end2Map`方法.
     */
    @Test
    public void testEnd2Map() {
        String sql = "SELECT MAX(u.n_age) AS maxAge, MIN(u.n_age) AS minAge FROM t_user AS u";
        Map<String, Object> ageMap = Adept.quickStart().query(sql).end2Map();
        Map<String, Object> ageMap2 = Adept.quickStart().queryForMap(sql);
        Assert.assertNotNull(ageMap);
        Assert.assertNotNull(ageMap2);
        log.info("end2Map方法结果:{}", ageMap);
    }

    /**
     * 测试`end2MapList`方法.
     */
    @Test
    public void testEnd2MapList() {
        String sql = "SELECT u.c_name AS name, u.n_age AS age FROM t_user AS u";
        List<Map<String, Object>> userMaps = Adept.quickStart().query(sql).end2MapList();
        List<Map<String, Object>> userMaps2 = Adept.quickStart().queryForMapList(sql);
        Assert.assertNotNull(userMaps);
        Assert.assertNotNull(userMaps2);
    }

    /**
     * 测试`end2Bean`方法.
     */
    @Test
    public void testEnd2Bean() {
        UserInfo userInfo = Adept.quickStart().query(USER_INFO_SQL, 5).end2Bean(UserInfo.class);
        Assert.assertNotNull(userInfo);
        UserInfo userInfo2 = Adept.quickStart().queryForBean(UserInfo.class, USER_INFO_SQL, 2);
        Assert.assertNotNull(userInfo2);
    }

    /**
     * 测试`end2BeanList`方法.
     */
    @Test
    public void testEnd2BeanList() {
        List<UserInfo> userInfos = Adept.quickStart().query(USER_INFO_SQL, 5).end2BeanList(UserInfo.class);
        Assert.assertNotNull(userInfos);
        List<UserInfo> userInfos3 = Adept.quickStart().queryForBeanList(UserInfo.class, USER_INFO_SQL, 5);
        Assert.assertNotNull(userInfos3);
    }

    /**
     * 测试`end2Columns`方法.
     */
    @Test
    public void testEnd2Columns() {
        List<Object> nickNames = Adept.quickStart().query(ALL_USER_NAME_SQL).end2Columns();
        List<Object> nickNames2 = Adept.quickStart().queryForColumns(ALL_USER_NAME_SQL);
        Assert.assertNotNull(nickNames);
        Assert.assertNotNull(nickNames2);
    }

    /**
     * 测试`end2Single`方法.
     */
    @Test
    public void testEnd2Single() {
        int count = (Integer) Adept.quickStart().query(MAX_USER_AGE_SQL).end2Single();
        int count2 = (Integer) Adept.quickStart().queryForSingle(MAX_USER_AGE_SQL);
        log.info("最大年龄是:{}", count);
        Assert.assertTrue(count >= 27);
        Assert.assertTrue(count2 >= 27);
    }

    /**
     * 测试`insert`方法.
     * 由于Sqlite busy问题,通过方法名称来保证'增删改'的一些方法先执行.
     */
    @Test
    public void testA01Insert() {
        // 插入的数据加上当前的时间戳.
        String now = String.valueOf(System.currentTimeMillis());
        Adept.quickStart().insert(INSERT_USER_SQL, UuidHelper.getUuid(), "testName" + now,
                "测试名称" + now, "123" + now, now + "test@gmail.com", "1995-05-19", 22, 1, 0, "测试备注信息" + now);
    }

    /**
     * 测试`update`方法.
     * 由于Sqlite busy问题,通过方法名称来保证'增删改'的一些方法先执行.
     */
    @Test
    public void testA02Update() {
        // 先更新数据
        String sql = "UPDATE t_user SET c_nickname = ?, c_email = ? WHERE n_status = ?";
        Adept.quickStart().update(sql, "李磊雷", "lileilei@163.com", 0);

        // 再查询数据，判断是否已经更新
        String sql2 = "SELECT c_id AS id, c_name AS name, c_nickname AS nickName,"
                + " c_email AS email, n_sex AS sex, c_birthday AS birthday FROM t_user AS u WHERE n_status = ?";
        UserInfo userInfo = Adept.quickStart().queryForBean(UserInfo.class, sql2, 0);
        Assert.assertNotNull(userInfo);
        Assert.assertEquals("李磊雷", userInfo.getNickName());
    }

    /**
     * 测试`delte`方法.
     * 由于Sqlite busy问题,通过方法名称来保证'增删改'的一些方法先执行.
     */
    @Test
    public void testB01Delte() {
        // 删掉刚插入的数据.
        Adept.quickStart().delete("DELETE FROM t_user WHERE n_status = ?", 0);

        // 批量删除
        List<Object[]> paramArrs = new ArrayList<Object[]>();
        paramArrs.add(new Object[]{0});
        Adept.quickStart().batchDelete("DELETE FROM t_user WHERE n_status = ?", paramArrs);

        // 判断删除后是否只剩5条数据了.
        Assert.assertEquals(5, Adept.quickStart().queryForSingle(ALL_USER_COUNT_SQL));
    }

    /**
     * 测试`batchInsert`方法.
     * 由于Sqlite busy问题,通过方法名称来保证'增删改'的一些方法先执行.
     */
    @Test
    public void testA04BatchInsert() {
        // 构造需要批量插入的数据，传入数组集合.
        List<Object[]> paramArrs = new ArrayList<Object[]>();
        for (int i = 1; i <= 9; i++) {
            paramArrs.add(new Object[]{UuidHelper.getUuid(), "batch_testName" + i, "batch_测试名称" + i,
                    "batch_123" + i, i + "batch_test@gmail.com", "1996-07-0" + i, 21, 0, 0, "batch_测试备注" + i});
        }
        Adept.quickStart().batchInsert(INSERT_USER_SQL, paramArrs);

        // 传入二维数组.
        paramArrs.clear();
        for (int i = 10; i <= 20; i++) {
            paramArrs.add(new Object[]{UuidHelper.getUuid(), "batch_testName" + i, "batch_测试名称" + i,
                    "batch_123" + i, i + "batch_test@gmail.com", "1994-07-" + i, 23, 0, 0, "batch_测试备注" + i});
        }
        Adept.quickStart().batchInsert(INSERT_USER_SQL, paramArrs.toArray(new Object[paramArrs.size()][]));
    }

    /**
     * 测试`batchUpdate`方法.
     * 由于Sqlite busy问题,通过方法名称来保证'增删改'的一些方法先执行.
     */
    @Test
    public void testA05Update() {
        // 构造需要更新的数据
        List<Object[]> paramArrs = new ArrayList<Object[]>();
        paramArrs.add(new Object[]{"批量修改者", "lileilei@163.com"});

        String sql = "UPDATE t_user SET c_nickname = ?, c_email = ? WHERE n_status = 0";
        Adept.quickStart().batchUpdate(sql, paramArrs);
        Adept.quickStart().batchUpdate(sql, paramArrs.toArray(new Object[paramArrs.size()][]));
    }

    /**
     * 测试获取mapList的实例.
     */
    @Test
    public void testToArray() {
        String sql = "SELECT c_name, c_nickname, c_email FROM t_user where n_age > ?";
        Object[] userArr = Adept.quickStart().query(sql, 25).end(new ArrayHandler());
        // 获取用户信息的对象数组
        Assert.assertNotNull(userArr);
        Assert.assertEquals("blinkfox", userArr[0]);
    }

    /**
     * 清除配置信息.
     */
    @AfterClass
    public static void destroy() {
        AdeptConfigManager.newInstance().destroy();
    }

}