package smart.order.client.TCPConnection;

public class MsgPreamble {

	public int msgID;
    public MsgProps msgProps;
    public static int MSG_PREAMBLE_SIZE = 3;

    public MsgPreamble(int msgID, MsgProps msgProps)
    {
      this.msgID = msgID;
      this.msgProps = msgProps;
    }

    public MsgPreamble(int msgID, byte msgTyp)
    {
      this.msgID = msgID;
      this.msgProps = new MsgProps(msgTyp);
    }

    public MsgPreamble(byte[] preamble)
    {
    	//get id
		byte[] id = new byte[2];
		System.arraycopy(preamble, 0, id, 0, 2);
		String helpStr = String.valueOf(id);
		msgID = Integer.parseInt(helpStr) & 0xFF;

		//get props
		msgProps = new MsgProps(preamble[2]);

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
      byte[] msgPreamble = new byte[3];
      String id = Integer.toString(msgID);
      
      byte[] idArray = id.getBytes();
      
      System.arraycopy(idArray, 0, msgPreamble, 0, 2);
      msgPreamble[2] = msgProps.getByteProps();


      return msgPreamble;
    }
}
