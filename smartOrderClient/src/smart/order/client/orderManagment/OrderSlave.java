package smart.order.client.orderManagment;

import smart.order.client.TCPConnection.TCPMessenger;
import smart.order.client.Command;

public class OrderSlave {

	private TCPMessenger tcpMessenger = null;
	
	public OrderSlave(TCPMessenger tcpMessenger) {
		this.tcpMessenger = tcpMessenger;
	}
	
	public void decodeCommandString(String command) {
		
		if(command.compareTo(Command.STILL_ALIVE) == 0)
			newStillAliveCommand();
	}
	
	private void newStillAliveCommand() {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received Still alive command - sending ACK to server");	
		tcpMessenger.prepareAndSendCmd(Command.ACK);
	}
	
}
