package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.sprint.Sprint;
import io.github.scitia.app.sprint.flow.SprintFlows;
import io.github.scitia.app.sprint.domain.sprint.SprintMapper;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public final class AgileSprintPlanningToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final SprintFlows sprintFlows;
    private final SprintMapper sprintMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toolName() {
        return "agile_sprint_planning";
    }

    @Override
    public String description() {
        return "Add a sprint based on sprint planning";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "name", Map.of("type", "string"),
                        "goal", Map.of("type", "string"),
                        "issues", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "name", "string",
                                        "description", "string",
                                        "reporter", "string",
                                        "estimation", "integer",
                                        "acceptanceCriteria", Map.of(
                                                "type", "array",
                                                "items", Map.of("type", "string")

                                        )
                                )
                        ),
                        "required", List.of("name", "goal")
                )
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'agile_sprint_planning' requires arguments payload");
            }

            SprintPlanningRequest request = objectMapper.convertValue(arguments, SprintPlanningRequest.class);
            Sprint item = flowEngine.run(sprintFlows.sprintPlanningFlow(), request);
            return sprintMapper.mapToDto(item);
        });
    }
}

