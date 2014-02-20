package smart.order.client.TCPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import smart.order.client.SmartOrderClient;

public class ZeroConfig extends Thread
{
	private SmartOrderClient client = SmartOrderClient.getInstance(null);
	

	public void run()
	{
		super.run();

		
	}

	
}
