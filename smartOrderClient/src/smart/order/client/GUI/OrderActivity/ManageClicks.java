package smart.order.client.GUI.OrderActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.database.AddOrder;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Order;
import android.view.View;
import android.widget.Toast;

public class ManageClicks
{
	private static final String TAG_OPEN = "1";
	
	public static void addItemToOrder(Order order, View v)
	{
		if(order != null)
		{
			int menuId = (Integer)v.getTag(R.id.TAG_ID);
			Vector<Food> food = SmartOrderClient.getInstance().getFood();
			Vector<Drink> drink = SmartOrderClient.getInstance().getDrink();

			if(v.getTag(R.id.TAG_MENU) == "food")
			{
				for(int i = 0; i < food.size(); i++)
				{
					if(food.elementAt(i).getId() == menuId)
					{
						order.addFoodToOrder(new Food(food.elementAt(i)));
					}
				}
			}
			if(v.getTag(R.id.TAG_MENU) == "drink")
			{
				for(int i = 0; i < drink.size(); i++)
				{
					if(drink.elementAt(i).getId() == menuId)
					{
						order.addDrinkToOrder(new Drink(drink.elementAt(i)));
					}
				}
			}
		}
		else
		{
			Toast.makeText(SmartOrderClient.getInstance().getOrderActivity(), "Bitte zuerst Bestellung erstellen!!!",
					Toast.LENGTH_LONG).show();
		}
	} 

	public static void sendOrder(Order order)
	{
		if(order != null)
		{
			String jsonOrder;
			try
			{
				jsonOrder = order.toJson();
			} 
			catch (Exception e)
			{}

			Map<String, String> map = new HashMap<String, String>();
			map.put("table", String.valueOf(order.getTable()));
			map.put("status", TAG_OPEN);

			String food = new String();
			for(int i = 0; i < order.getFoodItems().size(); i++)
			{
				food = food + order.getFoodItems().elementAt(i).getId() + ",";
			}
			
			String drink = new String();
			for(int i = 0; i < order.getDrinkItems().size(); i++)
			{
				drink = drink + order.getDrinkItems().elementAt(i).getId() + ",";
			}
			
			map.put("food", food);
			map.put("drink", drink);


			new AddOrder().execute(map);
		}
		else
		{
			Toast.makeText(SmartOrderClient.getInstance().getOrderActivity(), "Bitte zuerst Bestellung erstellen!!!",
					Toast.LENGTH_LONG).show();
		}
	} 
}

