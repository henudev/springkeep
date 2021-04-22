package com.h3c.bigdata.zhgx.ws.auth;

import com.h3c.bigdata.gop.websocket.core.security.AuthenticationException;

public class InvalidAuthTypeException extends AuthenticationException {

    public InvalidAuthTypeException(String message) {
        super(message);
    }
}
