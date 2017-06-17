package com.blinkfox.adept.exception;

/**
 * 未找到数据源DataSource异常.
 * @author blinkfox on 2017/6/5.
 */
public class NoDataSourceException extends RuntimeException {

    /**
     * 附带msg信息的构造方法.
     * @param msg 消息
     */
    public NoDataSourceException(String msg) {
        super(msg);
    }

}