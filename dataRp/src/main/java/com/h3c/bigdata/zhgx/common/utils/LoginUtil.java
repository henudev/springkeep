package com.h3c.bigdata.zhgx.common.utils;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录操作的工具类
 *
 * @author w17193
 */
@Component
public class LoginUtil {

  private static Boolean loginOnly;

  @Value("${login.only}")
  public void setHOSTNAME(Boolean loginOnly) {
    LoginUtil.loginOnly = loginOnly;
  }

  /**
   * 删除userId 对应的缓存 同时 将删除的token值保存到 USERLOUGOUTCHAHE 缓存中 当该token用户继续访问时，给出账号被挤掉的提示
   *
   * @param userId
   * @param isSaveCache
   */
  /*

  public static void removeCache(String userId,Boolean isSaveCache){
      String cacheRefreshTokens =  CaCheMapConst.USERTOKENCACHE.get(userId);

      if(!StringUtils.isEmpty(cacheRefreshTokens)){
          String[] cacheRefreshTokenArr = cacheRefreshTokens.split(",");

          for (String cacheRefreshToken: cacheRefreshTokenArr) {
              if(!StringUtils.isEmpty(cacheRefreshToken)){
                  String cacheAccessToken = CaCheMapConst.TOKENCACHE.get(cacheRefreshToken);
                  if(CaCheMapConst.TOKENCACHE.containsKey(cacheRefreshToken)){
                      if (isSaveCache){
                          CaCheMapConst.USERLOUGOUTCHAHE.put(cacheRefreshToken,userId);
                      }
                      CaCheMapConst.TOKENCACHE.remove(cacheRefreshToken);
                  }

                  if(StringUtils.isNotEmpty(cacheAccessToken)){
                      if( CaCheMapConst.USERCACHE.containsKey(cacheAccessToken)){
                          if (isSaveCache){
                              CaCheMapConst.USERLOUGOUTCHAHE.put(cacheAccessToken,userId);
                          }
                          CaCheMapConst.USERCACHE.remove(cacheAccessToken);
                      }
                  }

              }
          }
      }
      CaCheMapConst.USERTOKENCACHE.remove(userId);
  }

  */

  /**
   * 清空所有的缓存
   */
  public static void removeAllCache(){
    CaCheMapConst.USERCACHE.invalidateAll();
    CaCheMapConst.USERTICKETCACHE = new ConcurrentHashMap<>();
  }
  /**
   * 删除 userId 所对应的缓存值
   *
   * @param userId
   */
  public static void removeCache(String userId) {
    List<String> ticketList = CaCheMapConst.USERTICKETCACHE.get(userId);
    if (ticketList != null && ticketList.size() != 0) {
      for (String ticket : ticketList) {
          CaCheMapConst.USERCACHE.invalidate(ticket);
      }
    }

    CaCheMapConst.USERTICKETCACHE.remove(userId);
  }

  /**
   * 对应下的某个 refreshToken
   *
   * @param authToken
   */
  public static void removeCacheByToken(String authToken) {

    UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(authToken);
    if (userDetails != null) {
      List<String> ticketList = CaCheMapConst.USERTICKETCACHE.get(userDetails.getUsername());

      if (ticketList == null || ticketList.size() == 0) {
        CaCheMapConst.USERTICKETCACHE.remove(userDetails.getUsername());
      } else {
        if (loginOnly) {
          CaCheMapConst.USERTICKETCACHE.remove(userDetails.getUsername());
        } else {
          ticketList.removeIf(s -> s.equals(authToken));
          CaCheMapConst.USERTICKETCACHE.put(userDetails.getUsername(), ticketList);
        }
      }

        CaCheMapConst.USERCACHE.invalidate(authToken);
    }
  }
}
