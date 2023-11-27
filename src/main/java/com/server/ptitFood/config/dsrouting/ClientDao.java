package com.server.ptitFood.config.dsrouting;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

public class ClientDao {
    private static final String SQL_GET_CLIENT_NAME = "select name from client";

    private final JdbcTemplate jdbcTemplate;

    public ClientDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    public String getClientName() {
        return this.jdbcTemplate.query(SQL_GET_CLIENT_NAME, rowMapper).get(0);
    }

    private static final RowMapper<String> rowMapper = (rs, rowNum) -> {
        return rs.getString("name");
    };
}
