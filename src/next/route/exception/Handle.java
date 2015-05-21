package next.route.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 지정된 익셉션이 발생시, 처리합니다. <br>
 * next.route.exception.ExceptionHandler를 Implement해야 합니다.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handle {

	Class<? extends Exception>[] value();
}
