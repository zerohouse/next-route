package next.route.parameter.inject;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;

public interface Inject {

	public Object getParameter(Http http, Store store, Class<?> type, java.lang.reflect.Parameter obj) throws RequiredParamNullException;

	public boolean matches(Class<?> type, java.lang.reflect.Parameter obj);

}
