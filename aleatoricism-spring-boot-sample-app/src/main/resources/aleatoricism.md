# Scrum Processes - Aleatoricism Flow Router

## Overview

This application implements three core Scrum processes as agentic workflows. The HTTP router will analyze requests
and route them to the appropriate Scrum process based on the intent and path.

## Scrum Processes and Endpoints

### 1. Sprint Retrospective
- **Path**: `/scrum/retrospective` or `/scrum/sprint/{sprintId}/retrospective`
- **Method**: POST
- **Purpose**: Conduct a sprint retrospective - collect team feedback on what went well, what could improve, and generate action items
- **Tool**: `scrum_sprint_retrospective`
- **Practical aspects**:
  - Gathers team feedback on sprint execution
  - Identifies improvements for next sprint
  - Generates actionable items based on challenges
  - Estimates retrospective duration based on participant count
  - Emits audit trail for process tracking

### 2. Sprint Planning
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

### 3. Sprint Kickoff (Rozpoczynanie Sprintu)
- **Path**: `/scrum/kickoff` or `/scrum/sprint/{sprintId}/kickoff`
- **Method**: POST
- **Purpose**: Formally begin the sprint with team alignment on goals and confirm committed work
- **Tool**: `scrum_sprint_kickoff`
- **Practical aspects**:
  - Confirms team availability and participation
  - Aligns team on sprint goal and objectives
  - Reviews committed backlog items
  - Schedules sprint activities (daily standup, etc.)
  - Sets expectations for sprint duration
  - Emits audit trail for sprint initiation

## Request Format

All Scrum process requests should:
- Use **POST** method
- Include a **JSON body** with process-specific parameters
- Follow the input schema defined by each tool handler

### Example Request Paths:
```
POST /scrum/retrospective
POST /scrum/planning
POST /scrum/kickoff
POST /scrum/sprint/550e8400-e29b-41d4-a716-446655440000/retrospective
POST /scrum/sprint/550e8400-e29b-41d4-a716-446655440000/planning
POST /scrum/sprint/550e8400-e29b-41d4-a716-446655440000/kickoff
```

## Response Format

All endpoints return a JSON response with:
- ID of the created/processed entity
- Status of the operation
- Relevant metadata for the Scrum process
- Audit trail information

## Tool Availability

Available Scrum tools:
- `scrum_sprint_retrospective` - Conduct retrospective with feedback collection
- `scrum_sprint_planning` - Plan sprint backlog and validate capacity
- `scrum_sprint_kickoff` - Start sprint with team alignment

Legacy tools (if applicable):
- `agile_create_sprint` - Create sprint summary
- `agile_plan_sprint` - Plan backlog items with capacity check
- `agile_add_backlog_item` - Register backlog item

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

