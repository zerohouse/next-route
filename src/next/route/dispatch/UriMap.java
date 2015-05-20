package next.route.dispatch;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import next.route.dispatch.support.PatternAndKeys;
import next.route.http.Http;

public class UriMap {

	private Map<UriKey, Methods> uriMap;
	private Map<String, Queue<PatternAndKeys>> regexMap;
	private Map<PatternAndKeys, Methods> patternMap;
	private final Pattern pattern;

	public UriMap() {
		uriMap = new ConcurrentHashMap<UriKey, Methods>();
		regexMap = new ConcurrentHashMap<String, Queue<PatternAndKeys>>();
		patternMap = new ConcurrentHashMap<PatternAndKeys, Methods>();
		pattern = Pattern.compile(PatternAndKeys.REGEX);
	}

	public void put(UriKey key, Methods Methods) {
		Matcher matcher = pattern.matcher(key.getUri());
		if (!key.getUri().contains("*") && !matcher.find()) {
			uriMap.put(key, Methods);
			return;
		}
		Queue<PatternAndKeys> regexList = regexMap.get(key.getMethod());
		if (regexList == null) {
			regexList = new ConcurrentLinkedQueue<PatternAndKeys>();
			regexMap.put(key.getMethod(), regexList);
		}
		PatternAndKeys pak = new PatternAndKeys(key.getUri());
		regexList.add(pak);
		patternMap.put(pak, Methods);
	}

	public Methods get(UriKey key, Http http) {
		Methods methods = uriMap.get(key);
		if (methods != null)
			return methods;
		Queue<PatternAndKeys> regexList = regexMap.get(key.getMethod());
		if (regexList == null)
			return null;
		Iterator<PatternAndKeys> keys = regexList.iterator();
		while (keys.hasNext()) {
			PatternAndKeys pak = keys.next();
			if (pak.find(key.getUri(), http)) {
				methods = patternMap.get(pak);
				return methods;
			}
		}
		return null;
	}

}
