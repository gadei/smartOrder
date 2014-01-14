package smart.order.server;

import smart.order.server.Log.LogLevel;

public class SmartOrderServer {

	
	private static SmartOrderServer instance = null;
	
	
	private SmartOrderServer() {
		
		Log.setLogLevel(LogLevel.LOG_INFO_AND_ERROR);
		
		Log.info("Creating smartOrder server\n");		
		
	}
	
	
	public static SmartOrderServer getInstance() {
		
		if(instance == null) 
			instance = new SmartOrderServer();
		
		return instance;
	}
	
	
	
	public Error initServer() {
		
		Log.info("Init smartOrder server\n");	
		
		return Error.ERR_OK;
	}
	
}

