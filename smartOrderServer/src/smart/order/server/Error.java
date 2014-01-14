package smart.order.server;

public enum Error {
	
	
	//my Errors!
	
	  ERR_OK(0, "Works well"),
	  
	  ERR_UNKNOWN(1, "An unknown error occurred.");

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