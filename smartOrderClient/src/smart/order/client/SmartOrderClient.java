package smart.order.client;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;

import java.net.Socket;



public class SmartOrderClient {

	
	private static int TCP_PORT = 1419;
	private static String TCP_SERVER_IP = "10.0.0.45";
	
	private static SmartOrderClient instance = null;
	
	private FullscreenActivity androidActivity = null;

	private TCPClient client = null;
	
	 protected FullscreenActivity getAndroidActivity() {
		 return androidActivity;
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
		
		if(client != null) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Connecting smartOrder client to server failed: Client already connected!");	
			return;
		}
		
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Connecting smartOrder client to server!");	
		
		client = new TCPClient(this);
		client.start();
		
	}
	
}
