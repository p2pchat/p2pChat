package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is mean't to be if you see a problem in your
 * algorithm and cannot get it to run. Put this label on
 * there so you know where the compile error is.
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface CompileError
{
    String reason() default "";
}
