package org.SprintDeux;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generic annotation placed in package org.
 *
 * Note: this name shadows java.lang.annotation.Annotation when imported
 * without package qualification; keep imports explicit to avoid confusion.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AnnotationMethode {
    /**
     * Optional value for the annotation.
     */
    String value() default "";

}
