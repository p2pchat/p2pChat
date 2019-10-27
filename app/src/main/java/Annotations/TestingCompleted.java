package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Testing is completed.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestingCompleted
{
}
