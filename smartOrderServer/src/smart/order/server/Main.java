package smart.order.server;

public class Main {

	public static void main(String[] args) {

		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		orderServer.initServer();

	}

}
