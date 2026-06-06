package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.config.aleatoricism.tools.app.agile.AgileAddBacklogItemToolHandler;
import io.github.scitia.app.agile.api.response.BacklogItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AgileAddBacklogItemToolHandlerTest {

    @Autowired
    private AgileAddBacklogItemToolHandler handler;

    @Test
    void shouldAddBacklogItemByToolInvocation() {
        ToolInvocation invocation = new ToolInvocation()
                .setToolName("agile_add_backlog_item")
                .setArguments(new ObjectMapper().valueToTree(Map.of(
                        "title", "Implement sprint board",
                        "description", "Add basic sprint board with columns",
                        "type", "story",
                        "storyPoints", 5,
                        "priority", 2,
                        "reporter", "product-owner"
                )));

        Object result = handler.invoke(invocation).join();

        BacklogItemResponse response = (BacklogItemResponse) result;
        assertNotNull(response.itemId());
        assertEquals("NEW", response.status());
        assertEquals(2, response.priority());
    }
}

