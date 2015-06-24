package next.route.parameter.type;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes(String.class)
public class StringInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception {
		Object value = http.getParameter(obj.getName());
		if (null != value)
			return value;
		return http.getUriValue(obj.getName());
	}

}
