package io.github.scitia.app.sprint.mapper;

import io.github.scitia.app.sprint.api.IssueDto;
import io.github.scitia.app.sprint.domain.Issue;
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
