package smart.order.client;

import java.util.Vector;

import android.app.Activity;
import smart.order.client.Command;
import smart.order.client.GUI.Main.FullscreenActivity;
import smart.order.client.GUI.Main.SmartOrderActivity;
import smart.order.client.GUI.OpenOrderActivity.OpenOrderActivity;
import smart.order.client.GUI.OrderActivity.OrderActivity;
import smart.order.client.TCPConnection.TCPClient;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;

public class SmartOrderClient {

	private static SmartOrderClient instance = null;
	private FullscreenActivity fullscreenActivity = null;
	private SmartOrderActivity smartOrderActivity = null;
	private OrderActivity orderActivity = null;
	private OpenOrderActivity openOrderActivity = null;
	
	private TCPClient client = null;
	private String ipAddress = null;
	
	private Vector<Food> food = null;
	private Vector<Drink> drink = null;


	private SmartOrderClient() 
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Creating smartOrder client");		
	}

	public static SmartOrderClient getInstance() 
	{
		if(instance == null) 
		{
			instance = new SmartOrderClient();
		}

		return instance;
	}

	public FullscreenActivity getFullscreenActivity() 
	{
		return this.fullscreenActivity;
	}

	public void setFullscreenActivity(FullscreenActivity fullscreenActivity)
	{
		this.fullscreenActivity = fullscreenActivity;
	}

	public SmartOrderActivity getSmartOrderActivity()
	{
		return this.smartOrderActivity;
	}

	public void setSmartOrderActivity(SmartOrderActivity smartOrderActivity) 
	{
		this.smartOrderActivity = smartOrderActivity;
	}

	public String getIpAddress() 
	{
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}

	public OrderActivity getOrderActivity()
	{
		return orderActivity;
	}

	public void setOrderActivity(OrderActivity orderActivity)
	{
		this.orderActivity = orderActivity;
	}
	
	public OpenOrderActivity getOpenOrderActivity()
	{
		return openOrderActivity;
	}

	public void setOpenOrderActivity(OpenOrderActivity openOrderActivity)
	{
		this.openOrderActivity = openOrderActivity;
	}

	public Vector<Food> getFood()
	{
		return food;
	}

	public void setFood(Vector<Food> food)
	{
		this.food = food;
	}

	public Vector<Drink> getDrink()
	{
		return drink;
	}

	public void setDrink(Vector<Drink> drink)
	{
		this.drink = drink;
	}

	public void initConnection() 
	{

		if(client != null) 
		{
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Connecting smartOrder client to server failed: Client already connected!");	
			return;
		}

		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Connecting smartOrder client to server!");	

		client = new TCPClient(this);
		client.start();
	}

	public void tcpClientClosed() 
	{
		client = null;		
	}

	public boolean clientConnected()  
	{
		if(client == null)
		{
			return false;
		}

		return client.clientConnected();
	}


	public Error disconnectClient() 
	{

		if(client != null)
			client.closeConnection();

		return Error.ERR_OK;
	}

	public TCPClient getClient()
	{
		return client;
	}

}
