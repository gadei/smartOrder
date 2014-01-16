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
	
	private ServerSocket server = null;
	private Socket client = null;
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
	
	private ServerState serverState = ServerState.SERVER_CLOSED;
	private volatile boolean serverRunning = false;
	private volatile boolean threadRunning = false;
	private volatile String sendBuffer = null;
	
	public Error errStatus = Error.ERR_OK;
	
	public Error sendMessageToClient(String msg) {
		
		if(sendBuffer == null)
			sendBuffer = msg;
		else {
			Log.info("Thread wait: " + Error.ERR_MSG_QUEUE_FULL.getDescription());
			return Error.ERR_MSG_QUEUE_FULL;
		}
		
		return Error.ERR_OK;	
	}
	
	public boolean clientConnected() {
		
		if(client == null)
			return false;
		else
			return true;
		
	}
	
	@Override
	public void run() {
		super.run();
		
		threadRunning = true;
		
		if(threadRunning) {

				
			errStatus = initServer();
			
			if(errStatus == Error.ERR_OK) 
				serverRunning = true;
			
			while(serverRunning & threadRunning) {
				
				if(sendBuffer != null) {
					
					try {
						outMessage.writeBytes(sendBuffer);	
				        outMessage.flush();
					} catch (IOException e) {
						Log.error("Failed to send message via output stream");
						e.printStackTrace();
					}
					
					sendBuffer = null;
				}
			}
		}
		
		serverRunning = false;
		
		Log.info("TCP-Thread stopped!\n");	
		
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
			e.printStackTrace();
			Log.error("Failed to wait for a client to connect!\n");	
			return Error.ERR_UNKNOWN;
		}
		
		Log.info("Client connected!\n");	
		
		try {
			outMessage = new DataOutputStream(client.getOutputStream());
	        inMessage = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        
		} catch (IOException e) {
			Log.error("Failed to open input/output stream!\n");	
			e.printStackTrace();
			return Error.ERR_UNKNOWN;
		}

		return Error.ERR_OK;
	}
	
	public Error closeServer() {
		
		try {
			server.close();
			client.close();
		} catch (IOException e) {
			Log.error("Failed to close Server on port " + String.valueOf(TCP_PORT) + "!\n");
		}
		
		Log.info("Closed server on port " + String.valueOf(TCP_PORT) + "!\n");	
		
		threadRunning = false;
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
