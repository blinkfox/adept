## 支持场景

适用于Java (web)项目，`JDK1.6`及以上。

## 快速集成

你可以直接将Adept的jar包加入到你的classpath中。当然，更推荐使用`Maven`或`Gradle`之类的主流Java项目管理工具来集成。

### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>adept</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```
compile 'com.blinkfox:adept:1.0.0'
```

## 配置数据源

数据库连接是一种关键的、有限的、昂贵的资源，即使在简单的项目，也依然推荐使用数据库连接池来完成JDBC的数据库操作。作为使用示例，我这里使用sqlite数据库来作掩饰。

### 创建配置类

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

### 初始化加载配置

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