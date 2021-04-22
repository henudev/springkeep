package com.h3c.bigdata.zhgx.common.utils;

/**
 * @author fangzhiheng - f18467
 * @date 2018/9/14 10:12
 */
public class FileNotSupportException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public FileNotSupportException(final String message) {
        super(message);
    }

}
