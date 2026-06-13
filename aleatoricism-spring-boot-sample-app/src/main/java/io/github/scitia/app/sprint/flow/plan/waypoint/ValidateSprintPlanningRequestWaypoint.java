package io.github.scitia.app.sprint.flow.plan.waypoint;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.domain.issue.IssueDto;
import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ValidateSprintPlanningRequestWaypoint implements Waypoint<SprintPlanningRequest, SprintPlanningRequest, SprintPlanningStore> {

    @Override
    public SprintPlanningRequest handle(SprintPlanningRequest input, ExecutionContext<SprintPlanningStore> context) {
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

        context.getStore().setSprintPlanningRequest(input);

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

