# Aleatoricism Spring Boot Sample App

This sample app includes domain flows for quotes and agile scrum workflows. The agile package mirrors the quote structure with request/response models, flow definitions, and tool handlers.

## Agile tools

- `agile_sprint_planning` registers a backlog item with initial metadata.

### Sample payloads

```json
{
  "name": "Sprint 12",
  "goal": "Ship planning improvements",
  "startDate": "2026-06-01",
  "endDate": "2026-06-14",
  "capacity": 30,
  "team": "alpha"
}

