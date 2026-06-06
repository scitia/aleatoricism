package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.config.aleatoricism.tools.app.agile.AgilePlanSprintToolHandler;
import io.github.scitia.app.agile.api.response.SprintPlanningResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AgilePlanSprintToolHandlerTest {

    @Autowired
    private AgilePlanSprintToolHandler handler;

    @Test
    void shouldPlanSprintByToolInvocation() {
        ToolInvocation invocation = new ToolInvocation()
                .setToolName("agile_plan_sprint")
                .setArguments(new ObjectMapper().valueToTree(Map.of(
                        "sprintId", "spr-100",
                        "backlogItemIds", List.of("bi-1", "bi-2", "bi-3"),
                        "plannedStoryPoints", 34,
                        "capacity", 30,
                        "team", "alpha"
                )));

        Object result = handler.invoke(invocation).join();

        SprintPlanningResponse response = (SprintPlanningResponse) result;
        assertNotNull(response.sprintId());
        assertEquals("OVERFLOW", response.status());
        assertTrue(response.overflow());
    }
}

