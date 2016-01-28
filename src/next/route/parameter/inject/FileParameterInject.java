package next.route.parameter.inject;

import java.lang.reflect.Parameter;

import javax.servlet.http.Part;

import next.route.exception.RequiredParamNullException;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.UploadFile;
import next.route.parameter.annotation.FileParameter;
import next.route.parameter.annotation.ParameterInject;

@ParameterInject
public class FileParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws RequiredParamNullException {
		FileParameter param = obj.getAnnotation(FileParameter.class);
		String name = param.value();
		Object value = null;
		if (type.equals(UploadFile.class))
			value = new UploadFile(http.getPart(name));
		else if (type.equals(Part.class))
			value = http.getPart(name);
		if (param.require() && value == null)
			throw new RequiredParamNullException(param.errorWhenParamNull());
		return value;
	}

	@Override
	public boolean matches(Class<?> type, Parameter obj) {
		return obj.isAnnotationPresent(FileParameter.class);
	}
}
