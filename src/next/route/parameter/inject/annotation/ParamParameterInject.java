package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.StringParam;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(StringParam.class)
public class ParamParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		if (obj.getAnnotation(StringParam.class).value().equals(""))
			return http.getParameter(obj.getName());
		return obj.getAnnotation(StringParam.class).value();
	}
}
