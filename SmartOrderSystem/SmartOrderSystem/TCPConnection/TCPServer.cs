using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using SmartOrderSystem.Utils;
using System.Web.Script.Serialization;

namespace SmartOrderSystem.TCPConnection
{

  public class TCPServer
  {
    public int socketPort;

    private SmartOrderServer smartOrderServer = null;
    private Thread myThread = null;
    private StateObject connectedClient = null;
    private TCPMessenger tcpMessenger = null;
    private Mutex sendMutex = null;

    private volatile bool serverRunning = false;

    // Thread signal.
    public ManualResetEvent allDone = new ManualResetEvent(false);

    public TCPServer(SmartOrderServer smartOrderServer, int startSocketOnPort)
    {
      socketPort = startSocketOnPort;
      this.smartOrderServer = smartOrderServer;
      this.tcpMessenger = new TCPMessenger(this);
      this.sendMutex = new Mutex();
    }

    public void setMyThread(Thread myThread) 
    {
      this.myThread = myThread;
    }

    public void StartServer()
    {

      serverRunning = true;

      IPEndPoint localEndPoint = new IPEndPoint(smartOrderServer.getWIFIIPAdress(), socketPort);

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
          //check connection periodically
          Thread.Sleep(3000);
          //SendMsgToClient(Command.STILL_ALIVE);
          //byte[] msg = tcpMessenger.prepareSendCmd(Command.STILL_ALIVE);

          //Set up Person object...
          Orders.Beer beer1 = new Orders.Beer(2.30f, 1);
          Orders.Beer beer2 = new Orders.Beer(3.30f, 2);
          Orders.Beer beer3 = new Orders.Beer(4.30f, 3);

          List<Orders.Drinks> ordera = new List<Orders.Drinks>();
          ordera.Add(beer1);
          ordera.Add(beer2);
          ordera.Add(beer3);

          var serializer = new JavaScriptSerializer();
          var serializedResult = serializer.Serialize(beer1);
          //var serializedResult = serializer.Serialize(ordera);

          byte[] msg = tcpMessenger.prepareSendCmd(serializedResult);
          SendData(msg);
        }

        connectedClient.workSocket.Shutdown(SocketShutdown.Both);
        connectedClient.workSocket.Close();
        
        Log.info("MainThread: Stopping Server on port " + socketPort + "; Closing connection");

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

      handler.BeginReceive(connectedClient.buffer, 0, TCPMessenger.MAX_MSG_SIZE, 0,
          new AsyncCallback(ReadCallback), connectedClient);

      Log.info("Client Connected!");

      connectedClient.workSocketPort = socketPort;
    }

    public void ReadCallback(IAsyncResult ar)
    {

      if (serverRunning == false)
        return;

      Log.info("ReadCallback: Reading Message");
      String content = String.Empty;

      // Retrieve the state object and the handler socket
      // from the asynchronous state object.
      StateObject state = (StateObject)ar.AsyncState;
      Socket handler = state.workSocket;

      try
      {
          // Read data from the client socket. 
          int bytesRead = handler.EndReceive(ar);

          if (bytesRead > 0)
          {
              byte[] dataFromClient = new byte[bytesRead];
              Array.Copy(state.buffer, dataFromClient, bytesRead);

              if (tcpMessenger.getMsgSize(dataFromClient) == bytesRead)
              {
                  string theClientMsg = tcpMessenger.ReadMessage(dataFromClient);
                  Log.info("TCPMessenger: Reading Message:\n" + theClientMsg);
              }
              else
              {
                  //TODO: Append
                  throw new NotImplementedException();
              }

              handler.BeginReceive(connectedClient.buffer, 0, TCPMessenger.MAX_MSG_SIZE, 0,
                new AsyncCallback(ReadCallback), connectedClient);
          }
      }
      catch {}
    }

    protected void SendData(byte[] msgToClient)
    {

      sendMutex.WaitOne();
      int bytesSent = connectedClient.workSocket.Send(msgToClient);
      sendMutex.ReleaseMutex();
      if (bytesSent != msgToClient.Length)
        throw new Exception("Error in TCPServer ... DAMMIT! Should be blocking till all byte were sent");

    }


    public void CloseServer()
    {
      Log.info("TCPServer: Command close server on " + socketPort + "!");
      serverRunning = false;
    }

  }
}