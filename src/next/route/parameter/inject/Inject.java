package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;

public interface Inject {

	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException;

}
