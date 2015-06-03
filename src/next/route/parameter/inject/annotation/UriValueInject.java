package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.UriValue;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(UriValue.class)
public class UriValueInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		String value = obj.getAnnotation(UriValue.class).value();
		if ("".equals(value))
			return http.getUriValue(obj.getName());
		return http.getUriValue(obj.getAnnotation(UriValue.class).value());
	}

}
