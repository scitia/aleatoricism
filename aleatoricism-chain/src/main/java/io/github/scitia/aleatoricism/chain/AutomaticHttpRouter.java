package io.github.scitia.aleatoricism.chain;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Automatic HTTP Router for Aleatoric framework.
 *
 * This controller intercepts ALL HTTP requests (GET, POST, PUT, PATCH, DELETE)
 * on ANY path and passes them to a request handler.
 *
 * The handler (typically AleatoricChainController) will delegate to the AI model
 * which decides the appropriate business operation based on the HTTP method and path.
 *
 * The routing behavior is defined in the 'aleatoric-instructions.md' file in the project's resources.
 */
@RestController
@ConditionalOnProperty(
    name = "aleatoricism.auto-router.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class AutomaticHttpRouter {

    private final InstructionProvider instructionProvider;
    private final HttpRequestHandler requestHandler;

    public AutomaticHttpRouter(InstructionProvider instructionProvider, HttpRequestHandler requestHandler) {
        this.instructionProvider = instructionProvider;
        this.requestHandler = requestHandler;
    }

    @GetMapping("/**")
    public ResponseEntity<?> handleGet(HttpServletRequest request) {
        return handle(request, "GET");
    }

    @PostMapping("/**")
    public ResponseEntity<?> handlePost(HttpServletRequest request) {
        return handle(request, "POST");
    }

    @PutMapping("/**")
    public ResponseEntity<?> handlePut(HttpServletRequest request) {
        return handle(request, "PUT");
    }

    @PatchMapping("/**")
    public ResponseEntity<?> handlePatch(HttpServletRequest request) {
        return handle(request, "PATCH");
    }

    @DeleteMapping("/**")
    public ResponseEntity<?> handleDelete(HttpServletRequest request) {
        return handle(request, "DELETE");
    }

    /**
     * Core handler for all HTTP methods
     */
    private ResponseEntity<?> handle(HttpServletRequest request, String method) {
        try {
            HttpRequestContext context = new HttpRequestContext(
                    method,
                    request.getRequestURI(),
                    extractQueryParams(request),
                    extractHeaders(request),
                    extractBody(request)
            );

            return requestHandler.handle(context, instructionProvider.getInstructions());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "error", "Request processing failed",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * Extract query parameters from request
     */
    private Map<String, String> extractQueryParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            String[] pairs = request.getQueryString().split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                params.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
            }
        }
        return params;
    }

    /**
     * Extract headers from request
     */
    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    /**
     * Extract body from request
     */
    private String extractBody(HttpServletRequest request) throws IOException {
        if ("GET".equalsIgnoreCase(request.getMethod()) ||
            "DELETE".equalsIgnoreCase(request.getMethod())) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

}

