## Sample App in Agile Domain

This sample app includes domain flows for agile scrum workflows. The agile tools are implemented as flow handlers that can be invoked from the instruction set. The sample app is not published as a library but serves as an example of how to implement domain-specific tools and flows using the Aleatoricism framework.

### Aleatoric Chain and Business Process Execution Model

The tools defined in the application's source code form a collection of process trees (or process forests) in terms of process graph modeling. Individual subprocess components, such as Waypoints and Emission Points, can be shared across multiple business process definitions, enabling reuse and reducing duplication.

There is no strict one-to-one relationship between a business process and a specific REST endpoint. Instead, the chain library is responsible for handling incoming HTTP requests and dynamically selecting the appropriate business process to execute.

The request flow works as follows:

1. An HTTP request is received by the application.
2. The agent layer analyzes the request and identifies the user's intent.
3. Based on the detected intent, the system selects the most suitable business process definition.
4. The selected process is executed using the corresponding tools and subprocesses.

This architecture provides a high degree of flexibility and extensibility. New business processes can be introduced, or existing ones modified, without requiring changes to the HTTP handling layer.

### Defined Tools

The sample application defines the following tools:

- 📄 [`agile_sprint_planning`](src/main/java/io/github/scitia/config/aleatoricism/tools/app/agile/AgileSprintPlanningToolHandler.java) registers a backlog item with initial metadata.

### Sample request to server

```http request
POST http://localhost:8080/sprint/planning
Content-Type: application/json
Accept: application/json

{
  "intent": "plan sprint",
  "instructions": "Use 'agile_sprint_planning' tool",
  "payload": {
    "name": "Example name",
    "goal": "Example goal",
    "issues": [
      {
        "name": "Example Issue Name",
        "description": "Example Issue Description",
        "reporter": "John Doe",
        "estimation": 3,
        "acceptanceCriteria": ["Foo", "Criterion"]
      }
    ]
  }
}
```

### High-Level API Communication

This approach represents a form of high-level API communication. Instead of invoking a specific endpoint tied to a predefined operation, the client expresses an intent together with additional instructions. The server interprets the request, matches it against available business process definitions, and executes the most appropriate tool or workflow.

As a result, the API becomes more adaptive and capable of evolving over time while maintaining a stable external interface.

### How to run?

Application uses library: [`Copilot SDK`](https://github.com/github/copilot-sdk/tree/main/java)

```xml
<dependency>
    <groupId>com.github</groupId>
    <artifactId>copilot-sdk-java</artifactId>
    <version>${copilot.sdk.version}</version>
</dependency>
```

To run the application, you must configure a GitHub Copilot access token as the ```COPILOT_GITHUB_TOKEN``` environment variable.

The application also requires a PostgreSQL database. The database connection settings are defined in the application.yml file. Before starting the application, ensure that the database is running and accessible at the configured address.

You can quickly provision a PostgreSQL instance using Docker:

```yml
version: '3.8'
services:
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_USER: your_username
      POSTGRES_PASSWORD: your_password
      POSTGRES_DB: your_database
    ports:
      - "5432:5432"
```

Once the required values have been configured in application.yml and the database is running, the application can be started using the standard application startup procedure.

### Experimenting with the System

Feel free to experiment with different intents and instructions to observe how the system responds and which tools are invoked in various scenarios. This is a great way to explore the agent-driven workflow selection mechanism and better understand how business processes are dynamically matched to user requests.

