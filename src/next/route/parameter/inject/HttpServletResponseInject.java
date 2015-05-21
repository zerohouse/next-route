package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletResponse;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamTypes;

@CatchParamTypes(HttpServletResponse.class)
public class HttpServletResponseInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getResp();
	}

}
