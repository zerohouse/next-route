package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.StringParameter;

public class StringParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException {
		return http.getParameter(obj.getAnnotation(StringParameter.class).value());
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(StringParameter.class);
	}
}
