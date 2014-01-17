package smart.order.client;

import smart.order.client.Command;

public class SmartOrderClient {

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
		
		return Command.STOP_CLIENT;
	}
	
	public boolean clientConnected()  {
		if(client == null)
			return false;
		
		return client.clientConnected();
	}

}
