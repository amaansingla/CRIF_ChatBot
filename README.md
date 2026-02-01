# CRIF Internal Chatbot

## Overview

The CRIF Internal Chatbot is an MVP (Minimum Viable Product) solution designed to provide employees with a unified interface for accessing internal information and resources. This intelligent chatbot streamlines information retrieval by offering quick access to documentation, project details, links to internal portals, and commonly requested resources through a conversational interface.

## Key Features

- **Unified Information Access**: Single point of access for internal documentation, project information, and portal links
- **Intelligent Query Processing**: Natural language understanding with keyword-based search and query normalization
- **Performance Optimization**: Built-in caching mechanism to improve response times for frequently asked questions
- **Suggested Queries**: Pre-configured query buttons for common information requests
- **Audit Trail**: Comprehensive query logging for usage analytics and system improvement
- **Real-time Health Monitoring**: Connection status indicator for system availability

## Technology Stack

### Frontend
- **HTML5/CSS3**: Modern, responsive user interface
- **Vanilla JavaScript**: Lightweight client-side logic without external dependencies
- **RESTful Integration**: Clean API communication layer

### Backend
- **Spring Boot**: Enterprise-grade Java framework for robust API development
- **Spring Data JPA**: Simplified data access and repository pattern implementation
- **H2 Database**: In-memory relational database for rapid prototyping (suitable for MVP)
- **Maven**: Dependency management and build automation

## System Architecture

The application follows a layered architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────┐
│         UI Layer (Client)           │
│   HTML + CSS + JavaScript           │
└─────────────────┬───────────────────┘
                  │
                  │ HTTP/JSON
                  │
┌─────────────────▼───────────────────┐
│      API Layer (Controller)         │
│      Spring Boot REST API           │
└─────────────────┬───────────────────┘
                  │
                  │
┌─────────────────▼───────────────────┐
│     Service Layer (ChatService)     │
│  Business Logic + Cache Management  │
└─────────────────┬───────────────────┘
                  │
                  │
┌─────────────────▼───────────────────┐
│      Data Layer (Repositories)      │
│   Spring Data JPA + H2 Database     │
└─────────────────────────────────────┘
```

For detailed architecture documentation, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Getting Started

### Prerequisites

- **Java JDK**: Version 17 or higher
- **Maven**: Version 3.6 or higher
- **Web Browser**: Modern browser with JavaScript enabled (Chrome, Firefox, Edge, Safari)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd crif-internal-chatbot
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   
   Open your web browser and navigate to:
   ```
   http://localhost:8080
   ```

### Configuration

The application uses Spring Boot's default configuration. Key configuration files:

- `application.properties`: Database connection, server port, and application settings
- `DataLoader.java`: Initial knowledge base population

## Usage

### Basic Interaction

1. Type your query in the chat input field
2. Press Enter or click the Send button
3. Receive formatted responses with clickable links to resources
4. Use suggested query chips for quick access to common requests

### Sample Queries

- "Show me the JIRA link"
- "Where can I find documentation on authentication?"
- "Tell me about the customer portal project"
- "How do I contact IT support?"

### Query Types

The chatbot can handle various query types:

- **Portal and Tool Links**: Access links to JIRA, Confluence, internal portals
- **Documentation Search**: Find technical and process documentation
- **Project Information**: Retrieve details about internal projects
- **Help Resources**: Access InfoSec guidelines, technical support contacts

## Knowledge Base Management

### Current Implementation

The knowledge base is populated through the `DataLoader.java` class, which executes on application startup. This class inserts predefined records into the database across four main categories:

- **Links**: URLs to internal tools and portals
- **Projects**: Information about ongoing and completed projects
- **Documents**: Technical documentation and process guides
- **Help Resources**: Support contacts and information security guidelines

### Adding New Content

To add new knowledge to the chatbot:

1. Locate `DataLoader.java` in the source code
2. Add new entries using the appropriate repository methods
3. Rebuild and restart the application

### Future Enhancements

A planned improvement is an administrative UI panel that will allow authorized users to:

- Add new knowledge base entries without code modifications
- Edit existing entries
- Manage keywords and categories
- Remove outdated information

## API Documentation

### Chat Endpoint

**POST** `/api/chat`

Request body:
```json
{
  "message": "your query here"
}
```

Response:
```json
{
  "reply": "Formatted response with information and links"
}
```

### Health Check Endpoint

**GET** `/api/health`

Returns HTTP 200 if the service is operational.

## Database Schema

The application uses four main tables:

- **LINKS**: Stores URLs to internal resources with keywords
- **PROJECTS**: Contains project descriptions and metadata
- **DOCUMENTS**: Holds documentation snippets and references
- **QUERY_LOGS**: Maintains audit trail of all queries and responses

## Performance Optimization

### Caching Strategy

The chatbot implements an in-memory caching mechanism using `ConcurrentHashMap`:

- Queries are normalized before cache lookup
- Cache hits prevent redundant database queries
- Cache statistics are logged for monitoring
- Thread-safe implementation for concurrent access

## Logging and Monitoring

### Query Logging

All queries are logged with the following information:

- Original query text
- Generated response
- Source type (DATABASE or CACHE)
- Timestamp

### Health Monitoring

The application provides a real-time connection status indicator visible in the UI.

## Development Guidelines

### Code Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/crif/chatbot/
│   │       ├── controller/    # REST API endpoints
│   │       ├── service/       # Business logic
│   │       ├── repository/    # Data access layer
│   │       ├── model/         # Entity definitions
│   │       └── config/        # Configuration classes
│   └── resources/
│       ├── static/            # Frontend assets
│       │   ├── index.html
│       │   ├── style.css
│       │   └── app.js
│       └── application.properties
```

### Best Practices

- Follow standard Java naming conventions
- Write unit tests for service layer logic
- Document all public methods and classes
- Use meaningful commit messages
- Keep frontend and backend concerns separated

## Testing

### Running Tests

```bash
mvn test
```

### Test Coverage

The project includes tests for:

- Service layer business logic
- Repository data access operations
- API endpoint responses

## Deployment Considerations

### Production Readiness

For production deployment, consider the following upgrades:

1. **Database**: Replace H2 with a production-grade database (PostgreSQL, MySQL, Oracle)
2. **Authentication**: Implement user authentication and authorization
3. **Security**: Add HTTPS, input validation, and rate limiting
4. **Monitoring**: Integrate application performance monitoring (APM) tools
5. **Logging**: Configure centralized logging with log aggregation
6. **Scaling**: Implement distributed caching (Redis) for multi-instance deployment

## Troubleshooting

### Common Issues

**Application fails to start**
- Verify Java version (requires JDK 17+)
- Check if port 8080 is available
- Review application logs for detailed error messages

**Database initialization errors**
- Ensure H2 dependencies are correctly configured
- Check DataLoader.java for syntax errors
- Verify database schema compatibility

**Frontend not loading**
- Clear browser cache
- Check browser console for JavaScript errors
- Verify static resources are in correct directory

## Contributing

Contributions to improve the chatbot are welcome. Please follow these guidelines:

1. Create a feature branch from `main`
2. Write tests for new functionality
3. Ensure all tests pass before submitting
4. Update documentation as needed
5. Submit a pull request with a clear description of changes