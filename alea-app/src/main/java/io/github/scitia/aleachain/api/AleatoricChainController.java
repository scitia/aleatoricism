package io.github.scitia.aleachain.api;

import io.github.scitia.aleachain.core.AleatoricCopilotGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/aleachain", produces = MediaType.APPLICATION_JSON_VALUE)
public class AleatoricChainController {

    private final AleatoricCopilotGateway gateway;

    public AleatoricChainController(AleatoricCopilotGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse post(@RequestBody AleatoricCommandRequest request) {
        return execute(request);
    }

    @PatchMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse patch(@RequestBody AleatoricCommandRequest request) {
        return execute(request);
    }

    @DeleteMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AleatoricCommandResponse delete(@RequestBody AleatoricCommandRequest request) {
        return execute(request);
    }

    @GetMapping(path = "/invoke")
    public AleatoricCommandResponse get(@RequestParam Map<String, String> queryParams) {
        AleatoricCommandRequest request = new AleatoricCommandRequest(
                queryParams.getOrDefault("intent", ""),
                queryParams.getOrDefault("instructions", ""),
                Map.copyOf(queryParams),
                Map.of("entrypoint", "GET")
        );
        return execute(request);
    }

    private AleatoricCommandResponse execute(AleatoricCommandRequest request) {
        try {
            return gateway.execute(request);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aleatoric chain execution failed", exception);
        }
    }
}
