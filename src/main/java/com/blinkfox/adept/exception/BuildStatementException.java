package com.blinkfox.adept.exception;

/**
 * 构建SQL中的Statement实例及参数异常.
 * @author blinkfox on 2017/6/11.
 */
public class BuildStatementException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     * @param t Throwable实例.
     */
    public BuildStatementException(String msg, Throwable t) {
        super(msg, t);
    }

}