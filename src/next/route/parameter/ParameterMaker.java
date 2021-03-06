package next.route.parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.bind.MakeInstance;
import next.route.http.Http;
import next.route.parameter.annotation.Require;
import next.route.parameter.inject.Inject;
import next.route.parameter.inject.annotation.ParseInject;

public class ParameterMaker {
	private static final Logger logger = LoggerFactory.getLogger(ParameterMaker.class);

	private Map<Class<?>, Inject> typeParameters;
	private Map<Class<? extends Annotation>, Inject> annotationParameters;
	private Inject defaultParameter;

	private Object getParameter(Http http, Class<?> type, Parameter obj) throws Exception {
		Inject inject = findMatched(obj.getAnnotations());
		if (inject == null)
			inject = typeParameters.get(type);
		if (inject == null)
			inject = defaultParameter;
		return inject.getParameter(http, type, obj);
	}

	private Inject findMatched(Annotation[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			Inject inject = annotationParameters.get(annotations[i].annotationType());
			if (inject != null)
				return inject;
		}
		return null;
	}

	public Object[] getParamArray(Http http, Method method) throws Exception {
		Class<?>[] types = method.getParameterTypes();
		Parameter[] obj = method.getParameters();
		List<Object> parameters = new ArrayList<Object>();
		for (int i = 0; i < obj.length; i++) {
			Object param = getParameter(http, types[i], obj[i]);
			parameters.add(param);
			if (param != null)
				continue;
			if (obj[i].isAnnotationPresent(Require.class))
				throw MakeInstance.make(obj[i].getAnnotation(Require.class).value());

		}
		return parameters.toArray();
	}

	public ParameterMaker(Set<Inject> injects) {
		logger.info("\n");
		logger.info("파라미터 Inejctor를 등록합니다.");
		typeParameters = new ConcurrentHashMap<Class<?>, Inject>();
		annotationParameters = new ConcurrentHashMap<Class<? extends Annotation>, Inject>();
		injects.forEach(inject -> {
			if (inject.getClass().isAnnotationPresent(CatchParamAnnotations.class)) {
				Class<? extends Annotation>[] annotations = inject.getClass().getAnnotation(CatchParamAnnotations.class).value();
				for (int i = 0; i < annotations.length; i++) {
					annotationParameters.put(annotations[i], inject);
					logger.info(String.format("%s 어노테이션은 %s Injector가 처리합니다.", annotations[i].getSimpleName(), inject.getClass().getSimpleName()));
				}
			}
			if (inject.getClass().isAnnotationPresent(CatchParamTypes.class)) {
				Class<?>[] types = inject.getClass().getAnnotation(CatchParamTypes.class).value();
				for (int i = 0; i < types.length; i++) {
					typeParameters.put(types[i], inject);
					logger.info(String.format("%s 타입은 %s Injector가 처리합니다.", types[i].getSimpleName(), inject.getClass().getSimpleName()));
				}
			}
		});
	}

	public void setDefaultParameter(Set<Object> set) {
		Iterator<Object> iter = set.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (Inject.class.isAssignableFrom(obj.getClass()))
				this.defaultParameter = (Inject) obj;
		}
		if (this.defaultParameter != null)
			return;
		this.defaultParameter = new ParseInject();
		logger.info(String.format("그외 모든 타입은 %s Injector가 처리합니다.", defaultParameter.getClass().getSimpleName()));
	}
}
