package smart.order.server;

public enum Command {
	
	STOP_CLIENT(0, "CMD_STOP_CLIENT"),
	DEBUG_MSG(1, "CMD_DBG_MSG"),
	
	ACK(11, "CMD_ACK"),
	RECONNECT(12, "CMD_RECONNECT_ON_NEW_PORT");
	
	private final int code;
	private final String cmdTag;
	
	private Command(int code, String cmdTag) {
	    this.code = code;
	    this.cmdTag = cmdTag;
	}
	
	public String cmdTag() {
		return cmdTag;
	}
	
	public int getCode() {
		return code;
	}
}