package next.route.parameter.inject.annotation;

import java.lang.reflect.Parameter;

import javax.servlet.http.Part;

import next.route.http.Http;
import next.route.parameter.CatchParamAnnotations;
import next.route.parameter.UploadFile;
import next.route.parameter.annotation.FileParam;
import next.route.parameter.inject.Inject;

@CatchParamAnnotations(FileParam.class)
public class FileParameterInject implements Inject {

	@Override
	public Object getParameter(Http http, Class<?> type, Parameter obj) {
		FileParam param = obj.getAnnotation(FileParam.class);
		String name = param.value();
		if (name.equals(""))
			name = obj.getName();
		Object value = null;
		if (type.equals(UploadFile.class))
			value = new UploadFile(http.getPart(name));
		else if (type.equals(Part.class))
			value = http.getPart(name);
		return value;
	}

}
