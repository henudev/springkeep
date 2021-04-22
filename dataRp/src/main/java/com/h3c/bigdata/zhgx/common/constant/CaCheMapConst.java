package com.h3c.bigdata.zhgx.common.constant;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.h3c.bigdata.zhgx.common.listener.TicketRemovedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 缓存统一管理
 *
 * @author w17193
 */
@Configuration
public class CaCheMapConst {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TicketRemovedListener ticketRemovedListener;

    /* 保存单个用户已删除token, 当该token用户继续访问时，给出账号被挤掉的提示,格式<token:userName>*/
    public static Cache<String, String> USERLOUGOUTCHAHE;

    /**
     * 保存登录用户信息 key : ticket value : 用户登录信息
     */

    public static Cache<String, UserDetails> USERCACHE;

    @Value("${token.invalid}")
    public void setRefreshTokenTime(Long refreshTokenTime) {
        RemovalListener<String, UserDetails> asynchronous = RemovalListeners.asynchronous(ticketRemovedListener, Executors.newSingleThreadExecutor());
        USERCACHE = CacheBuilder.newBuilder().expireAfterAccess(refreshTokenTime, TimeUnit.SECONDS).removalListener(asynchronous).build();
        USERLOUGOUTCHAHE = CacheBuilder.newBuilder().expireAfterWrite(refreshTokenTime, TimeUnit.SECONDS).build();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> USERCACHE.cleanUp(), 0, 5, TimeUnit.SECONDS);
    }

    /**
     * key : userId value : ticket集合 支持只允许一个账号同一时间登陆
     */
    public static Map<String, List<String>> USERTICKETCACHE = new ConcurrentHashMap<>();

    /**
     * 存放de2.0工具对应的url
     */
    public static Map<String, String> DECACHE = new ConcurrentHashMap<>();
}
