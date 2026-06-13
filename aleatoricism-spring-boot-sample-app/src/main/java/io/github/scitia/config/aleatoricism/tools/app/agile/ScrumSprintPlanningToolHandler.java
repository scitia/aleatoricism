package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.SprintPlanningRequest;
import io.github.scitia.app.agile.domain.sprintplanning.SprintPlanning;
import io.github.scitia.app.agile.mapper.SprintPlanningDetailResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class ScrumSprintPlanningToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final Flow<SprintPlanningRequest, SprintPlanning> planningFlow;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScrumSprintPlanningToolHandler(
            FlowEngine flowEngine,
            Flow<SprintPlanningRequest, SprintPlanning> enhancedSprintPlanningFlow) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
        this.planningFlow = Objects.requireNonNull(enhancedSprintPlanningFlow, "planningFlow cannot be null");
    }

    @Override
    public String toolName() {
        return "scrum_sprint_planning";
    }

    @Override
    public String description() {
        return "Plan a sprint: select backlog items, estimate workload, and validate against team capacity with overflow detection";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "sprintId", Map.of("type", "string", "format", "uuid"),
                        "backlogItemIds", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string"),
                                "description", "IDs of backlog items to plan for sprint"
                        ),
                        "plannedStoryPoints", Map.of("type", "integer", "description", "Total story points to plan"),
                        "capacity", Map.of("type", "integer", "description", "Team capacity in story points"),
                        "team", Map.of("type", "string")
                ),
                "required", List.of("sprintId", "backlogItemIds", "plannedStoryPoints", "capacity", "team")
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'scrum_sprint_planning' requires arguments payload");
            }

            SprintPlanningRequest request = objectMapper.convertValue(arguments, SprintPlanningRequest.class);
            var planning = flowEngine.run(planningFlow, request);
            return SprintPlanningDetailResponseMapper.INSTANCE.apply(planning);
        });
    }
}

