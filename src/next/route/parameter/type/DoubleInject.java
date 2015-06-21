package next.route.parameter.type;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes({ Double.class, double.class })
public class DoubleInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception {
		return Double.parseDouble(http.getUriValue(obj.getName()));
	}

}
