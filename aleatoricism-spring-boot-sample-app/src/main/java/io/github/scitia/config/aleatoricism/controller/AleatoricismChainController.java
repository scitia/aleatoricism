package io.github.scitia.config.aleatoricism.controller;

import io.github.scitia.config.aleatoricism.command.AleatoricCommandRequest;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandResponse;
import io.github.scitia.config.aleatoricism.core.AleatoricCopilotGateway;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/aleachain", produces = MediaType.APPLICATION_JSON_VALUE)
public class AleatoricismChainController {

    private final AleatoricCopilotGateway gateway;

    public AleatoricismChainController(AleatoricCopilotGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse post(@Valid @RequestBody AleatoricCommandRequest request) {
        return gateway.execute(request);
    }

    @PatchMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse patch(@Valid @RequestBody AleatoricCommandRequest request) {
        return gateway.execute(request);
    }

    @DeleteMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse delete(@Valid @RequestBody AleatoricCommandRequest request) {
        return gateway.execute(request);
    }

    @GetMapping(path = "/invoke")
    public AleatoricCommandResponse get(@RequestParam Map<String, String> queryParams) {
        AleatoricCommandRequest request = new AleatoricCommandRequest(
                queryParams.getOrDefault("intent", ""),
                queryParams.getOrDefault("instructions", ""),
                Map.copyOf(queryParams),
                Map.of("entrypoint", "GET")
        );
        return gateway.execute(request);
    }
}
