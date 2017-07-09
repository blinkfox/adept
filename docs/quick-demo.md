## 表结构准备

我这里采用sqlite数据库，新建一个`t_user`的数据库表，包含了若干字段，建表语句如下：

```sql
CREATE TABLE t_user (
    c_id text(32,0) NOT NULL,
    c_name text(100,0),
    c_nickname text(100,0),
    c_password text(100,0),
    c_email text(100,0),
    c_birthday text,
    n_age integer(5,0),
    n_sex integer(5,0),
    n_status integer,
    c_remark text(300,0),
    PRIMARY KEY(c_id)
);
```

初始化插入几条基础数据，如下：

```sql
INSERT INTO t_user VALUES ('82f2d116540b11e7b114b2f933d5fe66', 'blinkfox', '闪烁之狐', 123456, 'blinkfoxcom@gmail.com', '1990-03-18', 27, 1, 1, '这就是我');
INSERT INTO t_user VALUES ('e482e0c4540b11e7b114b2f933d5fe66', 'Hanmeimei', '韩梅梅', 123456789, 'hanmeimei@163.com', '1992-07-15', 25, 0, 1, '梅梅');
INSERT INTO t_user VALUES ('28226714540c11e7b114b2f933d5fe66', 'lilei', '李雷', 233232, 'lilei@gmail.com', '1991-11-28', 26, 1, 1, null);
```

## 插入示例

使用`Adept.quickStart()`会初始化一个Adept实例，并从前面配置的数据源中初始化一个数据库连接`Connection`实例，方便做数据库的相关操作。以下是插入的示例：

```java
String sql = "INSERT INTO t_user (c_id, c_name, c_nickname, c_password, c_email, c_birthday, n_age, n_sex, n_status, c_remark) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
Adept.quickStart().insert(sql, UuidHelper.getUuid(), "testName", "测试名称", "123", "test@gmail.com", "1995-05-19", 22, 1, 0, "测试备注信息");
```

> **注**：书写SQL推荐使用绑定参数的方式，`insert(String sql , Object... params)`方法中除第一个sql字符串外，其他均为不定参数，即参数可以是单个灵活的参数，也可以是一个数组，当然如果是List集合，也可调用`list.toArray()`方法转换为数组之后作为参数来传递。

## 更新示例

```java
String sql = "UPDATE t_user SET c_nickname = ?, c_email = ? WHERE c_id = ?";
Adept.quickStart().update(sql, "李磊雷", "lileilei@163.com", "28226714540c11e7b114b2f933d5fe66");
```

> **注**：查看数据库可知，第三条数据的昵称和邮箱信息已被修改。

## 删除示例

```java
Adept.quickStart().delete("DELETE FROM t_user WHERE n_status = ?", 0);
```

> **注**：查看数据库可知，前面插入n_status为0的数据已被删除。

## 查询示例

### 返回MapList

`queryForMapList(String sql, Object... params)`方法会把查询结果转换为`List<Map<String, Object>>`类型，其中每一行的结果为集合中的一个Map型数据，Map中的键为SQL中的`AS`的别名，如果没有别名则为数据库字段名。值即为数据库的数据。

```java
String sql = "SELECT u.c_name AS name, u.n_age AS age FROM t_user AS u WHERE u.n_status = ?";
List<Map<String, Object>> userMaps = Adept.quickStart().queryForMapList(sql, 1);
```

查询输出的结果，使用JSON的形式展示如下：

```json
{
    {
        "name": "blinkfox",
        "age": "27",
        "c_email": "blinkfoxcom@gmail.com"
    },
    {
        "name": "Hanmeimei",
        "age": "25",
        "c_email": "hanmeimei@163.com"
    },
    {
        "name": "lilei",
        "age": "26",
        "c_email": "lilei@gmail.com"
    }
}
```

### 返回Map

`queryForMap(String sql, Object... params)`方法会把查询结果的第一行数据转换为`Map<String, Object>`类型，Map中的键为SQL中的`AS`的别名，如果没有别名则为数据库字段名。值即为数据库的数据。

```java
String sql = "SELECT MAX(u.n_age) AS maxAge, MIN(u.n_age) AS minAge FROM t_user AS u WHERE u.n_status = ?";
Map<String, Object> ageMap2 = Adept.quickStart().queryForMap(sql, 1);
```

查询输出的结果，使用JSON的形式展示如下：

```json
{
    "maxAge": "27",
    "minAge": "25"
}
```

> **注**：`queryForMap(String sql, Object... params)`方法只转换第一行的数据为Map，如果查询的结果集有多行（条）数据，其余行（条）的数据会被忽略。

### 返回Bean

`queryForBean(Class<T> beanClass, String sql, Object... params)`方法会把查询结果的第一行数据转换为JavaBean的类型，Bean中的属性为SQL中的`AS`的别名，如果没有别名则为数据库字段名，值即为数据库的数据，以下为预先定义的JavaBean：

```java
/**
 * 用户信息Bean.
 * @author blinkfox on 2017/6/13.
 */
public class UserInfo {

    /* 唯一标识 */
    private String id;

    /* 姓名name */
    private String name;

    /* 昵称 */
    private String nickName;

    /* 邮箱 */
    private String email;

    /* 性别 */
    private int sex;

    /* 生日 */
    private String birthday;

    /**
     * 空构造方法.
     */
    public UserInfo() {
        super();
    }

    /* getter和setter方法. */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}
```

查询的SQL为：

```java
String sql = "SELECT c_id AS id, c_name AS name, c_nickname AS nickName, c_email AS email, n_sex AS sex, c_birthday AS birthday FROM t_user AS u limit 0, ?";
UserInfo userInfo = Adept.quickStart().queryForBean(UserInfo.class, sql, 3);
```

查询输出的结果，使用JSON的形式展示如下：

```json
{
    "id": "82f2d116540b11e7b114b2f933d5fe66",
    "name": "blinkfox",
    "nickName": "闪烁之狐",
    "email": "blinkfoxcom@gmail.com",
    "sex": "1",
    "birthday": "1990-03-18"
}
```

> **注**：`queryForBean(Class<T> beanClass, String sql, Object... params)`方法只转换第一行的数据为JavaBean，如果查询的结果集有多行（条）数据，其余行（条）的数据会被忽略。

### 返回BeanList

`queryForBeanList(Class<T> beanClass, String sql, Object... params)`方法会把所有查询结果数据转换为JavaBean的集合类型，Bean中的属性为SQL中的`AS`的别名，如果没有别名则为数据库字段名，值即为数据库的数据，查询的SQL示例为：

```java
String sql = "SELECT c_id AS id, c_name AS name, c_nickname AS nickName, c_email AS email, n_sex AS sex, c_birthday AS birthday FROM t_user AS u limit 0, ?";
List<UserInfo> userInfos = Adept.quickStart().queryForBeanList(UserInfo.class, sql, 3);
```

查询输出的结果，使用JSON的形式展示如下：

```json
{
    {
        "id": "82f2d116540b11e7b114b2f933d5fe66",
        "name": "blinkfox",
        "nickName": "闪烁之狐",
        "email": "blinkfoxcom@gmail.com",
        "sex": "1",
        "birthday": "1990-03-18"
    },
    {
        "id": "e482e0c4540b11e7b114b2f933d5fe66",
        "name": "Hanmeimei",
        "nickName": "韩梅梅",
        "email": "hanmeimei@163.com",
        "sex": "0",
        "birthday": "1992-07-15"
    },
    {
        "id": "28226714540c11e7b114b2f933d5fe66",
        "name": "lilei",
        "nickName": "李雷",
        "email": "lilei@gmail.com",
        "sex": "1",
        "birthday": "1991-11-28"
    }
}
```

### 返回Columns

`queryForColumns(Class<T> beanClass, String sql, Object... params)`方法会把所有查询结果数据的第一列数据转换为对象集合类型，查询的SQL示例为：

```java
String sql = "SELECT c_name, c_nickname FROM t_user";
List<Object> names = Adept.quickStart().queryForColumns(sql);
```

查询输出的结果，使用JSON的形式展示如下：

```json
{
    {
        "c_name": "blinkfox"
    },
    {
        "c_name": "Hanmeimei"
    },
    {
        "c_name": "lilei"
    }
}
```

### 返回Single

`queryForSingle(String sql, Object... params)`方法会把所有查询结果数据的第一行、第一列数据转换为Java对象，查询的SQL示例为：

```java
String sql = "SELECT MAX(u.n_age) FROM t_user AS u";
int count = (Integer) Adept.quickStart().queryForSingle(sql);
```

查询输出的结果为27。

> **注**：`queryForSingle(String sql, Object... params)`方法只转换第一行、第一列的数据为单个Java对象，如果查询的结果集有多行列数据，其余行列的数据会被忽略。