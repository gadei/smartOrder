package smart.order.server;

public class SmartOrderTCPClient {

	
	private TCPServer tcpServer = null;
	private int tcpPort = 0;
	
	public SmartOrderTCPClient(int tcpPort) {
		
		this.tcpPort = tcpPort;
		Log.info("Creating SmartOrderTCPClient! Starting new TCP server on port " + tcpPort + " \n");
		this.tcpServer = new TCPServer(tcpPort);
		
		//now start the server on tcpPort
		tcpServer.start();
	}
	
	public int getConnectedPort() {
		return tcpPort;
	}
	
	public Error sendMessageToClient(String msg) {
	
		Error err = Error.ERR_OK;
		
		do {
			
			err = tcpServer.sendMessageToClient(msg);
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				err = Error.ERR_UNKNOWN;
				e.printStackTrace();
			}
		
		} while(err == Error.ERR_MSG_QUEUE_FULL);
	
		
		return err;
	}

	
	
	
}
