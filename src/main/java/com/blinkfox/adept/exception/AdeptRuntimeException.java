package com.blinkfox.adept.exception;

/**
 * Adept的自定义运行时异常.
 * @author blinkfox on 2017/6/12.
 */
public class AdeptRuntimeException extends RuntimeException {

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     */
    public AdeptRuntimeException(String msg) {
        super(msg);
    }

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     * @param t Throwable实例.
     */
    public AdeptRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }

}