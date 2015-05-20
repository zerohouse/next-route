package next.route.dispatch;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import next.bind.InstancePool;
import next.route.annotation.HttpMethod;
import next.route.annotation.Router;
import next.route.annotation.When;
import next.route.http.Http;
import next.route.http.Store;
import next.route.parameter.ParameterMaker;
import next.route.parameter.annotation.ParameterInject;
import next.route.parameter.inject.Inject;
import next.route.response.Response;
import next.route.setting.Setting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mapper {

	private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

	private Map<String, MethodWrapper> methodMap;
	private UriMap uriMap;

	private InstancePool instancePool;
	private ParameterMaker parameterMaker;

	Mapper() {
		instancePool = new InstancePool(Setting.getMapping().getBasePackage());
		instancePool.addClassAnnotations(Router.class, ParameterInject.class);
		instancePool.addMethodAnnotations(When.class, HttpMethod.class);
		instancePool.build();

		Set<Inject> set = new HashSet<Inject>();
		instancePool.getInstancesAnnotatedWith(ParameterInject.class).forEach(inject -> {
			set.add((Inject) inject);
		});

		parameterMaker = new ParameterMaker(set);

		methodMap = new HashMap<String, MethodWrapper>();
		uriMap = new UriMap();
		makeMethodMap();
		makeUriMap();
	}

	private void makeMethodMap() {
		logger.info("\n");
		logger.info("HttpMethod를 생성합니다.");
		instancePool.getInstancesAnnotatedWith(HttpMethod.class).forEach(instance -> {
			Method[] methods = instance.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (!methods[i].isAnnotationPresent(HttpMethod.class))
					continue;
				HttpMethod method = methods[i].getAnnotation(HttpMethod.class);
				String methodName = method.value();
				if (methodName.equals(""))
					methodName = methods[i].getName();
				methodMap.put(methodName, new MethodWrapper(instance, methods[i]));
				logger.info(String.format("[%s] %s", methodName, methods[i]));
			}
		});
	}

	private void makeUriMap() {
		instancePool.getInstancesAnnotatedWith(Router.class).forEach(
				router -> {
					logger.info("\n");
					logger.info(String.format("Router %s Uri를 매핑합니다. Default:%s", router.getClass().getSimpleName(),
							router.getClass().getAnnotation(Router.class).defaultFactory().getSimpleName()));
					Method[] methods = router.getClass().getMethods();
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].isAnnotationPresent(When.class))
							methodMapping(methods[i]);
					}
				});
	}

	private void methodMapping(Method m) {
		Class<?> declaringClass = m.getDeclaringClass();
		String[] prefix;
		Methods methods = new Methods(m, methodMap, instancePool);
		if (declaringClass.isAnnotationPresent(When.class)) {
			prefix = declaringClass.getAnnotation(When.class).value();
		} else {
			prefix = new String[] { "" };
		}
		When mapping = m.getAnnotation(When.class);
		for (int i = 0; i < prefix.length; i++)
			for (int j = 0; j < mapping.method().length; j++)
				for (int k = 0; k < mapping.value().length; k++) {
					String method = mapping.method()[j];
					String uri = prefix[i] + mapping.value()[k];
					UriKey urikey = new UriKey(method, uri);
					uriMap.put(urikey, methods);
					logger.info(String.format("%s -> %s", urikey, methods));
				}
	}

	public void execute(UriKey url, Http http) {
		Methods methods = uriMap.get(url, http);
		Queue<MethodWrapper> methodList = methods.getMethodList();
		if (methodList == null) {
			http.sendError(404);
			return;
		}
		logger.debug(String.format("%s -> %s", url, methodList.toString()));
		Iterator<MethodWrapper> miter = methodList.iterator();
		Store store = new Store(); // 생각을 좀 해봅시다..ㅇㅅㅇ;;
		try {
			while (miter.hasNext()) {
				MethodWrapper mw = miter.next();
				Object returned = mw.execute(parameterMaker.getParamArray(http, mw.getMethod(), store));

				if (returned == null)
					continue;
				if (Response.class.isAssignableFrom(returned.getClass())) {
					((Response) returned).render(http);
					return;
				}
				if (returned.getClass().equals(String.class)) {
					if (stringResponse(http, returned))
						return;
				}
				methods.getResponse(returned).render(http);
				return;
			}
			methods.getResponse(null).render(http);
		} catch (Exception e) {
			methods.getResponse(e.getMessage()).render(http);
		}
	}

	private boolean stringResponse(Http http, Object returned) {
		String res = returned.toString();
		if (!res.contains(":")) {
			return false;
		}
		String[] str = res.split(":");
		if ("forward".equals(str[0])) {
			if (str.length == 1) {
				http.sendError(508);
				return true;
			}
			http.forword(str[1]);
			return true;
		}
		if ("redirect".equals(str[0])) {
			if (str.length == 1) {
				http.sendError(508);
				return true;
			}
			http.sendRedirect(str[1]);
			return true;
		}
		if ("error".equals(str[0])) {
			if (str.length == 3) {
				http.sendError(Integer.parseInt(str[1]), str[2]);
				return true;
			}
			http.sendError(Integer.parseInt(str[1]));
			return true;
		}
		return false;
	}

}
