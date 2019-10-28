package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Redudant variable.
 */
@Target({ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
public @interface Useless
{
}
