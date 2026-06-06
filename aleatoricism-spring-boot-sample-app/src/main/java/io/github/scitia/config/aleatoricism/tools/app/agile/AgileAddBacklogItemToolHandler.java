package io.github.scitia.config.aleatoricism.tools.app.agile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.app.agile.api.request.BacklogItemRequest;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;
import io.github.scitia.app.agile.flows.AgileFlows;
import io.github.scitia.app.agile.mapper.BacklogItemResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class AgileAddBacklogItemToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgileAddBacklogItemToolHandler(FlowEngine flowEngine) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
    }

    @Override
    public String toolName() {
        return "agile_add_backlog_item";
    }

    @Override
    public String description() {
        return "Add a backlog item with initial metadata";
    }

    @Override
    public Map<String, Object> inputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "title", Map.of("type", "string"),
                        "description", Map.of("type", "string"),
                        "type", Map.of("type", "string"),
                        "storyPoints", Map.of("type", "integer"),
                        "priority", Map.of("type", "integer"),
                        "reporter", Map.of("type", "string")
                ),
                "required", List.of("title", "description", "type", "storyPoints", "priority", "reporter")
        );
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'agile_add_backlog_item' requires arguments payload");
            }

            BacklogItemRequest request = objectMapper.convertValue(arguments, BacklogItemRequest.class);
            BacklogItem item = flowEngine.run(AgileFlows.BACKLOG_ITEM_FLOW, request);
            return BacklogItemResponseMapper.INSTANCE.apply(item);
        });
    }
}

