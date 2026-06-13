package io.github.scitia.aleatoricism.autoconfigure;

import io.github.scitia.aleatoricism.chain.AutomaticHttpRouter;
import io.github.scitia.aleatoricism.chain.HttpRequestHandler;
import io.github.scitia.aleatoricism.chain.InstructionProvider;
import io.github.scitia.aleatoricism.chain.MarkdownInstructionProvider;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.aleatoricism.flows.execution.ExecutionOptions;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({FlowEngine.class, AutomaticHttpRouter.class})
@EnableConfigurationProperties(AleatoricismProperties.class)
public class AleatoricismAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    InstructionProvider instructionProvider() {
        return new MarkdownInstructionProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    FlowEngine flowEngine(AleatoricismProperties properties) {
        ExecutionOptions options = properties.getFlow().isWaitForSideEffects()
                ? ExecutionOptions.waitForAll()
                : ExecutionOptions.defaults();
        return new FlowEngine(options);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnBean(HttpRequestHandler.class)
    @ConditionalOnMissingBean
    AutomaticHttpRouter automaticHttpRouter(
            InstructionProvider instructionProvider,
            HttpRequestHandler requestHandler) {
        return new AutomaticHttpRouter(instructionProvider, requestHandler);
    }
}

