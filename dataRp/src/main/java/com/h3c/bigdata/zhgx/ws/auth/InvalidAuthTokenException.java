package com.h3c.bigdata.zhgx.ws.auth;

import com.h3c.bigdata.gop.websocket.core.security.AuthenticationException;

/**
 * {@code InvalidAuthTokenException}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvalidAuthTokenException extends AuthenticationException {

    public InvalidAuthTokenException(String token) {
        super(token);
    }

}
