package smart.order.client;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildMenu
{
	private SmartOrderActivity activity = null;
	private String json = null;
	private JSONObject jObject = null;
	
	private JSONArray jArrayFood = null;
	private JSONArray jArrayDrink = null;
	
	private String[] foodItems = null;
	private String[] drinkItems = null;
	
	public BuildMenu(SmartOrderActivity activity)
	{
		this.activity = activity;
		
		loadJSONFromAsset();
		getMenu();
	}
	
	private String loadJSONFromAsset() 
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
            return null;
        }
        return json;
    }
	
	private void getMenu()
	{
		try
		{
			jObject = new JSONObject(json);
			
			jArrayFood = jObject.getJSONArray("food");
			jArrayDrink = jObject.getJSONArray("drink");
			
			foodItems = new String[jArrayFood.length()];
			drinkItems = new String[jArrayDrink.length()];
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		
		
		for (int i=0; i < jArrayFood.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayFood.getJSONObject(i);
		        // Pulling items from the array
		        foodItems[i] = oneObject.getString("name");
		    } catch (JSONException e) {
		    }
		}
		
		for (int i=0; i < jArrayDrink.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayDrink.getJSONObject(i);
		        // Pulling items from the array
		        drinkItems[i] = oneObject.getString("name");
		    } catch (JSONException e) {
		    }
		}
	}
	
	public String[] getDrinkItems()
	{
		return drinkItems;
	}
	public String[] getFoodItems()
	{
		return foodItems;
	}
}
