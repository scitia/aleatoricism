package io.github.scitia.config.aleatoric.tools;

import com.github.copilot.sdk.json.ToolInvocation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AgenticBusinessTool {

    String toolName();

    String description();

    Map<String, Object> inputSchema();

    CompletableFuture<Object> invoke(ToolInvocation invocation);
}
