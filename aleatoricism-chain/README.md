# aleatoricism-chain

`aleatoricism-chain` provides HTTP entrypoints and routing helpers that translate incoming requests into a command-like context that can be executed by an agent or business handler.

## Purpose

- Accept HTTP requests on wildcard endpoints.
- Convert requests into a structured context (method, path, headers, query, body).
- Load instruction text from `aleatoricism.md` and pass it to a handler.

## Core types

- `AutomaticHttpRouter`
  - A `@RestController` that intercepts **all** HTTP methods on any path.
  - Builds `HttpRequestContext` and delegates to `HttpRequestHandler`.
  - Controlled by `aleatoricism.auto-router.enabled` (default: true).

- `HttpRequestHandler`
  - Strategy interface for handling a request and instruction string.
  - Typically bridges to an agentic execution layer.

- `InstructionProvider` / `MarkdownInstructionProvider`
  - Loads instructions from `aleatoricism.md` in classpath resources.
  - Falls back to a default instruction block if missing.

- `HttpMethodRouter` / `HttpMethodHandler`
  - Alternate routing style: route by HTTP method using an abstract controller.

- `HttpRequestAdapter` / `HttpRequestExtractor`
  - Helpers for mapping `HttpServletRequest` into a consistent payload.

## Architecture

1. `AutomaticHttpRouter` intercepts a request.
2. It builds a `HttpRequestContext` with method, path, headers, query, and body.
3. It reads instructions from `InstructionProvider`.
4. It delegates to your `HttpRequestHandler` implementation.

This keeps routing lightweight while allowing your handler to decide how to interpret the request based on instruction content.

## Configuration

```yaml
aleatoricism:
  auto-router:
    enabled: true
```

Place `aleatoricism.md` in `src/main/resources` to customize the routing instructions used by the handler.

## Extension points

- Provide your own `InstructionProvider` for dynamic or remote instruction sources.
- Implement `HttpRequestHandler` to translate requests into flows or tool calls.
- Use `HttpMethodRouter` when you want method-based routing without a single catch-all controller.

## Related modules

- `aleatoricism-flows` for typed business flow execution.
- `aleatoricism-spring-boot-autoconfigure` and `aleatoricism-spring-boot-starter` for Spring Boot integration.

