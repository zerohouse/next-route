package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;

@CatchParamTypes(Http.class)
public class HttpInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		return http;
	}

}
