package smart.order.client.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.SmartOrderClient;
import smart.order.client.order.Drink;
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

public class GetDrink extends AsyncTask<String, String, String> 
{
	private static String url_drink = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/getDrink.php";
	private ProgressDialog pDialog = null;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DRINK = "drink";
	private static final String TAG_ID = "drink_id";
	private static final String TAG_NAME = "drink_name";
	private static final String TAG_PRICE = "drink_price";

	private JSONParser jParser = new JSONParser();
	private JSONArray drink = null;

	private ArrayList<HashMap<String, String>> drinkList = new ArrayList<HashMap<String,String>>();

	private String[] drinkArray = null;
	private Vector<Drink> drinkVector = null;

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getSmartOrderActivity());
		pDialog.setMessage("Lade Speisen/Getr�nke von Datenbank...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}


	protected String doInBackground(String... args) 
	{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// getting JSON string from URL

		try 
		{
			JSONObject json = jParser.makeHttpRequest(url_drink, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Drink: ", json.toString());


			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1)
			{
				// products found
				// Getting Array of Products
				drink = json.getJSONArray(TAG_DRINK);

				// looping through All Products
				for (int i = 0; i < drink.length(); i++) 
				{
					JSONObject c = drink.getJSONObject(i);

					// Storing each json item in variable
					String drink_id = c.getString(TAG_ID);
					String drink_name = c.getString(TAG_NAME);
					String drink_price = c.getString(TAG_PRICE);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_ID, drink_id);
					map.put(TAG_NAME, drink_name);
					map.put(TAG_PRICE, drink_price);

					// adding HashList to ArrayList
					drinkList.add(map);
				}
			} 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	private void createDrinkVector()
	{
		drinkVector = new Vector<Drink>();

		for(int i = 0; i < drinkList.size(); i++)
		{
			drinkVector.add(new Drink(Integer.parseInt(drinkList.get(i).get(TAG_ID)), drinkList.get(i).get(TAG_NAME), Double.parseDouble(drinkList.get(i).get(TAG_PRICE))));
		}
	}

	protected void onPostExecute(String file_url) 
	{		
		createDrinkVector();

		SmartOrderClient.getInstance().setDrink(drinkVector);

		if(pDialog != null)
		{
			if(pDialog.isShowing())
			{
				pDialog.dismiss();
			}
		}

		if(SmartOrderClient.getInstance().getDrink() == null || SmartOrderClient.getInstance().getDrink().isEmpty())
		{
			SmartOrderClient.getInstance().getSmartOrderActivity().noDatabaseConnection();
		}
	}

}
