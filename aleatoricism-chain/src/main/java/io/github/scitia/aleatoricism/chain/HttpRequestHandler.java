package io.github.scitia.aleatoricism.chain;

import org.springframework.http.ResponseEntity;

/**
 * Handler for HTTP requests in the Aleatoric framework.
 *
 * Responsible for processing the HTTP request context and instructions,
 * then delegating to the appropriate business logic (typically via AI model).
 */
public interface HttpRequestHandler {

    /**
     * Handle an HTTP request within the context of Aleatoric instructions.
     *
     * @param context the HTTP request context (method, path, params, headers, body)
     * @param instructions the instructions from the markdown file
     * @return Spring ResponseEntity with the result
     */
    ResponseEntity<?> handle(HttpRequestContext context, String instructions);

}

