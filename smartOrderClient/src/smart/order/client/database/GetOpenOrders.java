package smart.order.client.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private static final String TAG_DRINK = "drink";
	private static final String TAG_FOOD = "food";
	private static final String TAG_TABLE = "table_nr";
	private static final String TAG_STATUS = "status";
	private static final String TAG_TIMESTAMP = "timestamp";

	private JSONParser jParser = new JSONParser();
	private JSONArray order;
	private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String,String>>();


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
		
		int table_nr = args[0];
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("table_nr", String.valueOf(table_nr)));
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
					String status = c.getString(TAG_STATUS);
					String table = c.getString(TAG_TABLE);
					String food = c.getString(TAG_FOOD);
					String drink = c.getString(TAG_DRINK);
					String timestamp = c.getString(TAG_TIMESTAMP);
					String order_id = c.getString(TAG_ID);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_STATUS, status);
					map.put(TAG_TABLE, table);
					map.put(TAG_FOOD, food);
					map.put(TAG_DRINK, drink);
					map.put(TAG_TIMESTAMP, timestamp);
					map.put(TAG_ID, order_id);

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



