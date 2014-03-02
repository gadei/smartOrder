package smart.order.client.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.SmartOrderClient;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AddOrder extends AsyncTask<Map<String, String>, String, String> {

	private ProgressDialog pDialog = null;
	private JSONParser jsonParser = new JSONParser();
	
	private static String url_create_order = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/createOrder.php";
	
	private static final String TAG_SUCCESS = "success";
	
	private int success;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getOrderActivity());
		pDialog.setMessage("Bestellung aufgeben...");
		pDialog.setIndeterminate(false);
		pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(Map<String, String>... args) 
	{
		if(args.length != 1)
		{
			return null;
		}
		
		int order_table = Integer.valueOf(args[0].get("order_table"));
		int order_status = Integer.valueOf(args[0].get("order_status"));;
		String order_food = args[0].get("order_food");
		String order_drink = args[0].get("order_drink");
		double order_price = Double.valueOf(args[0].get("order_price"));

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_table", String.valueOf(order_table)));
		params.add(new BasicNameValuePair("order_status", String.valueOf(order_status)));
		params.add(new BasicNameValuePair("order_food", order_food));
		params.add(new BasicNameValuePair("order_drink", order_drink));
		params.add(new BasicNameValuePair("order_price", String.valueOf(order_price)));

		// getting JSON Object
		// Note that create product url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(url_create_order,
				"GET", params);

		// check log cat fro response

		// check for success tag

		try
		{
			success = json.getInt(TAG_SUCCESS);
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}


		return null;
	}

	
	protected void onPostExecute(String file_url) 
	{
		pDialog.dismiss();
		
		if(success != 1)
		{
			Toast.makeText(SmartOrderClient.getInstance().getOrderActivity().getApplicationContext(), "Fehler!",
					   Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(SmartOrderClient.getInstance().getOrderActivity().getApplicationContext(), "Gesendet!",
					   Toast.LENGTH_SHORT).show();
		}
		
		new GetOpenOrders().execute(SmartOrderClient.getInstance().getOrderActivity().getTableId());
	}

}
