package next.route.http;

import java.lang.reflect.Field;
import java.util.Map;

import next.bind.MakeInstance;

public class ParameterMapToObject {

	public <T> T makeFromParameter(Map<String, String[]> parameterMap, Class<T> type) {
		T obj = MakeInstance.make(type);
		parameterMap.forEach((key, value) -> {
			try {
				Field field = type.getDeclaredField(key);
				field.setAccessible(true);
				field.set(obj, parseFromString(field.getType(), value[0]));
			} catch (Exception e) {
			}
		});
		return obj;
	}

	private Object parseFromString(Class<?> paramType, String s) {
		if (paramType.equals(String.class))
			return s;
		if (paramType.equals(byte.class) || paramType.equals(Byte.class))
			return Byte.parseByte(s);
		if (paramType.equals(short.class) || paramType.equals(Short.class))
			return Short.parseShort(s);
		if (paramType.equals(int.class) || paramType.equals(Integer.class))
			return Integer.parseInt(s);
		if (paramType.equals(long.class) || paramType.equals(Long.class))
			return Long.parseLong(s);
		if (paramType.equals(float.class) || paramType.equals(Float.class))
			return Float.parseFloat(s);
		if (paramType.equals(double.class) || paramType.equals(Double.class))
			return Double.parseDouble(s);
		if (paramType.equals(char.class) || paramType.equals(Character.class))
			return s.charAt(0);
		if (paramType.equals(boolean.class) || paramType.equals(Boolean.class))
			return Boolean.parseBoolean(s);
		return null;
	}
}
