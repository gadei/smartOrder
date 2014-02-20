package smart.order.client.TCPConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import smart.order.client.Command;
import smart.order.client.Error;
import smart.order.client.SmartOrderClient;



public class TCPClient  extends Thread {

	
	public static final String EOF = "<EOF>";
	private final static int TCP_INIT_PORT = 1419;	
	private static final int UDP_PORT = 1418;
	
	private SmartOrderClient client = null;
	private TCPMessenger tcpMesseger = null;
	private volatile Socket tcpSocket = null;
	
	private DataOutputStream outMessage;
    private BufferedReader inMessage;
    private InputStream inStream;
	
    private Lock sendDataToServerMutex = null;
    
	private volatile boolean clientRunning = false;
	private volatile boolean threadRunning = false;

	private String serverMessage = null;

	private int receivedDataLen = 0;
	private int connectedToPortNbr = -1;
	
	private int currentMsgSize = 0;
	byte[] receiveBuffer = null;
	
	public Error errStatus = Error.ERR_OK;
	
	public TCPClient(SmartOrderClient client) {
		this.client = client;
	}
	
	public int getConnectedToPortNbr() {
		return connectedToPortNbr;
	}
	
	public Error sendDataToServer(byte[] data) {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Sending Data to server");
		sendDataToServerMutex.lock();
		
		try {
			outMessage.write(data);
			outMessage.flush();
		} catch (IOException e) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Failed to send message via output stream");
			e.printStackTrace();
		}
		
		sendDataToServerMutex.unlock();
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Sending Data to server finihed!");
		
		return Error.ERR_OK;
	}

	
	private void connectToNewSocket(int newPort) {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "connectToNewSocket started!");
		
		errStatus = initConnection(newPort);

		if(errStatus == Error.ERR_OK) 
			clientRunning = true;
		else {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Connection to port " + newPort + "failed! Stopping Thread!");
			clientRunning = false;
			return;
		}
		
		connectedToPortNbr = newPort;
		tcpMesseger = new TCPMessenger(this);
		
		sendDataToServerMutex = new ReentrantLock();
		
		while(clientRunning && threadRunning) {
			
			try {
				if(inStream.available() > 0) {
					
					if(currentMsgSize == 0) {
						//in this while the client listens for the messages sent by the server
					    android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Waiting for a new message...");
					    receiveBuffer = new byte[TCPMessenger.MAX_MSG_SIZE];
					    
					    receivedDataLen = inStream.read(receiveBuffer);
					    android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received " + receivedDataLen + " chars. EOF included?");
					    
					    byte[] receivedDataArray = new byte[receivedDataLen];
					    System.arraycopy(receiveBuffer, 0, receivedDataArray, 0, receivedDataLen);
					    
					    if(tcpMesseger.getMsgSize(receivedDataArray) == receivedDataLen)
					    	tcpMesseger.ReadMessage(receivedDataArray);
					    else
					    	currentMsgSize = receivedDataLen;
					    
					} else {
						throw new Exception("NOT IMPLEMENTED YET");
					}

				}	
			} catch (Exception e) {
				android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Failed to read input stream");
				e.printStackTrace();
			}
		}
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Leaving connectToNewSocket!");
		connectedToPortNbr = -1;
	}
	

	@Override
	public void run() 
	{
		super.run();
		
		//Init phase! Connect to init server on reserved socket	
		int newPort = connectToInitServer();

		if(newPort == -1)
			return;
		
		//now start on new port
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Reopen server connection on new port (" + newPort + ")");
		connectToNewSocket(newPort);
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "TCP-Thread stopped!\n");	
		clientRunning = false;
		client.tcpClientClosed();
	}

	private int connectToInitServer() {
		
		DatagramSocket UDP_packet;
		String thePortFromServer = "-1";
				
		try
		{
			UDP_packet = new DatagramSocket(UDP_PORT);

			UDP_packet.setBroadcast(true);
			byte[] b = "order".getBytes("UTF-8");
			DatagramPacket outgoing = new DatagramPacket(b, b.length, getBroadcastAddress(client.getAndroidActivity()), UDP_PORT);                  
			UDP_packet.send(outgoing);

			boolean run = false;
			while (!run) {  
				android.util.Log.d("  ==> ZeroConfig <==", "Scanning IP Addresses");
				byte[] buffer = new byte[1024];
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);    
				UDP_packet.receive(incoming);
				String message = new String(incoming.getData(), 0, incoming.getLength(), "UTF-8");
				android.util.Log.d("  ==> ZeroConfig <==", "Received message: " + message);
				if (message.startsWith("smart")) 
				{
					run = true;
					String theIPFromServer = message.split(" ")[1];
					thePortFromServer = message.split(" ")[2];
					android.util.Log.d("  ==> ZeroConfig <==", "Received corrcet key: " 
							+ message + "! Setting new Server adress: " + theIPFromServer + 
							" ");
					client.setIpAddressAndPort(theIPFromServer);
					threadRunning = true;
				}                  
			}               

			UDP_packet.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return Integer.parseInt(thePortFromServer);
		
	}
	
	private InetAddress getBroadcastAddress(Context context) throws IOException 
	{
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) 
		{
			return null;
		}
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];

		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);

		return InetAddress.getByAddress(quads);
	}
	
	
	private Error initConnection(int portToConnectTo) {

		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Try to init connection on port " + portToConnectTo);	
		
		client.getAndroidActivity().peep();
        InetAddress serverAddr;
        
		try {	
			//here you must put your computer's IP address.
			serverAddr = InetAddress.getByName(client.getIpAddress());
	        //create a socket to make the connection with the server
	        tcpSocket = new Socket(serverAddr, portToConnectTo);

		} catch (Exception e) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "ERROR --- Cannot connect to server at " + client.getIpAddress());	
			e.printStackTrace();
			return Error.ERR_TCP_CONNECTION;
		}

		
        try {
        	//OutputStream to smartOrder-Server
            outMessage = new DataOutputStream(tcpSocket.getOutputStream());
            //BufferedReader from smartOrder-Server
            inMessage = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));//
            inStream = tcpSocket.getInputStream();
            
        } catch (IOException e) {
        	android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Message ERROR");	
			e.printStackTrace();
			clientRunning = false;
		}
 
        return Error.ERR_OK;
	}
	
	public Error closeConnection() {
		clientRunning = false;
		threadRunning = false;
		
		return Error.ERR_OK;
	}
	
	public boolean clientConnected()  {
		return clientRunning;
	}
	
}

