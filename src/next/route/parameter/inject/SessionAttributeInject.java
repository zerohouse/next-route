package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.SessionAttr;

@CatchParamAnnotations(SessionAttr.class)
public class SessionAttributeInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		SessionAttr session = obj.getAnnotation(SessionAttr.class);
		String name = session.value();
		if (name.equals(""))
			name = obj.getName();
		Object value = http.getSessionAttribute(Object.class, name);
		return value;
	}

}
