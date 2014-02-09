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
  class Food
  {
    [DataMember]
    private string name = null;
    [DataMember]
    private float price = 0;

    public Food(string name, float price)
    {
      this.name = name;
      this.price = price;
    }

  }
}
