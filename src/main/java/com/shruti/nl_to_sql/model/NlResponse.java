package com.shruti.nl_to_sql.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class NlResponse {

    private String sql;
    private List<Map<String, Object>> results;

    //
    private String answer;
}