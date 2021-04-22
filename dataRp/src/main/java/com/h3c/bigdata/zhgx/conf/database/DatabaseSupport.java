/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.conf.database;

import javax.sql.DataSource;

/**
 * {@code DatabaseSupport}
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/22
 */
public interface DatabaseSupport {

    DataSource dataSource();

}
