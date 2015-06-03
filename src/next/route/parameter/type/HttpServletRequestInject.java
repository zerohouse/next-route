package next.route.parameter.type;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes(HttpServletRequest.class)
public class HttpServletRequestInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		return http.getReq();
	}

}
