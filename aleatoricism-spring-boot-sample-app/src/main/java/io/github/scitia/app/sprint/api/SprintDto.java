package io.github.scitia.app.sprint.api;

import java.util.List;

public record SprintDto(Long id, String name, String goal, List<IssueDto> issues) {
}
