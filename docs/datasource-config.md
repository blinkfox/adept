前面安装配置的环节已经简单的展示了如何配置和使用数据源，但实际的项目环境中存在已有的其它数据库连接池，甚至更详细的数据库连接池的属性配置。Adept支持各种常见的数据库连接池，同时也支持扩展自定义的数据库连接池的数据源配置。

## 配置支持的数据源

`HikariCP`是默认使用的数据库连接池，通过`Maven`的方式会自动引入了`HikariCP`的`jar`包，而以下其他几种数据库连接池也是支持的，只不过需要自己额外引入对应数据库连接池的`jar`包。配置数据源的方式主要分为了两种，一种是通过数据库的基础连接信息来配置，另一种是通过已有数据源的方式来配置，配置的位置依然是在自己的Java配置类中，也就是前面的`MyAdeptConfig.java`文件中，示例如：

```java
/**
 * 我的Adept配置类.
 * @author blinkfox on 2017/6/30.
 */
public class MyAdeptConfig extends AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     * @param builder 数据源构建对象
     */
    @Override
    public void configDataSource(DataSourceConfigBuilder builder) {
        // dataSource对象可创建出来或者如果系统中使用Spring管理的话，可从Spring中获取.
        builder.buildHikariDataSource(dataSource);
    }

}
```

### HikariCP

`HikariCP`的调用配置示例如下：

```java
// 通过数据库基础连接信息进行配置，返回 HikariDataSource 对象
builder.buildHikariDataSource(driver, url, user, password);

// 或者使用Default方式
builder.buildDefaultDataSource(driver, url, user, password);
```

```java
// 通过已有数据源方式配置，返回 HikariDataSource 对象
builder.buildHikariDataSource(dataSource);
```

### Druid

`Druid`的调用配置示例如下：

```java
// 通过数据库基础连接信息进行配置，返回 DruidDataSource 对象
builder.buildDruidDataSource(driver, url, user, password);
```

```java
// 通过已有数据源方式配置，返回 DruidDataSource 对象
builder.buildDruidDataSource(dataSource);
```

### DBCP

`DBCP`的调用配置示例如下：

```java
// 通过数据库基础连接信息进行配置，返回 BasicDataSource 对象
builder.buildDbcpDataSource(driver, url, user, password);
```

```java
// 通过已有数据源方式配置，返回 BasicDataSource 对象
builder.buildDbcpDataSource(dataSource);
```

### C3P0

`C3P0`的调用配置示例如下：

```java
// 通过数据库基础连接信息进行配置，返回 ComboPooledDataSource 对象
builder.buildC3p0DataSource(driver, url, user, password);
```

```java
// 通过已有数据源方式配置，返回 ComboPooledDataSource 对象
builder.buildC3p0DataSource(dataSource);
```

## 配置其它的数据源

Java中的各种数据库连接池类都实现了`DataSource`接口，即使非以上四种数据库连接池的连接池也需要实现`DataSource`接口，这也是Adept中配置数据源的基础。那么如何建立其它数据源的配置类呢？

假设我们使用的数据库连接池为`BoneCP`，首先我们需要引入`BoneCP`的`jar`包，然后，继承`DataSourceConfig`的抽象类，实现其中的`close`方法，在Adept的配置类中调用`DataSourceConfigBuilder`类中的`saveDataSource`方法即可，具体步骤如下：

### 引入连接池jar包

```xml
<dependency>
	<groupId>com.jolbox</groupId>
	<artifactId>bonecp</artifactId>
	<version>0.8.0.RELEASE</version>
</dependency>
```

### 继承DataSourceConfig

新建`BonecpDataSourceConfig`类，继承自`DataSourceConfig`类，实现数据源的close方法。由于Java的`DataSource`接口中并未定义`close`方法，为了确保关闭应用程序时释放掉连接池所占用的内存，Adept中需要各个数据库连接池调用其中的`close`方法。

```java
import com.blinkfox.adept.datasource.DataSourceConfig;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * BoneCP 的数据源配置类.
 * @author blinkfox on 2017-07-10.
 */
public class BonecpDataSourceConfig extends DataSourceConfig<BoneCPDataSource> {
    
    /**
     * 关闭BoneCP数据源，目的是关闭应用程序时也能确保释放掉数据源所占用的内存.
     */
    @Override
    public void close() {
        if (super.dataSource != null) {
            super.dataSource.close();
            super.dataSource = null;
        }
    }

}
```

### 配置数据源

在`MyAdeptConfig`类中，创建并保存数据源，示例代码如下：

```java
public class MyAdeptConfig extends AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     */
    @Override
    public void configDataSource(DataSourceConfigBuilder builder) {
        Properties props = PropHelper.newInstance().loadPropFile("config.properties");
        
        // 创建数据源，或者将该创建代码提取到`BonecpDataSourceConfig`类中
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass(props.getProperty("driver"));
        ds.setJdbcUrl(props.getProperty("url"));
        ds.setUsername(props.getProperty("username"));
        ds.setPassword(props.getProperty("password"));
        // 其它的若干数据库连接池配置项
        
        // 保存 BoneCPDataSource 数据源
        builder.saveDataSource(BonecpDataSourceConfig.class, ds);
    }

}
```

通过以上几个简单的步骤即可配置好`Adept`的数据源。