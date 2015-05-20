package next.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import next.route.response.factory.JsonFactory;
import next.route.response.factory.ResponseFactory;

/**
 * 
 * 라우터 클래스를 지정합니다.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Router {
	Class<? extends ResponseFactory> defaultFactory() default JsonFactory.class;
}
