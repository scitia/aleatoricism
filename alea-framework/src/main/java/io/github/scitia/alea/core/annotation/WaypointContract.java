package io.github.scitia.alea.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a waypoint contract in human-readable form.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WaypointContract {

    String name();

    String description() default "";

    WaypointContractType contractType();
}
