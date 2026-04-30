package com.shruti.nl_to_sql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SqlExecutorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> executeSQL(String sql) {

        String cleanSql = sql.trim().toLowerCase();

        //  1. Must start with SELECT
        if (!cleanSql.startsWith("select")) {
            throw new RuntimeException("Only SELECT queries are allowed");
        }

        //  2. Block multiple statements
        if (cleanSql.contains(";")) {
            throw new RuntimeException("Multiple SQL statements not allowed");
        }

        //  3. Block dangerous keywords
        String[] banned = {
                "drop", "delete", "update", "insert",
                "alter", "truncate", "create"
        };

        for (String word : banned) {
            if (cleanSql.contains(word)) {
                throw new RuntimeException("Dangerous SQL operation detected: " + word);
            }
        }

        return jdbcTemplate.queryForList(sql);
    }
}