package com.shruti.nl_to_sql.controller;

import com.shruti.nl_to_sql.model.NlRequest;
import com.shruti.nl_to_sql.model.NlResponse;
import com.shruti.nl_to_sql.service.GroqService;
import com.shruti.nl_to_sql.service.SqlExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NlToSqlController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private SqlExecutorService sqlExecutorService;

    @PostMapping("/nl-to-sql")
    public NlResponse convert(@RequestBody NlRequest request) {

        String question = request.getQuestion();

        // 1. Generate SQL
        String sql = groqService.generateSQL(question);

        // SAFETY CHECK (VERY IMPORTANT)
        if (!sql.toLowerCase().startsWith("select")) {
            throw new RuntimeException("Only SELECT queries allowed");
        }

        // 2. Execute SQL
        List<Map<String, Object>> results = sqlExecutorService.executeSQL(sql);

        // 3. Generate HUMAN ANSWER
        String answer = groqService.generateAnswer(question, sql, results);

        // 4. Build response
        NlResponse response = new NlResponse();
        response.setSql(sql);
        response.setResults(results);
        response.setAnswer(answer);

        return response;
    }
}