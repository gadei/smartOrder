package smart.order.client.TCPConnection;

public class MsgPreamble {

	public int msgSize = 0;
	public int msgID;
    public MsgProps msgProps;
    public static byte MSG_PREAMBLE_SIZE = 5;

    public MsgPreamble(int msgID, MsgProps msgProps)
    {
      this.msgID = msgID;
      this.msgProps = msgProps;
    }

    public MsgPreamble(int payloadSize, int msgID, byte msgTyp)
    {
      this.msgSize = payloadSize + MSG_PREAMBLE_SIZE;
      this.msgID = msgID;
      this.msgProps = new MsgProps(msgTyp);
    }

    public MsgPreamble(byte[] preamble)
    {
    	//get size
		byte[] size = new byte[4];
		System.arraycopy(preamble, 0, size, 0, 2);
		size[2] = 0;
		size[3] = 0;
		
		msgSize = byteArrayToInt(size);
    	
		
    	//get id
		byte[] id = new byte[4];
		System.arraycopy(preamble, 2, id, 0, 2);
		id[2] = 0;
		id[3] = 0;

		msgID = byteArrayToInt(id);

		//get props
		msgProps = new MsgProps(preamble[4]);

    }

    public String toString()
    {
      String msgTypString = "";
      
      switch (msgProps.msgTyp)
      {
        case MsgTyp.ACK:
          msgTypString = "ACK";
          break;
        case MsgTyp.CMD:
          msgTypString = "CMD";
          break;
        case MsgTyp.FILE:
          msgTypString = "FILE";
          break;
        case MsgTyp.OBJ:
          msgTypString = "OBJ";
          break;
        default:
          msgTypString = "UNDEV";
          break;
      }


      return String.valueOf(msgID) + "|" + msgTypString;
    }

    public byte[] getBytePreamble()
    {
    	
      byte[] msgPreamble = new byte[MSG_PREAMBLE_SIZE];
      byte[] sizeArray 	= intToByteArray(msgSize);
      byte[] idArray 	= intToByteArray(msgID);
      
      System.arraycopy(sizeArray, 	0, msgPreamble, 0, 2);
      System.arraycopy(idArray, 	0, msgPreamble, 2, 2);
      msgPreamble[4] = msgProps.getByteProps();

      return msgPreamble;
    }
    
    
    private static int byteArrayToInt(byte[] b) 
    {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[3 - i] & 0x000000FF) << shift;
        }
        return value;
    }
    
    public static byte[] intToByteArray(int a)
    {
        byte[] ret = new byte[4];
        ret[0] = (byte) (a & 0xFF);   
        ret[1] = (byte) ((a >> 8) & 0xFF);   
        ret[2] = (byte) ((a >> 16) & 0xFF);   
        ret[3] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }
}
