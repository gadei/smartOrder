package smart.order.client.TCPConnection;

import java.util.concurrent.Semaphore;

import smart.order.client.*;
import smart.order.client.Error;
import smart.order.client.orderManagment.OrderSlave;


public class TCPMessenger {

	private static int USHORT_MAX_VAL = 65535;
	public static final int MAX_MSG_SIZE = 1024;
	
	
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
		return prepareAndSendCmd(theCommand, getNextFreeMessageID());
	}
	
	
	public Error prepareAndSendCmd(String theCommand, int reqID)
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Sending cmd #" + theCommand + "# with msgID " + reqID + " to server");

		byte[] cmdArray = StringToByteArray(theCommand + TCPClient.EOF);
		
		if(cmdArray.length + MsgPreamble.MSG_PREAMBLE_SIZE > MAX_MSG_SIZE) {
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "Command must not be longer than " 
					+ (MAX_MSG_SIZE - MsgPreamble.MSG_PREAMBLE_SIZE) + " bytes");
			return Error.ERR_UNKNOWN;
		}
		
		MsgPreamble msgPreamble = new MsgPreamble(cmdArray.length, reqID, MsgTyp.CMD);
	
	    byte[] msgToClient = new byte[cmdArray.length + MsgPreamble.MSG_PREAMBLE_SIZE];
	    System.arraycopy(msgPreamble.getBytePreamble(), 0, msgToClient, 0, MsgPreamble.MSG_PREAMBLE_SIZE);
	    System.arraycopy(cmdArray, 0, msgToClient, MsgPreamble.MSG_PREAMBLE_SIZE, cmdArray.length);
	    
	    tcpClient.sendDataToServer(msgToClient);

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
	
	
	private MsgPreamble getMsgPreamble(byte[] messageFromServer) {
		
		byte[] preamble = new byte[MsgPreamble.MSG_PREAMBLE_SIZE];
	    System.arraycopy(messageFromServer, 0, preamble, 0, MsgPreamble.MSG_PREAMBLE_SIZE);
	
	    return new MsgPreamble(preamble);
	}
	
	
	public int getMsgSize(byte[] messageFromServer) {

		return getMsgPreamble(messageFromServer).msgSize;
	}
	

	public void ReadMessage(byte[] messageFromServer)
	{

	    byte[] payload = null;
	    MsgPreamble msgPreamble = getMsgPreamble(messageFromServer);
	    
	    android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Reading new message with id " + msgPreamble.msgID);
	    
	    if(messageFromServer.length > MsgPreamble.MSG_PREAMBLE_SIZE) 
	    	  {
	    	  payload = new byte[messageFromServer.length - MsgPreamble.MSG_PREAMBLE_SIZE];
	    	  System.arraycopy(messageFromServer, MsgPreamble.MSG_PREAMBLE_SIZE, 
	    			  payload, 0, messageFromServer.length - MsgPreamble.MSG_PREAMBLE_SIZE);
	    	  }
	
	    
	
	    switch (msgPreamble.msgProps.msgTyp)
	    {
	    	  case MsgTyp.CMD:
	    		  decodeCmdMessage(payload, msgPreamble.msgID);
	    		  break;
	    }

	}
	

	private void decodeCmdMessage(byte[] payload, int reqID)
	{
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Decoded message header: New Command received!");
		String cmd = ByteArrayToString(payload);
		cmd = cmd.replaceAll(TCPClient.EOF, "");
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Received #" + cmd + "# command!");
		oderSlave.decodeCommandString(cmd, reqID);
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
