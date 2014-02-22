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
		pDialog.setCancelable(false);
		pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(Map<String, String>... args) 
	{
		if(args.length > 1)
		{
			return null;
		}
		
		int table = Integer.valueOf(args[0].get("table"));
		int status = Integer.valueOf(args[0].get("status"));;
		String food = args[0].get("food");
		String drink = args[0].get("drink");

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("table_nr", String.valueOf(table)));
		params.add(new BasicNameValuePair("status", String.valueOf(status)));
		params.add(new BasicNameValuePair("food", food));
		params.add(new BasicNameValuePair("drink", drink));

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
		
		if(success == 1)
		{
		Toast.makeText(SmartOrderClient.getInstance().getOrderActivity().getApplicationContext(), "Erfolgreich!",
				   Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(SmartOrderClient.getInstance().getOrderActivity().getApplicationContext(), "Fehler!",
					   Toast.LENGTH_LONG).show();
		}
	}

}
