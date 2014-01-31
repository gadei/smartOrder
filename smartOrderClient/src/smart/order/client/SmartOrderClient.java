package smart.order.client;

import android.app.Activity;
import smart.order.client.Command;
import smart.order.client.TCPConnection.TCPClient;

public class SmartOrderClient {

	private static SmartOrderClient instance = null;
	private FullscreenActivity androidActivity = null;
	private SmartOrderActivity smartOrderActivity = null;
	private TCPClient client = null;
	
	 public FullscreenActivity getAndroidActivity() {
		 return androidActivity;
	 }
	 
	 protected void setSmartOrderActivity(SmartOrderActivity act) {
		 smartOrderActivity = act;
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
	
	public void tcpClientClosed() {
		client = null;		
	}
	
	public Command decodeCommand(String msgFromServer) {
		
		if(!msgFromServer.contains("CMD_"))
			return null;
		
		if(msgFromServer.contains(Command.STOP_CLIENT.cmdTag()))
			return Command.STOP_CLIENT;
		else if(msgFromServer.contains(Command.DEBUG_MSG.cmdTag()))
			return Command.DEBUG_MSG;
		else if(msgFromServer.contains(Command.RECONNECT.cmdTag()))
			return Command.RECONNECT;
		
		return Command.STOP_CLIENT;
	}
	
	public boolean clientConnected()  {
		if(client == null)
			return false;
		
		return client.clientConnected();
	}
	
	public Activity getActivity() {
		return androidActivity;
	}
	
	public Error disconnectClient() {
		
		if(client != null)
			client.closeConnection();
		
		return Error.ERR_OK;
	}

}
