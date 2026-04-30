package com.shruti.nl_to_sql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    // COMMON API CALL METHOD
    private String callGroq(String prompt) {

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));
        body.put("temperature", 0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(GROQ_URL, request, Map.class);

        List<Map> choices = (List<Map>) response.getBody().get("choices");
        Map messageResponse = (Map) choices.get(0).get("message");

        return ((String) messageResponse.get("content")).trim();
    }
    // SQL GENERATION
    public String generateSQL(String question) {

        String schema = """
                Tables:

                users(user_id, username, email, first_name, last_name, role_id)

                orders(order_id, user_id, order_number, order_type, total_amount)

                order_items(order_item_id, order_id, product_id, quantity)

                products(product_id, product_name, category_id, base_price)

                Relationships:
                orders.user_id = users.user_id
                order_items.order_id = orders.order_id
                order_items.product_id = products.product_id
                """;

        String prompt = """
                You are an expert MySQL query generator.

                STRICT RULES:
                1. Use ONLY the tables and columns provided
                2. DO NOT invent column names
                3. DO NOT use columns not listed
                4. Use correct JOIN conditions
                5. Return ONLY SQL query (no explanation, no markdown)

                """ + schema + """

                Question:
                """ + question;

        String rawSql = callGroq(prompt);

        return cleanSQL(rawSql);
    }


    // ANSWER GENERATION
    public String generateAnswer(String question, String sql, List<Map<String, Object>> results) {

        String prompt = """
                You are a data analyst.

                USER QUESTION:
                %s

                SQL QUERY:
                %s

                QUERY RESULT:
                %s

                TASK:
                - Give a clear, human-readable answer
                - Summarize results
                - Be concise
                - DO NOT show SQL
                """.formatted(question, sql, results.toString());

        return callGroq(prompt);
    }

    // SQL CLEANER
    private String cleanSQL(String rawSql) {
        return rawSql
                .replaceAll("```sql", "")
                .replaceAll("```", "")
                .replaceAll(";", "")
                .trim();
    }
}