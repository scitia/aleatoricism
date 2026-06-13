package io.github.scitia.aleatoricism.chain;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Implementation of InstructionProvider that reads instructions from a Markdown file.
 * Looks for 'aleatoric-instructions.md' in the classpath resources.
 */
public class MarkdownInstructionProvider implements InstructionProvider {

    private static final String INSTRUCTION_FILE = "aleatoricism.md";
    private String cachedInstructions;

    @Override
    public String getInstructions() {
        if (cachedInstructions != null) {
            return cachedInstructions;
        }

        try {
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(INSTRUCTION_FILE);

            if (inputStream == null) {
                return getDefaultInstructions();
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                cachedInstructions = reader.lines().collect(Collectors.joining("\n"));
                return cachedInstructions;
            }

        } catch (Exception e) {
            return getDefaultInstructions();
        }
    }

    /**
     * Default instructions if markdown file not found
     */
    private String getDefaultInstructions() {
        return """
                # Aleatoric HTTP Router Instructions
               \s
                This is the default instruction set.\s
                To customize, create 'aleatoricism.md' in your project's resources folder.
               \s
                ## HTTP Method Handling
               \s
                The router will automatically handle:
                - GET: Retrieve resources
                - POST: Create resources
                - PUT: Replace resources
                - PATCH: Update resources
                - DELETE: Remove resources
               \s
                Each request will be processed by the AI model which will decide the appropriate business operation.
               \s""";
    }

}

