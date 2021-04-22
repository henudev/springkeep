package com.h3c.bigdata.zhgx.ws.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.h3c.bigdata.gop.websocket.core.security.AuthenticateService;
import com.h3c.bigdata.gop.websocket.core.security.AuthenticationException;
import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

/**
 * {@code WsLoginAuthenticateService}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class WsLoginAuthenticateService implements AuthenticateService {
    @Autowired
    private final Cache tokenCache;


    public WsLoginAuthenticateService(Cache tokenCache) {
        this.tokenCache = tokenCache;
    }

    @Override
    public String authenticate(WebSocketMessage<?> param) throws AuthenticationException {
        if (!(param instanceof TextMessage)) {
            throw new InvalidAuthTypeException(param == null ? "null message" : ClassUtils.getShortName(param.getClass()));
        }
        JSONObject object = JSON.parseObject(((TextMessage) param).getPayload());

        String username = object.getString("username");
        String token = object.getString("token");
        if (username == null || token == null) {
            throw new MissAuthParamException(username, token);
        }
        if(StringUtils.isEmpty(CaCheMapConst.USERTICKETCACHE.get(username))){
            throw new InvalidAuthTokenException("无效Token：" + token);
        }
        String storedToken = CaCheMapConst.USERTICKETCACHE.get(username).get(0);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new InvalidAuthTokenException("无效Token：" + token);
        }
        return username;
    }
}
