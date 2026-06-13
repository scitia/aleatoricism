package io.github.scitia.app.sprint.flow.plan;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.domain.Issue;
import io.github.scitia.app.sprint.domain.Sprint;
import io.github.scitia.app.sprint.flow.SprintFlows;

public class AddIssuesToSprintWaypoint implements Waypoint<Sprint, Sprint, SprintFlows.ExampleStore> {

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext<SprintFlows.ExampleStore> context) {
        context.getStore().getSprintPlanningRequest().issues().forEach(issueDto -> {
            Issue issue = new Issue();
            issue.setName(issueDto.name());
            issue.setDescription(issueDto.description());
            issue.setReporter(issueDto.reporter());
            issue.setEstimation(issueDto.estimation());
            issue.setAcceptanceCriteria(issueDto.acceptanceCriteria());
            sprint.addIssue(issue);
        });
        return sprint;
    }
}

