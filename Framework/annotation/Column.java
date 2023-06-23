package annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Column{
    String name() default "";
    boolean isPrimaryKey() default false;   
    boolean isForeignKey() default false;   
}