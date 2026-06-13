package io.github.scitia.app.sprint.flow.api;

import io.github.scitia.app.sprint.domain.issue.IssueDto;

import java.util.List;

public record SprintPlanningRequest(String name, String goal, List<IssueDto> issues) {
}

