package io.github.scitia.app.sprint.domain.sprint;

import io.github.scitia.app.sprint.domain.issue.IssueDto;
import io.github.scitia.app.sprint.domain.issue.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SprintMapper {

    private final IssueMapper issueMapper;

    public SprintDto mapToDto(Sprint sprint) {

        List<IssueDto> issues = sprint.getIssues()
                .stream()
                .map(issueMapper::mapToDto)
                .toList();

        return new SprintDto(sprint.getId(), sprint.getName(), sprint.getGoal(), issues);
    }
}
