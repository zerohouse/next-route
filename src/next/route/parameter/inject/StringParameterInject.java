package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.annotation.Param;

@CatchParamAnnotations(Param.class)
@CatchParamTypes(String.class)
public class StringParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		if (!obj.isAnnotationPresent(Param.class) || obj.getAnnotation(Param.class).value().equals(""))
			return http.getParameter(obj.getName());
		return http.getParameter(obj.getAnnotation(Param.class).value());
	}

}
