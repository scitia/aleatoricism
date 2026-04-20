# alea-core

Core framework for business path modelling based on graph decomposition through typed `Way` algebra.

## What is implemented

- `Waypoint<Input, Output>` as the smallest executable element.
- `ClosedOutputPoint<Input, Output>` and `OpenOutputPoint<Input>` markers.
- Sealed ADT `Way<Input, Output>` with:
  - `Step`
  - `Sequence`
  - `Conditional`
  - `Parallel`
  - `SideEffect`
  - `OutputSideEffect` (via `emit(...)`)
- `FlowEngine` and `FlowDefinition` for execution from app entry points.
- Annotation metadata:
  - `@BusinessFlow`
  - `@WaypointContract`

## Model

The library focuses on one modelling approach:

1. Way as recursive path algebra
   - You build typed executable business paths using `Way` combinators.
   - Complex flows are decomposed into smaller subpaths and recomposed as a directed execution graph.

## Example (typed flow)

```java
Way<String, Integer> flow = Way.step((input, ctx) -> input.trim())
        .then(Way.step((value, ctx) -> value.length()))
        .withSideEffect((input, ctx) -> {
            // emit event
            return null;
        });

try (FlowEngine engine = new FlowEngine()) {
    Integer result = engine.run(flow, "  alea  ");
}
```

## Tests

Unit tests are in:

- `src/test/java/io/github/scitia/alea/core/way/WayTest.java`
