package smart.order.server;



import java.util.LinkedList;

import smart.order.server.Log.LogLevel;

public class SmartOrderServer {

	
	private static SmartOrderServer instance = null;
	
	private TCPInitServer initServer = null;
	
	private LinkedList<SmartOrderTCPClient> connectedClients;
	
	

	private SmartOrderServer() {
		
		Log.setLogLevel(LogLevel.LOG_INFO_AND_ERROR);
		
		Log.info("Creating smartOrder server\n");	
		
		initServer = new TCPInitServer();
		initServer.start();
		
		connectedClients = new LinkedList<>();
		
	}
	
	
	public void addSocketToList(SmartOrderTCPClient newClient) {
		
		for(SmartOrderTCPClient client : connectedClients) {
			if(client.getConnectedPort() == newClient.getConnectedPort())
				Log.error("Client on port " + newClient.getConnectedPort() + " already exists\n");
			else
				connectedClients.add(newClient);
		}
	}
	
	public static SmartOrderServer getInstance() {
		
		if(instance == null) 
			instance = new SmartOrderServer();
		
		return instance;
	}
	
	public void stop() {
		
		initServer.closeServer();
		//server.stop();
	}
	
	public boolean clientConnected() {
		return initServer.clientConnected();
	}
	
	
}

