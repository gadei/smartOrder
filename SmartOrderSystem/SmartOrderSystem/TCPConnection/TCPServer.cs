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

  public class TCPServer
  {
    public static int socketPort;

    private volatile bool serverRunning = false;

    private Thread myThread = null;

    private StateObject connectedClient = null;
    private SmartOrderServer smartOrderServer = null;

    // Thread signal.
    public ManualResetEvent allDone = new ManualResetEvent(false);

    public TCPServer(SmartOrderServer smartOrderServer, int startSocketOnPort)
    {
      socketPort = startSocketOnPort;
      this.smartOrderServer = smartOrderServer;
    }

    public void setMyThread(Thread myThread) 
    {
      this.myThread = myThread;
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
      IPEndPoint localEndPoint = new IPEndPoint(ipAddress, socketPort);

      // Create a TCP/IP socket.
      Socket listener = new Socket(AddressFamily.InterNetwork,
          SocketType.Stream, ProtocolType.Tcp);

      // Bind the socket to the local endpoint and listen for incoming connections.
      try
      {
        listener.Bind(localEndPoint);
        listener.Listen(100);

        // Set the event to nonsignaled state.
        allDone.Reset();

        // Start an asynchronous socket to listen for connections.
        Log.info("Waiting for a connection...");
        listener.BeginAccept(
            new AsyncCallback(AcceptCallback),
            listener);

        Log.info("MainThread: Waiting for Client to connect on worker socket");
        allDone.WaitOne();
        Log.info("MainThread: Client connected to worker socket");

        while (serverRunning)
        {
          //TODO: maybe check connection periodically
          Thread.Sleep(1000);
        }

        Log.info("MainThread: Stopping Server on port " + socketPort + "; Closing connection");
        connectedClient.workSocket.Shutdown(SocketShutdown.Both);
        connectedClient.workSocket.Close();

      }
      catch (Exception e)
      {
        Log.error(e.ToString());
      }

    }

    public void AcceptCallback(IAsyncResult ar)
    {
      // Signal the main thread to continue.
      Log.info("AcceptCallback: No a client connects");
      allDone.Set();
      Log.info("AcceptCallback: Main thread is allowed to work again");

      // Get the socket that handles the client request.
      Socket listener = (Socket)ar.AsyncState;
      Socket handler = listener.EndAccept(ar);

      // Create the state object.
      connectedClient = new StateObject();
      connectedClient.workSocket = handler;

      handler.BeginReceive(connectedClient.buffer, 0, StateObject.BufferSize, 0,
          new AsyncCallback(ReadCallback), connectedClient);

      Log.info("Client Connected!");

      connectedClient.workSocketPort = socketPort;
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
        if (content.IndexOf(Command.EOF) > -1)
        {
          // All the data has been read from the 
          // client. Display it on the console.
          Log.info("Read" + content.Length + " bytes from socket. \n Data : " + content);

          content = content.Replace(Command.EOF, "");
        }
        else
        {
          // Not all data received. Get more.
          handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
          new AsyncCallback(ReadCallback), state);
        }
      }
    }

    public void SendMsgToClient(String data)
    {
      Send(connectedClient.workSocket, data);
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
        Log.info("Sent " + bytesSent + " bytes to client.");

      }
      catch (Exception e)
      {
        Log.info(e.ToString());
      }
    }

    public void CloseServer()
    {

      Log.info("TCPInitServer: Command close thread!");
      serverRunning = false;
      allDone.Set();

    }

  }
}