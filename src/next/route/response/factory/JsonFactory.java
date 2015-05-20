package next.route.response.factory;

import next.route.response.Json;
import next.route.response.Response;

public class JsonFactory implements ResponseFactory {

	@Override
	public Response getResponse(Object returned) {
		return new Json(returned);
	}

}
