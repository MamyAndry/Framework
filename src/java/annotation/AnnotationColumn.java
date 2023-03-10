package annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface AnnotationColumn{
    String nom() default "";
    boolean isPrimaryKey() default false;   
    boolean isForeignKey() default false;   
}