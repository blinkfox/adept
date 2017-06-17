package com.blinkfox.adept.exception;

/**
 * 未找到AdeptConfig配置信息的异常.
 * @author blinkfox on 2017/5/31.
 */
public class LoadAdeptConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     */
    public LoadAdeptConfigException(String msg) {
        super(msg);
    }

}