/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.ws;

/**
 * {@code TokenExpiredListener} Token失效监听器
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/15
 */
public interface TokenExpiredListener {

    void onTokenExpired(String user);

}
