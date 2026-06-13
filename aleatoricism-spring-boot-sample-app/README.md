# Aleatoricism Spring Boot Sample App

This sample app includes domain flows for quotes and agile scrum workflows. The agile package mirrors the quote structure with request/response models, flow definitions, and tool handlers.

## Agile tools

- `agile_sprint_planning` registers a backlog item with initial metadata.

### Sample payloads

```json
{
  "name": "Name",
  "goal": "Goal",
  "issues": [
    {
      "name": "Issue 1",
      "description": "Description 1",
      "reporter": "Reporter 1",
      "estimation": 5,
      "acceptanceCriteria": ["Criterion 1"]
    }
  ]
}
```

