package io.github.scitia.app.sprint.domain.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record IssueDto(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String description,
        @NotNull @NotBlank String reporter,
        @Positive Integer estimation,
        @NotEmpty List<String> acceptanceCriteria
) {
}
