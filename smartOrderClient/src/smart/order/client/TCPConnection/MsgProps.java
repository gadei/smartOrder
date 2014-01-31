package smart.order.client.TCPConnection;

public class MsgProps {
	
	public byte msgTyp;

    public MsgProps(byte msgProps)
    {
      msgTyp = (byte)((byte)(msgProps & 0xE0) >> 5);
    }

    public String toString()
    {
      byte typ = (byte)((byte)(msgTyp << 5) & 0xE0);
      return String.valueOf(typ);
    }
	
    public byte getByteProps()
    {
      byte typ = (byte)msgTyp;
      typ = (byte)((byte)(typ << 5) & 0xE0);

      return typ;
    }
	
}
