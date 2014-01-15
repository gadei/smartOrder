package smart.order.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.rules.ErrorCollector;

public class TCPServer extends Thread {

	
	private static int TCP_PORT = 1419;
	
	private ServerSocket server;
	private Socket client;
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
	
	private ServerState serverState = ServerState.SERVER_CLOSED;
	private static boolean serverRunning = false;
	private static boolean threadRunning = false;
	
	public Error errStatus = Error.ERR_OK;
	
	
	public static Error startServer() {
		
		//TODO: Error wenn server bereits läuft
		
		if(!serverRunning)
			serverRunning = true;
		
		return Error.ERR_OK;
	}
	
	public static Error stopServer() {
		
		//TODO: Error wenn server bereits läuft
		
		if(serverRunning)
			serverRunning = false;
		
		return Error.ERR_OK;
	}
	
	
	
	@Override
	public void run() {
		super.run();
		
		threadRunning = true;
		
		while(true) {

				
			errStatus = initServer();
			
			if(errStatus == Error.ERR_OK) 
				serverRunning = true;
			
			while(serverRunning) {
				
				
				
				
			}

		}
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

		return Error.ERR_OK;
	}
	
	public Error closeServer() {
		
		try {
			server.close();
		} catch (IOException e) {
			Log.error("Failed to close Server on port " + String.valueOf(TCP_PORT) + "!\n");
		}
		
		Log.info("Closed server on port " + String.valueOf(TCP_PORT) + "!\n");	
		
		serverRunning = false;
		
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
