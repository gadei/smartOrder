using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartOrderSystem.Utils
{
  public class Command {
	  
    public static string STOP_CLIENT  = "CMD_STOP_CLIENT";
	  public static string DEBUG_MSG    = "CMD_DBG_MSG";
    public static string ACK          = "CMD_ACK";
	  public static string RECONNECT    = "CMD_RECONNECT_ON_NEW_PORT";
    public static string STILL_ALIVE  = "CMD_STILL_ALIVE";

    public static string EOF = "<EOF>";

  }
}
