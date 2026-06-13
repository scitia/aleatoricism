package io.github.scitia.app.sprint.flow.plan;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.api.IssueDto;
import io.github.scitia.app.sprint.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.Issue;
import io.github.scitia.app.sprint.domain.Sprint;

public class AddIssuesToSprintWaypoint implements Waypoint<Sprint, Sprint> {

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext context) throws Exception {
        SprintPlanningRequest request = context.get("sprintRequest", SprintPlanningRequest.class)
                .orElseThrow(() -> new IllegalStateException("SprintPlanningRequest not found in execution context"));

        for (IssueDto issueDto : request.issues()) {
            Issue issue = new Issue();
            issue.setName(issueDto.name());
            issue.setDescription(issueDto.description());
            issue.setReporter(issueDto.reporter());
            issue.setEstimation(issueDto.estimation());
            issue.setAcceptanceCriteria(issueDto.acceptanceCriteria());
            sprint.addIssue(issue);
        }
        return sprint;
    }
}

