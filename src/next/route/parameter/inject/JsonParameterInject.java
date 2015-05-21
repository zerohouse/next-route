package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.JsonParam;

@CatchParamAnnotations(JsonParam.class)
public class JsonParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		JsonParam jparam = obj.getAnnotation(JsonParam.class);
		String name = jparam.value();
		Object value = http.getJsonObject(type, name);
		return value;
	}

}
