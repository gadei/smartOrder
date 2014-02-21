package smart.order.client.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import smart.order.client.SmartOrderClient;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AddOrder extends AsyncTask<String, String, String> {

	private ProgressDialog pDialog = null;
	private JSONParser jsonParser = new JSONParser();
	
	private static String url_create_order = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/createOrder.php";
	
	private static final String TAG_SUCCESS = "success";
	
	private int success;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getSmartOrderActivity());
		pDialog.setMessage("Bestellung aufgeben...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(String... args) 
	{
		int table = 1;
		int order_id = 1;
		int status = 1;
		String food = "1,4,3,2";
		String drink = "2,3,2";

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("table_nr", String.valueOf(table)));
		params.add(new BasicNameValuePair("order_id", String.valueOf(order_id)));
		params.add(new BasicNameValuePair("status", String.valueOf(status)));
		params.add(new BasicNameValuePair("food", food));
		params.add(new BasicNameValuePair("drink", drink));

		// getting JSON Object
		// Note that create product url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(url_create_order,
				"POST", params);

		// check log cat fro response
		Log.d("Create Response", json.toString());

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
		Toast.makeText(SmartOrderClient.getInstance().getSmartOrderActivity().getApplicationContext(), "Erfolgreich!",
				   Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(SmartOrderClient.getInstance().getSmartOrderActivity().getApplicationContext(), "Fehler!",
					   Toast.LENGTH_LONG).show();
		}
	}

}
