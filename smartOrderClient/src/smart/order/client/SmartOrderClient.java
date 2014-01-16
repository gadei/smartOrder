package smart.order.client;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;

import java.net.Socket;



public class SmartOrderClient extends Thread {

	
	private static int TCP_PORT = 1419;
	private static String TCP_SERVER_IP = "10.0.0.45";
	
	private static SmartOrderClient instance = null;
	
	private Socket tcpSocket;
	private FullscreenActivity androidActivity = null;
	
	private volatile boolean serverRunning = false;
	private volatile boolean threadRunning = false;
	
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
    
    private String serverMessage;
	
	 @Override
	public void run() {
		super.run();
		
		threadRunning = true;
		
	}
	
	
	private SmartOrderClient(FullscreenActivity activity) {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Creating smartOrder client");		
		androidActivity = activity;
	}
	
	
	public static SmartOrderClient getInstance(FullscreenActivity activity) {
		
		if(instance == null) 
			instance = new SmartOrderClient(activity);
		
		return instance;
	}
	
	
	public void initConnection() {
		
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Try to init connection");	
		
		androidActivity.peep();
		
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


//            if (serverMessage != null && mMessageListener != null) {
//                //call the method messageReceived from MyActivity class
//                mMessageListener.messageReceived(serverMessage);
//            }
            
            if (serverMessage != null) {
            	android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received message: #" + serverMessage + "#");
            	serverRunning = false;
            }
            
            serverMessage = null;
	
	        
        
        } catch (IOException e) {
        	android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Message ERROR");	
			e.printStackTrace();
		}
 
	}
	
}
