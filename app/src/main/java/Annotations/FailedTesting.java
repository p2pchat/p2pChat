package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Let remind yourself that a certain package or method
 * failed testing.
 */
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.TYPE})
public @interface FailedTesting
{
}
