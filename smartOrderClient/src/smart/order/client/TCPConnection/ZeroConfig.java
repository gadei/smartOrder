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
	private static final int UDP_PORT = 1418;

	public void run()
	{
		super.run();

		DatagramSocket UDP_packet;
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
				if (message.equals("smart")) 
				{
					run = true;
					client.setIpAddress(incoming.getAddress().toString().substring(1));
				}                  
			}               

			UDP_packet.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
}
