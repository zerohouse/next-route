package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;

public interface Inject {

	public Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception;

}
