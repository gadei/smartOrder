package smart.order.server;

public class Main {

	public static void main(String[] args) {

		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		orderServer.initServer();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		orderServer.closeServer();

	}

}
