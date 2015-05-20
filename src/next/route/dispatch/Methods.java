package next.route.dispatch;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import next.bind.InstancePool;
import next.route.annotation.After;
import next.route.annotation.Before;
import next.route.annotation.Router;
import next.route.response.Response;
import next.route.response.factory.ResponseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Methods {

	@Override
	public String toString() {
		return "methodList [" + methodList + "], factory=" + factory.getClass().getSimpleName();
	}

	private static final Logger logger = LoggerFactory.getLogger(Methods.class);

	Queue<MethodWrapper> methodList;
	Map<String, MethodWrapper> methodMap;
	InstancePool instancePool;
	ResponseFactory factory;

	public Methods(Method m, Map<String, MethodWrapper> methodMap, InstancePool instancePool) {
		this.methodMap = methodMap;
		this.instancePool = instancePool;
		this.methodList = new ConcurrentLinkedQueue<MethodWrapper>();

		Class<?> declaringClass = m.getDeclaringClass();
		Class<? extends ResponseFactory> factory = declaringClass.getAnnotation(Router.class).defaultFactory();
		if (instancePool.getInstance(factory) == null)
			instancePool.putMakeEmpty(factory);
		this.factory = (ResponseFactory) instancePool.getInstance(factory);

		String[] beforeClass = null;
		String[] afterClass = null;
		if (declaringClass.isAnnotationPresent(Before.class))
			beforeClass = declaringClass.getAnnotation(Before.class).value();
		if (declaringClass.isAnnotationPresent(After.class))
			afterClass = declaringClass.getAnnotation(After.class).value();
		String[] before = null;
		String[] after = null;
		if (m.isAnnotationPresent(Before.class))
			before = m.getAnnotation(Before.class).value();
		if (m.isAnnotationPresent(After.class))
			after = m.getAnnotation(After.class).value();
		addAll(beforeClass);
		addAll(before);
		methodList.add(new MethodWrapper(instancePool.getInstance(declaringClass), m));
		addAll(after);
		addAll(afterClass);
	}

	private void addAll(String[] stringArray) {
		if (stringArray == null)
			return;
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i].equals(""))
				continue;
			MethodWrapper method;
			if (stringArray[i].charAt(0) == '!') {
				method = methodMap.get(stringArray[i].substring(1));
				if (method == null) {
					logger.warn(String.format("없는 Method [%s]를 제외하려고 했습니다.", stringArray[i]));
					continue;
				}

				if (!methodList.contains(method)) {
					logger.warn(String.format("실행할 메소드리스트에 추가되지 않은 Method [%s]를 제외하려고 했습니다.", stringArray[i]));
					continue;
				}
				methodList.remove(method);
			}
			method = methodMap.get(stringArray[i]);
			if (method == null) {
				logger.warn(String.format("없는 Method [%s]를 매핑하려고 했습니다.", stringArray[i]));
				continue;
			}
			methodList.add(methodMap.get(stringArray[i]));
		}
	}

	public Queue<MethodWrapper> getMethodList() {
		return methodList;
	}

	public Response getResponse(Object returned) {
		return factory.getResponse(returned);
	}
}
