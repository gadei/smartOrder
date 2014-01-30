using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SmartOrderSystem.Utils;
using System.Threading;
using System.Collections.Concurrent;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;

namespace SmartOrderSystem.TCPConnection
{

  class ThreadServerTupel
  {
    public Thread thread { get; set; }
    public TCPServer server { get; set; }

    public ThreadServerTupel(Thread thread, TCPServer server)
    {
      this.thread = thread;
      this.server = server;
    }
  }

  public class SmartOrderServer
  {

    private volatile bool threadRunning = false;
    private TCPInitServer initServer = null;
    private Thread initServerThread = null;
    private ManualResetEvent waitOnServer;
    private IPAddress wifiIPAdress = null;

    private Dictionary<int, ThreadServerTupel> workerServers = null;

    private static Mutex nextFreePortMutex = new Mutex();

    private int nextFreePortForWorkerServer = -1;

    public SmartOrderServer(ManualResetEvent waitOnServer)
    {
      this.waitOnServer = waitOnServer;
      workerServers = new Dictionary<int, ThreadServerTupel>();

      nextFreePortForWorkerServer = TCPInitServer.TCP_INIT_PORT + 1;

      initServer = new TCPInitServer(this);
      initServerThread = new Thread(initServer.StartServer);

      Log.info("TcpInitServer and Thread created");

      readWIFIIPAdress();
      if (wifiIPAdress == null)
        Log.error("No WIFI IP adress found! System won't work properly!");

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
      initServerThread.Join();
      Log.info("Init server closed and worker thread finished");

      foreach (ThreadServerTupel tupel in workerServers.Values) {
        tupel.server.CloseServer();
        tupel.thread.Join();
      }
      Log.info("All worker server closed and worker thread finished");

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

      workerServers.Add(workerPort, new ThreadServerTupel(serverThread, server));

      Log.info("Starting TcpServer on port " + workerPort + "!");
      serverThread.Start();

      waitOnServer.Set();
    }

    public IPAddress getWIFIIPAdress()
    {
      return wifiIPAdress;
    }

    private void readWIFIIPAdress()
    {
      foreach(NetworkInterface ni in NetworkInterface.GetAllNetworkInterfaces())
      {
        if(ni.NetworkInterfaceType == NetworkInterfaceType.Wireless80211)
        {
          Console.WriteLine(ni.Name);
          foreach (UnicastIPAddressInformation ip in ni.GetIPProperties().UnicastAddresses)
          {
            if (ip.Address.AddressFamily == AddressFamily.InterNetwork)
            {
              wifiIPAdress = ip.Address;
            }
          }
        }  
      }

    }


  }



}
