package next.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import next.route.Methods;

/**
 * 
 * 해당 메소드, 혹은 클래슬르 Uri와 매핑합니다.<br>
 * 지정된 메소드들을 먼저, 혹은 나중에 실행합니다.<br>
 * <br>
 * 모든 파라미터를 받을때 {}와 *를 사용합니다.<br>
 * ex) &#064;When("/{variableName}/*");<br>
 * <br>
 * {}를 사용하면 변수를 꺼낼 수 있습니다.<br>
 * <br>
 * &#064;When("/{abc}");<br>
 * &#064;UriValue("abc") String abc;<br>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface When {
	String[] value() default "";

	Methods[] method() default Methods.GET;
}