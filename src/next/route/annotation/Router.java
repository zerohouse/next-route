package next.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import next.route.response.factory.JsonFactory;
import next.route.response.factory.ResponseFactory;

/**
 * 
 * 라우터 클래스를 지정합니다.<br>
 * defaultFactory는 해당 클래스에서 Object리턴시 Response할 값을 지정합니다.<br>
 * ResponseFactory를 Implement한 클래스를 지정합니다.<br>
 * 지정하지 않으면 Json으로 리턴합니다. <br>
 * 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Router {
	Class<? extends ResponseFactory> defaultFactory() default JsonFactory.class;
}
