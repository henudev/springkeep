package com.h3c.bigdata.zhgx.ws.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Optional;

/**
 * @author fangzhiheng
 */
public abstract class EndpointBase implements MessageSender, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(EndpointBase.class);

    private String remote;

    private Session session;

    private EndpointConfig endpointConfig;

    private String httpSessionId;

    public Session getSession() {
        return session;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    public void setRemote(final String remote) {
        this.remote = remote;
    }

    public void setHttpSessionId(final String sid) {
        this.httpSessionId = sid;
    }

    public String getHttpSessionId() {
        return httpSessionId;
    }

    public EndpointConfig getEndpointConfig() {
        return endpointConfig;
    }

    public void setEndpointConfig(final EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
    }

    protected <T> Optional<T> resolveMessage(String message, Class<T> c) {
        try {
            return Optional.of(JSON.parseObject(message, c));
        } catch (Exception e) {
            logger.error("create {} message failed: {}", c.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public void sendMessage(Message message) {
        Session session = getSession();
        try {
            String jsonString = JSON.toJSONString(message, SerializerFeature.WriteMapNullValue);
            session.getAsyncRemote().sendText(jsonString);

            //session.getAsyncRemote().sendText(JSON.toJSONString(message, SerializerFeature.WriteEnumUsingToString));
        } catch (Exception e) {
            logger.error("send message error: websocket was closed", e);
        }
    }

    public String getRemote() {
        return remote;
    }

    public void close() throws IOException {
        getSession().close(new CloseReason(CloseReason.CloseCodes.RESERVED, "强制断开连接"));
    }

}
