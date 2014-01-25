package smart.order.server;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;

import smart.order.server.Command;

public class Main {
	
	public static void quickTest() {
		Log.info("TestString");
	}

	public static void main(String[] args) {


		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/smart_order_database?user=gadei&password=mastege");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			
			
			MainFrame mainFrame = new MainFrame();
			mainFrame.setVisible(true);
			
			
			
			
			while(!orderServer.clientConnected())
				Thread.sleep(25);
				
//			orderServer.sendMessageToClient("HEHEHHE::Test message to client\n");
//			orderServer.sendMessageToClient(Command.DEBUG_MSG.cmdTag() + "\n");
			//orderServer.sendMessageToClient(Command.STOP_CLIENT.cmdTag() + "\n");
			
			Thread.sleep(25000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		orderServer.stop();

	}

}
