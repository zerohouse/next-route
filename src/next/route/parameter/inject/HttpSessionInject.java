package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpSession;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamTypes;

@CatchParamTypes(HttpSession.class)
public class HttpSessionInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getReq().getSession();
	}

}
