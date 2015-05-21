package next.route.exception;

import next.route.http.Http;

public interface ExceptionHandler {

	void handle(Http http, Exception e);

}
