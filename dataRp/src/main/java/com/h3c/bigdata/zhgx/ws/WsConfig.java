package com.h3c.bigdata.zhgx.ws;

import com.h3c.bigdata.gop.websocket.annotation.EnableWs;
import com.h3c.bigdata.gop.websocket.core.mq.MqType;
import com.h3c.bigdata.gop.websocket.spring.security.UserInfo;
import com.h3c.bigdata.gop.websocket.spring.security.UsernameResolver;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code WebSocketConfig}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */

@Configuration
@EnableWs(mqType = MqType.NO_MQ)
public class WsConfig {

    @Autowired
    private BaseController baseController;
    @Bean
    public Cache userCache() {
        return new ConcurrentMapCache("userCache");
    }

    @Bean
    public UsernameResolver usernameResolver() {
        return (request, authentication) -> {

            String username =baseController.getUserIdByToken();
            return new UserInfo(username, "wsSessionContainer");
        };
    }

}
