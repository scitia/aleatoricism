package io.github.scitia.app.sprint.api;

import java.util.List;

public record SprintPlanningRequest(String name, String goal, List<IssueDto> issues) {
}

