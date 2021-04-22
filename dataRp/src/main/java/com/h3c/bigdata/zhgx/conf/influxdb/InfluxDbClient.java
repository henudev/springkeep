package com.h3c.bigdata.zhgx.conf.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

/**
 * 源自rh-mlmh
 * @author yys1402
 * @version 1.0.0
 * @since D001
 */
public class InfluxDbClient {

    private String database;

    private InfluxDB influxDB;

    public InfluxDbClient(final String database, final InfluxDB influxDB) {
        this.database = database;
        this.influxDB = influxDB;
    }

    public QueryResult query(String sql){
        return influxDB.query(new Query(sql, database));
    }

}
