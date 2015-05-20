package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.SessionAttribute;

public class SessionAttributeInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException {
		SessionAttribute session = obj.getAnnotation(SessionAttribute.class);
		String name = session.value();
		Object value = http.getSessionAttribute(Object.class, name);
		if (session.require() && value == null)
			throw new RequiredParamNullException(session.errorWhenSessionNull());
		return value;
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(SessionAttribute.class);
	}
}
