package group.rxcloud.capa.springboot.schedule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Schedule.
 *
 * @author capa
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Schedule {

    /**
     * The name of the job, which is globally unique
     */
    String value() default "";

    /**
     * Name of the shared thread pool bean
     */
    String executor() default "";

    /**
     * Filter bean Name, in order
     */
    String[] filters() default {};
}
