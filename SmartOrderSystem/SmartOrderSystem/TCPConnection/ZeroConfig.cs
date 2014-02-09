using SmartOrderSystem.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace SmartOrderSystem.TCPConnection
{
    class ZeroConfig
    {
        SmartOrderServer smartOrderServer;
        private static int UDP_PORT = 1418;
        private volatile bool threadRunning = true;
        private UdpClient UDP_packet = null;

        public ZeroConfig(SmartOrderServer smartOrderServer)
        {
            this.smartOrderServer = smartOrderServer;

            if (smartOrderServer.getWIFIIPAdress() == null)
                Log.error("Cannot Stream UDP");

        }

        public void startListener()
        {
            while(threadRunning)
            {
                UDP_packet = new UdpClient(UDP_PORT);
                UDP_packet.EnableBroadcast = true;
                IPEndPoint RemoteIpEndPoint = new IPEndPoint(smartOrderServer.getWIFIIPAdress(), UDP_PORT);
                IPAddress from_addr = null;
                bool run = false;
                while (threadRunning && !run)
                {
                    Log.info("Listening for UDP Ping");

                    try
                    {
                      Byte[] receiveBytes = UDP_packet.Receive(ref RemoteIpEndPoint);
                      string returnData = Encoding.UTF8.GetString(receiveBytes);
                      Log.info("Received data: " + returnData);
                      if (returnData.CompareTo("order") == 0)
                      {
                        run = true;
                      }
                      from_addr = RemoteIpEndPoint.Address;
                    
                    
                    } catch(Exception e) {
                      if(threadRunning)
                        Log.error("Error in ZeroConfig: " + e.StackTrace);
                    }

                    
                }

                if (threadRunning)
                {
                  IPEndPoint ipEndPoint = new IPEndPoint(from_addr, UDP_PORT);
                  Byte[] sendBytes = Encoding.UTF8.GetBytes("smart " + smartOrderServer.getWIFIIPAdress().ToString());
                  UDP_packet.Send(sendBytes, sendBytes.Length, ipEndPoint);
                  UDP_packet.Close();
                }
            }

            Log.info("UDP thread stopped!");
        }

        public void close()
        {
          threadRunning = false;
          UDP_packet.Close();
          Log.info("Closing UDP thread!");
        }

    }
}
