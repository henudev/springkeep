package com.h3c.bigdata.zhgx.common.listener;


import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.h3c.bigdata.zhgx.ws.WsSessionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 监听cache移除事件
 */
@Component
public class TicketRemovedListener implements RemovalListener<String, UserDetails> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    WsSessionContainer wsSessionContainer;
    @Override
    public void onRemoval(RemovalNotification<String, UserDetails> notification) {
        if(RemovalCause.EXPIRED.equals(notification.getCause())){
            UserDetails userDetails = notification.getValue();
            String username = userDetails.getUsername();
            wsSessionContainer.onTokenExpired(username);
            logger.info("移除已超期的ticket：{}",username);
        }
    }
}
