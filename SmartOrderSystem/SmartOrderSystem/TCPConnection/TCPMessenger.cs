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
    private ushort nextMsgID = 0;
    private Mutex nextMsgIDMutex;

    public TCPMessenger()
    {
      nextMsgIDMutex = new Mutex();
    }

    public byte[] prepareSendCmd(string theCommand)
    {
      MsgPreamble msgPreamble = new MsgPreamble(getNextFreeMessageID(), MsgTyp.CMD);
      byte[] cmdArray = StringToByteArray(theCommand + Command.EOF);

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
  }

  public class MsgPreamble
  {
    public ushort msgID { get; set; }
    public MsgProps msgProps { get; set; }
    public static int MSG_PREAMBLE_SIZE = 3;

    public MsgPreamble(ushort msgID, MsgProps msgProps)
    {
      this.msgID = msgID;
      this.msgProps = msgProps;
    }

    public MsgPreamble(ushort msgID, MsgTyp msgTyp)
    {
      this.msgID = msgID;
      this.msgProps = new MsgProps(msgTyp);
    }

    public MsgPreamble(byte[] preamble)
    {
      //get id
      msgID = BitConverter.ToUInt16(preamble, 0);

      //get props
      msgProps = new MsgProps(preamble[2]);

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
      byte[] msgPreamble = new byte[3];
      byte[] idArray = BitConverter.GetBytes(msgID);
      Array.Copy(idArray, msgPreamble, 2);


      return msgPreamble;
    }




  }

}
