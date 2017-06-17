package com.blinkfox.adept.exception;

/**
 * 执行SQL语句异常.
 * @author blinkfox on 2017/6/11.
 */
public class ExecuteSqlException extends RuntimeException {

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     * @param t Throwable实例.
     */
    public ExecuteSqlException(String msg, Throwable t) {
        super(msg, t);
    }

}