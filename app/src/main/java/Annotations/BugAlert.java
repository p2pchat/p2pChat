package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation for alerting developers that the following
 * method contains a bug.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface BugAlert
{
    String bug() default "";
}
