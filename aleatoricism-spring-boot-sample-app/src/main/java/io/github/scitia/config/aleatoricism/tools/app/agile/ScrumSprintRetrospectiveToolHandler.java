package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.SprintRetrospectiveRequest;
import io.github.scitia.app.agile.domain.retrospective.SprintRetrospective;
import io.github.scitia.app.agile.mapper.SprintRetrospectiveResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class ScrumSprintRetrospectiveToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final Flow<SprintRetrospectiveRequest, SprintRetrospective> retrospectiveFlow;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScrumSprintRetrospectiveToolHandler(
            FlowEngine flowEngine,
            Flow<SprintRetrospectiveRequest, SprintRetrospective> enhancedSprintRetrospectiveFlow) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
        this.retrospectiveFlow = Objects.requireNonNull(enhancedSprintRetrospectiveFlow, "retrospectiveFlow cannot be null");
    }

    @Override
    public String toolName() {
        return "scrum_sprint_retrospective";
    }

    @Override
    public String description() {
        return "Conduct a sprint retrospective: collect team feedback on what went well, what could improve, and generate action items";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "sprintId", Map.of("type", "string", "format", "uuid"),
                        "sprintName", Map.of("type", "string"),
                        "team", Map.of("type", "string"),
                        "whatWentWell", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string"),
                                "description", "Positive outcomes and successes from the sprint"
                        ),
                        "whatCouldImprove", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string"),
                                "description", "Areas for improvement and challenges faced"
                        ),
                        "actionItems", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string"),
                                "description", "Concrete actions to be taken in future sprints"
                        ),
                        "participantCount", Map.of("type", "integer", "description", "Number of team members participating"),
                        "retrospectiveDate", Map.of("type", "string", "format", "date")
                ),
                "required", List.of("sprintId", "sprintName", "team", "whatWentWell", "whatCouldImprove", "actionItems", "participantCount", "retrospectiveDate")
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'scrum_sprint_retrospective' requires arguments payload");
            }

            SprintRetrospectiveRequest request = objectMapper.convertValue(arguments, SprintRetrospectiveRequest.class);
            var retrospective = flowEngine.run(retrospectiveFlow, request);
            return SprintRetrospectiveResponseMapper.INSTANCE.apply(retrospective);
        });
    }
}

