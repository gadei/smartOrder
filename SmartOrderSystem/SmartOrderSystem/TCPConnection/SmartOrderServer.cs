using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SmartOrderSystem.Utils;
using System.Threading;
using System.Collections.Concurrent;

namespace SmartOrderSystem.TCPConnection
{


  public class SmartOrderServer
  {

    private volatile bool threadRunning = false;
    private TCPInitServer initServer = null;
    private Thread initServerThread = null;
    private ManualResetEvent waitOnServer;

    private Dictionary<int, TCPServer> workerServers = null;

    private static Mutex nextFreePortMutex = new Mutex();

    private int nextFreePortForWorkerServer = -1;

    public SmartOrderServer(ManualResetEvent waitOnServer)
    {
      this.waitOnServer = waitOnServer;
      workerServers = new Dictionary<int, TCPServer>();

      nextFreePortForWorkerServer = TCPInitServer.TCP_INIT_PORT + 1;

      initServer = new TCPInitServer(this);
      initServerThread = new Thread(initServer.StartServer);
      Log.info("TcpInitServer and Thread created");
    }

    public void runSmartOrderServers() {

      threadRunning = true;

      this.initServerThread.Start();
      Log.info("Init server started");

      while(threadRunning) {
        Thread.Sleep(100);
      }

      Log.info("Closing smartServers main thread");
     

    }

    public void closeSmartOrderServers() {

      Log.info("Closing all smartOder server");
      initServer.CloseServer();

      foreach (TCPServer server in workerServers.Values)
        server.CloseServer();

      threadRunning = false;
    }

    public int getNextFreePortForWorkerServer()
    {     
      nextFreePortMutex.WaitOne();
      int freePort = nextFreePortForWorkerServer;
      nextFreePortForWorkerServer++;
      nextFreePortMutex.ReleaseMutex();

      return freePort;
    }

    public void connectNewClientToWorkerServer(int workerPort)
    {
      TCPServer server = new TCPServer(this, workerPort);
      Thread serverThread = new Thread(server.StartServer);
      server.setMyThread(serverThread);
      Log.info("TcpServer on port " + workerPort + " and Thread created");

      workerServers.Add(workerPort, server);

      Log.info("Starting TcpServer on port " + workerPort + "!");
      serverThread.Start();

      waitOnServer.Set();
    }


  }



}
