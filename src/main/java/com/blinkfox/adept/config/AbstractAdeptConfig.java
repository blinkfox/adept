package com.blinkfox.adept.config;

/**
 * Adept的抽象配置类.
 * @author blinkfox on 2017/5/30.
 */
public abstract class AbstractAdeptConfig {

    /**
     * 配置数据库连接池.
     */
    protected abstract void configDataSource(ConfigInfo info);

}