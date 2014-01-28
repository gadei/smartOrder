using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using SmartOrderSystem.Utils;

namespace SmartOrderSystem.TCPConnection
{

  // State object for reading client data asynchronously
  public class StateObject
  {
    // Client  socket.
    public Socket workSocket = null;
    // Size of receive buffer.
    public const int BufferSize = 1024;
    // Receive buffer.
    public byte[] buffer = new byte[BufferSize];
    // Received data string.
    public StringBuilder sb = new StringBuilder();
  }

  public class TCPInitServer
  {
    private static int TCP_INIT_PORT = 1419;
    private static int TCP_NEXT_FREE_PORT = 1420;
    private static String RECONNECT_MSG = "CMD_RECONNECT_ON_NEW_PORT";

    private volatile bool serverRunning = false;

    private StateObject connectedClient = null;
    private SmartOrderServer smartOrderServer = null;

    // Thread signal.
    public ManualResetEvent allDone = new ManualResetEvent(false);

    public TCPInitServer(SmartOrderServer smartOrderServer)
    {
      this.smartOrderServer = smartOrderServer;
    }

    public void StartServer()
    {

      serverRunning = true;

      // Data buffer for incoming data.
      byte[] bytes = new Byte[1024];

      // Establish the local endpoint for the socket.
      // The DNS name of the computer
      // running the listener is "host.contoso.com".
      IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
      IPAddress ipAddress = ipHostInfo.AddressList[1]; //TODO: This must be improved!!!
      IPEndPoint localEndPoint = new IPEndPoint(ipAddress, TCP_INIT_PORT);

      // Create a TCP/IP socket.
      Socket listener = new Socket(AddressFamily.InterNetwork,
          SocketType.Stream, ProtocolType.Tcp);

      // Bind the socket to the local endpoint and listen for incoming connections.
      try
      {
        listener.Bind(localEndPoint);
        listener.Listen(100);

        while (serverRunning)
        {
          // Set the event to nonsignaled state.
          allDone.Reset();

          // Start an asynchronous socket to listen for connections.
          Console.WriteLine("Waiting for a connection...");
          listener.BeginAccept(
              new AsyncCallback(AcceptCallback),
              listener);

          if (serverRunning)
          {
            // Wait until a connection is made before continuing.
            Console.WriteLine("MainThread: Waiting for Connections");
            allDone.WaitOne();
            Console.WriteLine("MainThread: Not waiting any more");
          } 
        }

        Console.WriteLine("MainThread: Stopping Server; Closing connection");
        connectedClient.workSocket.Shutdown(SocketShutdown.Both);
        connectedClient.workSocket.Close();

      }
      catch (Exception e)
      {
        Console.WriteLine(e.ToString());
      }

    }

    public void AcceptCallback(IAsyncResult ar)
    {
      // Signal the main thread to continue.
      Console.WriteLine("AcceptCallback: No a client connects");
      allDone.Set();
      Console.WriteLine("AcceptCallback: Main thread is allowed to work again");
      
      // Get the socket that handles the client request.
      Socket listener = (Socket)ar.AsyncState;
      Socket handler = listener.EndAccept(ar);

      // Create the state object.
      StateObject state = new StateObject();
      state.workSocket = handler;
      handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
          new AsyncCallback(ReadCallback), state);

      connectedClient = state;

      Console.WriteLine("Client Connected!");
      Send(handler, RECONNECT_MSG + ": Client use port " + TCP_NEXT_FREE_PORT + "\n");

    }

    public void ReadCallback(IAsyncResult ar)
    {
      String content = String.Empty;

      // Retrieve the state object and the handler socket
      // from the asynchronous state object.
      StateObject state = (StateObject)ar.AsyncState;
      Socket handler = state.workSocket;

      // Read data from the client socket. 
      int bytesRead = handler.EndReceive(ar);

      if (bytesRead > 0)
      {
        // There  might be more data, so store the data received so far.
        state.sb.Append(Encoding.ASCII.GetString(
            state.buffer, 0, bytesRead));

        // Check for end-of-file tag. If it is not there, read 
        // more data.
        content = state.sb.ToString();
        if (content.IndexOf("<EOF>") > -1)
        {
          // All the data has been read from the 
          // client. Display it on the console.
          Console.WriteLine("Read {0} bytes from socket. \n Data : {1}",
              content.Length, content);


          //TODO: REMOVE THIS!
          smartOrderServer.connectNewClientToWorkerServer(-1);

        }
        else
        {
          // Not all data received. Get more.
          handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
          new AsyncCallback(ReadCallback), state);
        }
      }
    }

    private void Send(Socket handler, String data)
    {
      // Convert the string data to byte data using ASCII encoding.
      byte[] byteData = Encoding.ASCII.GetBytes(data);

      // Begin sending the data to the remote device.
      handler.BeginSend(byteData, 0, byteData.Length, 0,
          new AsyncCallback(SendCallback), handler);
    }

    private void SendCallback(IAsyncResult ar)
    {
      try
      {
        // Retrieve the socket from the state object.
        Socket handler = (Socket)ar.AsyncState;

        // Complete sending the data to the remote device.
        int bytesSent = handler.EndSend(ar);
        Console.WriteLine("Sent {0} bytes to client.", bytesSent);

      }
      catch (Exception e)
      {
        Console.WriteLine(e.ToString());
      }
    }

    public void CloseServer() {

      Log.info("TCPInitServer: Command close thread!");
      serverRunning = false;
      allDone.Set();
 
    }
      
  }
}