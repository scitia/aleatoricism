package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.SprintKickoffRequest;
import io.github.scitia.app.agile.domain.sprintkickoff.SprintKickoff;
import io.github.scitia.app.agile.mapper.SprintKickoffResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class ScrumSprintKickoffToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final Flow<SprintKickoffRequest, SprintKickoff> kickoffFlow;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScrumSprintKickoffToolHandler(
            FlowEngine flowEngine,
            Flow<SprintKickoffRequest, SprintKickoff> enhancedSprintKickoffFlow) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
        this.kickoffFlow = Objects.requireNonNull(enhancedSprintKickoffFlow, "kickoffFlow cannot be null");
    }

    @Override
    public String toolName() {
        return "scrum_sprint_kickoff";
    }

    @Override
    public String description() {
        return "Start a sprint kickoff: formally begin the sprint with team alignment on goals, confirm committed work, and schedule sprint activities";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "sprintId", Map.of("type", "string", "format", "uuid"),
                        "sprintName", Map.of("type", "string"),
                        "goal", Map.of("type", "string", "description", "Main sprint goal"),
                        "team", Map.of("type", "string"),
                        "committedItems", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string"),
                                "description", "Backlog item IDs committed for this sprint"
                        ),
                        "totalStoryPoints", Map.of("type", "integer", "description", "Total story points committed"),
                        "kickoffDate", Map.of("type", "string", "format", "date"),
                        "kickoffTime", Map.of("type", "string", "format", "time"),
                        "expectedTeamSize", Map.of("type", "integer", "description", "Expected number of team participants"),
                        "facilitator", Map.of("type", "string", "description", "Person facilitating the kickoff")
                ),
                "required", List.of("sprintId", "sprintName", "goal", "team", "committedItems", "totalStoryPoints", "kickoffDate", "kickoffTime", "expectedTeamSize", "facilitator")
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'scrum_sprint_kickoff' requires arguments payload");
            }

            SprintKickoffRequest request = objectMapper.convertValue(arguments, SprintKickoffRequest.class);
            var kickoff = flowEngine.run(kickoffFlow, request);
            return SprintKickoffResponseMapper.INSTANCE.apply(kickoff);
        });
    }
}

