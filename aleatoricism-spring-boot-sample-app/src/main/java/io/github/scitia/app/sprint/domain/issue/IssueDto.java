package io.github.scitia.app.sprint.domain.issue;

import java.util.List;

public record IssueDto(
        String name,
        String description,
        String reporter,
        Integer estimation,
        List<String> acceptanceCriteria
) {
}
