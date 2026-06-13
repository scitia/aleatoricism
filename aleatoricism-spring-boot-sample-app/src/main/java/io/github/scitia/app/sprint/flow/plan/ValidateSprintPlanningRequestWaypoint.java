package io.github.scitia.app.sprint.flow.plan;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.api.IssueDto;
import io.github.scitia.app.sprint.api.SprintPlanningRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ValidateSprintPlanningRequestWaypoint implements Waypoint<SprintPlanningRequest, SprintPlanningRequest> {

    @Override
    public SprintPlanningRequest handle(SprintPlanningRequest input, ExecutionContext context) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Sprint planning request cannot be null.");
        }
        if (!StringUtils.hasText(input.name())) {
            throw new IllegalArgumentException("Sprint name cannot be empty.");
        }
        if (!StringUtils.hasText(input.goal())) {
            throw new IllegalArgumentException("Sprint goal cannot be empty.");
        }
        if (CollectionUtils.isEmpty(input.issues())) {
            throw new IllegalArgumentException("Sprint must have at least one issue.");
        }
        for (IssueDto issue : input.issues()) {
            validateIssue(issue);
        }
        return input;
    }

    private void validateIssue(IssueDto issue) {
        if (issue == null) {
            throw new IllegalArgumentException("Issue cannot be null.");
        }
        if (!StringUtils.hasText(issue.name())) {
            throw new IllegalArgumentException("Issue name cannot be empty.");
        }
        if (!StringUtils.hasText(issue.description())) {
            throw new IllegalArgumentException("Issue description cannot be empty.");
        }
        if (!StringUtils.hasText(issue.reporter())) {
            throw new IllegalArgumentException("Issue reporter cannot be empty.");
        }
        if (issue.estimation() == null || issue.estimation() <= 0) {
            throw new IllegalArgumentException("Issue estimation must be a positive number.");
        }
        if (CollectionUtils.isEmpty(issue.acceptanceCriteria())) {
            throw new IllegalArgumentException("Issue must have at least one acceptance criterion.");
        }
    }
}

