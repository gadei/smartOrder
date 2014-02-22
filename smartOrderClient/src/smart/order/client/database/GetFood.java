package smart.order.client.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.order.Food;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GetFood extends AsyncTask<String, String, String> 
{
	private static String url_food = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/getFood.php";
	private ProgressDialog pDialog = null;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_FOOD = "food";
	private static final String TAG_ID = "food_id";
	private static final String TAG_NAME = "name";
	private static final String TAG_PRICE = "price";

	private JSONParser jParser = new JSONParser();
	private JSONArray food = null;

	private ArrayList<HashMap<String, String>> foodList = new ArrayList<HashMap<String,String>>();

	private String[] foodArray = null;
	private Vector<Food> foodVector = null;

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getSmartOrderActivity());
		pDialog.setMessage("Lade Speisen/Getränke von Datenbank...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}


	protected String doInBackground(String... args) 
	{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		try 
		{		
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_food, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Food: ", json.toString());

			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1)
			{
				// products found
				// Getting Array of Products
				food = json.getJSONArray(TAG_FOOD);

				// looping through All Products
				for (int i = 0; i < food.length(); i++) {
					JSONObject c = food.getJSONObject(i);

					// Storing each json item in variable
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_NAME);
					String price = c.getString(TAG_PRICE);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_ID, id);
					map.put(TAG_NAME, name);
					map.put(TAG_PRICE, price);

					// adding HashList to ArrayList
					foodList.add(map);
				}
			} 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	private void createStringArray()
	{
		foodArray = new String[foodList.size()];

		for(int i = 0; i < foodList.size(); i++)
		{
			foodArray[i] = foodList.get(i).get(TAG_NAME);
		}
	}
	private void createFoodVector()
	{
		foodVector = new Vector<Food>();

		for(int i = 0; i < foodList.size(); i++)
		{
			foodVector.add(new Food(Integer.parseInt(foodList.get(i).get(TAG_ID)), foodList.get(i).get(TAG_NAME), Double.parseDouble(foodList.get(i).get(TAG_PRICE))));
		}
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String file_url) 
	{
		createFoodVector();

		SmartOrderClient.getInstance().setFood(foodVector);

		if(pDialog != null)
		{
			if(pDialog.isShowing())
			{
				pDialog.dismiss();
			}
		}

		if(SmartOrderClient.getInstance().getFood() == null || SmartOrderClient.getInstance().getFood().isEmpty())
		{
			SmartOrderClient.getInstance().getSmartOrderActivity().noDatabaseConnection();
		}
	}

}
