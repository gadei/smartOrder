using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SmartOrderSystem.Utils;
using System.Threading;

namespace SmartOrderSystem.TCPConnection
{


  public class SmartOrderServer
  {

    private volatile bool threadRunning = false;
    private TCPInitServer initServer = null;
    private Thread initServerThread = null;
    private ManualResetEvent waitOnServer;

    public SmartOrderServer(ManualResetEvent waitOnServer)
    {
      this.waitOnServer = waitOnServer;

      initServer = new TCPInitServer(this);
      initServerThread = new Thread(initServer.StartServer);
      Log.info("TcpInitServer and Thread created");
    }

    public void runSmartOrderServers() {

      threadRunning = true;

      this.initServerThread.Start();
      Log.info("Init server started");

      while(threadRunning) {
        ;
      }

      Log.info("Closing smartServers main thread");
      waitOnServer.Set();

    }

    public void closeSmartOrderServers() {

      Log.info("Closing all smartOder server");
      initServer.CloseServer();

      threadRunning = false;
    }

    public void connectNewClientToWorkerServer(int workerPort)
    {
      //TODO: Replace this!
      closeSmartOrderServers();
    }


  }



}
