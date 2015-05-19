package next.route.setting;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Setting {

	private static Mapping mapping;
	private static Gson gson;

	static {
		try {
			mapping = new Gson().fromJson(new FileReader(Setting.class.getResource("/next-route.json").getFile()), Mapping.class);
		} catch (Exception e) {
			mapping = new Mapping();
			e.printStackTrace();
		}
		gson = new GsonBuilder().setDateFormat(mapping.getDateFormat()).create();
	}

	public static Mapping getMapping() {
		return mapping;
	}

	public static Gson getGson() {
		return gson;
	}

}
