# Adept

[![Build Status](https://secure.travis-ci.org/blinkfox/adept.svg)](https://travis-ci.org/blinkfox/adept) [![codecov](https://codecov.io/gh/blinkfox/adept/branch/master/graph/badge.svg)](https://codecov.io/gh/blinkfox/adept)

## 简单介绍

Adept是一个用于简化JDBC操作的轻量级DAO工具库。如果你不想在例的项目中使用庞大的Hibernate等ORM工具，只是想更简单、灵活而又方便的使用原生的JDBC来操作数据库，那么Adept也许是一个不错的选择。

相对于JDBC原生繁琐的API，Adept大大简化了增删改查(CRUD)的常用操作。为了更大程度的增强数据库连接和数据库操作的性能，默认内置了Java界最快的数据库连接池[HikariCP][1]，当然你也可以根据项目的实际情况，引入和切换为其他数据库连接池，如：[Druid][2]、[Commons Pool][3]、[C3P0][4]等。

## 功能特性

- 轻量级，jar包仅仅39k大小，简单集成和使用
- API使用简单、灵活，支持链式调用
- 支持多种查询结果类型，支持映射为JavaBean
- 可自定义扩展其他结果类型和类型复用
- 支持快速的批量操作

## 安装配置

### 支持场景

适用于Java (web)项目，`JDK1.6`及以上。

### 快速集成

你可以直接将Adept的jar包加入到你的classpath中。当然，更推荐使用`Maven`或`Gradle`之类的主流Java项目管理工具来集成。

#### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>adept</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

```
compile 'com.blinkfox:adept:1.0.0'
```

### 配置数据源

数据库连接是一种关键的、有限的、昂贵的资源，即使在简单的项目，也依然推荐使用数据库连接池来完成JDBC的数据库操作。作为使用示例，我这里使用sqlite数据库来作掩饰。

#### 创建配置类

在你的Java(web)项目中，创建一个继承自`AbstractAdeptConfig`的Adept配置类，如以下示例：

```java
/**
 * 我的Adept配置类.
 * @author blinkfox on 2017/5/30.
 */
public class MyAdeptConfig extends AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     */
    @Override
    public void configDataSource(DataSourceConfigBuilder builder) {
        HikariDataSource hds = builder.buildDefaultDataSource("org.sqlite.JDBC",
                "jdbc:sqlite:src/test/resources/db/adept.sqlite", "", "");
        // HikariCP有默认的参数配置，下面是更多自定义的参数配置.
        hds.setMaximumPoolSize(20);
    }

}
```

以上是sqlite数据源的配置示例，实际推荐将数据库相关的配置信息写到`properties`或`yml`文件中，在配置类中通过加载`roperties`文件等来读取配置信息，从而将数据库配置信息做到灵活可配置。加载属性文件的代码如下：

```java
Properties props = PropHelper.newInstance().loadPropFile("config.properties");
HikariDataSource hds = builder.buildDefaultDataSource(props.getProperty("driver"), props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
```

#### 初始化加载配置

如果你是Java web项目，可通过在`web.xml`中通过配置`listener`类完成数据源的初始化加载，示例代码如下：

```xml
<!-- Adept的相关配置配置 -->
<context-param>
   <!-- paramName必须为`adeptConfigClass`名称，param-value对应刚创建的Java配置的类路径 -->
   <param-name>adeptConfigClass</param-name>
   <param-value>com.blinkfox.adept.test.MyAdeptConfig</param-value>
</context-param>
<!-- listener-class必须配置，JavaEE容器启动时才会执行 -->
<listener>
   <listener-class>com.blinkfox.adept.config.AdeptConfigLoader</listener-class>
</listener>
```

> **注**：如果你不是Java web项目，或者你想通过Java代码来初始化加载Adept的配置信息，或者在单元测试中，这样调用即可完成数据源等配置信息的初始化加载：

```java
AdeptConfigManager.newInstance().initLoad(MyAdeptConfig.class);
```

## 快速示例

### 表结构准备

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

### 插入示例

使用`Adept.quickStart()`会初始化一个Adept实例，并从前面配置的数据源中初始化一个数据库连接`Connection`实例，方便做数据库的相关操作。以下是插入的示例：

```java
String sql = "INSERT INTO t_user (c_id, c_name, c_nickname, c_password, c_email, c_birthday, n_age, n_sex, n_status, c_remark) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
Adept.quickStart().insert(sql, UuidHelper.getUuid(), "testName", "测试名称", "123", "test@gmail.com", "1995-05-19", 22, 1, 0, "测试备注信息");
```

> **注**：书写SQL推荐使用绑定参数的方式，`insert(String sql , Object... params)`方法中除第一个sql字符串外，其他均为不定参数，即参数可以是单个灵活的参数，也可以是一个数组，当然如果是List集合，也可调用`list.toArray()`方法转换为数组之后作为参数来传递。

### 更新示例

```java
String sql = "UPDATE t_user SET c_nickname = ?, c_email = ? WHERE c_id = ?";
Adept.quickStart().update(sql, "李磊雷", "lileilei@163.com", "28226714540c11e7b114b2f933d5fe66");
```

> **注**：查看数据库可知，第三条数据的昵称和邮箱信息已被修改。

### 删除示例

```java
Adept.quickStart().delete("DELETE FROM t_user WHERE n_status = ?", 0);
```

> **注**：查看数据库可知，前面插入n_status为0的数据已被删除。

### 查询示例

#### 返回MapList

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

#### 返回Map

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

#### 返回Bean

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

#### 返回BeanList

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

#### 返回Columns

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

#### 返回Single

`queryForSingle(String sql, Object... params)`方法会把所有查询结果数据的第一行、第一列数据转换为Java对象，查询的SQL示例为：

```java
String sql = "SELECT MAX(u.n_age) FROM t_user AS u";
int count = (Integer) Adept.quickStart().queryForSingle(sql);
```

查询输出的结果为27。

> **注**：`queryForSingle(String sql, Object... params)`方法只转换第一行、第一列的数据为单个Java对象，如果查询的结果集有多行列数据，其余行列的数据会被忽略。

## 其他

### 许可证

本Adept类库使用[Apache License 2.0][5]许可证

### 版本更新记录

- v1.0.0(2017-07-03)
  - 基础功能完成


  [1]: http://brettwooldridge.github.io/HikariCP
  [2]: https://github.com/alibaba/druid
  [3]: http://commons.apache.org/proper/commons-pool/index.html
  [4]: http://www.mchange.com/projects/c3p0/
  [5]: http://www.apache.org/licenses/LICENSE-2.0