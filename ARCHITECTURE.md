# CRIF Internal Chatbot - Architecture Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [System Overview](#system-overview)
3. [Architecture Layers](#architecture-layers)
4. [Component Details](#component-details)
5. [Data Flow](#data-flow)
6. [Database Design](#database-design)
7. [Caching Strategy](#caching-strategy)
8. [API Specification](#api-specification)
9. [Security Considerations](#security-considerations)
10. [Scalability and Performance](#scalability-and-performance)
11. [Future Architecture Enhancements](#future-architecture-enhancements)

## Introduction

### Purpose

This document provides comprehensive architectural documentation for the CRIF Internal Chatbot MVP. It describes the system's structural design, component interactions, data models, and technical decisions that guide the implementation.

### Scope

The architecture covers all layers of the application stack, from the user interface to data persistence, including API contracts, business logic, and database design.

### Architectural Goals

- **Modularity**: Clear separation of concerns across distinct layers
- **Maintainability**: Simple, readable code with minimal dependencies
- **Performance**: Efficient query processing with intelligent caching
- **Extensibility**: Design that accommodates future enhancements
- **Reliability**: Robust error handling and logging mechanisms

## System Overview

### High-Level Architecture

The CRIF Internal Chatbot follows a traditional four-tier architecture pattern:

```
┌──────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                         │
│                                                               │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐            │
│  │  HTML UI   │  │    CSS     │  │ JavaScript │            │
│  │  Layout    │  │  Styling   │  │   Logic    │            │
│  └────────────┘  └────────────┘  └────────────┘            │
│                                                               │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            │ HTTP/JSON (REST API)
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                      API LAYER                                │
│                                                               │
│  ┌──────────────────────────────────────────────┐           │
│  │        ChatController                        │           │
│  │  - Handles HTTP requests                     │           │
│  │  - Request/Response mapping                  │           │
│  │  - Input validation                          │           │
│  └──────────────────────────────────────────────┘           │
│                                                               │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            │ Method Calls
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                    SERVICE LAYER                              │
│                                                               │
│  ┌──────────────────────────────────────────────┐           │
│  │           ChatService                        │           │
│  │  ┌────────────────────────────────────────┐ │           │
│  │  │  Query Processing Pipeline             │ │           │
│  │  │  1. Normalize input                    │ │           │
│  │  │  2. Check cache                        │ │           │
│  │  │  3. Search knowledge base              │ │           │
│  │  │  4. Format response                    │ │           │
│  │  │  5. Log query                          │ │           │
│  │  └────────────────────────────────────────┘ │           │
│  │                                              │           │
│  │  ┌────────────────────────────────────────┐ │           │
│  │  │  In-Memory Cache                       │ │           │
│  │  │  ConcurrentHashMap<String, String>     │ │           │
│  │  └────────────────────────────────────────┘ │           │
│  └──────────────────────────────────────────────┘           │
│                                                               │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            │ JPA Repositories
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                    DATA ACCESS LAYER                          │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │    Link      │  │   Project    │  │  Document    │      │
│  │  Repository  │  │  Repository  │  │  Repository  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                               │
│  ┌──────────────────────────────────────────────┐           │
│  │        QueryLogRepository                    │           │
│  └──────────────────────────────────────────────┘           │
│                                                               │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            │ JDBC/SQL
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                   DATABASE LAYER                              │
│                                                               │
│                    H2 In-Memory Database                      │
│                                                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌───────────┐   │
│  │  LINKS   │  │ PROJECTS │  │DOCUMENTS │  │QUERY_LOGS │   │
│  └──────────┘  └──────────┘  └──────────┘  └───────────┘   │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

### Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Presentation | HTML5, CSS3, Vanilla JavaScript | User interface and client-side logic |
| API | Spring Boot, Spring Web MVC | RESTful web services |
| Business Logic | Spring Framework, Java 17 | Service layer implementation |
| Data Access | Spring Data JPA | Repository abstraction |
| Persistence | H2 Database | In-memory relational database |
| Build Tool | Maven | Dependency management and build automation |

## Architecture Layers

### 1. Presentation Layer (UI Layer)

**Location**: `src/main/resources/static/`

**Components**:

- **index.html**: Main application interface
  - Chat message display area
  - User input field
  - Suggested query chips
  - Connection status indicator
  
- **style.css**: Visual styling
  - Responsive layout design
  - Message bubble formatting
  - Color scheme and typography
  - Loading indicators
  
- **app.js**: Client-side application logic
  - Event handling (user input, button clicks)
  - API communication (fetch API)
  - DOM manipulation for message rendering
  - Health check polling
  - Chat history management

**Responsibilities**:

- Render user interface components
- Capture and validate user input
- Send HTTP requests to backend API
- Display formatted responses with clickable links
- Provide visual feedback for loading states
- Monitor and display connection status

**Communication**:

- Communicates with API layer via RESTful HTTP calls
- Sends POST requests with JSON payload
- Receives JSON responses with formatted content

### 2. API Layer (Controller Layer)

**Location**: `src/main/java/com/crif/chatbot/controller/`

**Primary Component**: `ChatController`

**Endpoints**:

```java
@RestController
@RequestMapping("/api")
public class ChatController {
    
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest request)
    
    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck()
}
```

**Responsibilities**:

- Expose RESTful API endpoints
- Receive and deserialize HTTP requests
- Delegate business logic to service layer
- Serialize and return HTTP responses
- Handle HTTP status codes and error responses
- Perform basic input validation

**Request/Response Models**:

```java
// Request DTO
public class ChatRequest {
    private String message;
}

// Response DTO
public class ChatResponse {
    private String reply;
}
```

### 3. Service Layer (Business Logic Layer)

**Location**: `src/main/java/com/crif/chatbot/service/`

**Primary Component**: `ChatService`

**Core Processing Pipeline**:

```
User Query → Normalization → Cache Lookup → Knowledge Base Search → Response Formatting → Logging → Response
                                    ↓
                                Cache Hit? → Return Cached Response
                                    ↓
                                 No Hit
                                    ↓
                            Database Queries:
                            1. Links
                            2. Documentation (if doc keywords present)
                            3. Projects
                            4. Documents (fallback)
```

**Key Methods**:

```java
public class ChatService {
    
    // Main entry point for query processing
    public String handleQuery(String userMessage)
    
    // Normalize query for consistent matching
    private String normalizeQuery(String query)
    
    // Search links table
    private List<Link> searchLinks(String normalizedQuery)
    
    // Search documentation
    private List<Document> searchDocumentation(String normalizedQuery)
    
    // Search projects
    private List<Project> searchProjects(String normalizedQuery)
    
    // Search all documents
    private List<Document> searchDocuments(String normalizedQuery)
    
    // Format search results into readable response
    private String formatResponse(...)
    
    // Log query and response for audit
    private void logQuery(String query, String response, String type)
}
```

**Normalization Logic**:

The service normalizes queries to improve matching accuracy:

- Convert to lowercase
- Remove extra whitespace
- Strip special characters (except spaces and hyphens)
- Standardize common abbreviations

**Search Priority Logic**:

1. **Link Search**: Always executed first for portal/tool links
2. **Documentation Search**: Prioritized when query contains keywords:
   - "doc", "documentation", "docs"
   - "guide", "manual", "how to"
3. **Project Search**: Executed in all queries
4. **Document Fallback**: Executed when no documentation results found

**Response Formatting**:

Responses are formatted with:

- Introductory text based on result type
- Clickable HTML links for resources
- Descriptions and context
- Fallback messages when no results found

### 4. Data Access Layer (Repository Layer)

**Location**: `src/main/java/com/crif/chatbot/repository/`

**Repository Interfaces**:

All repositories extend Spring Data JPA's `JpaRepository` interface:

```java
@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    @Query("SELECT l FROM Link l WHERE " +
           "LOWER(l.name) LIKE %:keyword% OR " +
           "LOWER(l.keywords) LIKE %:keyword%")
    List<Link> findByKeyword(@Param("keyword") String keyword);
}

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.name) LIKE %:keyword% OR " +
           "LOWER(p.description) LIKE %:keyword% OR " +
           "LOWER(p.keywords) LIKE %:keyword%")
    List<Project> findByKeyword(@Param("keyword") String keyword);
}

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE " +
           "LOWER(d.title) LIKE %:keyword% OR " +
           "LOWER(d.content) LIKE %:keyword% OR " +
           "LOWER(d.keywords) LIKE %:keyword%")
    List<Document> findByKeyword(@Param("keyword") String keyword);
}

@Repository
public interface QueryLogRepository extends JpaRepository<QueryLog, Long> {
    // Standard CRUD operations
}
```

**Responsibilities**:

- Abstract database access operations
- Provide custom query methods
- Handle entity persistence
- Execute JPQL queries
- Manage transactions (via Spring)

## Component Details

### Frontend Components

#### Chat Interface (`index.html`)

**Structure**:
```html
<div id="chat-container">
  <div id="chat-messages">
    <!-- Messages rendered here dynamically -->
  </div>
  <div id="suggested-queries">
    <!-- Query chips -->
  </div>
  <div id="input-area">
    <input type="text" id="user-input" />
    <button id="send-button">Send</button>
  </div>
  <div id="connection-status">
    <!-- Health indicator -->
  </div>
</div>
```

#### Message Rendering (`app.js`)

```javascript
function addMessage(text, isUser) {
  const messageDiv = document.createElement('div');
  messageDiv.className = isUser ? 'user-message' : 'bot-message';
  messageDiv.innerHTML = text; // Supports HTML links
  chatMessages.appendChild(messageDiv);
  scrollToBottom();
}
```

#### Health Monitoring

```javascript
async function checkHealth() {
  try {
    const response = await fetch('/api/health');
    updateConnectionStatus(response.ok);
  } catch (error) {
    updateConnectionStatus(false);
  }
}

// Poll every 30 seconds
setInterval(checkHealth, 30000);
```

### Backend Components

#### Entity Models

**Link Entity**:
```java
@Entity
@Table(name = "LINKS")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String url;
    private String keywords;
}
```

**Project Entity**:
```java
@Entity
@Table(name = "PROJECTS")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String keywords;
}
```

**Document Entity**:
```java
@Entity
@Table(name = "DOCUMENTS")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String category;    // e.g., "InfoSec", "Tech Help"
    private String projectName; // Optional project association
    private String title;
    private String content;     // Snippet or full content
    private String keywords;
}
```

**QueryLog Entity**:
```java
@Entity
@Table(name = "QUERY_LOGS")
public class QueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String queryText;
    private String responseText;
    private String type;        // "DATABASE" or "CACHE"
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

## Data Flow

### Query Processing Sequence

```
1. User Input
   └─> Frontend captures message
       └─> POST /api/chat { message: "jira link" }
           └─> ChatController receives request
               └─> ChatService.handleQuery("jira link")
                   
2. Query Normalization
   └─> Convert to lowercase: "jira link"
       └─> Trim whitespace
           └─> Normalized: "jira link"
               
3. Cache Lookup
   └─> Check ConcurrentHashMap
       ├─> Cache Hit?
       │   └─> Log as "CACHE"
       │       └─> Return cached response
       │
       └─> Cache Miss?
           └─> Proceed to database search
           
4. Database Search
   └─> Search Links by keyword
       ├─> Results found?
       │   └─> Format link results
       │
       └─> Search Documentation (if doc keywords present)
       └─> Search Projects by keyword
       └─> Search Documents by keyword (fallback)
           
5. Response Assembly
   └─> Combine all results
       └─> Format with HTML links
           └─> Generate user-friendly message
               
6. Caching & Logging
   └─> Store in cache for future queries
       └─> Log to QUERY_LOGS table
           └─> Record: query, response, type, timestamp
               
7. Response Delivery
   └─> ChatService returns formatted string
       └─> ChatController wraps in ChatResponse
           └─> JSON response sent to frontend
               └─> JavaScript renders message with links
```

### Health Check Flow

```
Frontend Timer (30s interval)
   └─> GET /api/health
       └─> ChatController.healthCheck()
           └─> Returns HTTP 200 OK
               └─> Frontend updates status indicator
```

## Database Design

### Schema Overview

```sql
-- Links Table
CREATE TABLE LINKS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    keywords VARCHAR(500)
);

-- Projects Table
CREATE TABLE PROJECTS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    keywords VARCHAR(500)
);

-- Documents Table
CREATE TABLE DOCUMENTS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100),
    project_name VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    content TEXT,
    keywords VARCHAR(500)
);

-- Query Logs Table
CREATE TABLE QUERY_LOGS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query_text TEXT NOT NULL,
    response_text TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Indexing Strategy

For optimal query performance, the following indexes are recommended:

```sql
-- Indexes for keyword searches
CREATE INDEX idx_links_name ON LINKS(name);
CREATE INDEX idx_links_keywords ON LINKS(keywords);

CREATE INDEX idx_projects_name ON PROJECTS(name);
CREATE INDEX idx_projects_keywords ON PROJECTS(keywords);

CREATE INDEX idx_documents_title ON DOCUMENTS(title);
CREATE INDEX idx_documents_category ON DOCUMENTS(category);
CREATE INDEX idx_documents_keywords ON DOCUMENTS(keywords);

-- Index for query analytics
CREATE INDEX idx_query_logs_created ON QUERY_LOGS(created_at);
CREATE INDEX idx_query_logs_type ON QUERY_LOGS(type);
```

### Data Initialization

**DataLoader Component**:

```java
@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private LinkRepository linkRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Override
    public void run(String... args) {
        loadLinks();
        loadProjects();
        loadDocuments();
        loadHelpResources();
    }
    
    private void loadLinks() {
        // Insert portal links (JIRA, Confluence, etc.)
    }
    
    private void loadProjects() {
        // Insert project information
    }
    
    private void loadDocuments() {
        // Insert documentation
    }
    
    private void loadHelpResources() {
        // Insert InfoSec, Tech Help documents
    }
}
```

## Caching Strategy

### Implementation

**Cache Structure**:
```java
private final Map<String, String> queryCache = new ConcurrentHashMap<>();
```

**Cache Operations**:

```java
// Cache lookup
public String handleQuery(String userMessage) {
    String normalized = normalizeQuery(userMessage);
    
    // Check cache first
    if (queryCache.containsKey(normalized)) {
        String cachedResponse = queryCache.get(normalized);
        logQuery(userMessage, cachedResponse, "CACHE");
        return cachedResponse;
    }
    
    // Process query and cache result
    String response = processQuery(normalized);
    queryCache.put(normalized, response);
    logQuery(userMessage, response, "DATABASE");
    
    return response;
}
```

### Cache Characteristics

| Aspect | Implementation |
|--------|---------------|
| **Type** | In-memory, application-scoped |
| **Data Structure** | ConcurrentHashMap |
| **Thread Safety** | Yes (concurrent access supported) |
| **Persistence** | Non-persistent (cleared on restart) |
| **Eviction Policy** | None (MVP - manual clear needed) |
| **Size Limit** | Unbounded (MVP) |

### Performance Benefits

- **Reduced Database Load**: Frequent queries served from memory
- **Faster Response Times**: Eliminates database round-trip
- **Scalability**: Reduces database connection usage

### Future Enhancements

For production deployment, consider:

- Distributed caching (Redis, Hazelcast)
- TTL (Time-To-Live) based eviction
- Cache size limits with LRU eviction
- Cache invalidation on knowledge base updates
- Cache hit rate metrics

## API Specification

### Endpoints

#### Chat Endpoint

**Request**:
```http
POST /api/chat
Content-Type: application/json

{
  "message": "show me the jira link"
}
```

**Response** (Success):
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "reply": "Here are the links I found:\n\n<strong>JIRA</strong>\n<a href='https://jira.crif.com' target='_blank'>https://jira.crif.com</a>\nProject management and issue tracking\n\n"
}
```

**Response** (No Results):
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "reply": "I couldn't find any information matching your query. Please try rephrasing or contact IT support."
}
```

**Error Response**:
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Invalid request format",
  "message": "Message field is required"
}
```

#### Health Check Endpoint

**Request**:
```http
GET /api/health
```

**Response**:
```http
HTTP/1.1 200 OK
```

### Response Formatting

The API returns formatted HTML in the reply field to support rich text display:

**Supported HTML Elements**:
- `<strong>` - Bold text for titles
- `<a href='...' target='_blank'>` - Clickable links
- `<br>` - Line breaks
- `\n\n` - Paragraph separation

## Security Considerations

### Current Implementation (MVP)

The MVP prioritizes rapid development and internal use:

- No authentication/authorization
- Internal network deployment assumed
- Basic input validation only
- No rate limiting
- No encryption of data at rest

### Production Security Recommendations

For production deployment, implement:

#### Authentication & Authorization

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Spring Security configuration
    // Integration with corporate SSO/LDAP
    // Role-based access control
}
```

#### Input Validation

```java
@PostMapping("/chat")
public ResponseEntity<ChatResponse> handleChat(
    @Valid @RequestBody ChatRequest request) {
    // Validated input
}

public class ChatRequest {
    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message too long")
    private String message;
}
```

#### Rate Limiting

```java
// Implement request throttling
// Prevent abuse and DDoS
// Per-user or per-IP limits
```

#### HTTPS

```properties
# application.properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
```

#### SQL Injection Protection

- Already mitigated by using JPA with parameterized queries
- Continue using `@Query` with `@Param` annotations
- Never concatenate user input into queries

#### XSS Protection

Frontend must sanitize HTML before rendering:

```javascript
function sanitizeHTML(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
```

## Scalability and Performance

### Current Limitations (MVP)

- **Single Instance**: No horizontal scaling
- **In-Memory Cache**: Lost on restart, not shared across instances
- **H2 Database**: In-memory, single-threaded
- **No Load Balancing**: Single point of failure

### Scaling Strategies

#### Horizontal Scaling

```
        Load Balancer (nginx/HAProxy)
              /        |        \
         Instance 1  Instance 2  Instance 3
              \        |        /
            Distributed Cache (Redis)
                      |
            Production Database (PostgreSQL)
```

#### Database Migration

**Recommended Production Database**: PostgreSQL

```properties
# application-prod.properties
spring.datasource.url=jdbc:postgresql://db-host:5432/crif_chatbot
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Migration Considerations**:
- Schema migration tools (Flyway, Liquibase)
- Connection pooling (HikariCP)
- Read replicas for query distribution
- Database indexing optimization

#### Caching Enhancement

**Redis Integration**:

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
            .cacheDefaults(defaultCacheConfig())
            .build();
    }
}

@Service
public class ChatService {
    
    @Cacheable(value = "queries", key = "#normalizedQuery")
    public String handleQuery(String normalizedQuery) {
        // Method result automatically cached in Redis
    }
}
```

#### Performance Optimization

**Query Optimization**:
- Add database indexes on keyword columns
- Implement full-text search (PostgreSQL, Elasticsearch)
- Use query result pagination
- Implement lazy loading for large result sets

**Response Time Targets**:
| Scenario | Target |
|----------|--------|
| Cache hit | < 50ms |
| Single DB query | < 200ms |
| Complex multi-query | < 500ms |

## Future Architecture Enhancements

### Short-Term Improvements

#### 1. Administrative UI Panel

**Purpose**: Allow non-technical users to manage knowledge base

**Features**:
- Add/Edit/Delete links, projects, documents
- Keyword management
- Category organization
- Search and filter existing content
- Bulk import (CSV upload)

**Architecture Addition**:
```
New Admin Controller → Admin Service → Existing Repositories
                    ↓
              Cache Invalidation
```

#### 2. Advanced Search

**Natural Language Processing**:
- Intent recognition
- Entity extraction
- Synonym expansion
- Query suggestions

**Implementation**:
```java
@Service
public class NLPService {
    public ParsedQuery parseQuery(String userMessage);
    public List<String> extractEntities(String text);
    public List<String> getSynonyms(String keyword);
}
```

#### 3. User Feedback Mechanism

**Thumbs Up/Down**:
- Track response quality
- Identify gaps in knowledge base
- Improve search algorithms

**Schema Addition**:
```sql
CREATE TABLE FEEDBACK (
    id BIGINT PRIMARY KEY,
    query_log_id BIGINT FOREIGN KEY REFERENCES QUERY_LOGS(id),
    rating BOOLEAN,  -- true=helpful, false=not helpful
    comment TEXT,
    created_at TIMESTAMP
);
```

### Long-Term Vision

#### 1. Multi-Channel Support

Extend chatbot to additional platforms:
- Slack integration
- Microsoft Teams bot
- Email interface
- Mobile app

#### 2. AI/ML Integration

**Machine Learning Enhancements**:
- Query understanding via transformers (BERT, GPT)
- Semantic search with vector embeddings
- Personalized recommendations
- Automated knowledge base updates

**Architecture with ML**:
```
User Query → NLP Service → Vector Embedding → Similarity Search
                                                     ↓
                                            Vector Database
                                            (Pinecone, Weaviate)
```

#### 3. Analytics Dashboard

**Metrics to Track**:
- Query volume and trends
- Popular search terms
- Cache hit rates
- Response time distribution
- User satisfaction scores
- Coverage gaps

**Visualization**:
- Real-time dashboards (Grafana)
- Custom admin reports
- Export capabilities

#### 4. Multi-Tenant Support

For enterprise deployment across multiple CRIF departments:

```sql
-- Add tenant_id to all tables
ALTER TABLE LINKS ADD COLUMN tenant_id BIGINT;
ALTER TABLE PROJECTS ADD COLUMN tenant_id BIGINT;
-- Etc.

-- Filter queries by tenant
WHERE tenant_id = :currentUserTenantId
```

#### 5. Microservices Architecture

For large-scale deployment:

```
API Gateway
    ├── Chat Service (Query handling)
    ├── Knowledge Service (Content management)
    ├── Analytics Service (Metrics & logging)
    ├── User Service (Authentication)
    └── Integration Service (External systems)
```

## Conclusion

The CRIF Internal Chatbot architecture provides a solid foundation for internal information retrieval with room for growth. The layered design ensures maintainability and extensibility, while the caching strategy optimizes performance for common queries.

Key architectural strengths:

- Clear separation of concerns across layers
- RESTful API design for potential multi-channel expansion
- Intelligent query processing pipeline
- Comprehensive audit logging
- Extensible data model
