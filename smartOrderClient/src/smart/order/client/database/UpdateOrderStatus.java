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
import android.widget.Toast;

public class UpdateOrderStatus extends AsyncTask<String, String, String> {

	private ProgressDialog pDialog = null;
	private JSONParser jsonParser = new JSONParser();
	
	private static String url_create_order = "http://" + SmartOrderClient.getInstance().getIpAddress() + "/smartorder/updateOrderStatus.php";
	
	private static final String TAG_SUCCESS = "success";
	
	private int success;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(SmartOrderClient.getInstance().getOpenOrderActivity());
		pDialog.setMessage("Bestellung abschlieﬂen...");
		pDialog.setIndeterminate(false);
		pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(String... args) 
	{
		if(args.length != 1)
		{
			return null;
		}
		
		int orderId = Integer.valueOf(args[0]);

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_id", String.valueOf(orderId)));

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
		
		SmartOrderClient.getInstance().getOpenOrderActivity().finish();
		new GetOpenOrders().execute(SmartOrderClient.getInstance().getOrderActivity().getTableId());
	}

}
