# aleatoricism-flows

`aleatoricism-flows` defines the typed flow runtime used to model business operations as a directed graph of waypoints with optional side effects.

## Core concepts

- **Waypoint** (`Waypoint<I, O>`)
  - Smallest executable unit that transforms input to output.
   - Implements Singleton pattern; stateless, no dependencies.
  - Errors are wrapped into `FlowExecutionException`.

- **Datapoint** (`Datapoint<I, O>`)
  - Flow node supporting dependency injection.
  - Spring-managed bean; can inject repositories, services, configurations.
  - Ideal for data persistence, external service calls, and state management.
  - Same signature as Waypoint but allows constructor-based dependency injection.

- **EmissionPoint** (`EmissionPoint<I>`)
  - Special waypoint for side effects; returns `Void`.

- **Way** (`Way<I, O>`)
  - Algebraic data type that models a flow graph.
  - Supports sequential composition (`then`), conditionals (`when`), and parallel branches (`parallel`).
  - Supports side effects before or after the main path (`withSideEffect`, `emit`).
  - Can compose both Waypoint and Datapoint nodes seamlessly.

- **Flow** (`Flow<I, O>`)
  - Simple wrapper around `Way` used as a named business flow definition.

- **BusinessFlowBuilder**
  - Typed builder for readability and safety.
  - Typical usage: `define().start(...).then(...).emit(...).build()`.
  - Supports both Waypoint and Datapoint nodes.

- **FlowEngine**
  - Executes a `Way` or `Flow` within an `ExecutionContext`.
  - Accepts `ExecutionOptions` for side-effect behavior.

- **ExecutionContext / ExecutionOptions**
  - Provides executor and runtime policy.
  - `waitForSideEffects` controls whether side effects are awaited.

## Execution model

1. Build a flow using `BusinessFlowBuilder` or by composing `Way` nodes.
2. Execute it with `FlowEngine` using a context and options.
3. Side effects run on the context executor; you can wait for them or allow async completion.

## Example flow

```java
Flow<MyInput, MyOutput> flow = BusinessFlowBuilder.define()
    .start(MyNormalizeWaypoint.INSTANCE)
    .then(MyValidateWaypoint.INSTANCE)
    .then(MyEnrichWaypoint.INSTANCE)
    .emit(MyAuditEmission.INSTANCE)
    .build();
```

## Parallel and conditional execution

- Use `Way.parallel(left, right)` to run branches concurrently.
- Use `Way.when(condition, whenTrue, whenFalse)` to branch execution.

The `ExecutionContext` executor controls concurrency.

## Extension points

- Implement new `Waypoint` types for domain-specific logic.
- Implement `Datapoint` types for data access with dependency injection.
- Implement `EmissionPoint` for logging, notifications, or audit trails.
- Provide custom `ExecutionContext` for specialized executors or tracing.

## Waypoint vs Datapoint

### Use Waypoint When:
- Logic is stateless and requires no external dependencies.
- Implementation is a pure transformation function.
- Performance-critical and Spring overhead is undesirable.
- No interaction with databases, repositories, or services is needed.

**Example:**
```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NormalizeInputWaypoint implements Waypoint<Input, Input> {
    public static final NormalizeInputWaypoint INSTANCE = new NormalizeInputWaypoint();
    
    @Override
    public Input handle(Input input, ExecutionContext context) {
        return normalize(input);
    }
}
```

### Use Datapoint When:
- Node needs to access repositories, services, or external APIs.
- Requires configuration or environment-specific behavior.
- Clean separation of concerns and testability matter.
- Dependency injection is needed for resource management.

**Example:**
```java
@Component
public class PersistItemDatapoint implements Datapoint<Item, Item> {
    private final ItemRepository repository;
    
    public PersistItemDatapoint(ItemRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Item handle(Item item, ExecutionContext context) {
        return repository.save(item);
    }
}
```

### Mixed Usage Example:
```java
Flow<Request, Response> flow = BusinessFlowBuilder.define()
    .start(ValidateWaypoint.INSTANCE)        // Stateless transformation
    .then(EnrichWaypoint.INSTANCE)           // Stateless transformation
    .then(persistenceDatapoint)              // Persistence with injected repository
    .emit(auditEmissionPoint)                // Side effect
    .build();
```

## Related modules

- `aleatoricism-chain` for HTTP request ingress.
- `aleatoricism-spring-boot-autoconfigure` and `aleatoricism-spring-boot-starter` for Spring Boot integration.

