# Aleatoricism

Aleatoricism is a framework for building agentic, flow-based business operations with Spring Boot integration. It is composed of small core libraries and a Spring Boot starter.

## Modules

- `aleatoricism-chain` - HTTP routing and instruction-driven execution entrypoints.
- `aleatoricism-flows` - typed flow runtime, waypoints, and execution engine.
- `aleatoricism-spring-boot-autoconfigure` - auto-configuration and properties wiring.
- `aleatoricism-spring-boot-starter` - starter dependencies for Spring Boot apps.
- `aleatoricism-bom` - Bill of Materials for consistent dependency versions.
- `aleatoricism-spring-boot-sample-app` - example application (not published).

## Architecture overview

- Requests enter through HTTP routers in `aleatoricism-chain` and are converted into a command-like context.
- Instructions are loaded from `aleatoricism.md` and provided to a request handler.
- Business operations are modeled as typed flows in `aleatoricism-flows` using waypoints and emission points.
- A `FlowEngine` executes the flow graph with an execution context that controls concurrency and side effects.

For details, see module docs:

- `D:\Development\ideas\aleatoricism-project\aleatoricism-chain\README.md`
- `D:\Development\ideas\aleatoricism-project\aleatoricism-flows\README.md`

## Quick start (Spring Boot)

1. Add the starter dependency.
2. Provide a `HttpRequestHandler` implementation.
3. Create `aleatoricism.md` in `src/main/resources` with routing instructions.

Example dependency:

```xml
<dependency>
  <groupId>io.github.scitia</groupId>
  <artifactId>aleatoricism-spring-boot-starter</artifactId>
</dependency>
```

Example properties:

```yaml
aleatoricism:
  auto-router:
    enabled: true
  flow:
    wait-for-side-effects: true
```

## Publishing notes

The root POM contains placeholders for Maven Central metadata (license, developers, SCM). Replace the `REPLACE_WITH_*` properties before release builds.

## Sample app

The sample application demonstrates tool handlers and flow usage. See `D:\Development\ideas\aleatoricism-project\aleatoricism-spring-boot-sample-app\README.md` and `.dev/agile-requests.http` for example requests.

