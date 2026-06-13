package io.github.scitia.app.sprint.flow.api;

import io.github.scitia.app.sprint.domain.issue.IssueDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SprintPlanningRequest(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String goal,
        @NotEmpty List<IssueDto> issues
) {
}

