using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

namespace SmartOrderSystem.Orders
{

  [DataContract]
  class Drinks
  {
    [DataMember]
    public string name = null;
    [DataMember]
    public float price = 0;

    public Drinks(string name, float price)
    {
      this.name = name;
      this.price = price;
    }


  }
}
