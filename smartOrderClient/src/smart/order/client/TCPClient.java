package smart.order.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import smart.order.client.Command;



public class TCPClient  extends Thread {

	
	private SmartOrderClient client = null;
	
	private final static int TCP_INIT_PORT = 1419;
	private static String TCP_SERVER_IP = "10.0.0.55";
	
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
	
    private volatile Socket tcpSocket = null;
	private volatile boolean clientRunning = false;
	private volatile boolean threadRunning = false;
	private volatile String sendBuffer = null;
	private String serverMessage = null;
	
	private int connectedToPortNbr = -1;
	
	public Error errStatus = Error.ERR_OK;
	
	public TCPClient(SmartOrderClient client) {
		this.client = client;
	}
	
	public Error sendMessageToServer(String msg) {
		
		if(sendBuffer == null)
			sendBuffer = msg;
		else
			return Error.ERR_UNKNOWN;
		
		return Error.ERR_OK;	
	}
	
	protected String getWifiMAC() {
		
		WifiManager wifiMan = (WifiManager) client.getActivity().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();

		return wifiInf.getMacAddress();
	}
	
	public int getConnectedToPortNbr() {
		return connectedToPortNbr;
	}
	
	private int connectToInitServer() {
		
		errStatus = initConnection(TCP_INIT_PORT);
		
		if(errStatus == Error.ERR_OK) 
			threadRunning = true;
		
		while(threadRunning && !tcpSocket.isClosed()) {
						
			try {
				if(inMessage.ready()) {
					
					//in this while the client listens for the messages sent by the server
				    android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Waiting for the new port...");
					serverMessage = inMessage.readLine();
				    
				    if (serverMessage != null) {
				    	android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received message: #" + serverMessage + "#");
				    	Command cmd = client.decodeCommand(serverMessage);
				    	
				    	if(cmd != null && cmd == Command.RECONNECT) {
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "(1)Received command: #Reconnect to port " + getNewPort(serverMessage) + "#");
				    		outMessage.writeBytes(Command.ACK.cmdTag());	
					        outMessage.flush();
				    		tcpSocket.close();
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "(2)Received command: #Reconnect to port " + getNewPort(serverMessage) + "#");			    		
				    	} else if(cmd != null && cmd == Command.DEBUG_MSG) {
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received debug message: #" + serverMessage + "#");
				    	} else {
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "!!Unknown command! Closing thread! #" + serverMessage + "#");
				    		threadRunning = false;
				    	}
				    }  
				}
			} catch (IOException e) {
				android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Failed to read input stream");
				e.printStackTrace();
			}
		}
		
		connectedToPortNbr = TCP_INIT_PORT;
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "connectToInitServer finished!");
		
		return getNewPort(serverMessage);

	}
	
	private void connectToNewSocket(int newPort) {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "connectToNewSocket started!");
		
		errStatus = initConnection(newPort);
		
		if(errStatus == Error.ERR_OK) 
			clientRunning = true;
		
		connectedToPortNbr = newPort;
		
		while(clientRunning && threadRunning) {
			
			if(sendBuffer != null) {
				
				try {
					outMessage.writeBytes(sendBuffer);	
			        outMessage.flush();
				} catch (IOException e) {
					android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Failed to send message via output stream");
					e.printStackTrace();
				}
				sendBuffer = null;
			}
			
			try {
				if(inMessage.ready()) {
					
					//in this while the client listens for the messages sent by the server
				    android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Waiting for the message...");
					serverMessage = inMessage.readLine();
				    
				    if (serverMessage != null) {
				    	android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received message: #" + serverMessage + "#");
				    	Command cmd = client.decodeCommand(serverMessage);
				    	
				    	if(cmd != null && cmd == Command.STOP_CLIENT) {
				    		threadRunning = false;
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received command: #Stop client#");
				    	} else if(cmd != null && cmd == Command.DEBUG_MSG)
				    		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received debug message: #" + serverMessage + "#");
				    	
				    }
				    
				    serverMessage = null;     
				    
				}
			} catch (IOException e) {
				android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Failed to read input stream");
				e.printStackTrace();
			}
		}
		
		connectedToPortNbr = -1;
	}
	

	@Override
	public void run() {
		super.run();
		
		//Init phase! Connect to init server on reserved socket
		int newPort = connectToInitServer();

		//now start on new port
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Reopen server connection on new port (" + newPort + ")");
		connectToNewSocket(newPort);
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "TCP-Thread stopped!\n");	
		clientRunning = false;
		client.tcpClientClosed();
	}
	
	private int getNewPort(String serverMsg) {
		String tmpString = serverMsg.split(" ")[4];
				
		return Integer.parseInt(tmpString);
	}
	
	private Error initConnection(int portToConnectTo) {

		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Try to init connection on port " + portToConnectTo);	
		
		client.getAndroidActivity().peep();
		
        InetAddress serverAddr;
		try {
			
			//here you must put your computer's IP address.
			serverAddr = InetAddress.getByName(TCP_SERVER_IP);
	
	        //create a socket to make the connection with the server
	        tcpSocket = new Socket(serverAddr, portToConnectTo);

		} catch (Exception e) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "ERROR --- Cannot connect to server at " + TCP_SERVER_IP);	
			e.printStackTrace();
			return Error.ERR_TCP_CONNECTION;
		}

		
        try {

        	//OutputStream to smartOrder-Server
            outMessage = new DataOutputStream(tcpSocket.getOutputStream());
            //BufferedReader from smartOrder-Server
            inMessage = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));//
 
            
        } catch (IOException e) {
        	android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Message ERROR");	
			e.printStackTrace();
			clientRunning = false;
		}
 
        return Error.ERR_OK;
	}
	
	public boolean clientConnected()  {
		return clientRunning;
	}
	
}

