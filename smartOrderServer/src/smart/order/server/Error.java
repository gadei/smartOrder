package smart.order.server;

public enum Error {

	ERR_OK(0, "Works well"),
	ERR_TCP_SERVER(1, "TCP-Server error."),

	ERR_MSG_QUEUE_FULL(2, "Send buffer full"),

	ERR_UNKNOWN(99, "An unknown error occurred.");

	private final int code;
	private final String description;

	private Error(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}
}