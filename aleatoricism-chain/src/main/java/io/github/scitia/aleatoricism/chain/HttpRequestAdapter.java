package io.github.scitia.aleatoricism.chain;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Adapter for converting HTTP requests to command/payload format
 */
public class HttpRequestAdapter {

    /**
     * Convert HTTP request to a command-like structure
     */
    public static Map<String, Object> adaptRequest(HttpServletRequest request) throws IOException {
        String path = HttpRequestExtractor.extractPath(request);
        String method = request.getMethod();
        Map<String, String> queryParams = HttpRequestExtractor.extractQueryParams(request);
        Map<String, String> headers = HttpRequestExtractor.extractHeaders(request);

        String body = "";
        if (!"GET".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
            body = HttpRequestExtractor.extractBody(request);
        }

        return Map.of(
                "path", path,
                "method", method,
                "queryParams", queryParams,
                "headers", headers,
                "body", body,
                "contentType", request.getContentType() != null ? request.getContentType() : ""
        );
    }

}

