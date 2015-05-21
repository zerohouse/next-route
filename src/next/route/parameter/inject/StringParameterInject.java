package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.Param;

@CatchParamAnnotations(Param.class)
public class StringParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		return http.getParameter(obj.getAnnotation(Param.class).value());
	}

}
