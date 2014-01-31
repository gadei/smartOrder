package smart.order.client.TCPConnection;

import java.util.concurrent.Semaphore;



public class TCPMessenger {

	private static int USHORT_MAX_VAL = 65535;
	private int nextMsgID = 1;
	private Semaphore nextMsgIDMutex;
	
	
	public TCPMessenger()
	{
		nextMsgIDMutex = new Semaphore(1);
	}
	
	public byte[] prepareSendCmd(String theCommand)
	{
		MsgPreamble msgPreamble = new MsgPreamble(getNextFreeMessageID(), MsgTyp.CMD);
	    byte[] cmdArray = StringToByteArray(theCommand + TCPClient.EOF);
	
	    byte[] msgToClient = new byte[cmdArray.length + MsgPreamble.MSG_PREAMBLE_SIZE];
	    System.arraycopy(msgPreamble.getBytePreamble(), 0, msgToClient, 0, MsgPreamble.MSG_PREAMBLE_SIZE);
	    System.arraycopy(cmdArray, 0, msgToClient, MsgPreamble.MSG_PREAMBLE_SIZE, cmdArray.length);

	  return msgToClient;
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
	
	//TODO: just for testing! change the type to void!
	public String ReadMessage(byte[] messageFromClient)
	{
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
	
	  //TODO: just for testing! remove this var!
	  String ret = "";
	
	  switch (msgPreamble.msgProps.msgTyp)
	  {
	    case MsgTyp.CMD:
	      ret = decodeCmdMessage(payload);
	      break;
	  }
	
	  return ret;
	}
	
	//TODO: just for testing! change the type to void!
	private String decodeCmdMessage(byte[] payload)
	{
	  String msg = ByteArrayToString(payload);
	  msg = msg.replaceAll(TCPClient.EOF, "");
	  return msg;
	}
	
	private byte[] StringToByteArray(String str)
	{
	  return str.getBytes();
	}
	
	private String ByteArrayToString(byte[] arr)
	{
	  return String.valueOf(arr);
	}
}
