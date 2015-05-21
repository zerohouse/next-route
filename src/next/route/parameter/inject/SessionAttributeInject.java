package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.SessionAttr;

@CatchParamAnnotations(SessionAttr.class)
public class SessionAttributeInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException {
		SessionAttr session = obj.getAnnotation(SessionAttr.class);
		String name = session.value();
		Object value = http.getSessionAttribute(Object.class, name);
		if (session.require() && value == null)
			throw new RequiredParamNullException(session.messageWhenNull());
		return value;
	}

}
