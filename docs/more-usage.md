前面的快速示例中展示了使用`Adept`的一些的简单基础用法，`Adept`支持扩展自定义的查询结果类型，支持批量操作，支持更灵活的获取使用原生`JDBC`对象等。

## 自定义结果类型

Adept默认支持`6`种结果类型，分别如下：

- `Map`，将查询结果集的`第一行`数据转换成`有序Map`
- `MapList`，将查询结果集`所有行`的数据转换成`Map集合`
- `Bean`，将查询结果集的`第一行`数据转换成`JavaBean对象`
- `MapList`，将查询结果集的`所有行`的数据转换成`JavaBean对象集合`
- `Columns`，将查询结果集的`第一列`的所有数据转换成`对象集合`
- `Single`，将查询结果集`第一行第一列`的数据转换成`单个对象`

以上`6`种原生支持的结果类型基本上适用于大多数使用场景，查询时调用这`6`种对应的`Handler`即可。假设我们想将查询的结果转换为`Map`，则调用`MapHandler`的主要调用方式有如下几种：

- `Adept.quickStart().queryForMap(sql, params);`
- `Adept.quickStart().query(sql, params).end2Map();`
- `Adept.quickStart().query(new MapHandler(), sql, params);`
- `Adept.quickStart().query(MapHandler.class, sql, params);`
- `Adept.quickStart().query(sql, params).end(new MapHandler());`
- `Adept.quickStart().query(sql, params).end(MapHandler.class);`

以上`6`种调用方式，可根据自己的喜好选择，效果都一样。`Adept`同时也支持扩展自定义的结果类型，见以下的三种方式。

### 定义结果处理器的方式

第一种**有利于复用和职责分离**的扩展方式就是通过自定义`结果处理器`，该处理器须要实现`ResultHandler<T>`接口，并实现其中的`transform(ResultSet rs)`方法即可。

假设，我们需要的结果是将查询结果的第一行数据转换成对象数组的结果类型。那么扩展使用示例如下：

```java
import com.blinkfox.adept.core.results.ResultHandler;
import com.blinkfox.adept.exception.ResultsTransformException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 将'ResultSet'结果集的第一行数据转换为'对象数组'的处理器.
 * Created by blinkfox on 2017/7/11.
 */
public class ArrayHandler implements ResultHandler<Object[]> {

    /**
     * 将'ResultSet'结果集的第一行数据转换为'对象数组'.
     * @param rs ResultSet实例
     * @return 对象数组
     */
    @Override
    public Object[] transform(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        try {
            // 获取Resultset元数据和查询的列数.
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            // 初始化列数长度的数组,将第一行各列的数据存到'对象数组'中.
            if (rs.next()) {
                Object[] objArr = new Object[cols];
                for (int i = 0; i < cols; i++)  {
                    objArr[i] = rs.getObject(i + 1);
                }
                return objArr;
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'对象数组'出错!", e);
        }

        return null;
    }

}
```

?> **代码解释**：上面的代码，新增了一个`ArrayHandler`类，实现了`ResultHandler<T>`接口，其中泛型`T`是需要转换成的结果类型，这里即为对象数组`Object[]`。然后`ArrayHandler`中实现了`transform(ResultSet rs)`方法。该方法中是通过`ResultSet`结果集参数，得到第一行数据，并遍历各列的数据，将数据结果存到数组中返回。当然，如果`ResultSet`为null或空数据的话，直接返回null了。

接下来，是通过`ArrayHandler`来把查询结果转换成数组的示例代码：

```java
String sql = "SELECT c_name, c_nickname, c_email FROM t_user where n_age > ?";
Object[] userArr = Adept.quickStart().query(sql, 25).end(new ArrayHandler());
log.info(Arrays.toString(userArr));
```

查询输出的结果为：

?> [blinkfox, 闪烁之狐, blinkfoxcom@gmail.com]

### 使用匿名内部类的方式

第一种方式的好处是便于处理器的复用和职责分离，是推荐的方式，但是如果你只是想在某个地方使用一次，不想增加新的类的话，就使用这第二种匿名内部类的方式，方式和第一种相似，只不过不需要额外定义新的类而已，代码如下：

```java
String sql = "SELECT c_name, c_nickname, c_email FROM t_user where n_age > ?";
Object[] userArr = Adept.quickStart().query(sql, 25).end(new ResultHandler<Object[]>() {
    @Override
    public Object[] transform(ResultSet rs) {
        if (rs == null) {
            return null;
        }

        try {
            // 获取Resultset元数据和查询的列数.
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            // 初始化列数长度的数组,将第一行各列的数据存到'对象数组'中.
            if (rs.next()) {
                Object[] objArr = new Object[cols];
                for (int i = 0; i < cols; i++)  {
                    objArr[i] = rs.getObject(i + 1);
                }
                return objArr;
            }
        } catch (Exception e) {
            throw new ResultsTransformException("将'ResultSet'结果集转换为'对象数组'出错!", e);
        }

        return null;
    }
});
```

### Java8中的Lambda方式

如果你项目的的Jdk版本是Jdk8及以上的话，那么对于第二种方式就可以通过`Lambda`表达式来简化了，代码如下：

```java
String sql = "SELECT c_name, c_nickname, c_email FROM t_user where n_age > ?";
Object[] userArr = Adept.quickStart().query(sql, 25).end(rs -> {
    if (rs == null) {
        return null;
    }

    try {
        // 获取Resultset元数据和查询的列数.
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        // 初始化列数长度的数组,将第一行各列的数据存到'对象数组'中.
        if (rs.next()) {
            Object[] objArr = new Object[cols];
            for (int i = 0; i < cols; i++)  {
                objArr[i] = rs.getObject(i + 1);
            }
            return objArr;
        }
    } catch (Exception e) {
        throw new ResultsTransformException("将'ResultSet'结果集转换为'对象数组'出错!", e);
    }

    return null;
});
```

## 批量操作

`Adept`封装了`JDBC`的批量操作，主要是批量操作和批量更新，调用的方法分别是`batchInsert`和`batchUpdate`方法。详细的方法如下：

- `batchInsert(String sql, Object[][] params)`，绑定变量参数为二维对象数组参数
- `batchInsert(String sql, List<Object[]> paramArrs)`，绑定变量参数为对象数组的`List`集合参数
- `batchUpdate(String sql, Object[][] params)`，绑定变量参数为二维对象数组参数
- `batchUpdate(String sql, List<Object[]> paramArrs)`，绑定变量参数为对象数组的`List`集合参数

### 批量插入示例

```java
// 批量插入的SQL语句.
String sql = "INSERT INTO t_user "
            + "(c_id, c_name, c_nickname, c_password, c_email, c_birthday, n_age, n_sex, n_status, c_remark) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

// 构造需要批量插入的数据，传入数组集合.
List<Object[]> paramArrs = new ArrayList<Object[]>();
for (int i = 1; i <= 9; i++) {
    paramArrs.add(new Object[]{UuidHelper.getUuid(), "batch_testName" + i, "batch_测试名称" + i,
            "batch_123" + i, i + "batch_test@gmail.com", "1996-07-0" + i, 21, 0, 0, "batch_测试备注" + i});
}

// 调用Adept批量插入语句.
Adept.quickStart().batchInsert(INSERT_USER_SQL, paramArrs);
```

### 批量更新示例

```java
// 批量更新的SQL语句.
String sql = "UPDATE t_user SET c_nickname = ?, c_email = ? WHERE n_status = 0";

// 构造需要更新的数据
List<Object[]> paramArrs = new ArrayList<Object[]>();
paramArrs.add(new Object[]{"批量修改者", "lileilei@163.com"});

// 调用Adept批量更新语句.
Adept.quickStart().batchUpdate(sql, paramArrs);
```

## 得到原生JDBC对象

`Adept`只是对`JDBC`做了`薄封装`，为了赋予`Adept`更大程度上的灵活性。Adept仍然提供给了开发者灵活使用原生`JDBC`的各种对象来做数据库的相关操作。最核心的操作类`Adept`中主要提供了以下几个`JDBC`对象或者方法来供开发人员自由、灵活的操作数据。

### 得到Adept实例

```java
// 获取Adept实例.
Adept adept = Adept.newInstance();
```

### 得到数据源DataSource

```java
// Adept的静态方法，得到数据源`DataSource`实例.
DataSource ds = Adept.getDataSource();
```

`Adept`提供了快速获取数据源的静态方法，得到了数据源，就可以从数据库连接池中获取到`Connection`实例，供开发人员自由的做数据库相关操作。

### 得到连接Connection

```java
// 通过 newInstance().getConnection() 得到数据库连接.
Connection conn = Adept.newInstance().getConnection().getConn();

// 通过quickStart()得到数据库连接.
Adept.quickStart().getConn();
```

!> `getConnection()`会从数据源中获取`Connection`。为了后续的链式操作，返回了`Adept`的当前实例，并为直接返回`Connection`实例，要获取`Connection`实例，可以通过`getConn()`方法得到。

### 得到预编译语句PreparedStatement

```java
Connection conn = Adept.newInstance().getConnection().getPreparedStatement(sql, params).getPstmt();

Adept.quickStart().getPreparedStatement(sql, params).getPstmt();
```

!> `getPreparedStatement(sql, params)`会从`Connection`中获取`PreparedStatement`并注入对应的SQL语句和绑定参数。为了后续的链式操作，返回了`Adept`的当前实例，并未直接返回`PreparedStatement`实例，要获取`PreparedStatement`实例，可以通过`getPstmt()`方法得到。

### 得到结果集ResultSet

```java
// 获取ResultSet结果集实例
Adept.quickStart().query(sql, params).getRs();
```

!> `query(sql, params)`会根据`PreparedStatement`执行查询的SQL语句并得到查询的ResultSet结果集。为了后续的链式操作，返回了`Adept`的当前实例，并未直接返回`ResultSet`实例，要获取`ResultSet`实例，可以通过`getRs()`方法得到。


