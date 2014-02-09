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
  class Order
  {
    //[DataMember]
    //private List<Food> foodOrders = null;

    [DataMember]
    private List<Drinks> drinksOrders = null;

    [DataMember]
    private int tableID = -1;

    public Order(int tableID)
    {
      //foodOrders = new List<Food>();
      drinksOrders = new List<Drinks>();

      this.tableID = tableID;
    }

    //public void addFoodOrder(Food foodToAdd)
    //{
    //  foodOrders.Add(foodToAdd);
    //}

    public void addDrinksOrder(Drinks drinksToAdd)
    {
      drinksOrders.Add(drinksToAdd);
    }

  }

}
