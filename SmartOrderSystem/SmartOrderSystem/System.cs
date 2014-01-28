using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace SmartOrderSystem
{
  class System
  {
    static void Main(string[] args)
    {
      Console.WriteLine("Starting SmartOder System!");

      PrintService.PrinterInterface printer = new PrintService.PrinterInterface();
      //printer.printLabel();


      ManualResetEvent waitOnServer = new ManualResetEvent(false);

      TCPConnection.SmartOrderServer servers = new TCPConnection.SmartOrderServer(waitOnServer);
      Thread serversThread = new Thread(servers.runSmartOrderServers);

      serversThread.Start();

      waitOnServer.WaitOne();

      Console.WriteLine("Stopping SmartOder System!\nPress any Key...");
      Console.ReadKey();
    }
  }
}
