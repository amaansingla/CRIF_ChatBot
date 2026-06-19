# CRIF Internal Chatbot

A full-stack internal knowledge assistant built with Spring Boot, JPA, H2 Database, and JavaScript. The chatbot provides employees with a centralized interface for accessing project information, documentation, internal tools, and support resources through natural language queries.

## Demo

📸 **Application Screenshot**

<img width="1202" height="1300" alt="CRIF_Chatbot Demo_Photo" src="https://github.com/user-attachments/assets/31b567d2-065f-45fa-9e3f-31007caaf1f7" />


🎥 **Demo Video**

https://youtu.be/VLs4wyUOvvk

---

## Key Highlights

* Built a full-stack chatbot using Spring Boot and JavaScript
* Implemented intelligent keyword-based search across internal knowledge resources
* Added in-memory caching using ConcurrentHashMap to improve response performance
* Designed a layered architecture following enterprise software design principles
* Developed REST APIs for chatbot communication and health monitoring
* Implemented query logging and audit tracking for analytics and debugging
* Created a responsive web interface with real-time status monitoring

---

## Features

### Intelligent Knowledge Retrieval

Search internal documentation, project information, support resources, and portal links using natural language queries.

### Query Optimization

Frequently requested queries are cached in memory to reduce redundant database lookups and improve response times.

### Suggested Queries

Pre-configured prompts help users quickly discover available functionality.

### Audit Logging

Every interaction is logged with timestamps and response metadata for monitoring and future analytics.

### Health Monitoring

Built-in health check endpoint and connection status indicator provide visibility into application availability.

---

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Maven

### Frontend

* HTML5
* CSS3
* Vanilla JavaScript
* REST API Integration

---

## Architecture

The application follows a layered architecture pattern:

```text
┌─────────────────────────────────────┐
│          Frontend Layer             │
│      HTML + CSS + JavaScript        │
└─────────────────┬───────────────────┘
                  │
                  │ HTTP / JSON
                  │
┌─────────────────▼───────────────────┐
│          REST API Layer             │
│     Spring Boot Controllers         │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│        Business Logic Layer         │
│      ChatService + Caching          │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│          Persistence Layer          │
│      Spring Data JPA + H2 DB        │
└─────────────────────────────────────┘
```

For additional implementation details, see ARCHITECTURE.md.

---

## Example Queries

* Show me the JIRA link
* Where can I find authentication documentation?
* Tell me about the customer portal project
* How do I contact IT support?
* Show available support resources

---

## API Endpoints

### Send Chat Message

```http
POST /api/chat
```

Request:

```json
{
  "message": "Show me the JIRA link"
}
```

Response:

```json
{
  "reply": "JIRA Portal: https://..."
}
```

### Health Check

```http
GET /api/health
```

Returns HTTP 200 when the service is operational.

---

## Database Design

The chatbot stores information across several domain entities:

| Table      | Purpose                              |
| ---------- | ------------------------------------ |
| LINKS      | Internal tools and portal URLs       |
| PROJECTS   | Project information and metadata     |
| DOCUMENTS  | Documentation and reference material |
| QUERY_LOGS | User interactions and audit history  |

---

## Performance Enhancements

* Query normalization before searching
* In-memory caching using ConcurrentHashMap
* Reduced database lookups for repeated queries
* Thread-safe cache implementation for concurrent access

---

## Running Locally

### Prerequisites

* Java 17+
* Maven 3.6+

### Installation

```bash
git clone <repository-url>
cd CRIF_ChatBot
```

```bash
mvn clean install
```

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

---

## Future Improvements

* Admin dashboard for managing knowledge base content
* PostgreSQL integration for production deployment
* Authentication and role-based access control
* AI-powered semantic search
* Distributed caching using Redis
* Enhanced analytics and reporting

---

## Lessons Learned

This project provided hands-on experience with:

* Spring Boot application development
* REST API design
* Repository and service layer architecture
* Database modeling with JPA
* Application caching strategies
* Full-stack integration between frontend and backend
* Enterprise software design patterns

---

## Author

Amaan Singla

GitHub: https://github.com/amaansingla

