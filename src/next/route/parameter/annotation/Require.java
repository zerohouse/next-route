package next.route.parameter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import next.route.exception.ParamNullException;

/**
 * 해당파라미터가 null일 경우 지정된 에러를 발생시킵니다.<br>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Require {

	Class<? extends Exception> value() default ParamNullException.class;

}
