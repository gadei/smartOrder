package smart.order.client.order;

import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Order
{
	private Vector<Food> foodItems = new Vector<Food>();
	private Vector<Drink> drinkItems = new Vector<Drink>();
	
	private int table = 0;
	private int orderId = 0;
	
	private JSONObject jsonObject = new JSONObject();
	
	public Order(int table, int orderId)
	{
		this.table = table;
		this.orderId = orderId;
	}
		

	public String toJson() throws JSONException
	{		
		JSONArray jArrayFood = new JSONArray();
		for(int i = 0; i < foodItems.size(); i++)
		{
			JSONObject styleJSON = new JSONObject();
			
			styleJSON.put("id", foodItems.elementAt(i).getId());
			styleJSON.put("name", foodItems.elementAt(i).getName());
			styleJSON.put("price", foodItems.elementAt(i).getPrice());
			styleJSON.put("quantity", foodItems.elementAt(i).getQuantity());	
		
			jArrayFood.put(styleJSON);
		}		

		JSONArray jArrayDrink = new JSONArray();
		for(int i = 0; i < drinkItems.size(); i++)
		{
			JSONObject styleJSON = new JSONObject();
			
			styleJSON.put("id", drinkItems.elementAt(i).getId());
			styleJSON.put("name", drinkItems.elementAt(i).getName());
			styleJSON.put("price", drinkItems.elementAt(i).getPrice());
			styleJSON.put("quantity", drinkItems.elementAt(i).getQuantity());	
		
			jArrayDrink.put(styleJSON);
		}		
		
		jsonObject.put("table", table);
		jsonObject.put("oderID", orderId);
		
		jsonObject.put("drink", jArrayDrink);
		jsonObject.put("food", jArrayFood);
		
		return jsonObject.toString();
	}
	
	
	public void setTable(int table)
	{
		this.table = table;
	}
	public int getTable()
	{
		return table;
	}
	public void addFoodToOrder(Food foodItem)
	{
		this.foodItems.addElement(foodItem);
	}
	public void addDrinkToOrder(Drink drinkItem)
	{
		this.drinkItems.addElement(drinkItem);
	}
}
