package next.route.parameter.type;

import java.lang.reflect.Parameter;

import next.route.http.Http;
import next.route.parameter.CatchParamTypes;
import next.route.parameter.inject.Inject;

@CatchParamTypes({ Float.class, float.class })
public class FloatInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception {
		return Float.parseFloat(http.getParameter(obj.getName()));
	}

}
