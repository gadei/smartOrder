package smart.order.server;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import smart.order.server.Log.LogLevel;

public class SmartOrderServer {

	
	private static SmartOrderServer instance = null;
	
	private TCPServer server = null;
	
	
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
	}
	
}

