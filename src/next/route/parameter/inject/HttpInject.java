package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;

public class HttpInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http;
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return type.equals(Http.class);
	}

}
