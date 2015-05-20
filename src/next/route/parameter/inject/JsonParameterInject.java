package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.JsonParameter;

public class JsonParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException {
		JsonParameter jparam = obj.getAnnotation(JsonParameter.class);
		String name = jparam.value();
		Object value = http.getJsonObject(type, name);
		if (jparam.require() && value == null)
			throw new RequiredParamNullException(jparam.messageWhenNull());
		return value;
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(JsonParameter.class);
	}
}
