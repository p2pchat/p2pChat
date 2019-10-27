package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes what you have TODO
 * whether it is inclass or package level.
 */
@Target({ElementType.METHOD, ElementType.PACKAGE, ElementType.TYPE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Todo
{
    String item1() default "";
    String item2() default "";
    String item3() default "";
    String item4() default "";
    String item5() default "";
}
