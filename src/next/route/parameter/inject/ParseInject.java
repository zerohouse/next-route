package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.Parse;

@CatchParamAnnotations(Parse.class)
public class ParseInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws Exception {
		return http.getObjectFromParameterMap(type);
	}

}
