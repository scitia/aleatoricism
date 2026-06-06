package io.github.scitia.aleatoricism.flows.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a Datapoint contract in human-readable form.
 * Used for Spring-managed beans that support dependency injection.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatapointContract {

    String name();

    String description() default "";

    WaypointContractType contractType();
}

