package next.route.dispatch.support;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import next.route.http.Http;

public class PatternAndKeys {

	private static final String ALL_CHARACTER = "[^/^.]+";
	private Pattern pattern;
	private static final Pattern REGEXPattern;
	public static final String REGEX = "\\{(.*?)\\}";

	static {
		REGEXPattern = Pattern.compile(REGEX);
	}

	private Queue<String> keys;

	public PatternAndKeys(String uri) {
		keys = new ConcurrentLinkedQueue<String>();
		if (uri.matches("^(?:\\*).(" + ALL_CHARACTER + ")$")) {
			pattern = Pattern.compile(uri.replaceFirst("^(?:\\*).(" + ALL_CHARACTER + ")$", "(?:.*).$1"));
			return;
		}
		uri = uri.replace("*", "(?:" + ALL_CHARACTER + ")");
		Matcher matcher = REGEXPattern.matcher(uri);
		while (matcher.find()) {
			for (int j = 1; j < matcher.groupCount() + 1; j++) {
				keys.add(matcher.group(j));
			}
		}
		String regex = uri.replaceAll(REGEX, "(" + ALL_CHARACTER + ")");
		pattern = Pattern.compile(regex);
	}

	public boolean find(String uri, Http http) {
		Matcher matcher = pattern.matcher(uri);
		while (matcher.find()) {
			Iterator<String> key = keys.iterator();
			for (int j = 1; j < matcher.groupCount() + 1; j++) {
				http.putUriValue(key.next(), matcher.group(j));
			}
		}
		return matcher.matches();
	}

}
