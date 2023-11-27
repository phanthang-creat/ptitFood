package com.server.ptitFood.config.dsrouting;

import javax.sql.DataSource;

public class DataSourceContextHolder {
    private static final ThreadLocal<DataSource> CONTEXT = new ThreadLocal<>();

    public static void setDataSource(DataSource dataSource) {
        CONTEXT.set(dataSource);
    }

    public static DataSource getDataSource() {
        return CONTEXT.get();
    }

    public static void clearDataSource() {
        CONTEXT.remove();
    }
}
