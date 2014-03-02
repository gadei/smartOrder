package smart.order.client.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import smart.order.client.SmartOrderClient;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GetOpenOrders extends AsyncTask<Integer, String, ArrayList<HashMap<String, String>>> 
{
	private static String url_drink = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/getOpenOrders.php";
	private ProgressDialog pDialog = null;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ORDER = "order";
	private static final String TAG_ID = "order_id";
	private static final String TAG_DRINK = "order_drink";
	private static final String TAG_FOOD = "order_food";
	private static final String TAG_TABLE = "order_table";
	private static final String TAG_STATUS = "order_status";
	private static final String TAG_TIMESTAMP = "order_timestamp";

	private JSONParser jParser = new JSONParser();
	private JSONArray order;
	private ArrayList<HashMap<String, Object>> orderList = new ArrayList<HashMap<String,Object>>();


	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getOrderActivity());
		pDialog.setMessage("Lade Bestellungen von Datenbank...");
		pDialog.setIndeterminate(false);
		pDialog.show();
	}


	protected ArrayList<HashMap<String, String>> doInBackground(Integer... args) 
	{
		if(args.length != 1)
		{
			return null;
		}
		
		int table = args[0];
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_table", String.valueOf(table)));
		// getting JSON string from URL

		try 
		{
			JSONObject json = jParser.makeHttpRequest(url_drink, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Order: ", json.toString());


			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1)
			{
				order = json.getJSONArray(TAG_ORDER);

				// looping through All Products
				for (int i = 0; i < order.length(); i++) 
				{
					JSONObject c = order.getJSONObject(i);

					// Storing each json item in variable
					String order_status = c.getString(TAG_STATUS);
					String order_table = c.getString(TAG_TABLE);
					String order_timestamp = c.getString(TAG_TIMESTAMP);
					String order_id = c.getString(TAG_ID);
					JSONArray jArrayDrink = c.getJSONArray(TAG_DRINK);
					JSONArray jArrayFood = c.getJSONArray(TAG_FOOD);
					
					Vector<Integer> order_drink = new Vector<Integer>();
					Vector<Integer> order_food = new Vector<Integer>();
					for(int j = 0; j < jArrayDrink.length(); j++)
					{
						order_drink.add(jArrayDrink.getInt(j));
					}
					for(int j = 0; j < jArrayFood.length(); j++)
					{
						order_food.add(jArrayFood.getInt(j));
					}
					
					// creating new HashMap
					HashMap<String, Object> map = new HashMap<String, Object>();

					// adding each child node to HashMap key => value
					map.put(TAG_STATUS, order_status);
					map.put(TAG_TABLE, order_table);
					map.put(TAG_TIMESTAMP, order_timestamp);
					map.put(TAG_ID, order_id);
					map.put(TAG_FOOD, order_food);
					map.put(TAG_DRINK, order_drink);

					// adding HashList to ArrayList
					orderList.add(map);
				}
			} 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(ArrayList<HashMap<String, String>> arrayList) 
	{		
		if(pDialog != null)
		{
			if(pDialog.isShowing())
			{
				pDialog.dismiss();
			}
		}
		SmartOrderClient.getInstance().getOrderActivity().setOpenOrderList(orderList);
		SmartOrderClient.getInstance().getOrderActivity().updateOpenOrderList();
	}
}



