package next.route.parameter.inject;


import java.lang.reflect.Parameter;

import javax.servlet.http.HttpSession;

import next.route.http.Http;
import next.route.http.Store;

public class HttpSessionInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getReq().getSession();
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return type.equals(HttpSession.class);

	}
}
