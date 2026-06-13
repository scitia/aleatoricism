package io.github.scitia.aleatoricism.chain;

import java.util.Map;

/**
 * Context of an incoming HTTP request.
 * Contains all necessary information for the RequestHandler to process the request.
 */
public record HttpRequestContext(
        String method,
        String path,
        Map<String, String> queryParams,
        Map<String, String> headers,
        String body
) {
}

