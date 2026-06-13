package io.github.scitia.aleatoricism.chain;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Auto-Configuration for Aleatoricism Framework.
 * Automatically registers the necessary components for automatic HTTP routing
 * based on instructions from Markdown file.
 */
@AutoConfiguration
public class AleatoricismAutoConfiguration {

    /**
     * Create default InstructionProvider if not already defined
     */
    @Bean
    @ConditionalOnMissingBean
    public InstructionProvider instructionProvider() {
        return new MarkdownInstructionProvider();
    }

    /**
     * Register the automatic HTTP router
     * Requires HttpRequestHandler to be provided by the application
     */
    @Bean
    @ConditionalOnMissingBean
    public AutomaticHttpRouter automaticHttpRouter(
            InstructionProvider instructionProvider,
            HttpRequestHandler requestHandler) {
        return new AutomaticHttpRouter(instructionProvider, requestHandler);
    }

}

