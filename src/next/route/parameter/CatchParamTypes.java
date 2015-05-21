package next.route.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 지정된 타입을 파라미터로 만듭니다. <br>
 * next.route.parameter.inject.Inject를 Implement해야 합니다.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CatchParamTypes {

	Class<?>[] value();
}
