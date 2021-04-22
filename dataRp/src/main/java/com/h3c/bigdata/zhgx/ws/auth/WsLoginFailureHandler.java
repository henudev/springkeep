package com.h3c.bigdata.zhgx.ws.auth;

import com.h3c.bigdata.gop.websocket.core.security.AuthenticateFailureHandler;
import com.h3c.bigdata.gop.websocket.core.security.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code WsLoginFailureHandler}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
public class WsLoginFailureHandler implements AuthenticateFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public String handle(AuthenticationException ex) {
        if (logger.isDebugEnabled()) {
            logger.debug("-------- 校验失败, {}", ex.getMessage());
        }
        return ex.getMessage();
    }

}
