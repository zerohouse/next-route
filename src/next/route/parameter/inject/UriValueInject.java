package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.ParameterInject;
import next.route.parameter.annotation.UriValue;

@ParameterInject
public class UriValueInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getUriValue(obj.getAnnotation(UriValue.class).value());
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(UriValue.class);
	}
}
