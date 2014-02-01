using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;

namespace SmartOrderSystem.TCPConnection
{
  // State object for reading client data asynchronously
  public class StateObject
  {
    // Client  socket.
    public Socket workSocket = null;
    // Receive buffer.
    public byte[] buffer = new byte[TCPMessenger.MAX_MSG_SIZE];
    // Received data string.
    public StringBuilder sb = new StringBuilder();

    public int workSocketPort = -1;

    public void renewBuffer() {
      buffer = new byte[TCPMessenger.MAX_MSG_SIZE];
    }
  }
}
