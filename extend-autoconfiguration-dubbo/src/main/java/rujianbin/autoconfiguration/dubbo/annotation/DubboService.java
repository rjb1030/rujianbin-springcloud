package rujianbin.autoconfiguration.dubbo.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DubboService {

	String version() default "";
}
