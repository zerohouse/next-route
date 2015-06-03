package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.JsonParam;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(JsonParam.class)
public class JsonParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		JsonParam jparam = obj.getAnnotation(JsonParam.class);
		String name = jparam.value();
		if (name.equals(""))
			name = obj.getName();
		Object value = http.getJsonObject(type, name);
		return value;
	}

}
