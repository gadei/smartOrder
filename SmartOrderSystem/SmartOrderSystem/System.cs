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

      Console.SetWindowSize(120, 30);
      Console.WriteLine("Starting SmartOder System! ushort" + ushort.MaxValue);

      //TCPConnection.TCPMessenger messenger = new TCPConnection.TCPMessenger();

      //string theMsg = "Das ist ein Teststring";
      //byte[] msg = messenger.prepareSendCmd(theMsg);
      //string ret = messenger.ReadMessage(msg);
      //Console.WriteLine("Testen des Messengers:\nDer Teststring=#" + theMsg + "#\nDie Antwort=#" + ret + "#");



      PrintService.PrinterInterface printer = new PrintService.PrinterInterface();
      //printer.printLabel();

      ManualResetEvent waitOnServer = new ManualResetEvent(false);

      TCPConnection.SmartOrderServer servers = new TCPConnection.SmartOrderServer(waitOnServer);
      Thread serversThread = new Thread(servers.runSmartOrderServers);

      serversThread.Start();

      waitOnServer.WaitOne();

      Thread.Sleep(10000);

      servers.closeSmartOrderServers();
      serversThread.Join();


      Console.WriteLine("Stopping SmartOder System!\nPress any Key...");
      Console.ReadKey();
    }
  }
}
