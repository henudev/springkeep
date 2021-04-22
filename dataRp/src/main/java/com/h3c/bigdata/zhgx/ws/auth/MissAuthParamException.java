package com.h3c.bigdata.zhgx.ws.auth;

import com.h3c.bigdata.gop.websocket.core.security.AuthenticationException;

/**
 * {@code MissAuthParamException}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
public class MissAuthParamException extends AuthenticationException {

    public MissAuthParamException(String username, String token) {
        super("username: " + username + ", token: " + token);
    }

}
