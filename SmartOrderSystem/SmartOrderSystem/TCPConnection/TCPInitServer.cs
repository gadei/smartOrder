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

  public class TCPInitServer
  {
    public static int TCP_INIT_PORT = 1419;
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

      IPEndPoint localEndPoint = new IPEndPoint(smartOrderServer.getWIFIIPAdress(), TCP_INIT_PORT);

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
          Log.info("Waiting for a connection...");
          listener.BeginAccept(
              new AsyncCallback(AcceptCallback),
              listener);

          if (serverRunning)
          {
            // Wait until a connection is made before continuing.
            Log.info("MainThread: Waiting for Connections");
            allDone.WaitOne();
            Log.info("MainThread: Not waiting any more");
          } 
        }

        Log.info("MainThread: Stopping Server; Closing connection");
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

      connectedClient.workSocketPort = smartOrderServer.getNextFreePortForWorkerServer();
      Send(handler, RECONNECT_MSG + ": Client use port " + connectedClient.workSocketPort + "\n");

    }

    public void ReadCallback(IAsyncResult ar)
    {

      if (serverRunning == false)
        return;

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

          if(content.CompareTo(Command.ACK) == 0)
            smartOrderServer.connectNewClientToWorkerServer(state.workSocketPort);

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
        Log.info("Sent " + bytesSent + " bytes to client.");

      }
      catch (Exception e)
      {
        Log.error(e.ToString());
      }
    }

    public void CloseServer() {

      Log.info("TCPInitServer: Command close thread!");
      serverRunning = false;
      allDone.Set();
 
    }
      
  }
}