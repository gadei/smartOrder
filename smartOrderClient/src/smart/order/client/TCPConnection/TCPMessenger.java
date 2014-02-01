package smart.order.client.TCPConnection;

import java.util.concurrent.Semaphore;

import smart.order.client.*;
import smart.order.client.Error;
import smart.order.client.orderManagment.OrderSlave;


public class TCPMessenger {

	private static int USHORT_MAX_VAL = 65535;
	private int nextMsgID = 1;
	private Semaphore nextMsgIDMutex;
	
	private TCPClient tcpClient = null;
	private OrderSlave oderSlave = null;
	
	public TCPMessenger(TCPClient tcpClient)
	{
		nextMsgIDMutex = new Semaphore(1);
		this.tcpClient = tcpClient;
		this.oderSlave = new OrderSlave(this);
	}
	
	public Error prepareAndSendCmd(String theCommand)
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Sending #" + theCommand + "# command to server");
		
		MsgPreamble msgPreamble = new MsgPreamble(getNextFreeMessageID(), MsgTyp.CMD);
	    byte[] cmdArray = StringToByteArray(theCommand + TCPClient.EOF);
	
	    byte[] msgToClient = new byte[cmdArray.length + MsgPreamble.MSG_PREAMBLE_SIZE];
	    System.arraycopy(msgPreamble.getBytePreamble(), 0, msgToClient, 0, MsgPreamble.MSG_PREAMBLE_SIZE);
	    System.arraycopy(cmdArray, 0, msgToClient, MsgPreamble.MSG_PREAMBLE_SIZE, cmdArray.length);
	    
	    tcpClient.sendDataToServer(cmdArray);

	    return Error.ERR_OK;
	}
	
	private int getNextFreeMessageID()
	{
	  int nextID = 0;
	  
	  try {
		  nextMsgIDMutex.acquire();
		  nextID = nextMsgID;
		  nextMsgID += 2;
		  if (nextMsgID >= USHORT_MAX_VAL)
		    nextMsgID = 0;
		  nextMsgIDMutex.release();

		} catch(InterruptedException ie) {
		  // ...
		}
	
	
	  return nextID;
	}
	

	public void ReadMessage(byte[] messageFromClient)
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Reading new message!");
		
	    byte[] payload = null;
	    byte[] preamble = new byte[MsgPreamble.MSG_PREAMBLE_SIZE];
	    System.arraycopy(messageFromClient, 0, preamble, 0, MsgPreamble.MSG_PREAMBLE_SIZE);
	
	    if(messageFromClient.length > MsgPreamble.MSG_PREAMBLE_SIZE) 
	    	  {
	    	  payload = new byte[messageFromClient.length - MsgPreamble.MSG_PREAMBLE_SIZE];
	    	  System.arraycopy(messageFromClient, MsgPreamble.MSG_PREAMBLE_SIZE, 
	    			  payload, 0, messageFromClient.length - MsgPreamble.MSG_PREAMBLE_SIZE);
	    	  }
	
	    MsgPreamble msgPreamble = new MsgPreamble(preamble);
	
	    switch (msgPreamble.msgProps.msgTyp)
	    {
	    	  case MsgTyp.CMD:
	    		  decodeCmdMessage(payload);
	    		  break;
	    }

	}
	

	private void decodeCmdMessage(byte[] payload)
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Decoded message header: New Command received!");
		String cmd = ByteArrayToString(payload);
		cmd = cmd.replaceAll(TCPClient.EOF, "");
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received #" + cmd + "# command!");
		oderSlave.decodeCommandString(cmd);
	}
	
	private byte[] StringToByteArray(String str)
	{
	  return str.getBytes();
	}
	
	private String ByteArrayToString(byte[] data)
	{
		StringBuilder sb = new StringBuilder(data.length);
	    for (int i = 0; i < data.length; ++ i) {
	        if (data[i] < 0) throw new IllegalArgumentException();
	        sb.append((char) data[i]);
	    }
	    return sb.toString();

	}
}
