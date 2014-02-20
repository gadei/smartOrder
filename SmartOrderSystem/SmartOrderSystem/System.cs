using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.IO;
using System.Runtime.Serialization.Json;
using SmartOrderSystem.Orders;
using System.Web.Script.Serialization;
using SmartOrderSystem.Database;

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

      GetFood hallo = new GetFood();
      hallo.getJsonMenu();

      //Set up Person object...
      Orders.Beer beer1 = new Orders.Beer(2.30f, 1);
      Orders.Beer beer2 = new Orders.Beer(3.30f, 2);
      Orders.Beer beer3 = new Orders.Beer(4.30f, 3);

      List<Orders.Drinks> ordera = new List<Orders.Drinks>();
      ordera.Add(beer1);
      ordera.Add(beer2);
      ordera.Add(beer3);

      var serializer = new JavaScriptSerializer();
      var serializedResult = serializer.Serialize(ordera);

      Console.WriteLine(serializedResult);

      byte[] theList1;// = ordera.ToJSON();

      //MemoryStream stream1 = new MemoryStream();
      //DataContractJsonSerializer ser = new DataContractJsonSerializer(order.GetType());
      //ser.WriteObject(stream1, order);

      //byte[] theObj = stream1.GetBuffer();

      ////Show the JSON output.
      //stream1.Position = 0;
      //StreamReader sr = new StreamReader(stream1);
      //Console.Write("JSON form of Person object: ");
      //Console.WriteLine(sr.ReadToEnd());

      ////Deserialize the JSON back into a new Person object.
      //stream1.Position = 0;
      //List<int> order1 = (List<int>)ser.ReadObject(stream1);
      ////List<Orders.Drinks> order1 = (List<Orders.Drinks>)ser.ReadObject(stream1);

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
