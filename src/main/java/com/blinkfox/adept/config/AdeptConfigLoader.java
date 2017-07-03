package com.blinkfox.adept.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Adept配置的servlet监听器，Java web项目中Adept配置信息的初始化加载类.
 * Created by blinkfox on 2017/7/3.
 */
public class AdeptConfigLoader implements ServletContextListener {

    /** Adept配置类对应的类全路径常量字符串. */
    private static final String CONFIG_CLASS = "adeptConfigClass";

    /**
     * 应用服务器启动时执行.
     * @param event 上下文事件对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String configClass = event.getServletContext().getInitParameter(CONFIG_CLASS);
        AdeptConfigManager.newInstance().initLoad(configClass);
    }

    /**
     * 应用服务器销毁时执行的方法.
     * @param event 上下文事件对象
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ConfigInfo.getInstance().clear();
    }

}