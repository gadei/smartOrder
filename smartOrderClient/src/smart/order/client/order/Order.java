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
		
		
		//Testobjects --> Ignore
		Food food = new Food(1, "ESSEN!!!!", 12f);
		Food food2 = new Food(3, "ESSEN!!!!", 12f);
		Food food1 = new Food(4, "ESSEN!!!!", 12f);
		
		Drink drink = new Drink(1, "TRINKEN!!!!", 12f);
		
		foodItems.add(food);
		foodItems.add(food1);
		foodItems.add(food2);
		drinkItems.add(drink);
		//End Testobject
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
