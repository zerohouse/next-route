package next.route.parameter.type;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes({ Long.class, long.class })
public class LongInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception {
		return Long.parseLong(http.getParameter(obj.getName()));
	}

}
