package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.SessionAttr;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(SessionAttr.class)
public class SessionAttributeInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		SessionAttr session = obj.getAnnotation(SessionAttr.class);
		String name = session.value();
		if (name.equals(""))
			name = obj.getName();
		return http.getSessionAttribute(Object.class, name);
	}

}
