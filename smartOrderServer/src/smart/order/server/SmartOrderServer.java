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
	private static int TCP_PORT = 1419;
	
	private ServerSocket server;
	private Socket client;
	
	private ServerState serverState = ServerState.SERVER_CLOSED;
	
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
    
    
	
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
		
		
		try {
			
			server = new ServerSocket(TCP_PORT);
			
		} catch (IOException e) {

			Log.error("Failed to open Server on port " + String.valueOf(TCP_PORT) + "!\n");
			return Error.ERR_TCP_SERVER;
		}
		
		Log.info("Started server on port " + String.valueOf(TCP_PORT) + "!\n");	
		
		try {
			client = server.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.info("Client connected!\n");	
		
		
		//sends the message to the client
		try {
			outMessage = new DataOutputStream(client.getOutputStream());
		

	        //read the message received from client
	        BufferedReader inMessage = new BufferedReader(new InputStreamReader(client.getInputStream()));
	
	        
	        outMessage.writeBytes("Test message to client\n");
	        outMessage.flush();
	        
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //in this while we wait to receive messages from client (it's an infinite loop)
        //this while it's like a listener for messages
//        while (running) {
//            String message = in.readLine();
//
//            if (message != null && messageListener != null) {
//                //call the method messageReceived from ServerBoard class
//                messageListener.messageReceived(message);
//            }
//        }
        
		
		return Error.ERR_OK;
	}
	
	public Error closeServer() {
		
		try {
			server.close();
		} catch (IOException e) {
			Log.error("Failed to close Server on port " + String.valueOf(TCP_PORT) + "!\n");
		}
		
		Log.info("Closed server on port " + String.valueOf(TCP_PORT) + "!\n");	
		return Error.ERR_OK;
	}
	
	
	public ServerState getServerState() {
		return serverState;
	}
	
	
	public enum ServerState{
		SERVER_CLOSED,
		SERVER_OPEN
	}
	
}

