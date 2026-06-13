package io.github.scitia.app.sprint.flow.plan.waypoint;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.domain.issue.Issue;
import io.github.scitia.app.sprint.domain.sprint.Sprint;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;

public class AddIssuesToSprintWaypoint implements Waypoint<Sprint, Sprint, SprintPlanningStore> {

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext<SprintPlanningStore> context) {
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

