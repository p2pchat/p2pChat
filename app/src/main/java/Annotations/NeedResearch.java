package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Remind yourself that there is a certain
 * method or class that needs research.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
public @interface NeedResearch
{
}
