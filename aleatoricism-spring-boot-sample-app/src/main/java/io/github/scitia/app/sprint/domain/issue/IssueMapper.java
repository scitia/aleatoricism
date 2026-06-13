package io.github.scitia.app.sprint.domain.issue;

import org.springframework.stereotype.Component;

@Component
public class IssueMapper {

    public IssueDto mapToDto(Issue issue) {
        return new IssueDto(
                issue.getName(),
                issue.getDescription(),
                issue.getReporter(),
                issue.getEstimation(),
                issue.getAcceptanceCriteria()
        );
    }
}
