package smart.order.client;

public enum Error {

	ERR_OK(0, "Works well"),
	ERR_TCP_SERVER(1, "TCP-Server error."),
	ERR_TCP_CLIENT(2, "TCP-Client error."),
	ERR_TCP_CONNECTION(3, "TCP-connection error."),
	ERR_MSG_QUEUE_FULL(12, "Send buffer full"),
	ERR_NO_ORDER(20, "No Order entered"),

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