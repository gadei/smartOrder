using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SmartOrderSystem.Utils;

namespace SmartOrderSystem.TCPConnection
{
  

  public class TCPMessenger
  {

    // Size of receive buffer.
    public const int MAX_MSG_SIZE = ushort.MaxValue;
    
    private ushort nextMsgID = 12;
    private Mutex nextMsgIDMutex;


    public TCPMessenger()
    {
      nextMsgIDMutex = new Mutex();
    }

    public byte[] prepareSendCmd(string theCommand)
    {
      byte[] cmdArray = StringToByteArray(theCommand + Command.EOF);

      if (cmdArray.Length + MsgPreamble.MSG_PREAMBLE_SIZE > MAX_MSG_SIZE)
      {
        Log.error("Command must not be longer than " + (MAX_MSG_SIZE - MsgPreamble.MSG_PREAMBLE_SIZE) + " bytes");
        return null;
      }

      MsgPreamble msgPreamble = new MsgPreamble( (ushort)(cmdArray.Length), getNextFreeMessageID(), MsgTyp.CMD);
      
      byte[] msgToClient = new byte[cmdArray.Length + MsgPreamble.MSG_PREAMBLE_SIZE];
      Array.Copy(msgPreamble.getBytePreamble(), msgToClient, MsgPreamble.MSG_PREAMBLE_SIZE);
      Array.Copy(cmdArray, 0, msgToClient, MsgPreamble.MSG_PREAMBLE_SIZE, cmdArray.Length);

      return msgToClient;
    }

    private ushort getNextFreeMessageID()
    {
      ushort nextID = 0;

      nextMsgIDMutex.WaitOne();
      nextID = nextMsgID;
      nextMsgID += 2;
      if (nextMsgID >= ushort.MaxValue)
        nextMsgID = 0;
      nextMsgIDMutex.ReleaseMutex();

      return nextID;
    }

    //TODO: just for testing! change the type to void!
    public string ReadMessage(byte[] messageFromClient)
    {
      byte[] payload = null;
      byte[] preamble = new byte[MsgPreamble.MSG_PREAMBLE_SIZE];
      Array.Copy(messageFromClient, preamble, MsgPreamble.MSG_PREAMBLE_SIZE);

      if(messageFromClient.Length > MsgPreamble.MSG_PREAMBLE_SIZE) 
      {
        payload = new byte[messageFromClient.Length - MsgPreamble.MSG_PREAMBLE_SIZE];
        Array.Copy(messageFromClient, MsgPreamble.MSG_PREAMBLE_SIZE, 
          payload, 0, messageFromClient.Length - MsgPreamble.MSG_PREAMBLE_SIZE);
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
    private string decodeCmdMessage(byte[] payload)
    {
      string msg = ByteArrayToString(payload);
      return msg.Replace(Command.EOF, "");
    }

    private byte[] StringToByteArray(string str)
    {
      ASCIIEncoding enc = new ASCIIEncoding();
      return enc.GetBytes(str);
    }

    private string ByteArrayToString(byte[] arr)
    {
      ASCIIEncoding enc = new ASCIIEncoding();
      return enc.GetString(arr);
    }

  }

  public enum MsgTyp
  {
    CMD,
    FILE,
    OBJ,
    ACK,
    UNDEV
  }

  public class MsgProps
  {
    public MsgTyp msgTyp { get; set; }

    public MsgProps(byte msgProps)
    {
      byte typ = (byte)((byte)(msgProps & 0xE0) >> 5);
      this.msgTyp = (MsgTyp)typ;
    }

    public MsgProps(MsgTyp msgTyp)
    {
      this.msgTyp = msgTyp;
    }

    public string toString()
    {
      byte typ = (byte)msgTyp;
      typ = (byte)((byte)(typ << 5) & 0xE0);

      return typ.ToString();
    }

    public byte getByteProps()
    {
      byte typ = (byte)msgTyp;
      typ = (byte)((byte)(typ << 5) & 0xE0);

      return typ;
    }
  }


  public class MsgPreamble
  {
    public ushort msgSize { get; set; }
    public ushort msgID { get; set; }
    public MsgProps msgProps { get; set; }
    public static ushort MSG_PREAMBLE_SIZE = 5;

    public MsgPreamble(ushort payloadSize, ushort msgID, MsgTyp msgTyp)
    {
      this.msgSize = (ushort)(payloadSize + MSG_PREAMBLE_SIZE);
      this.msgID = msgID;
      this.msgProps = new MsgProps(msgTyp);
    }

    public MsgPreamble(byte[] preamble)
    {
      //get size
      msgSize = BitConverter.ToUInt16(preamble, 0);

      //get id
      msgID = BitConverter.ToUInt16(preamble, 2);

      //get props
      msgProps = new MsgProps(preamble[4]);

    }

    public string toString()
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


      return msgID.ToString() + "|" + msgTypString;
    }

    public byte[] getBytePreamble()
    {
      byte[] msgPreamble = new byte[MSG_PREAMBLE_SIZE];

      byte[] sizeArray  = BitConverter.GetBytes(msgSize);
      byte[] idArray    = BitConverter.GetBytes(msgID);

      Array.Copy(sizeArray, msgPreamble, 2);
      Array.Copy(idArray, 0, msgPreamble, 2, 2);
      msgPreamble[4] = msgProps.getByteProps();

      return msgPreamble;
    }




  }

}
