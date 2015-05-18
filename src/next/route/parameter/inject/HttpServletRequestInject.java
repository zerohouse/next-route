package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.ParameterInject;

@ParameterInject
public class HttpServletRequestInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getReq();
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return type.equals(HttpServletRequest.class);

	}

}
