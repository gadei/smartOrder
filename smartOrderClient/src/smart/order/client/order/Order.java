package smart.order.client.order;

import java.lang.reflect.Type;
import java.util.Vector;

import smart.order.client.Error;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Order
{
	private Vector<Food> foodItems = new Vector<Food>();
	private Vector<Drink> drinkItems = new Vector<Drink>();
	
	private Vector<Vector> orderItems = new Vector<Vector>();
	
	private int table = 0;
	
	private Gson gson = new Gson();
	private String jsonSerialization = null;
	
	public Order(int table)
	{
		this.table = table;
		
		//Testobjects --> Ignore
		Food food = new Food(1, "ESSEN!!!!", 12f);
		Food food2 = new Food(1, "ESSEN!!!!", 12f);
		Food food1 = new Food(1, "ESSEN!!!!", 12f);
		
		Drink drink = new Drink(1, "TRINKEN!!!!", 12f);
		
		foodItems.add(food);
		foodItems.add(food1);
		foodItems.add(food2);
		drinkItems.add(drink);
		//End Testobjects
	}
	

	public String toJson()
	{
		if(!foodItems.isEmpty())
		{
			orderItems.add(foodItems);
		}
		if(!drinkItems.isEmpty())
		{
			orderItems.add(drinkItems);
		}
		
		if(!orderItems.isEmpty())
		{	
			jsonSerialization = gson.toJson(orderItems);
			
			//create valid Json
			jsonSerialization = "{\"table\":" + table + ",\"order\":" + jsonSerialization + "}";
		}
		
		return jsonSerialization;
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
