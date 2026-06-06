package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.SprintCreateRequest;
import io.github.scitia.app.agile.domain.sprint.Sprint;
import io.github.scitia.app.agile.flows.AgileFlows;
import io.github.scitia.app.agile.mapper.SprintResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class AgileCreateSprintToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgileCreateSprintToolHandler(FlowEngine flowEngine) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
    }

    @Override
    public String toolName() {
        return "agile_create_sprint";
    }

    @Override
    public String description() {
        return "Create a sprint with schedule and capacity";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "name", Map.of("type", "string"),
                        "goal", Map.of("type", "string"),
                        "startDate", Map.of("type", "string"),
                        "endDate", Map.of("type", "string"),
                        "capacity", Map.of("type", "integer"),
                        "team", Map.of("type", "string")
                ),
                "required", List.of("name", "goal", "startDate", "endDate", "capacity", "team")
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'agile_create_sprint' requires arguments payload");
            }

            SprintCreateRequest request = objectMapper.convertValue(arguments, SprintCreateRequest.class);
            Sprint sprint = flowEngine.run(AgileFlows.SPRINT_CREATE_FLOW, request);
            return SprintResponseMapper.INSTANCE.apply(sprint);
        });
    }
}

