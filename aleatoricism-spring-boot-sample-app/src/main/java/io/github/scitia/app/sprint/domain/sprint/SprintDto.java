package io.github.scitia.app.sprint.domain.sprint;

import io.github.scitia.app.sprint.domain.issue.IssueDto;

import java.util.List;

public record SprintDto(Long id, String name, String goal, List<IssueDto> issues) {
}
