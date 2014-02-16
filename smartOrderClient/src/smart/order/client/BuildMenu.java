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
	
	private String[] foodItemsStringArray = null;
	private String[] drinkItemsStringArray = null;
	
	public BuildMenu(SmartOrderActivity activity)
	{
		this.activity = activity;
		
		loadJSONFromAsset();
		getMenuStringArray();
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
	
	private void getMenuStringArray()
	{
		try
		{
			jObject = new JSONObject(json);
			
			jArrayFood = jObject.getJSONArray("food");
			jArrayDrink = jObject.getJSONArray("drink");
			
			foodItemsStringArray = new String[jArrayFood.length()];
			drinkItemsStringArray = new String[jArrayDrink.length()];
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
		        foodItemsStringArray[i] = oneObject.getString("name");
		        foodItemsStringArray[i] = oneObject.getInt("id") + " - " + foodItemsStringArray[i];
		    } catch (JSONException e) {
		    }
		}
		
		for (int i=0; i < jArrayDrink.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayDrink.getJSONObject(i);
		        // Pulling items from the array
		        drinkItemsStringArray[i] = oneObject.getString("name");
		        drinkItemsStringArray[i] = oneObject.getInt("id") + " - " + drinkItemsStringArray[i];
		    } catch (JSONException e) {
		    }
		}
	}
	
	public double getPriceFromId(int id)
	{
		for (int i=0; i < jArrayFood.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayFood.getJSONObject(i);

		        if(id == oneObject.getInt("id"))
		        {
		        	return oneObject.getDouble("price");
		        }
		    } catch (JSONException e) {
		    }
		}
		
		for (int i=0; i < jArrayDrink.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayDrink.getJSONObject(i);
		        
		        if(id == oneObject.getInt("id"))
		        {
		        	return oneObject.getDouble("price");
		        }
		    } catch (JSONException e) {
		    }
		}
		
		return -1;
	}
	
	public String getNameFromId(int id)
	{
		for (int i=0; i < jArrayFood.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayFood.getJSONObject(i);

		        if(id == oneObject.getInt("id"))
		        {
		        	return oneObject.getString("name");
		        }
		    } catch (JSONException e) {
		    }
		}
		
		for (int i=0; i < jArrayDrink.length(); i++)
		{
		    try {
		        JSONObject oneObject = jArrayDrink.getJSONObject(i);
		        
		        if(id == oneObject.getInt("id"))
		        {
		        	return oneObject.getString("name");
		        }
		    } catch (JSONException e) {
		    }
		}
		
		return null;
	}
	
	public String[] getDrinkItemsStringArray()
	{
		return drinkItemsStringArray;
	}
	public String[] getFoodItemsStringArray()
	{
		return foodItemsStringArray;
	}
}
