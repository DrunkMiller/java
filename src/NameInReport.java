import java.lang.annotation.*;

@Inherited
@Target(value=ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NameInReport {
    String name() default "";
}
