package smart.order.server;



import smart.order.server.Log.LogLevel;

public class SmartOrderServer {

	
	private static SmartOrderServer instance = null;
	
	private TCPServer server = null;
	
	
	public Error sendMessageToClient(String msg) {
		
		return server.sendMessageToClient(msg);
	}
	
	private SmartOrderServer() {
		
		Log.setLogLevel(LogLevel.LOG_INFO_AND_ERROR);
		
		Log.info("Creating smartOrder server\n");	
		
		server = new TCPServer();
		server.start();
		
	}
	
	
	public static SmartOrderServer getInstance() {
		
		if(instance == null) 
			instance = new SmartOrderServer();
		
		return instance;
	}
	
	public void stop() {
		
		server.closeServer();
		//server.stop();
	}
	
	
	
}

