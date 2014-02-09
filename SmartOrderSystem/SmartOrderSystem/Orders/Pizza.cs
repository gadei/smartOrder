using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartOrderSystem.Orders
{
  class Pizza : Food
  {
    private string ingredients = "add_a_ingredientslist_here";
    private bool normalSize = true;

    public Pizza(string name, float price, bool isNormalSize) : base(name, price)
    {
      normalSize = isNormalSize;
    }

    public Pizza(string name, float price)
      : base(name, price)
    {
  
    }


  }
}
