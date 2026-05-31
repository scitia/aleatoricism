package io.github.scitia.config.engine;

import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.aleatoricism.flows.execution.ExecutionOptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowEngineConfiguration {

    @Bean
    FlowEngine flowEngine() {
        return new FlowEngine(ExecutionOptions.waitForAll());
    }
}
