package io.github.scitia.aleatoricism.chain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Abstract REST controller that routes HTTP requests based on method rather than specific endpoints.
 * All HTTP methods (GET, POST, PUT, PATCH, DELETE) are handled dynamically.
 * Concrete implementations must provide the handler logic.
 */
@RestController
public abstract class HttpMethodRouter {

    protected abstract HttpMethodHandler getHandler();

    /**
     * Wildcard GET handler - routes to handler implementation
     */
    @GetMapping("/**")
    public ResponseEntity<?> get(HttpServletRequest request) {
        return getHandler().handleGet(request);
    }

    /**
     * Wildcard POST handler - routes to handler implementation
     */
    @PostMapping("/**")
    public ResponseEntity<?> post(HttpServletRequest request) {
        return getHandler().handlePost(request);
    }

    /**
     * Wildcard PUT handler - routes to handler implementation
     */
    @PutMapping("/**")
    public ResponseEntity<?> put(HttpServletRequest request) {
        return getHandler().handlePut(request);
    }

    /**
     * Wildcard PATCH handler - routes to handler implementation
     */
    @PatchMapping("/**")
    public ResponseEntity<?> patch(HttpServletRequest request) {
        return getHandler().handlePatch(request);
    }

    /**
     * Wildcard DELETE handler - routes to handler implementation
     */
    @DeleteMapping("/**")
    public ResponseEntity<?> delete(HttpServletRequest request) {
        return getHandler().handleDelete(request);
    }

}

