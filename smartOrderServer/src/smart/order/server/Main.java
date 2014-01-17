package smart.order.server;

import smart.order.server.Command;

public class Main {

	public static void main(String[] args) {


		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		try {
			
			while(!orderServer.clientConnected())
				Thread.sleep(25);
				
			orderServer.sendMessageToClient("HEHEHHE::Test message to client\n");
			orderServer.sendMessageToClient(Command.DEBUG_MSG.cmdTag() + "\n");
			//orderServer.sendMessageToClient(Command.STOP_CLIENT.cmdTag() + "\n");
			
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		orderServer.stop();

	}

}
