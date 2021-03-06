package next.route.parameter.type;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpSession;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes(HttpSession.class)
public class HttpSessionInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		return http.getReq().getSession();
	}

}
