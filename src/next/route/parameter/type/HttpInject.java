package next.route.parameter.type;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes(Http.class)
public class HttpInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		return http;
	}

}
