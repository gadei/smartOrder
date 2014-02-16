package smart.order.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.order.Drink;
import smart.order.client.order.Food;

public class BuildMenu
{
	private SmartOrderActivity activity = null;
	private String json = null;
	
	private String[] foodItemsStringArray = null;
	private String[] drinkItemsStringArray = null;
	
	private Vector<Food> foodItemsVector = new Vector<Food>();
	private Vector<Drink> drinkItemsVector = new Vector<Drink>();
	
	public BuildMenu(SmartOrderActivity activity)
	{
		this.activity = activity;
		
		loadJSONFromAsset();
		getMenuArrays();
	}
	
	private void loadJSONFromAsset() 
	{
        try 
        {
            InputStream is = activity.getAssets().open("menu.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
	
	private void getMenuArrays()
	{
		JSONArray jArrayFood = null;
		JSONArray jArrayDrink = null;
		JSONObject jObject = null;
		
		try
		{
			jObject = new JSONObject(json);
			
			jArrayFood = jObject.getJSONArray("food");
			jArrayDrink = jObject.getJSONArray("drink");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		
		for (int i=0; i < jArrayFood.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayFood.getJSONObject(i);
		        
		        foodItemsVector.add(new Food(oneObject.getInt("id"), oneObject.getString("name"), oneObject.getDouble("price")));
		    } 
		    catch (JSONException e) {
		    }
		}
		
		for (int i=0; i < jArrayDrink.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayDrink.getJSONObject(i);
		        
		        drinkItemsVector.add(new Drink(oneObject.getInt("id"), oneObject.getString("name"), oneObject.getDouble("price")));
		    } catch (JSONException e) {
		    }
		}
	}
	
	public double getPriceFromId(int id)
	{
		for(int i = 0; i < drinkItemsVector.size(); i++)
		{
			if(drinkItemsVector.elementAt(i).getId() == id)
			{
				return drinkItemsVector.elementAt(i).getPrice();
			}
		}
		for(int i = 0; i < foodItemsVector.size(); i++)
		{
			if(foodItemsVector.elementAt(i).getId() == id)
			{
				return foodItemsVector.elementAt(i).getPrice();
			}
		}
		
		return -1;
	}
	
	public String getNameFromId(int id)
	{
		for(int i = 0; i < drinkItemsVector.size(); i++)
		{
			if(drinkItemsVector.elementAt(i).getId() == id)
			{
				return drinkItemsVector.elementAt(i).getName();
			}
		}
		for(int i = 0; i < foodItemsVector.size(); i++)
		{
			if(foodItemsVector.elementAt(i).getId() == id)
			{
				return foodItemsVector.elementAt(i).getName();
			}
		}
		
		return null;
	}
	
	public String[] getDrinkItemsStringArray()
	{
		if(drinkItemsStringArray == null)
		{
			drinkItemsStringArray = new String[drinkItemsVector.size()];
			
			for(int i = 0; i < drinkItemsVector.size(); i++)
			{
				drinkItemsStringArray[i] = drinkItemsVector.elementAt(i).getId() + " - " + drinkItemsVector.elementAt(i).getName();
			}
		}
		
		return drinkItemsStringArray;
	}
	
	public String[] getFoodItemsStringArray()
	{
		if(foodItemsStringArray == null)
		{
			foodItemsStringArray = new String[foodItemsVector.size()];
			
			for(int i = 0; i < foodItemsVector.size(); i++)
			{
				foodItemsStringArray[i] = foodItemsVector.elementAt(i).getId() + " - " + foodItemsVector.elementAt(i).getName();
			}
		}
		return foodItemsStringArray;
	}
}
