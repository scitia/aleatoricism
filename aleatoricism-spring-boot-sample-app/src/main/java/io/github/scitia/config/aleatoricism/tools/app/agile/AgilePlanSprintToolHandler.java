package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.SprintPlanningRequest;
import io.github.scitia.app.agile.flows.AgileFlows;
import io.github.scitia.app.agile.mapper.SprintPlanningResponseMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class AgilePlanSprintToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgilePlanSprintToolHandler(FlowEngine flowEngine) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
    }

    @Override
    public String toolName() {
        return "agile_plan_sprint";
    }

    @Override
    public String description() {
        return "Plan sprint backlog and validate capacity";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "sprintId", Map.of("type", "string"),
                        "backlogItemIds", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string")
                        ),
                        "plannedStoryPoints", Map.of("type", "integer"),
                        "capacity", Map.of("type", "integer"),
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
                throw new IllegalArgumentException("Tool 'agile_plan_sprint' requires arguments payload");
            }

            SprintPlanningRequest request = objectMapper.convertValue(arguments, SprintPlanningRequest.class);
            return SprintPlanningResponseMapper.INSTANCE.apply(flowEngine.run(AgileFlows.SPRINT_PLANNING_FLOW, request));
        });
    }
}

