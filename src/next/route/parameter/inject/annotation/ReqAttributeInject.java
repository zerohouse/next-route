package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.ReqAttr;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(ReqAttr.class)
public class ReqAttributeInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		ReqAttr req = obj.getAnnotation(ReqAttr.class);
		String name = req.value();
		if (name.equals(""))
			name = obj.getName();
		return http.getReq().getAttribute(name);
	}

}
