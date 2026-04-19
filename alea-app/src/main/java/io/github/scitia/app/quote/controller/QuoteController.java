package io.github.scitia.app.quote.controller;

import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.app.quote.api.request.QuoteRequest;
import io.github.scitia.app.quote.api.response.QuoteResponse;
import io.github.scitia.app.quote.flows.QuoteFlows;
import io.github.scitia.app.quote.mapper.QuoteResponseMapper;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuoteController {

    private final FlowEngine flowEngine;
    private static final QuoteResponseMapper responseMapper = QuoteResponseMapper.INSTANCE;

    public QuoteController(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @PostMapping(path = "/evaluate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteResponse evaluate(@RequestBody QuoteRequest request) {
        return responseMapper.apply(
            flowEngine.run(QuoteFlows.QUOTE_EVALUATION_FLOW, request)
        );
    }
}
