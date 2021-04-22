package com.h3c.bigdata.zhgx.ws;


import com.h3c.bigdata.gop.websocket.annotation.ConcreteContainer;
import com.h3c.bigdata.gop.websocket.core.SessionId;
import com.h3c.bigdata.gop.websocket.core.session.MessageTransportException;
import com.h3c.bigdata.gop.websocket.core.session.SessionContainer;
import com.h3c.bigdata.zhgx.ws.auth.WsLoginAuthenticateService;
import com.h3c.bigdata.zhgx.ws.auth.WsLoginFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * {@code WsSessionContainer}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
@ConcreteContainer(handle = "/ws/logined",
                   enableSockJs = true,
                   messageStrategy = LoginMessages.class,
                   authenticateService = WsLoginAuthenticateService.class,
                   authenticateFailureHandler = WsLoginFailureHandler.class)
public class WsSessionContainer extends SessionContainer implements TokenExpiredListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public WsSessionContainer(Cache userCache) {
        super(userCache);
    }

    @Override
    public void onTokenExpired(String user) {
        SessionId sessionId = getMappedSessionId(user);
        if (sessionId != null) {
            if (getMappedSession(sessionId) != null) {
                try {
                    send(sessionId, LoginMessages.tokenExpired(), false);
                } catch (MessageTransportException e) {
                    logger.error("------ ws process token expired failed, {}", e.getMessage());
                }
            }
        }
    }

}
