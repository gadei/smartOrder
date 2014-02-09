using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using SmartOrderSystem.Utils;

namespace SmartOrderSystem.Orders
{
  [DataContract]
  class Beer : Drinks
  {
    [DataMember]
    public int size = 0;

    public Beer(float price, int beerSize) : base("beer", price)
    {
      size = beerSize;
    }

  }
}
