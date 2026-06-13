package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.app.sprint.domain.sprint.SprintDto;
import io.github.scitia.config.aleatoricism.tools.app.agile.AgileSprintPlanningToolHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AgileSprintPlanningToolHandlerTest {

    @Autowired
    private AgileSprintPlanningToolHandler handler;

    @Test
    void shouldAddBacklogItemByToolInvocation() {
        ToolInvocation invocation = new ToolInvocation()
                .setToolName("agile_sprint_planning")
                .setArguments(new ObjectMapper()
                        .valueToTree(
                                Map.of(
                                        "name", "Name",
                                        "goal", "Goal",
                                        "issues", List.of(
                                                Map.of(
                                                        "name", "Issue 1",
                                                        "description", "Description 1",
                                                        "reporter", "Reporter 1",
                                                        "estimation", 5,
                                                        "acceptanceCriteria", List.of("Criterion 1")
                                                )
                                        )
                                )
                        )
                );

        Object result = handler.invoke(invocation).join();

        SprintDto response = (SprintDto) result;
        assertNotNull(response.id());
        assertEquals("Name", response.name());
        assertEquals("Goal", response.goal());
        assertFalse(response.issues().isEmpty());
    }
}

