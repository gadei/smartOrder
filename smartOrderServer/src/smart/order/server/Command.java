package smart.order.server;

public enum Command {
	
	STOP_CLIENT(0, "CMD_STOP_CLIENT"),
	DEBUG_MSG(1, "CMD_DBG_MSG");
	
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