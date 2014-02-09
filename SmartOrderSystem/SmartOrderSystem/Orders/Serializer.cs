using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.IO;


namespace SmartOrderSystem.Orders
{
  static class Serializer
  {
    public static string ToJSON<T>(this T obj) where T : class
    {
      DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
      using (MemoryStream stream = new MemoryStream())
      {
        serializer.WriteObject(stream, obj);
        return Encoding.Default.GetString(stream.GetBuffer());
      }
    }

    public static byte[] OrderToJSON(Order order)
    {

      string theJSONString = "[";
       


      return null;
    }

    public static string drinksToJSON(List<Drinks> drinks)
    {

      string theJSONString = "[";
      foreach(Drinks drink in drinks) 
        theJSONString += drink.ToJSON() + "&";
      return theJSONString;
    }


    public static T FromJSON<T>(this T obj, byte[] jsonData) where T : class
    {
      using (MemoryStream stream = new MemoryStream(jsonData))
      {
        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
        return serializer.ReadObject(stream) as T;
      }
    }



  }
}
