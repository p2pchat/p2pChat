package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Warn other developers or yourself
 * that you need to add a try and catch statement.
 */
@Target(ElementType.METHOD)
public @interface AddTryStatement
{
}
