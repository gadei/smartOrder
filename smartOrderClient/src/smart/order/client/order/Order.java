package smart.order.client.order;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.TCPConnection.TCPClient;
import smart.order.client.database.AddOrder;

import android.os.AsyncTask;
import android.view.View;


public class Order
{
	private static final String TAG_OPEN = "1";
	private Vector<Food> foodItems = new Vector<Food>();
	private Vector<Drink> drinkItems = new Vector<Drink>();
	
	private int table = 0;
	private int orderId = 0;

	private JSONObject jsonObject = new JSONObject();

	public Order(int table)
	{
		this.table = table;
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

			jArrayFood.put(styleJSON);
		}		

		JSONArray jArrayDrink = new JSONArray();
		for(int i = 0; i < drinkItems.size(); i++)
		{
			JSONObject styleJSON = new JSONObject();

			styleJSON.put("id", drinkItems.elementAt(i).getId());
			styleJSON.put("name", drinkItems.elementAt(i).getName());
			styleJSON.put("price", drinkItems.elementAt(i).getPrice());	

			jArrayDrink.put(styleJSON);
		}		

		jsonObject.put("table", table);
		jsonObject.put("oderID", orderId);

		jsonObject.put("drink", jArrayDrink);
		jsonObject.put("food", jArrayFood);

		return jsonObject.toString();
	}


	public Menu addClickedItemToOrder(View view)
	{
		int menuId = (Integer)view.getTag(R.id.TAG_ID);
		Vector<Food> foodVector = SmartOrderClient.getInstance().getFood();
		Vector<Drink> drinkVector = SmartOrderClient.getInstance().getDrink();

		if(view.getTag(R.id.TAG_MENU) == "food")
		{
			for(Food food : foodVector)
			{
				if(food.getId() == menuId)
				{
					Food newFood = new Food(food);
					addFoodToOrder(newFood);
					return newFood;
				}
			}
		}
		if(view.getTag(R.id.TAG_MENU) == "drink")
		{
			for(Drink drink : drinkVector)
			{
				if(drink.getId() == menuId)
				{
					Drink newDrink = new Drink(drink);
					addDrinkToOrder(newDrink);
					return newDrink;
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void sendOrder()
	{
		String jsonOrder;
		try
		{
			jsonOrder = toJson();
			TCPClient client = SmartOrderClient.getInstance().getClient();
			//-----------------------------------------------------------------
			//TODO: Sent json String to C# Server 
			//-----------------------------------------------------------------
		} 
		catch (Exception e)
		{}

		Map<String, String> map = new HashMap<String, String>();
		map.put("order_table", String.valueOf(getTable()));
		map.put("order_status", TAG_OPEN);

		String food = new String();
		for(int i = 0; i < getFoodItems().size(); i++)
		{
			food = food + getFoodItems().elementAt(i).getId() + ",";
		}

		String drink = new String();
		for(int i = 0; i < getDrinkItems().size(); i++)
		{
			drink = drink + getDrinkItems().elementAt(i).getId() + ",";
		}

		map.put("order_food", food);
		map.put("order_drink", drink);
		map.put("order_price", String.valueOf(Math.round(getSum() * 100.0) / 100.0));

		new AddOrder().execute(map);
	}



	public Vector<Food> getFoodItems()
	{
		return foodItems;
	}
	public Vector<Drink> getDrinkItems()
	{
		return drinkItems;
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
	
	
	public double getSum()
	{
		double sum = 0;
		for(int i = 0; i < foodItems.size(); i++)
		{
			sum += foodItems.elementAt(i).getPrice();
		}
		for(int i = 0; i < drinkItems.size(); i++)
		{
			sum += drinkItems.elementAt(i).getPrice();
		}

		return sum;
	}
	
	public void removeDrink(int id)
	{		
		for(Drink drink : drinkItems)
		{
			if(drink.getId() == id)
			{
				drinkItems.remove(drink);
				return;
			}
		}
	}
	
	public void removeFood(int id)
	{
		for(Food food : foodItems)
		{
			if(food.getId() == id)
			{
				foodItems.remove(food);
				return;
			}
		}
	}
}
