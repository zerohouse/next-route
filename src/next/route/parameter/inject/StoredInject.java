package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.annotation.ParameterInject;
import next.route.parameter.annotation.Stored;

@ParameterInject
public class StoredInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		Stored stored = obj.getAnnotation(Stored.class);
		if (stored.value().equals("")) {
			return store.get(type);
		}
		return store.get(stored.value());
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(Stored.class);
	}
}
