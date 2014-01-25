package smart.order.server;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPInitServer extends Thread {

	
	private static int TCP_INIT_PORT = 1419;
	private static int TCP_NEXT_FREE_PORT = 1420;
	
	private ServerSocket server = null;
	private Socket client = null;
	private DataOutputStream outMessage;
	private BufferedReader inMessage;

	private volatile boolean serverRunning = false;
	private volatile boolean threadRunning = false;

	public Error errStatus = Error.ERR_OK;
	
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
		errStatus = initServer();
		
		if(errStatus == Error.ERR_OK) 
			serverRunning = true;
		
		while(serverRunning && threadRunning) {

			client = null;
			
			try {
				client = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
				Log.error("Failed to wait for a client to connect!\n");	
				continue;
			}	
			
			Log.info("New client connected! Creating data streams\n");	
			
			try {
				outMessage = new DataOutputStream(client.getOutputStream());
				inMessage = new BufferedReader(new InputStreamReader(client.getInputStream()));

		        outMessage.writeBytes(Command.RECONNECT.cmdTag() + ": Client use port " + TCP_NEXT_FREE_PORT  + "\n");	
		        TCP_NEXT_FREE_PORT++;
		        outMessage.flush();

		        
			} catch (IOException e) {
				Log.error("Failed to open input/output stream!\n");	
				e.printStackTrace();
			}
			
			//Waiting for Client ACK
			try {
				while(serverRunning && threadRunning && !inMessage.ready()) {
					String clientMessage = inMessage.readLine();
					if(clientMessage.contains(Command.ACK.cmdTag())) {
						
						SmartOrderServer.getInstance().addSocketToList(new SmartOrderTCPClient(TCP_NEXT_FREE_PORT - 1));
						client.close();
						server.close();
						
						while(!client.isClosed() && !server.isClosed() && serverRunning) { 
							;
						}
						
						Log.info("(!!!) Client disconnected\n");
						server = new ServerSocket(TCP_INIT_PORT);
						break;
						
					} else {
						Log.error("Wrong message! No ACK cmd from client received!\n");
					}
							
					
				}
				
				Log.info("Waiting for client to disconnect\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		serverRunning = false;
		
		Log.info("TCP init Thread stopped!\n");	
	}
	
	public Error initServer() {
		
		Log.info("Init smartOrder server. Starting thread to init TCP connection\n");	
		
		
		try {		
			server = new ServerSocket(TCP_INIT_PORT);
		} catch (IOException e) {

			Log.error("Failed to open Server on port " + String.valueOf(TCP_INIT_PORT) + "!\n");
			return Error.ERR_TCP_SERVER;
		}
		
		Log.info("Started init server on port " + String.valueOf(TCP_INIT_PORT) + "!\n");	

		return Error.ERR_OK;
	}
	
	
	public Error closeServer() {
		
		try {
			server.close();
			
			if(client != null && client.isConnected())
				client.close();
		} catch (IOException e) {
			Log.error("Failed to close Server on port " + String.valueOf(TCP_INIT_PORT) + "!\n");
		}
		
		Log.info("Closed init server on port " + String.valueOf(TCP_INIT_PORT) + "!\n");	
		
		threadRunning = false;
		serverRunning = false;

		return Error.ERR_OK;
	}
		
}
