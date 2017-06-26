package com.blinkfox.adept.exception;

/**
 * Null Connection异常.
 * @author blinkfox on 2017/6/7.
 */
public class NullConnectionException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     */
    public NullConnectionException(String msg) {
        super(msg);
    }

}