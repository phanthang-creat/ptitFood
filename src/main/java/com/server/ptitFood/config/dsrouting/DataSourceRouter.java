package com.server.ptitFood.config.dsrouting;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ClientDatabaseContextHolder.getClientDatabase();
    }

    public void initDatasource(DataSource adminDataSource,
                               DataSource staffDataSource,
                               DataSource clientDataSource) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(ClientDatabase.ADMIN, adminDataSource);
        dataSourceMap.put(ClientDatabase.CLIENT, clientDataSource);
        dataSourceMap.put(ClientDatabase.STAFF, staffDataSource);
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(clientDataSource);
    }
}
