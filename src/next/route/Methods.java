package next.route;

public enum Methods {
	POST("POST"), GET("GET"), DELETE("DELETE"), PUT("PUT");

	private String value;

	private Methods(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
