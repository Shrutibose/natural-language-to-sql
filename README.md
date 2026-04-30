# Natural Language to SQL
### Just ask a question. Get the answer, no SQL needed.

---

## What is this?

**Natural Language to SQL** is a fully AI-powered backend service that lets anyone query a database using plain English — no SQL knowledge required.

The user simply types a question like:

> _"Which employees earned the most last quarter?"_

And the system handles **everything**:
1. Converts the question into a SQL query
2. Executes the query on the database
3. Returns the actual result
4. Explains the result in plain English

**Zero SQL. Zero complexity. Just answers.**

---

## Key Features

- **Natural language input** — Users only need to type a question, nothing else
- **AI SQL generation** — Powered by LLaMA 3.3 70B via Groq for accurate, fast SQL generation
- **Auto query execution** — Generated SQL is executed on the real database automatically
- **Real results** — Returns actual data from the database
- **English explanation** — Result is explained back to the user in plain English
- **Blazing fast** — Groq inference is among the fastest available for open-source LLMs
- **Secure** — API keys managed via environment variables, never hardcoded

---

## How It Works

```
User types a question in plain English
            |
    NlToSqlController  (receives the request)
            |
    GroqService  (sends question to LLaMA 3.3 70B via Groq API)
            |
    LLaMA generates the SQL query
            |
    SqlExecutorService  (executes the SQL on the database)
            |
    GroqService  (generates a plain English explanation of the result)
            |
User gets: SQL Query + Query Result + English Explanation
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| AI Model | LLaMA 3.3 70B via Groq API |
| HTTP Client | RestTemplate |
| Build Tool | Maven |
| API Style | REST |

---

## API Usage

### Endpoint
```
POST /api/nl-to-sql
```

### Request — User only sends their question!
```json
{
  "question": "Which product had the highest sales last month?"
}
```

### Response — SQL + Result + Explanation all in one!
```json
{
  "sql": "SELECT product_name, SUM(amount) as total_sales FROM orders WHERE MONTH(order_date) = MONTH(NOW()) - 1 GROUP BY product_name ORDER BY total_sales DESC LIMIT 1;",
  "result": [
    { "product_name": "Wireless Headphones", "total_sales": 125000 }
  ],
  "explanation": "Wireless Headphones had the highest sales last month with a total revenue of 1,25,000."
}
```

---

## Example Interactions

| User Question | What the system returns |
|---|---|
| "How many users signed up this week?" | SQL + count + plain English summary |
| "Who are the top 5 customers by order value?" | SQL + ranked list + explanation |
| "Which department has the most employees?" | SQL + result + English answer |

---

## Project Structure

```
├── GroqService.java            # Calls Groq API -> generates SQL + explanation
├── NlToSqlController.java      # REST endpoint — accepts user's question
├── SqlExecutorService.java     # Executes generated SQL on the database
├── NlRequest.java              # Request model
├── NlResponse.java             # Response model (sql + result + explanation)
├── pom.xml                     # Maven dependencies
└── .gitignore                  # Keeps secrets safe
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- A [Groq API Key](https://console.groq.com) (free)
- A running MySQL/PostgreSQL database

### 1. Clone the repository
```bash
git clone https://github.com/Shrutibose/natural-language-to-sql.git
cd natural-language-to-sql
```

### 2. Set your Groq API Key
```bash
# Windows CMD
set GROQ_API_KEY=your_groq_api_key_here

# Mac/Linux
export GROQ_API_KEY=your_groq_api_key_here
```

### 3. Configure `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
spring.datasource.username=root
spring.datasource.password=yourpassword
groq.api.key=${GROQ_API_KEY}
```

### 4. Run
```bash
mvn spring-boot:run
```

---

## Security

- API key stored as **environment variable** — never hardcoded
- `application.properties` excluded from version control via `.gitignore`

---

## Author

**Shruti Bose**
[GitHub](https://github.com/Shrutibose)

---

## License

This project is open source and available under the [MIT License](LICENSE).
