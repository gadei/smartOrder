package smart.order.server;

import static org.junit.Assert.assertTrue;

public class Main {

	public static void main(String[] args) {


		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		try {
			
			Thread.sleep(1000);
			
//			orderServer.sendMessageToClient("HEHEHHE::Test message to client\n");
			
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		orderServer.stop();

	}

}
