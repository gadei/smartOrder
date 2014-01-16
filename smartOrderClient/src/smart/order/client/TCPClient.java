package smart.order.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class TCPClient  extends Thread {

	
	private SmartOrderClient client = null;
	
	private static int TCP_PORT = 1419;
	private static String TCP_SERVER_IP = "10.0.0.45";
	
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
	
    private Socket tcpSocket = null;
	private volatile boolean serverRunning = false;
	private volatile boolean threadRunning = false;
	private volatile String sendBuffer = null;
	private String serverMessage = null;
	
	public Error errStatus = Error.ERR_OK;
	
	public TCPClient(SmartOrderClient client) {
		this.client = client;
	}
	
	public Error sendMessageToServer(String msg) {
		
		if(sendBuffer == null)
			sendBuffer = msg;
		else
			return Error.ERR_UNKNOWN; //TODO
		
		return Error.ERR_OK;	
	}
	

	@Override
	public void run() {
		super.run();
		
		threadRunning = true;
		
		while(threadRunning) {

				
			errStatus = initConnection();
			
			if(errStatus == Error.ERR_OK) 
				serverRunning = true;
			
			while(serverRunning & threadRunning) {
				
				if(sendBuffer != null) {
					
					try {
						outMessage.writeBytes(sendBuffer);	
				        outMessage.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					sendBuffer = null;
				}
			}
		}
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "TCP-Thread stopped!\n");	
		
	}
	
	public Error initConnection() {
		
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Try to init connection");	
		
		client.getAndroidActivity().peep();
		
        InetAddress serverAddr;
		try {
			
			//here you must put your computer's IP address.
			serverAddr = InetAddress.getByName(TCP_SERVER_IP);
	
	        //create a socket to make the connection with the server
	        tcpSocket = new Socket(serverAddr, TCP_PORT);

		} catch (Exception e) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "ERROR");	
			e.printStackTrace();
		}
		
		serverRunning = true;
		
		//send the message to the server
        try {


        	//OutputStream to Arduino-Server
            outMessage = new DataOutputStream(tcpSocket.getOutputStream());
            //BufferedReader from Arduino-Server
            inMessage = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));//
            
	
	        //in this while the client listens for the messages sent by the server
            android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Waiting for the message...");
            
        	serverMessage = inMessage.readLine();
            
            if (serverMessage != null) {
            	android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received message: #" + serverMessage + "#");
            	serverRunning = false;
            }
            
            serverMessage = null;
	
	        
        
        } catch (IOException e) {
        	android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Message ERROR");	
			e.printStackTrace();
		}
        
 
        return Error.ERR_OK;
	}
	
}
