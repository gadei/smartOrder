package smart.order.server.tests;

import static org.junit.Assert.*;
import smart.order.server.Error;

import org.junit.Test;

import smart.order.server.SmartOrderServer;

public class InitTestCase {

	@Test
	public void test() {
		
		SmartOrderServer orderServer = SmartOrderServer.getInstance();
		
		assertTrue("init server failed!\n", orderServer != null);
		
		

	}

}
