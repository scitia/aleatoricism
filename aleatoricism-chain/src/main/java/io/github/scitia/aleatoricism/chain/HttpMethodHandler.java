package io.github.scitia.aleatoricism.chain;

import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Handler for HTTP method-based routing.
 * Implementations should handle various HTTP methods and delegate to appropriate business logic.
 */
public interface HttpMethodHandler {

    /**
     * Handle HTTP GET request
     */
    ResponseEntity<?> handleGet(HttpServletRequest request);

    /**
     * Handle HTTP POST request
     */
    ResponseEntity<?> handlePost(HttpServletRequest request);

    /**
     * Handle HTTP PUT request
     */
    ResponseEntity<?> handlePut(HttpServletRequest request);

    /**
     * Handle HTTP PATCH request
     */
    ResponseEntity<?> handlePatch(HttpServletRequest request);

    /**
     * Handle HTTP DELETE request
     */
    ResponseEntity<?> handleDelete(HttpServletRequest request);

}

