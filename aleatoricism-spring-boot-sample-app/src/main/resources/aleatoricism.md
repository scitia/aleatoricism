# Scrum Processes - Aleatoricism Flow Router

## Overview

This application implements three core Scrum processes as agentic workflows. The HTTP router will analyze requests
and route them to the appropriate Scrum process based on the intent and path.

## Scrum Processes and Endpoints

### 1. Sprint Planning
- **Path**: `/scrum/planning` or `/scrum/sprint/{sprintId}/planning`
- **Method**: POST
- **Purpose**: Plan a sprint by selecting backlog items, estimating workload, and validating against team capacity
- **Tool**: `scrum_sprint_planning`
- **Practical aspects**:
  - Validates story points against team capacity
  - Detects capacity overflow situations
  - Commits specific backlog items to sprint
  - Ensures realistic sprint goals
  - Emits audit trail for commitment tracking

## Request Format

All Scrum process requests should:
- Use **POST** method
- Include a **JSON body** with process-specific parameters
- Follow the input schema defined by each tool handler

### Example Request Paths:
```
POST /scrum/planning
POST /scrum/sprint/550e8400-e29b-41d4-a716-446655440000/planning
```

## Response Format

All endpoints return a JSON response with:
- ID of the created/processed entity
- Status of the operation
- Relevant metadata for the Scrum process
- Audit trail information

## Tool Availability

Available Scrum tools:
- `scrum_sprint_planning` - Plan sprint backlog and validate capacity

## Error Handling

If the AI model cannot safely execute a request:
- Return a clear error message describing the issue
- Do not attempt to execute if the intent is unclear
- Always validate that required parameters are present
- Provide helpful feedback on missing or invalid data

## Workflow Stages

Each Scrum process follows this pattern:
1. **Normalize** - Sanitize input, apply defaults, handle missing values
2. **Build** - Create domain object with calculated values
3. **Audit** - Emit event for process tracking and monitoring

---

**Note**: Modify this file to customize Scrum process handling. Restart the application for changes to take effect.

For benchmarking and performance testing, all three processes are instrumented with audit trails.

