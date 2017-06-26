package com.blinkfox.adept.exception;

/**
 * ResultSet结果集转换异常.
 * @author blinkfox on 2017/6/11.
 */
public class ResultsTransformException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     * @param t Throwable实例.
     */
    public ResultsTransformException(String msg, Throwable t) {
        super(msg, t);
    }

}