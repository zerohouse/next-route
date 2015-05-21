package next.route.parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.inject.Inject;

public class ParameterMaker {
	private Map<Class<?>, Inject> typeParameters;
	private Map<Class<? extends Annotation>, Inject> annotationParameters;

	private Object getParameter(Http http, Store store, Class<?> type, Parameter obj) throws Exception {
		Inject inject;
		if (obj.getAnnotations().length == 0)
			inject = typeParameters.get(type);
		else
			inject = annotationParameters.get(obj.getAnnotations()[0].annotationType());
		if (inject == null)
			return null;
		return inject.getParameter(http, store, type, obj);
	}

	public Object[] getParamArray(Http http, Method method, Store store) throws Exception {
		Class<?>[] types = method.getParameterTypes();
		Parameter[] obj = method.getParameters();
		List<Object> parameters = new ArrayList<Object>();
		for (int i = 0; i < obj.length; i++) {
			parameters.add(getParameter(http, store, types[i], obj[i]));
		}
		return parameters.toArray();
	}

	public ParameterMaker(Set<Inject> injects) {
		typeParameters = new ConcurrentHashMap<Class<?>, Inject>();
		annotationParameters = new ConcurrentHashMap<Class<? extends Annotation>, Inject>();
		injects.forEach(inject -> {
			if (inject.getClass().isAnnotationPresent(CatchParamAnnotations.class)) {
				Class<? extends Annotation>[] annotations = inject.getClass().getAnnotation(CatchParamAnnotations.class).value();
				for (int i = 0; i < annotations.length; i++)
					annotationParameters.put(annotations[i], inject);
			}
			if (inject.getClass().isAnnotationPresent(CatchParamTypes.class)) {
				Class<?>[] types = inject.getClass().getAnnotation(CatchParamTypes.class).value();
				for (int i = 0; i < types.length; i++)
					typeParameters.put(types[i], inject);
			}
		});
	}
}
