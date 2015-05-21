package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.annotation.Stored;

@CatchParamAnnotations(Stored.class)
public class StoredInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
		Stored stored = obj.getAnnotation(Stored.class);
		if (stored.value().equals("")) {
			return store.get(type);
		}
		return store.get(stored.value());
	}

}
