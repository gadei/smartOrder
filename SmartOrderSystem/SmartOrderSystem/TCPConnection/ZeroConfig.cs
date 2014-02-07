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

        public ZeroConfig(SmartOrderServer smartOrderServer)
        {
            this.smartOrderServer = smartOrderServer;

            if (smartOrderServer.getWIFIIPAdress() == null)
                Log.error("Cannot Stream UDP");

        }

        public void startListener()
        {
            while(true)
            { 
                UdpClient UDP_packet = new UdpClient(UDP_PORT);
                UDP_packet.EnableBroadcast = true;
                IPEndPoint RemoteIpEndPoint = new IPEndPoint(smartOrderServer.getWIFIIPAdress(), UDP_PORT);
                IPAddress from_addr = null;
                Boolean run = false;
                while (!run)
                {
                    Log.info("Listening for UDP Ping");
                    Byte[] receiveBytes = UDP_packet.Receive(ref RemoteIpEndPoint);
                    string returnData = Encoding.UTF8.GetString(receiveBytes);
                    if (returnData.ToString() == "order")
                    {
                        run = true;
                    }
                    from_addr = RemoteIpEndPoint.Address;
                }
            
                IPEndPoint ipEndPoint = new IPEndPoint(from_addr, UDP_PORT);
                Byte[] sendBytes = Encoding.UTF8.GetBytes("smart");
                UDP_packet.Send(sendBytes, sendBytes.Length, ipEndPoint);
                UDP_packet.Close();
            }
        }

    }
}
