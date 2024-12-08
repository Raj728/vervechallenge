
# Thought Process for REST Service Implementation

## Design Goals

- **Scalability**: The application must handle at least 10,000 requests per second, ensuring that each request has a unique ID and is processed without duplication.
- **Concurrency**: The deduplication mechanism should be efficient in a distributed environment with multiple instances running behind load balancers.
- **Flexibility**: The system should support different communication methods (HTTP for internal requests, Kafka for messaging).
- **Simplicity**: Utilize existing tools like Redis and Kafka to handle distributed coordination and message processing without reinventing the wheel.

## Key Components

1. **API Layer**:
    - The main entry point is the `/api/verve/accept` endpoint.
    - It receives an `id` and `endpoint` as query parameters, parses them, and processes the business logic.

2. **Deduplication**:
    - In-memory tracking is handled using a `ConcurrentHashMap` to maintain thread-safety.
    - Redis is used for storing the state globally across instances, ensuring deduplication even when requests are handled by different app instances.

3. **Logging/Streaming**:
    - A scheduled task periodically logs or streams the count of unique request IDs every minute.
    - Kafka is used for streaming the data to a topic, enabling scalable and reliable message processing.

4. **HTTP Communication**:
    - I used Apache HttpClient to make GET/POST requests to external services.
    - Every response is logged for easy debugging and troubleshooting.

## Deployment

- **Local Setup**: Redis and Kafka are used locally to simulate a production-like environment.
- **Docker Setup**: The application is containerized for ease of deployment. This helps in maintaining consistency across different environments (dev, staging, prod).

## Scaling Considerations

- Redis plays a key role in deduplication across instances, ensuring that we only process unique IDs even when requests are spread across multiple application instances behind a load balancer.
- Kafka is used for message processing, ensuring fault tolerance and scaling the message delivery system without worrying about message loss.
- The embedded server in Spring Boot allows efficient handling of multiple concurrent requests.

## Extensions


1. **Switching to POST Requests**:
    - The HTTP client now supports POST requests, with JSON payloads, rather than just GET requests. This was done to make it easier to pass complex data if needed in the future.

2. **Load Balancer Compatibility**:
    - Shared state management is achieved using Redis, which ensures that any instance of the application can participate in deduplication, even when behind a load balancer.

3. **Kafka Integration for Streaming**:
    - We publish the count of unique IDs to a Kafka topic every minute. This allows the system to handle high volumes of data asynchronously, making it scalable and reliable.
